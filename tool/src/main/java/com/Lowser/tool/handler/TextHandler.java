package com.Lowser.tool.handler;

import com.Lowser.common.error.BizException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;

/**
 * 文本处理
 */
public class TextHandler extends AbstractHandler {

    public String urlEncode(String url) {
        try {
            return URLEncoder.encode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new BizException("不支持编码： " + e.getMessage());
        }
    }
    public String urlDecode(String url) {
        try {
            return URLDecoder.decode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new BizException("不支持编码： " + e.getMessage());
        }
    }
    public String base64Decode(String text) {
        byte[] bytes = null;
        try {
            bytes = text.getBytes("utf-8");
            return new String(Base64.getDecoder().decode(bytes), "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new BizException("不支持编码");
        }
    }
    public String base64Encode(String text) {
        try {
            return Base64.getEncoder().encodeToString(text.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new BizException("不支持编码");
        }
    }

}
