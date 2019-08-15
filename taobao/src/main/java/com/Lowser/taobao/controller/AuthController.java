package com.Lowser.taobao.controller;

import com.Lowser.common.dao.domain.AppConfig;
import com.Lowser.common.utils.SecurityUtils;
import com.Lowser.taobao.dao.domain.AccessToken;
import com.Lowser.taobao.dao.repository.AccessTokenRepository;
import com.Lowser.common.dao.repository.AppConfigRepository;
import com.Lowser.taobao.utils.UrlUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
public class AuthController extends BaseController{
    @Autowired
    private AppConfigRepository appConfigRepository;
    @Autowired
    private AccessTokenRepository accessTokenRepository;
//    @RequestMapping("/auth")
//    public String auth(@RequestParam("appId") Integer appId) {
//        Optional<AppConfig> optional = appConfigRepository.findById(appId);
//        if (optional.isPresent()) {
//            AppConfig appConfig = optional.get();
//            return "redirect:" + UrlUtils.getAuthUrl(appConfig);
//        }
//        throw new BizException("非法入参");
//    }
    @RequestMapping("/auth")
    public String callback(@RequestParam("top_appkey") String appkey) {
        AppConfig appConfig = appConfigRepository.findByAppKey(appkey);
        return "redirect:" + UrlUtils.getAuthUrl(appConfig);
    }
    @RequestMapping("/accessToken/{appIdString}")
    @ResponseBody
    public void getToken(@RequestParam("code") String code, @PathVariable("appIdString") String appIdString) {
        Integer appId =  Integer.parseInt(SecurityUtils.AESDncode(appIdString));
        AccessToken accessToken = UrlUtils.getAccessToken(appConfigRepository.findById(appId).get(), code);
        accessToken.setUpdateTime(new Date());
        AccessToken in = accessTokenRepository.findByUserNick(accessToken.getUserNick());
        if (in == null) {
            accessTokenRepository.save(accessToken);
        }
        BeanUtils.copyProperties(accessToken, in, "id","insertTime","delete");
        accessTokenRepository.save(in);
    }
    @RequestMapping("/index")
    public String index() {
        return "index.html";
    }
    public static void main(String[] args) {
        System.out.println(SecurityUtils.AESDncode("BZrW7DqWpH2lLRbjbYGIzA=="));
    }

}
