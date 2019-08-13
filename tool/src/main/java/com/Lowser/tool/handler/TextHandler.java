package com.Lowser.tool.handler;

import com.Lowser.common.error.BizException;
import com.Lowser.tool.annotations.MethodParams;
import com.Lowser.tool.utils.QRCodeUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;

/**
 * 文本处理
 */
public class TextHandler extends AbstractHandler {

    @MethodParams
    public String base64Decode(String text) {
        byte[] bytes = null;
        try {
            bytes = text.getBytes("utf-8");
            return new String(Base64.getDecoder().decode(bytes), "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new BizException("不支持编码");
        }
    }
    @MethodParams
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
    @MethodParams(description = "识别链接或者base64图片二维码")
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
    @MethodParams(ext = "{\"width\":\"int-1\",\"height\":\"int-1\",\"imageUrl\":\"String-0\"}", limit = true, limitTimes = 50,
    description = "创建二维码")
    public String createQRCode(String text, JSONObject jsonObject) throws IOException {
        Integer width = jsonObject.getInteger("width");
        Integer height = jsonObject.getInteger("height");
        String imageUrl = jsonObject.getString("imageUrl");
        if (StringUtils.isEmpty(imageUrl)) {
            return QRCodeUtils.createQRCode(text, width, height, null);
        }
        return QRCodeUtils.createQRCode(text, width, height, ImageIO.read(new URL(imageUrl)));
    }

    public static void main(String[] args) throws IOException {
        String url = "https://crm.mytijian.com/static/img/rili.0bac3ac.png";
        System.out.println(ImageIO.read(new URL(url)));
    }
}
