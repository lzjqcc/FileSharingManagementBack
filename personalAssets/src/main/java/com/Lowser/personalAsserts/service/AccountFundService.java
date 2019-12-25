package com.Lowser.personalAsserts.service;

import com.Lowser.common.error.BizException;
import com.Lowser.personalAsserts.dao.AccountFundDetailsRepository;
import com.Lowser.personalAsserts.dao.AccountFundRepository;
import com.Lowser.personalAsserts.dao.AccountFundTargetDetailsRepository;
import com.Lowser.personalAsserts.dao.AccountFundTypeRepository;
import com.Lowser.personalAsserts.dao.domain.*;
import com.alibaba.fastjson.JSON;
import com.beust.jcommander.internal.Lists;
import org.apache.poi.ss.formula.functions.Finance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
@Service
public class AccountFundService {
    @Autowired
    private AccountFundTargetDetailsRepository accountFundTargetDetailsRepository;
    private static Integer monthOfYear = 12;
    @Transactional
    public  void initAccountFundTargetDetails(Account account) {
        assert account.getTargetYear() != null;
        assert account.getTargetYear() <=30;
        assert account.getTargetReturnRate() != null;
        if (account.getTargetReturnRate() == null || account.getTargetReturnRate() >= 1 || account.getTargetReturnRate() <=0) {
            throw new BizException("收益率必须在0~1之间");
        }
        accountFundTargetDetailsRepository.removeByAccountId(account.getId());

        List<AccountFundTargetDetails> list = new ArrayList<>();
        Integer currentCash = 0;
        int cashOfYear = calculateCash(account.getTargetReturnRate(), account.getTargetYear(), account.getTargetAmount());
        double rateOfMonth = rateOfMonth(account.getTargetReturnRate());
        int everyMonthCash = calculateCash(rateOfMonth, monthOfYear, cashOfYear);
        int nper = 1;
        for (int i = 0; i < account.getTargetYear(); i++) {

            for (int month = 1; month <= monthOfYear ; month ++ ) {
                nper ++ ;
                AccountFundTargetDetails details = new AccountFundTargetDetails();
                details.setAddCash(everyMonthCash);
                currentCash = currentCash + everyMonthCash;
                details.setCurrentInterest(calculateInterest(everyMonthCash, rateOfMonth, nper));
                details.setAddInterest(details.getCurrentInterest() - calculateInterest(everyMonthCash, rateOfMonth, nper - 1));
                details.setCurrentCash(currentCash);
                details.setCurrentAmount(currentCash + details.getCurrentInterest());
                details.setTargetDate(getDate(i, month));
                details.setAccountId(account.getId());
                list.add(details);
            }
        }
        accountFundTargetDetailsRepository.saveAll(list);
    }
    private  int calculateInterest(int everyMonthCash, double rateOfMonth, int nper) {
        int totalCash = everyMonthCash * nper;
        return (int) (Finance.fv(rateOfMonth , nper, -everyMonthCash, 0) - totalCash);
    }
    private int calculateCash(double rate, int nper, int targetAmount) {
        return (int) Finance.pmt(rate , nper  , 0d, -targetAmount);
    }
    private double rateOfMonth( double rateOfYear) {
        return Math.pow(1d +rateOfYear, 1d / (double)(monthOfYear)) -1d;
    }
    private static Date getDate(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, year * monthOfYear + month  );
        return calendar.getTime();
    }

    public static void main(String[] args) {
        AccountFundService accountFundService = new AccountFundService();
        for (int i = 0;i<5;i++) {
            for (int month = 1;month <= monthOfYear; month++) {
                getDate(i, month);
            }
        }
//        Account account = new Account();
//        account.setTargetAmount(5000000);
//        account.setTargetYear(10);
//        account.setTargetReturnRate(0.1);
//       accountFundService.initAccountFundTargetDetails(account);
//
//        System.out.println(accountFundService.rateOfMonth(0.1));
//        System.out.println(accountFundService.calculateCash(0.1, 5, 5000000));

    }

}
