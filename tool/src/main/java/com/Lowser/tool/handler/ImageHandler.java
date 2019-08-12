package com.Lowser.tool.handler;

import com.Lowser.common.utils.FileUpload2Qiniu;
import com.alibaba.fastjson.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class ImageHandler extends AbstractHandler {

    public byte[] imageBase64Decode(byte[] bytes) throws UnsupportedEncodingException {
        return bytes;
    }

    /**
     * 图片类型转换
     * @param bytes
     * @param jsonObject
     * @return
     */
    public String imageChangeType(byte[] bytes, JSONObject jsonObject) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(2048);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        jsonObject.getString("with");
        jsonObject.getString("height");
        ImageIO.write(bufferedImage, jsonObject.getString("toType"), outputStream);
        return FileUpload2Qiniu.uploadToFile(outputStream.toByteArray());
    }
    public String imageChangeSize(byte[] bytes, JSONObject jsonObject) {
        jsonObject.get("with");
        jsonObject.get("height");
    }

}
