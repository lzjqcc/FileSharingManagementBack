package com.Lowser.common.utils;

import com.Lowser.common.error.BizException;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Objects;

public class CodeUtils {
    public static void validateCode(String code, HttpSession session) {
        Object codeObject = session.getAttribute("code");
        if (code == null) {
            throw new BizException("输入验证码");
        }
        Date codeTime = (Date) session.getAttribute("codeTime");
        if (System.currentTimeMillis() - codeTime.getTime() > 1000 * 60 * 3 || codeObject == null) {
            throw new BizException("验证码失效，请刷新验证码");
        }
        if (!code.equalsIgnoreCase(codeObject.toString())) {
            throw new BizException("验证码输入错误");
        }

    }
}
