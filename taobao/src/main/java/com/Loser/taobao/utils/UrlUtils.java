package com.Loser.taobao.utils;

import com.taobao.api.internal.util.WebUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UrlUtils {
    public static String getAuthUrl(String appKey) {
        StringBuilder builder = new StringBuilder("https://oauth.taobao.com/authorize?response_type=code&client_id=");
        builder.append(appKey).append("redirect_uri=https://www.shouzan.top:8080/test&state=1212&view=web");
        return builder.toString();
    }
    public static String getAccessToken(String appKey, String appSecret, String code) {
        String url="https://oauth.taobao.com/token";
        Map<String,String> props=new HashMap<String,String>();
        props.put("grant_type","authorization_code");
/*测试时，需把test参数换成自己应用对应的值*/
        props.put("code", code);
        props.put("client_id", appKey);
        props.put("client_secret", appSecret);
        props.put("redirect_uri","https://www.shouzan.top:8080/token");
        props.put("view","web");
        String s="";
        try {
            s= WebUtils.doPost(url, props, 30000, 30000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
