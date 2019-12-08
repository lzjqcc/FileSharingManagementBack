package com.Lowser.personalAsserts.controller;

import com.Lowser.personalAsserts.controller.param.AccountFundDetailsParam;
import com.Lowser.personalAsserts.controller.param.AccountFundParam;
import com.Lowser.personalAsserts.controller.param.AccountFundTypeParam;
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
import org.springframework.util.CollectionUtils;
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
    /**
     * 第一步：创建资金账户类型
     * @param param
     */
    @PostMapping("/createAccountFundType")
    public void createAccountFundType(AccountFundTypeParam param, Account account) {
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
    public void createAccountFund(AccountFundParam param, Account account) {
        AccountFund accountFund = new AccountFund();
        AssertUtils.notEmpty(param.getName(), "账户名称不能为空");
        AssertUtils.notNull(param.getAccountfundTypeId(), "请选资金账户类型");
        BeanUtils.copyProperties(accountFund, param);
        accountFund.setAccountId(account.getId());
        accountFundRepository.save(accountFund);
    }

    /**
     * 第三步：添加增量现金或收益
     * @param param
     */
    @PostMapping("/addFundDetails")
    public void addAccountFundDetails(AccountFundDetailsParam param, Account account) {

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
    public void addAccountFundInfo(@PathVariable("fundId") Integer fundId,
                                   @Param("currentCash") Integer currentCash,
                                   @Param("currentInterest") Integer currentInterest, Account account) {
        AccountFund accountFund = accountFundRepository.findById(fundId).get();

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
            for (AccountFund child : childs) {
                CurrentAccountVO childVo = buildCurrentAccountVo(child, idAndAccountFundTypeMap);
                childVos.add(childVo);
            }
            currentAccountVO.setChilds(childVos);
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
        currentAccountVO.setReturnRate(accountFund.getTotalInterest() / (accountFund.getTotalCash()));
        return currentAccountVO;
    }
    private Map<Integer, AccountFundType> getAccountFundTypes(List<Integer> fundTypeIds) {
        if (CollectionUtils.isEmpty(fundTypeIds)) {
            return new HashMap<>();
        }
        List<AccountFundType> accountFundTypes = null;
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
