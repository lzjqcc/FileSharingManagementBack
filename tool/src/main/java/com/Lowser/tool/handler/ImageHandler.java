package com.Lowser.tool.handler;

import com.Lowser.common.utils.FileUpload2Qiniu;
import com.Lowser.tool.annotations.MethodParams;
import com.Lowser.tool.utils.QRCodeUtils;
import com.alibaba.fastjson.JSONObject;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageHandler extends AbstractHandler {
    @MethodParams(description = "图片转base64")
    public byte[] imageBase64Decode(byte[] bytes) throws UnsupportedEncodingException {
        return bytes;
    }

    /**
     * 图片类型转换
     *
     * @param bytes
     * @param jsonObject
     * @return
     */
    @MethodParams(ext = "{\"newWidth\":\"int-0\",\"quality\":\"float-0\",\"imageType\":\"String-0\"}",
            limit = true, limitTimes = 50, description = "修改图片(压缩、拉伸、转换)")
    public String imageChangeType(byte[] bytes, JSONObject jsonObject) throws IOException {
        Integer width = jsonObject.getInteger("newWidth");
        //
        if (width == null) {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            width = bufferedImage.getWidth(null);
        }
        Float quality = jsonObject.getFloat("quality");
        if (quality == null) {
            quality = 1.0f;
        }
        String imageType = jsonObject.getString("imageType");
        if (StringUtils.isEmpty(imageType)) {
            imageType = "jpg";
        }
        ByteArrayOutputStream outputStream = resize(bytes, width,quality, imageType);
        return FileUpload2Qiniu.uploadToFileAutoDeleteAfterOneDay(outputStream.toByteArray());
    }
    private  ByteArrayOutputStream resize(byte[] bytes,
                                                int newWidth, float quality, String imageType) throws IOException {

        if (quality > 1) {
            throw new IllegalArgumentException(
                    "Quality has to be between 0 and 1");
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        BufferedImage image = ImageIO.read(inputStream);
        double scale = newWidth / image.getWidth(null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bytes.length);
        Thumbnails.of(image).scale(scale).outputQuality(quality).outputFormat(imageType).toOutputStream(outputStream);
        return outputStream;
    }

    /**
     * 识别二维码
     * @param bytes
     * @return
     */
    @MethodParams(description = "识别上传的二维码")
    public String readQRCode(byte[] bytes) {
        return QRCodeUtils.readQRCode(bytes);
    }


}
