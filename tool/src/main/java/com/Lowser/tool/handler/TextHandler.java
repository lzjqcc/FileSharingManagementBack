package com.Lowser.tool.handler;

import ch.qos.logback.core.rolling.helper.FileStoreUtil;
import com.Lowser.common.error.BizException;
import com.Lowser.common.service.FileUpload2QiniuService;
import com.Lowser.tool.annotations.MethodParams;
import com.Lowser.tool.utils.QRCodeUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;

/**
 * 文本处理
 */
@Component
public class TextHandler extends AbstractHandler {
    @Autowired
    private FileUpload2QiniuService fileUpload2QiniuService;
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
    @MethodParams(ext = "{\"red\":\"int-1\",\"green\":\"int-1\",\"blue\":\"int-1\",\"width\":\"int-1\",\"height\":\"int-1\",\"logoUrl\":\"String-0\",\"backgroundUrl\":\"String-0\"}", limit = true, limitTimes = 50,
    description = "创建二维码")
    public String createQRCode(String text, JSONObject jsonObject) throws IOException {
        Integer width = jsonObject.getInteger("width");
        Integer height = jsonObject.getInteger("height");
        String imageUrl = jsonObject.getString("logoUrl");
        String backgroundImageUrl = jsonObject.getString("backgroundUrl");
        Integer red = jsonObject.getInteger("red");
        if (red == null) {
            red = 0;
        }
        Integer green = jsonObject.getInteger("green");
        if (green == null) {
            green = 0;
        }
        Integer blue = jsonObject.getInteger("blue");
        if (blue == null) {
            blue = 0;
        }
        byte[] bytes = QRCodeUtils.createQRCode(text, width, height, imageUrl, backgroundImageUrl, new Color(red, green, blue));
        ImageIO.write(ImageIO.read(new ByteArrayInputStream(bytes)), "png", new File("D:\\pptImage\\as.png"));
        return null;
    }

    @Override
    public String handlerType() {
        return "handleString";
    }
}
