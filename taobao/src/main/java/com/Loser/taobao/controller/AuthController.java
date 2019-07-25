package com.Loser.taobao.controller;

import com.Loser.common.error.BizException;
import com.Loser.taobao.dao.domain.AppConfig;
import com.Loser.taobao.dao.repository.AppConfigRepository;
import com.Loser.taobao.utils.UrlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AuthController {
    private final static String authUrl = "https://oauth.taobao.com/authorize?response_type=code&client_id=27720411&redirect_uri=https://www.shouzan.top:8080/test&state=1212&view=web";
    @Autowired
    private AppConfigRepository appConfigRepository;
    @RequestMapping("/auth")
    public String auth(@RequestParam("appId") Integer appId) {
        Optional<AppConfig> optional = appConfigRepository.findById(appId);
        if (optional.isPresent()) {
            AppConfig appConfig = optional.get();
            return "redirect:" + UrlUtils.getAuthUrl(appConfig.getAppKey());
        }
        throw new BizException("非法入参");
    }
}
