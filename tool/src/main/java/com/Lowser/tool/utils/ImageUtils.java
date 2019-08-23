package com.Lowser.tool.utils;

import com.Lowser.common.error.BizException;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUtils {
    public ByteArrayOutputStream resize(byte[] bytes,
                                         int newWidth, float quality, String imageType) throws IOException {

        if (quality > 1) {
            throw new IllegalArgumentException(
                    "Quality has to be between 0 and 1");
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        builder(bytes,newWidth,quality, imageType).toOutputStream(outputStream);
        return outputStream;
    }
    public static BufferedImage resizeAndToImage(byte[] bytes, int newWidth, float quality, String imageType) {
        try {
            return builder(bytes, newWidth, quality, imageType).asBufferedImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new BizException("转换失败");
    }
    private static Thumbnails.Builder builder(byte[] bytes, int newWidth, float quality, String imageType)  {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        BufferedImage image = null;
        try {
            image = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert image != null;
        double scale = newWidth / image.getWidth(null);
        return Thumbnails.of(image).scale(scale).outputQuality(quality).outputFormat(imageType);
    }

}
