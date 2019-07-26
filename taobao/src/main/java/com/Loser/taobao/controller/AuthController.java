package com.Loser.taobao.controller;

import com.Loser.common.error.BizException;
import com.Loser.common.utils.SecurityUtils;
import com.Loser.taobao.dao.domain.AccessToken;
import com.Loser.taobao.dao.domain.AppConfig;
import com.Loser.taobao.dao.repository.AccessTokenRepository;
import com.Loser.taobao.dao.repository.AppConfigRepository;
import com.Loser.taobao.utils.UrlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.Column;
import java.util.Optional;

@Controller
public class AuthController extends BaseController{
    @Autowired
    private AppConfigRepository appConfigRepository;
    @Autowired
    private AccessTokenRepository accessTokenRepository;
    @RequestMapping("/auth")
    public String auth(@RequestParam("appId") Integer appId) {
        Optional<AppConfig> optional = appConfigRepository.findById(appId);
        if (optional.isPresent()) {
            AppConfig appConfig = optional.get();
            return "redirect:" + UrlUtils.getAuthUrl(appConfig);
        }
        throw new BizException("非法入参");
    }
    @GetMapping("/accessToken/{appIdString}")
    public void getToken(@RequestParam("code") String code, @PathVariable("appIdString") String appIdString) {
        Integer appId =  Integer.parseInt(SecurityUtils.AESDncode(appIdString));
        AccessToken accessToken = UrlUtils.getAccessToken(appConfigRepository.findById(appId).get(), code);
        accessTokenRepository.save(accessToken);
    }

}
