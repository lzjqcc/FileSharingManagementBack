package com.Loser.utils;

import com.Loser.dao.domain.Account;
import com.Lowser.common.error.BizException;

import javax.servlet.http.HttpSession;

public class LoginUtil {
    private static String key = "login_account";
    public static Account getAccount(HttpSession session) {
       Object account =  session.getAttribute(key);
       if (account == null) {
           throw new BizException("必须先登录");
       }
       return (Account) account;
    }
    public static void setAccount(HttpSession session, Account account) {
        session.setAttribute(key, account);
    }
}
