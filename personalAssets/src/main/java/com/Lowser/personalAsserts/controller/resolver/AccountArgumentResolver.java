package com.Lowser.personalAsserts.controller.resolver;

import com.Lowser.personalAsserts.dao.domain.Account;
import com.Lowser.personalAsserts.utils.LoginUtil;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class AccountArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
      return methodParameter.getParameterType() == Account.class;
    }

    @Nullable
    @Override
    public Object resolveArgument(MethodParameter methodParameter, @Nullable ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, @Nullable WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpSession session = request.getSession();
        return LoginUtil.getAccount(session);

    }
}
