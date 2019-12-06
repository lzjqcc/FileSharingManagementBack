package com.Loser.service;

import com.Loser.dao.AccountFundTargetDetailsRepository;
import com.Loser.dao.domain.Account;
import com.Loser.dao.domain.AccountFundTargetDetails;
import com.alibaba.fastjson.JSON;
import org.apache.poi.ss.formula.functions.Finance;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class AccountService {
    @Autowired
    private AccountFundTargetDetailsRepository accountFundTargetDetailsRepository;
    private static Integer monthOfYear = 12;
    public  void initAccountFundTargetDetails(Account account) {
        assert account.getTargetYear() != null;
        assert account.getTargetYear() <=30;
        int everyMonthCash = calculateCash(account.getTargetReturnRate(), account.getTargetYear(), account.getTargetAmount());
        List<AccountFundTargetDetails> list = new ArrayList<>();
        Integer currentCash = 0;
        for (int i = 0; i < account.getTargetYear(); i++) {

            for (int month = 1; month <= monthOfYear; month ++ ) {
                AccountFundTargetDetails details = new AccountFundTargetDetails();
                details.setAddCash((int) everyMonthCash);
                currentCash = currentCash + everyMonthCash;
                details.setCurrentInterest(calculateInterest(everyMonthCash, account.getTargetReturnRate(), i, month));
                details.setAddInterest(details.getCurrentInterest() - calculateInterest(everyMonthCash, account.getTargetReturnRate(), i, month -1));
                details.setCurrentCash(currentCash);
                details.setCurrentAmount(currentCash + details.getCurrentInterest());
                details.setTargetDate(getDate(i, month));
                list.add(details);
            }
        }
        //accountFundTargetDetailsRepository.saveAll(list);
        System.out.println(JSON.toJSONString(list));
    }
    private  int calculateInterest(int everyMonthCash, double rate, int currentYear, int currentMonth) {
        int currentNper = currentYear * currentMonth + currentMonth;
        int totalCash = everyMonthCash * currentNper;
        return (int) (Finance.fv(rate / monthOfYear, currentNper, -everyMonthCash, 0) - totalCash);
    }
    private int calculateCash(double rate, int targetYear, int targetAmount) {
        return (int) Finance.pmt(rate / monthOfYear, targetYear * monthOfYear, 0d, -targetAmount);
    }
    private static Date getDate(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,calendar.get(Calendar.MONTH) + month );
        calendar.add(Calendar.YEAR, calendar.get(Calendar.YEAR) + year+1);
        return calendar.getTime();
    }

    public static void main(String[] args) {
        AccountService accountService = new AccountService();
        Account account = new Account();
        account.setTargetYear(10);
        account.setTargetAmount(5000000);
        account.setTargetReturnRate(0.1);
        accountService.initAccountFundTargetDetails(account);
        System.out.println(accountService.calculateInterest(24408, 0.1, 1,1));
    }


}
