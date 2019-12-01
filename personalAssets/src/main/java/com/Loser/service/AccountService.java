package com.Loser.service;

import com.Loser.dao.AccountFundTargetDetailsRepository;
import com.Loser.dao.domain.Account;
import com.Loser.dao.domain.AccountFundTargetDetails;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class AccountService {
    @Autowired
    private AccountFundTargetDetailsRepository accountFundTargetDetailsRepository;
    private Integer yearOfMonth = 12;
    public void initAccountFundTargetDetails(Account account) {
        assert account.getTargetYear() != null;
        assert account.getTargetYear() <=30;
        double everyYearCash = pmt(account.getTargetReturnRate(), account.getTargetYear(), 0d, account.getTargetAmount(), false);
        int everyMonthCash = (int) (everyYearCash / yearOfMonth);
        AccountFundTargetDetails details = new AccountFundTargetDetails();
        List<AccountFundTargetDetails> list = new ArrayList<>();

        for (int i = 0; i < account.getTargetYear(); i++) {

            for (int month = 0; month < yearOfMonth ; month ++ ) {
                details.setAddCash(everyMonthCash);
                // todo
                details.setAddInterest(0);
                details.setCurrentCash(details.getCurrentCash() + everyMonthCash);
                // todo
                details.setCurrentInterest(details.getCurrentInterest() + details.getAddInterest());
                details.setCurrentAmount(details.getCurrentCash() + details.getCurrentInterest());
                details.setTargetDate(getDate(i, month));
                list.add(details);
            }
        }
        accountFundTargetDetailsRepository.saveAll(list);
    }
    private static Date getDate(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,calendar.get(Calendar.MONTH) + month + 1);
        calendar.add(Calendar.YEAR, calendar.get(Calendar.YEAR) + year);
        return calendar.getTime();
    }

    public static void main(String[] args) {
        System.out.println(Calendar.getInstance().get(Calendar.YEAR));
    }
    public  double pmt(double r, double n, double p, double f, boolean t) {
        double retval = 0;
        if (r == 0) {
            retval = -1*(f+p)/n;
            return retval;
        }
        double r1 = r + 1;
        retval = ( f + p * Math.pow(r1, n) ) * r
                /
                ((t ? r1 : 1) * (1 - Math.pow(r1, n)));
        return retval;
    }
}
