package com.Lowser.tool.handler;

import com.Lowser.common.error.BizException;
import com.Lowser.tool.utils.QRCodeUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;

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

    /**
     * 识别二维码
     * @param text 链接 or bas4图片
     * @return
     */
    public  String readQRCode(String text) {
        if (UrlUtils.isAbsoluteUrl(text)) {
            return QRCodeUtils.readQRCode(text);
        }
        try {
            return QRCodeUtils.readQRCode(Base64.getDecoder().decode(text));
        }catch (Exception e) {
            String[] strings = text.split(",");
            try {
                if (strings.length != 2 ) {
                    throw new BizException("如果base64图片，请检验数据是否准确");
                }
                return QRCodeUtils.readQRCode(Base64.getDecoder().decode(strings[1]));
            }catch (Exception ex) {
                throw new BizException("如果base64图片，请检验数据是否准确");
            }
        }
    }

    /**
     * 创建二维码
     * @param text
     * @param jsonObject
     * @return
     */
    public String createQRCode(String text, JSONObject jsonObject) {
        Integer width = jsonObject.getInteger("width");
        Integer height = jsonObject.getInteger("height");
        String imageUrl = jsonObject.getString("imageUrl");
        if (StringUtils.isEmpty(imageUrl)) {
            return QRCodeUtils.createQRCode(text, width, height);
        }
        return QRCodeUtils.createQRCode(text, width, height, imageUrl);
    }

}
