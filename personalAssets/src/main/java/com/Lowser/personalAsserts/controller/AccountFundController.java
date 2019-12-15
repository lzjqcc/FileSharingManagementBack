package com.Lowser.personalAsserts.controller;

import com.Lowser.personalAsserts.controller.param.AccountFundDetailsParam;
import com.Lowser.personalAsserts.controller.param.AccountFundParam;
import com.Lowser.personalAsserts.controller.param.AccountFundTypeParam;
import com.Lowser.personalAsserts.controller.vo.AccountFundVo;
import com.Lowser.personalAsserts.controller.vo.AccountVo;
import com.Lowser.personalAsserts.controller.vo.CurrentAccountVO;
import com.Lowser.personalAsserts.controller.vo.TargetAccountVO;
import com.Lowser.personalAsserts.dao.*;
import com.Lowser.personalAsserts.dao.domain.*;
import com.Lowser.personalAsserts.service.AccountFundService;
import com.Lowser.personalAsserts.utils.LoginUtil;
import com.Lowser.common.error.BizException;
import com.Lowser.common.utils.AssertUtils;
import com.beust.jcommander.internal.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;
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
    @PostMapping("/login")
    public Object login(String email, String password, HttpSession session) {
        if (session.getAttribute(LoginUtil.key) != null) {
            ;
            return "ok";
        }
        Account account = accountRepository.findByEmailAndPassword(email, password);
        if (account == null) {
            throw new BizException("登录失败");
        }

        LoginUtil.setAccount(session, account);
        return "ok";
    }

    /**
     * 初始化目标金额
     * @param targetAmount
     * @param targetRate
     * @param targetYear
     * @param account
     */
    @PostMapping("initTargetAmount")
    public void initTargetAmount(Integer targetAmount, Double targetRate, Integer targetYear, Account account) {
        assert targetAmount != null;
        assert targetRate != null;
        assert targetYear != null;
        account.setTargetAmount(targetAmount);
        account.setTargetReturnRate(targetRate);
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
        // 保存父帐号
        if (param.getParentId() == null) {
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
        parentAccountFund.setTotalAmount(parentAccountFund.getTotalAmount() + accountFund.getTotalAmount());
        parentAccountFund.setTotalInterest(parentAccountFund.getTotalInterest() + accountFund.getTotalInterest());
        parentAccountFund.setTotalCash(parentAccountFund.getTotalCash() + accountFund.getTotalCash());
        accountFundRepository.saveAll(Lists.newArrayList(accountFund, parentAccountFund));
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
        accountFundDetailsRepository.saveAll(Lists.newArrayList(childAccountFundDetails, parentAccountFundDetails));
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
     * @param currentCash
     * @param currentInterest
     * @param account
     */
    @GetMapping("/addFundInfo/{fundId}")
    @Transactional
    public void addAccountFundInfo(@PathVariable("fundId") Integer fundId,
                                   @Param("currentCash") Integer currentCash,
                                   @Param("currentInterest") Integer currentInterest, Account account) {
        AccountFund accountFund = accountFundRepository.findById(fundId).get();
        // 子帐号
        AccountFundDetails fundDetails = new AccountFundDetails();
        fundDetails.setAccountId(account.getId());
        fundDetails.setAccountFundId(fundId);
        fundDetails.setCurrentCash(currentCash);
        fundDetails.setCurrentInterest(currentInterest);
        fundDetails.setAddCash(currentCash - accountFund.getTotalCash());
        fundDetails.setAddInterest(currentInterest - accountFund.getTotalInterest());
        accountFundDetailsRepository.save(fundDetails);
        accountFund.setTotalCash(currentCash);
        accountFund.setTotalInterest(currentInterest);
        accountFund.setTotalAmount(accountFund.getTotalCash() + accountFund.getTotalInterest());
        accountFundRepository.save(accountFund);
        // 父帐号
        AccountFund parentAccountFund = accountFundRepository.findById(accountFund.getParentId()).get();
        parentAccountFund.setTotalCash(parentAccountFund.getTotalCash() + fundDetails.getAddCash());
        parentAccountFund.setTotalInterest(parentAccountFund.getTotalInterest() + fundDetails.getAddInterest());
        parentAccountFund.setTotalAmount(parentAccountFund.getTotalCash() + parentAccountFund.getTotalAmount());
        accountFundRepository.save(parentAccountFund);
        AccountFundDetails parentAccountFundDetails = new AccountFundDetails();
        parentAccountFundDetails.setAccountId(account.getId());
        parentAccountFundDetails.setAddCash(fundDetails.getAddCash());
        parentAccountFundDetails.setAddInterest(fundDetails.getAddInterest());
        parentAccountFundDetails.setCurrentCash(parentAccountFund.getTotalCash());
        parentAccountFundDetails.setCurrentInterest(parentAccountFund.getTotalInterest());
        parentAccountFundDetails.setAccountFundId(parentAccountFund.getId());
        accountFundDetailsRepository.save(parentAccountFundDetails);


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
        // 删除父帐号
        if (accountFund.getParentId() == null) {
            List<AccountFund> childAccountFunds = accountFundRepository.findByAccountIdAndParentId(account.getId(), accountFundId);
            List<Integer> accountFundIds = childAccountFunds.stream().map(AccountFund::getId).collect(Collectors.toList());
            accountFundIds.add(accountFundId);
            accountFundRepository.deleteByIdIn(accountFundIds);
            accountFundDetailsRepository.deleteByAccountFundIdIn(accountFundIds);
            return;
        }
        // 删除子帐号
        AccountFund parentAccountFund = accountFundRepository.getOne(accountFund.getId());
        parentAccountFund.setTotalAmount(parentAccountFund.getTotalAmount() - accountFund.getTotalAmount());
        parentAccountFund.setTotalCash(parentAccountFund.getTotalCash() - accountFund.getTotalCash());
        parentAccountFund.setTotalInterest(parentAccountFund.getTotalInterest() - accountFund.getTotalInterest());
        accountFundRepository.deleteById(accountFund.getId());
        accountFundRepository.save(parentAccountFund);

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
        //currentAccountVO.setReturnRate(accountFund.getTotalInterest() / (accountFund.getTotalCash()));
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
        return funds.stream().filter(t-> t.getParentId() == null).collect(Collectors.toList());
    }
    private Map<Integer, List<AccountFund>> getChildAccountFunds(List<AccountFund> funds) {
        if (CollectionUtils.isEmpty(funds)) {
            return new HashMap<>();
        }
        List<AccountFund> childs = funds.stream().filter(t -> t.getParentId() != null).collect(Collectors.toList());
        return childs.stream().collect(Collectors.groupingBy(AccountFund::getParentId, Collectors.toList()));
    }
}
