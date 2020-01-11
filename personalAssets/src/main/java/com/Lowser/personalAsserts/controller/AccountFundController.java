package com.Lowser.personalAsserts.controller;

import com.Lowser.common.error.BizException;
import com.Lowser.common.utils.AssertUtils;
import com.Lowser.common.utils.CodeUtils;
import com.Lowser.common.utils.DateUtils;
import com.Lowser.personalAsserts.controller.param.AccountFundDetailsParam;
import com.Lowser.personalAsserts.controller.param.AccountFundParam;
import com.Lowser.personalAsserts.controller.param.AccountFundTypeParam;
import com.Lowser.personalAsserts.controller.vo.*;
import com.Lowser.personalAsserts.dao.*;
import com.Lowser.personalAsserts.dao.domain.*;
import com.Lowser.personalAsserts.service.AccountFundService;
import com.Lowser.personalAsserts.utils.LoginUtil;
import com.beust.jcommander.internal.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/asserts")
@ResponseBody
public class AccountFundController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountFundTypeRepository accountFundTypeRepository;
    @Autowired
    private AccountFundRepository accountFundRepository;
    @Autowired
    private AccountFundDetailsRepository accountFundDetailsRepository;
    @Autowired
    private AccountFundService accountService;
    @Autowired
    private AccountFundTargetDetailsRepository accountFundTargetDetailsRepository;
    private String loginTimes = "loginTimes";
    private List<Character> codes = null;
    @PostConstruct
    public void init() {
        codes = new ArrayList<>(23);
        for (int i = 0; i<23;i++) {
            codes.add((char) ('A' + i));
        }
    }
    @PostMapping("/login")
    public Object login(String email, String password,String code,Boolean autoRegister,  HttpSession session) {
        if (session.getAttribute(LoginUtil.key) != null) {
            return "ok";
        }
        CodeUtils.validateCode(code, session);
        if (session.getAttribute(loginTimes) == null) {
            session.setAttribute(loginTimes, 0);
        }
        if ((int)session.getAttribute(loginTimes) > 10) {
            throw new BizException("超过最大登录次数");
        }

        Account account = accountRepository.findByEmailAndPassword(email, password);
        if (account == null ) {
            if (!Objects.equals(autoRegister, true)) {
                session.setAttribute(loginTimes, (int)session.getAttribute(loginTimes) + 1);
                throw new BizException("登录失败:帐号或密码错误,剩余登录次数 " + (10 -(int)(session.getAttribute(loginTimes))));
            }
            if (accountRepository.findByEmail(email) != null) {
                throw new BizException("自动注册登录失败:帐号"+ email +"已经存在");
            }
            account = new Account();
            account.setTargetAmount(0);
            account.setTargetReturnRate(0d);
            account.setTargetYear(0);
            account.setEmail(email);
            account.setPassword(password);
            account = accountRepository.save(account);
        }
        List<AccountFund> topAccountFunds = accountFundRepository.findByAccountIdAndParentId(account.getId(), null);
        if (CollectionUtils.isEmpty(topAccountFunds)) {
            AccountFund topAccountFund = new AccountFund();
            topAccountFund.setTotalAmount(0);
            topAccountFund.setTotalCash(0);
            topAccountFund.setTotalInterest(0);
            topAccountFund.setAccountId(account.getId());
            topAccountFund.setName("总帐号");
            accountFundRepository.save(topAccountFund);
        }
        LoginUtil.setAccount(session, account);
        return "ok";
    }
    @GetMapping("/isLogin")
    public boolean isLogin(Account account) {
        return account != null;
    }
    @PostMapping("/register")
    public void register(@RequestParam("email") String email,
                           @RequestParam("password") String password,
                           @RequestParam("code") String code,
                         HttpSession session) {
        CodeUtils.validateCode(code, session);
        Account account = new Account();
        account.setTargetAmount(0);
        account.setTargetReturnRate(0d);
        account.setTargetYear(0);
        account.setEmail(email);
        account.setPassword(password);
        accountRepository.save(account);
    }
    @GetMapping("/code")
    public void code(HttpServletResponse response, HttpSession session) throws IOException {

        BufferedImage image = new BufferedImage(40, 30, BufferedImage.TYPE_INT_RGB);

        Graphics graphics = image.getGraphics();
        Random random = new Random();
        String codeString = "";
        for (int i = 0;i < 4;i++) {
            Character code = codes.get(random.nextInt(codes.size()-1));
            codeString = codeString + code;
            graphics.drawString(code.toString(),  10 * i, 15);
        }
        session.setAttribute("code", codeString);
        session.setAttribute("codeTime", new Date());
        //干扰线
        for (int i = 0;i < 4; i++ ) {
            graphics.drawLine(0, random.nextInt(13) * i,
                                random.nextInt(20) * i,random.nextInt(13) * i);
        }
        ImageIO.write(image, "png", response.getOutputStream());
       // graphics.drawString();

    }
    /**
     * 初始化目标金额
     * @param targetAmount
     * @param targetReturnRate
     * @param targetYear
     * @param account
     */
    @PostMapping("initTargetAmount")
    public void initTargetAmount(Integer targetAmount, Double targetReturnRate, Integer targetYear, Account account) {
        assert targetAmount != null;
        assert targetReturnRate != null;
        assert targetYear != null;
        account.setTargetAmount(targetAmount);
        account.setTargetReturnRate(targetReturnRate);
        account.setTargetYear(targetYear);
        accountService.initAccountFundTargetDetails(account);
    }
    @RequestMapping(value = "/getTargetInfo", method = RequestMethod.GET)
    public List<AccountFundTargetDetails> getTargetPlan(Account account) {
        return accountFundTargetDetailsRepository.findByAccountId(account.getId());
    }
    @GetMapping(value = "/getAccountFunds")
    public List<AccountFundVo> getAccountFunds(Account account) {
        List<AccountFund> funds = accountFundRepository.findByAccountId(account.getId());
        return buildAccountFundVos(funds);
    }
    private List<AccountFundVo> buildAccountFundVos(List<AccountFund> accountFunds) {
        List<AccountFundVo> accountFundVos = new ArrayList<>();
        List<AccountFund> parentAccountFunds = filterAccountFunds(accountFunds);
        Map<Integer, List<AccountFund>> parentIdAndChildAccountFunds = parentIdAndChildAccountFunds(accountFunds);
        for (AccountFund accountFund : parentAccountFunds) {
            AccountFundVo accountFundVo = toAccountFundVo(accountFund);
            List<AccountFund> childFunds = parentIdAndChildAccountFunds.get(accountFund.getId());
            List<AccountFundVo> childFundVos = new ArrayList<>();
            for (AccountFund childFund : childFunds) {
                childFundVos.add(toAccountFundVo(childFund));
            }
            accountFundVo.setChildAccountFunds(childFundVos);
            accountFundVos.add(accountFundVo);
        }
        return accountFundVos;

    }
    private List<AccountFund> filterAccountFunds(List<AccountFund> accountFunds) {
        return accountFunds.stream().filter(accountFund -> accountFund.getParentId() == null).collect(Collectors.toList());
    }
    private Map<Integer, List<AccountFund>> parentIdAndChildAccountFunds(List<AccountFund> accountFunds) {
        return accountFunds.stream().filter(accountFund -> accountFund.getParentId() != null).collect(Collectors.groupingBy(AccountFund::getParentId, Collectors.toList()));
    }
    private AccountFundVo toAccountFundVo(AccountFund accountFund) {
        AccountFundVo accountFundVo = new AccountFundVo();
        BeanUtils.copyProperties(accountFund, accountFundVo);
        accountFundVo.setId(accountFund.getId());
        AccountFundType accountFundType = accountFundTypeRepository.findById(accountFund.getId()).get();
        if (accountFundType != null) {
            accountFundVo.setFundType(accountFundType.getName());
            accountFundVo.setRemarks(accountFundType.getRemarks());
        }
        return accountFundVo;
    }
    /**
     * 第一步：创建资金账户类型
     * @param param
     */
    @PostMapping("/createAccountFundType")
    public void createAccountFundType(@RequestBody AccountFundTypeParam param, Account account) {
        AccountFundType type = new AccountFundType();
        AssertUtils.notEmpty(param.getName(), "账户类不能为空");
        BeanUtils.copyProperties(param, type);
        type.setAccountId(account.getId());
        accountFundTypeRepository.save(type);
    }
    @GetMapping("/totalParentAccountDetails")
    public List<AccountFundDetailsVO> getTotalParentAccountDetails(Account account) {
        AccountFund topAccountFund = accountFundRepository.findByAccountIdAndParentId(account.getId(), null).get(0);
        List<Integer> fundIds = new ArrayList<>();
        fundIds.add(topAccountFund.getId());
        List<AccountFundDetails> accountFundDetails = accountFundDetailsRepository.findByAccountIdAndAccountFundIdIn(account.getId(), fundIds);
        return toAccountFundDetailsVO(accountFundDetails);
    }
    /**
     * 第二步：创建资金账户
     * @param param
     */
    @PostMapping("/createAccountFund")
    public void createAccountFund(@RequestBody AccountFundParam param, Account account) {
        AccountFund accountFund = new AccountFund();
        AssertUtils.notEmpty(param.getName(), "账户名称不能为空");
        if (param.getParentId() != null && StringUtils.isEmpty(param.getFundType())) {
            throw new BizException("指定子账户类型");
        }
        if (param.getParentId() != null && param.getTotalAmount() != param.getTotalCash() + param.getTotalInterest()) {
            throw new BizException("总资产 = 收益 +  贡献现金");
        }
        BeanUtils.copyProperties(param, accountFund);
        accountFund.setAccountId(account.getId());
        AccountFund toPAccountFund = accountFundRepository.findByAccountIdAndParentId(account.getId(), null).get(0);

        // 保存父帐号
        if (param.getParentId() == null) {
            accountFund.setParentId(toPAccountFund.getId());
            accountFundRepository.save(accountFund);
            return;
        }
        // 保存子账户
        AccountFundType accountFundType = accountFundTypeRepository.findByAccountIdAndName(account.getId(), param.getFundType());
        // 保存账户类型
        if (accountFundType == null) {
            accountFundType = new AccountFundType();
            accountFundType.setAccountId(account.getId());
            accountFundType.setName(param.getFundType());
            accountFundType = accountFundTypeRepository.save(accountFundType);
        }
        accountFund.setAccountfundTypeId(accountFundType.getId());
        AccountFund parentAccountFund = accountFundRepository.findById(param.getParentId()).get();

        parentAccountFund.setTotalInterest(parentAccountFund.getTotalInterest() + param.getTotalInterest());
        parentAccountFund.setTotalCash(parentAccountFund.getTotalCash() + param.getTotalCash());
        parentAccountFund.setTotalAmount(parentAccountFund.getTotalCash() + parentAccountFund.getTotalInterest());

        toPAccountFund.setTotalInterest(toPAccountFund.getTotalInterest() + param.getTotalInterest());
        toPAccountFund.setTotalCash(toPAccountFund.getTotalCash() + param.getTotalCash() );
        toPAccountFund.setTotalAmount(toPAccountFund.getTotalInterest() + toPAccountFund.getTotalCash());
        List<AccountFund> afterAccountFunds = accountFundRepository.saveAll(Lists.newArrayList(accountFund, parentAccountFund, toPAccountFund));
        // parentFunDetails
        AccountFundDetails parentAccountFundDetails = new AccountFundDetails();
        parentAccountFundDetails.setAccountId(account.getId());
        parentAccountFundDetails.setAccountFundId(param.getParentId());
        parentAccountFundDetails.setAddCash(accountFund.getTotalCash());
        parentAccountFundDetails.setAddInterest(accountFund.getTotalInterest());
        parentAccountFundDetails.setCurrentCash(parentAccountFund.getTotalCash());
        parentAccountFundDetails.setCurrentInterest(parentAccountFund.getTotalInterest());
        // childFundDetails
        AccountFundDetails childAccountFundDetails = new AccountFundDetails();
        childAccountFundDetails.setAccountId(account.getId());
        childAccountFundDetails.setCurrentInterest(accountFund.getTotalInterest());
        childAccountFundDetails.setCurrentCash(accountFund.getTotalCash());
        childAccountFundDetails.setAddInterest(accountFund.getTotalInterest());
        childAccountFundDetails.setAddCash(accountFund.getTotalCash());
        AccountFund afterChildAccountFund = afterAccountFunds.stream().filter(saveAccountFund -> saveAccountFund.getParentId() != null).findFirst().get();
        childAccountFundDetails.setAccountFundId(afterChildAccountFund.getId());

        AccountFundDetails topFundDetail = new AccountFundDetails();
        topFundDetail.setAddCash(parentAccountFundDetails.getAddCash());
        topFundDetail.setAddInterest(parentAccountFundDetails.getAddInterest());
        topFundDetail.setCurrentCash(toPAccountFund.getTotalCash());
        topFundDetail.setCurrentInterest(toPAccountFund.getTotalInterest());
        topFundDetail.setAccountFundId(toPAccountFund.getId());
        topFundDetail.setAccountId(account.getId());
        accountFundDetailsRepository.saveAll(Lists.newArrayList(childAccountFundDetails, parentAccountFundDetails,topFundDetail));
    }
    @GetMapping("/fundTypes")
    public List<AccountFundType> getAccountFundType(Account account) {
        return accountFundTypeRepository.findByAccountId(account.getId());
    }
    /**
     * 第三步：添加增量现金或收益
     * @param param
     */
    @PostMapping("/addFundDetails")
    public void addAccountFundDetails(@RequestBody AccountFundDetailsParam param, Account account) {

        AssertUtils.notNull(param.getAccountFundId(), "选择资金账户");
        AccountFundDetails details = new AccountFundDetails();
        BeanUtils.copyProperties(param, details);
        details.setAccountId(account.getId());

        accountFundDetailsRepository.save(details);
        AccountFund accountFund = accountFundRepository.findById(param.getAccountFundId()).get();
        accountFund.setTotalCash(accountFund.getTotalCash() + param.getAddCash());
        accountFund.setTotalInterest(accountFund.getTotalInterest() + param.getAddInterest());
        accountFund.setTotalAmount(accountFund.getTotalCash() + accountFund.getTotalInterest());
        accountFundRepository.save(accountFund);
    }

    /**
     * 第三步： 添加当前现金和收益
     * 增量现金 = 当前现金 - 之前现金
     * @param fundId
     * @param totalCash
     * @param totalInterest
     * @param account
     */
    @GetMapping("/addFundInfo/{fundId}")
    @Transactional
    public void addAccountFundInfo(@PathVariable("fundId") Integer fundId,
                                   @Param("totalCash") Integer totalCash,
                                   @Param("totalInterest") Integer totalInterest,
                                   @Param("fundType") String fundType,
                                   Account account) {
        if (totalCash <=0) {
            throw new BizException("现金必须大于0");
        }
        if (totalInterest <0) {
            throw new BizException("收益必须大于或等于0");
        }
        AccountFundType accountFundType = accountFundTypeRepository.findByAccountIdAndName(account.getId(), fundType);
        if (accountFundType == null) {
            accountFundType = new AccountFundType();
            accountFundType.setName(fundType);
            accountFundType.setAccountId(account.getId());
            accountFundType = accountFundTypeRepository.save(accountFundType);
        }
        AccountFund accountFund = accountFundRepository.findById(fundId).get();
        accountFund.setAccountfundTypeId(accountFundType.getId());
        // 子帐号
        AccountFundDetails fundDetails = new AccountFundDetails();
        fundDetails.setAccountId(account.getId());
        fundDetails.setAccountFundId(fundId);
        fundDetails.setCurrentCash(totalCash);
        fundDetails.setCurrentInterest(totalInterest);
        fundDetails.setAddCash(totalCash - accountFund.getTotalCash());
        fundDetails.setAddInterest(totalInterest - accountFund.getTotalInterest());
        accountFundDetailsRepository.save(fundDetails);
        accountFund.setTotalCash(totalCash);
        accountFund.setTotalInterest(totalInterest);
        accountFund.setTotalAmount(accountFund.getTotalCash() + accountFund.getTotalInterest());
        accountFundRepository.save(accountFund);
        // 父帐号
        AccountFund parentAccountFund = accountFundRepository.findById(accountFund.getParentId()).get();
        parentAccountFund.setTotalCash(parentAccountFund.getTotalCash() + fundDetails.getAddCash());
        parentAccountFund.setTotalInterest(parentAccountFund.getTotalInterest() + fundDetails.getAddInterest());
        parentAccountFund.setTotalAmount(parentAccountFund.getTotalCash() + parentAccountFund.getTotalInterest());
        accountFundRepository.save(parentAccountFund);
        AccountFundDetails parentAccountFundDetails = new AccountFundDetails();
        parentAccountFundDetails.setAccountId(account.getId());
        parentAccountFundDetails.setAddCash(fundDetails.getAddCash());
        parentAccountFundDetails.setAddInterest(fundDetails.getAddInterest());
        parentAccountFundDetails.setCurrentCash(parentAccountFund.getTotalCash());
        parentAccountFundDetails.setCurrentInterest(parentAccountFund.getTotalInterest());
        parentAccountFundDetails.setAccountFundId(parentAccountFund.getId());
        accountFundDetailsRepository.save(parentAccountFundDetails);

        // top 帐号
        AccountFund topAccountFund = accountFundRepository.findByAccountIdAndParentId(account.getId(), null).get(0);
        topAccountFund.setTotalCash(topAccountFund.getTotalCash() + fundDetails.getAddCash());
        topAccountFund.setTotalInterest(topAccountFund.getTotalInterest() + fundDetails.getAddInterest());
        topAccountFund.setTotalAmount(topAccountFund.getTotalCash() + topAccountFund.getTotalInterest());
        accountFundRepository.save(topAccountFund);
        AccountFundDetails topDetails = new AccountFundDetails();
        topDetails.setAccountId(account.getId());
        topDetails.setAccountFundId(topAccountFund.getId());
        topDetails.setAddCash(fundDetails.getAddCash());
        topDetails.setAddInterest(fundDetails.getAddInterest());
        topDetails.setCurrentCash(topAccountFund.getTotalCash());
        topDetails.setCurrentInterest(topAccountFund.getTotalInterest());
        accountFundDetailsRepository.save(topDetails);
    }

    /**
     * 目标账户：初始金额、目标金额、年化利率
     * @param account
     * @return
     */
    @GetMapping("/accountInfo")
    public AccountVo findAccountInfo(Account account) {
        AccountVo accountVo = new AccountVo();
        TargetAccountVO targetAccountVO = buildTargetAccountVO(account);
        accountVo.setTargetAccountVO(targetAccountVO);
        List<CurrentAccountVO> currentAccountVOS = buildCurrentAccountVOs(account);
        accountVo.setCurrentAccountVOS(currentAccountVOS);
        return accountVo;
    }
    @GetMapping("/deleteAccountFund")
    @Transactional
    public void deleteAccountFund(@RequestParam("accountFundId") Integer accountFundId, Account account) {
        AccountFund accountFund = accountFundRepository.getOne(accountFundId);
        AccountFund topAccountFund = accountFundRepository.findByAccountIdAndParentId(account.getId(), null).get(0);
        topAccountFund.setTotalInterest(topAccountFund.getTotalInterest() - accountFund.getTotalInterest());
        topAccountFund.setTotalCash(topAccountFund.getTotalCash() - accountFund.getTotalCash());
        topAccountFund.setTotalAmount(topAccountFund.getTotalCash() + topAccountFund.getTotalInterest());
        accountFundRepository.save(topAccountFund);
        AccountFundDetails topFundDetails = new AccountFundDetails();
        topFundDetails.setCurrentInterest(topAccountFund.getTotalInterest());
        topFundDetails.setCurrentCash(topAccountFund.getTotalCash());
        topFundDetails.setAddInterest(-accountFund.getTotalCash());
        topFundDetails.setAddCash(-accountFund.getTotalCash());
        topFundDetails.setAccountFundId(topAccountFund.getId());
        topFundDetails.setAccountId(account.getId());
        accountFundDetailsRepository.save(topFundDetails);
        // 删除父帐号
        if (accountFund.getParentId() != null && accountFund.getAccountfundTypeId() == null) {
            List<AccountFund> childAccountFunds = accountFundRepository.findByAccountIdAndParentId(account.getId(), accountFundId);
            List<Integer> accountFundIds = childAccountFunds.stream().map(AccountFund::getId).collect(Collectors.toList());
            accountFundIds.add(accountFundId);
            accountFundRepository.deleteByIdIn(accountFundIds);
            accountFundDetailsRepository.deleteByAccountFundIdIn(accountFundIds);
            return;
        }
        // 删除子帐号
        AccountFund parentAccountFund = accountFundRepository.getOne(accountFund.getParentId());
        parentAccountFund.setTotalAmount(parentAccountFund.getTotalAmount() - accountFund.getTotalAmount());
        parentAccountFund.setTotalCash(parentAccountFund.getTotalCash() - accountFund.getTotalCash());
        parentAccountFund.setTotalInterest(parentAccountFund.getTotalInterest() - accountFund.getTotalInterest());
        accountFundRepository.deleteById(accountFund.getId());
        accountFundRepository.save(parentAccountFund);
        AccountFundDetails parentDetails = new AccountFundDetails();
        parentDetails.setCurrentInterest(parentAccountFund.getTotalInterest());
        parentDetails.setCurrentCash(parentAccountFund.getTotalCash());
        parentDetails.setAddInterest(-accountFund.getTotalCash());
        parentDetails.setAddCash(-accountFund.getTotalCash());
        parentDetails.setAccountFundId(parentAccountFund.getId());
        parentDetails.setAccountId(account.getId());
        accountFundDetailsRepository.save(parentDetails);
    }
    @GetMapping(value = "/getAllParentAcountFundAndDetails")
    public Map<String, List<AccountFundDetailsVO>> getParentAccountFundAndDetails(Account account) {
        AccountFund topAccountFund = accountFundRepository.findByAccountIdAndParentId(account.getId(), null).get(0);
        List<AccountFund> parentAccountFunds = accountFundRepository.findByAccountIdAndParentId(account.getId(), topAccountFund.getId());
        List<Integer> parentAccountFundIds = parentAccountFunds.stream().map(AccountFund::getId).collect(Collectors.toList());
        List<AccountFundDetails> accountFundDetails = accountFundDetailsRepository.findByAccountIdAndAccountFundIdIn(account.getId(), parentAccountFundIds);

        return toParentAccountFundAndDetailsMap(parentAccountFunds, accountFundDetails);
    }
    private Map<String, List<AccountFundDetailsVO>> toParentAccountFundAndDetailsMap(List<AccountFund> parentAccountFunds, List<AccountFundDetails> details) {
        List<AccountFundDetailsVO> detailsVOS = toAccountFundDetailsVO(details);
        Map<String, List<AccountFundDetailsVO>> map = new HashMap<>();
        Map<Integer, List<AccountFundDetailsVO>> parentFundAccountIdAndDetails = detailsVOS.stream().collect(Collectors.groupingBy(AccountFundDetailsVO::getAccountFundId, Collectors.toList()));
        for (AccountFund accountFund : parentAccountFunds) {
            map.put(accountFund.getName(), parentFundAccountIdAndDetails.get(accountFund.getId()));
        }
        return map;
    }
    private List<AccountFundDetailsVO> toAccountFundDetailsVO(List<AccountFundDetails> accountFunds) {
        List<AccountFundDetailsVO> detailsVOS = new ArrayList<>();
        for (AccountFundDetails accountFundDetails : accountFunds) {
            AccountFundDetailsVO accountFundDetailsVO = new AccountFundDetailsVO();
            BeanUtils.copyProperties(accountFundDetails, accountFundDetailsVO);
            accountFundDetailsVO.setCurrentAmount(accountFundDetails.getCurrentCash() + accountFundDetails.getCurrentInterest());
            detailsVOS.add(accountFundDetailsVO);
        }
        return detailsVOS;
    }
    private TargetAccountVO buildTargetAccountVO(Account account) {
        TargetAccountVO targetAccountVO = new TargetAccountVO();
        targetAccountVO.setInitAmount(account.getInitAmount());
        targetAccountVO.setTargetAmount(account.getTargetAmount());
        targetAccountVO.setTargetReturnRate(account.getTargetReturnRate());
        targetAccountVO.setTargetYear(account.getTargetYear());
        return targetAccountVO;
    }
    private List<CurrentAccountVO> buildCurrentAccountVOs(Account account) {
        List<AccountFund> funds = accountFundRepository.findByAccountId(account.getId());
        List<AccountFund> parentAccountFunds = getParentAccountFund(funds);
        Map<Integer, List<AccountFund>> parentIdAndChilds = getChildAccountFunds(funds);
        Map<Integer, AccountFundType> idAndAccountFundTypeMap = getAccountFundTypes(funds.stream().map(AccountFund::getAccountfundTypeId).collect(Collectors.toList()));
        List<CurrentAccountVO> parentVo = new ArrayList<>();
        for (AccountFund accountFund : parentAccountFunds) {
            CurrentAccountVO currentAccountVO = buildCurrentAccountVo(accountFund, idAndAccountFundTypeMap);
            List<AccountFund> childs = parentIdAndChilds.get(accountFund.getId());
            List<CurrentAccountVO> childVos = new ArrayList<>();
            if (!CollectionUtils.isEmpty(childs)) {
                for (AccountFund child : childs) {
                    CurrentAccountVO childVo = buildCurrentAccountVo(child, idAndAccountFundTypeMap);
                    childVos.add(childVo);
                }
            }

            currentAccountVO.setChildAccountFunds(childVos);
            parentVo.add(currentAccountVO);
        }
        return parentVo;

    }
    private CurrentAccountVO buildCurrentAccountVo(AccountFund accountFund, Map<Integer, AccountFundType> typeMap) {
        CurrentAccountVO currentAccountVO = new CurrentAccountVO();
        AccountFundType type = typeMap.get(accountFund.getAccountfundTypeId());

        if (type != null) {
            currentAccountVO.setFundType(type.getName());
        }
        currentAccountVO.setName(accountFund.getName());
        currentAccountVO.setTotalAmount(accountFund.getTotalAmount());
        currentAccountVO.setTotalCash(accountFund.getTotalCash());
        currentAccountVO.setTotalInterest(accountFund.getTotalInterest());
        Integer totalCash = accountFund.getTotalCash() == 0 ? 1 : accountFund.getTotalCash();
        DecimalFormat format = new DecimalFormat("#.0000");
        currentAccountVO.setReturnRate(Double.parseDouble(format.format((double)accountFund.getTotalInterest()/ (double) totalCash)));
        currentAccountVO.setId(accountFund.getId());
        return currentAccountVO;
    }
    private Map<Integer, AccountFundType> getAccountFundTypes(List<Integer> fundTypeIds) {
        if (CollectionUtils.isEmpty(fundTypeIds)) {
            return new HashMap<>();
        }
        fundTypeIds = fundTypeIds.stream().filter(Objects::nonNull).collect(Collectors.toList());
        List<AccountFundType> accountFundTypes = accountFundTypeRepository.findByIdIn(fundTypeIds);
        return accountFundTypes.stream().collect(Collectors.toMap(AccountFundType::getId, Function.identity()));
    }
    private List<AccountFund> getParentAccountFund(List<AccountFund> funds) {
        if (CollectionUtils.isEmpty(funds)) {
            return Lists.newArrayList();
        }
        return funds.stream().filter(t-> t.getParentId() != null && t.getAccountfundTypeId() == null).collect(Collectors.toList());
    }
    private Map<Integer, List<AccountFund>> getChildAccountFunds(List<AccountFund> funds) {
        if (CollectionUtils.isEmpty(funds)) {
            return new HashMap<>();
        }
        List<AccountFund> childs = funds.stream().filter(t -> t.getParentId() != null && t.getAccountfundTypeId() != null).collect(Collectors.toList());
        return childs.stream().collect(Collectors.groupingBy(AccountFund::getParentId, Collectors.toList()));
    }
}
