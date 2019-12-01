package com.Loser.controller;

import com.Loser.controller.param.AccountFundDetailsParam;
import com.Loser.controller.param.AccountFundParam;
import com.Loser.controller.param.AccountFundTypeParam;
import com.Loser.controller.vo.AccountDetailsVO;
import com.Loser.controller.vo.AccountVo;
import com.Loser.controller.vo.CurrentAccountVO;
import com.Loser.controller.vo.TargetAccountVO;
import com.Loser.dao.AccountFundDetailsRepository;
import com.Loser.dao.AccountFundRepository;
import com.Loser.dao.AccountFundTypeRepository;
import com.Loser.dao.AccountRepository;
import com.Loser.dao.domain.Account;
import com.Loser.dao.domain.AccountFund;
import com.Loser.dao.domain.AccountFundDetails;
import com.Loser.dao.domain.AccountFundType;
import com.Loser.utils.LoginUtil;
import com.Lowser.common.error.BizException;
import com.Lowser.common.utils.AssertUtils;
import com.beust.jcommander.internal.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountFundTypeRepository accountFundTypeRepository;
    @Autowired
    private AccountFundRepository accountFundRepository;
    @Autowired
    private AccountFundDetailsRepository accountFundDetailsRepository;
    @PostMapping("/login")
    public Object login(String email, String passowrd, HttpSession session) {
        Account account = accountRepository.findByEmailAndPassword(email, passowrd);
        if (account == null) {
            throw new BizException("登录失败");
        }
        LoginUtil.setAccount(session, account);
        return "ok";
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

    public static double pmt(double r, double n, double p, double f, boolean t) {
        double retval = 0;
        if (r == 0) {
            retval = -1*(f+p)/n;
        }
        else {
            double r1 = r + 1;
            retval = ( f + p * Math.pow(r1, n) ) * r
                    /
                    ((t ? r1 : 1) * (1 - Math.pow(r1, n)));
        }
        return retval;
    }

    public static void main(String[] args) {
        System.out.println(pmt(0.1 /12 , 10 *12 , 0, -5000000, false)
        );
    }
}
