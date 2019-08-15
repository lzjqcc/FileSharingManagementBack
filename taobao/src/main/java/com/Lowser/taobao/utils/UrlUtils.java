package com.Lowser.taobao.utils;

import com.Lowser.common.dao.domain.AppConfig;
import com.Lowser.common.error.BizException;
import com.Lowser.common.error.SystemError;
import com.Lowser.common.utils.SecurityUtils;
import com.Lowser.taobao.dao.domain.AccessToken;
import com.alibaba.fastjson.JSON;
import com.taobao.api.internal.util.WebUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class UrlUtils {
    public static String getAuthUrl(AppConfig appConfig) {
        StringBuilder builder = new StringBuilder("https://oauth.taobao.com/authorize?response_type=code&client_id=");
        builder.append(appConfig.getAppKey()).append("&").append("redirect_uri=http://www.shouzan.top:8080/taobao/accessToken/").append(SecurityUtils.AESEncode(appConfig.getId() + "")).append("&state=1212&view=web");
        return builder.toString();
    }

    public static AccessToken getAccessToken(AppConfig appConfig, String code) {
        if (appConfig == null) {
            throw new BizException("app配置不存在");
        }
        String url = "https://oauth.taobao.com/token";
        Map<String, String> props = new HashMap<String, String>();
        props.put("grant_type", "authorization_code");
/*测试时，需把test参数换成自己应用对应的值*/
        props.put("code", code);
        props.put("client_id", appConfig.getAppKey());
        props.put("client_secret", appConfig.getAppSecret());
        props.put("redirect_uri", appConfig.getRedirectUrl());
        props.put("view", "web");
        String s = "";
        try {
            s = WebUtils.doPost(url, props, 30000, 30000);
        } catch (IOException e) {
            throw new SystemError("获取access_token失败");
        }
        return toToken(s);
    }
    private static AccessToken toToken(String response) {
        Map<String, Object> map = JSON.parseObject(response, Map.class);
        AccessToken accessToken = new AccessToken();
        accessToken.setAccessToken((String) map.get("access_token"));
        accessToken.setExpireTime((Long) map.get("expire_time"));
        accessToken.setRefreshToken((String) map.get("refresh_token"));
        accessToken.setUserId((String) map.get("taobao_user_id"));
        accessToken.setSubUserId(map.get("sub_taobao_user_id") != null? (String) map.get("sub_taobao_user_id") : null);
        try {
            if (map.get("taobao_user_nick") != null) {
                accessToken.setUserNick(URLDecoder.decode((String) map.get("taobao_user_nick"), "UTF-8"));
            }
            if (map.get("sub_taobao_user_nick") != null) {
                accessToken.setSubUserNick(URLDecoder.decode((String) map.get("sub_taobao_user_nick"), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return accessToken;
    }
    public static void main(String[] args) {
        String json = "{\"w1_expires_in\":86400,\"refresh_token_valid_time\":1564110509679,\"taobao_user_nick\":\"%E5%9D%9A%E6%8C%81%E7%9A%84%E6%9C%A8%E5%A4%B4%E4%BA%BA\",\"re_expires_in\":86400,\"expire_time\":1564110509679,\"token_type\":\"Bearer\",\"access_token\":\"6201b25bdf68e70fa4bb2a19af1a53d9egib6879bd4903e2243075008\",\"taobao_open_uid\":\"AAHr2kzVAJUvedcvlnCkoZWB\",\"w1_valid\":1564110509679,\"refresh_token\":\"6201725b143915e27ba9f1d70f5df3ad6ZZe0eb582a35f72243075008\",\"w2_expires_in\":86400,\"w2_valid\":1564110509679,\"r1_expires_in\":86400,\"r2_expires_in\":86400,\"r2_valid\":1564110509679,\"r1_valid\":1564110509679,\"taobao_user_id\":\"2243075008\",\"expires_in\":86400}";
        AccessToken accessToken = toToken(json);
        System.out.println(JSON.toJSONString(accessToken));
    }
}
