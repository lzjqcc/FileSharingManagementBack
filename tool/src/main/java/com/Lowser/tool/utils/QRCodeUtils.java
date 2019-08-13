package com.Lowser.tool.utils;

import com.Lowser.common.error.BizException;
import com.Lowser.common.utils.FileUpload2Qiniu;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.common.StringUtils;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class QRCodeUtils {
    public static String readQRCode(byte[] bytes) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
            return readQRCode(bufferedImage);
        } catch (IOException e) {
            throw new BizException("识别失败");
        }
    }
    public static String readQRCode(String url) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new URL(url));
            return readQRCode(bufferedImage);
        } catch (IOException e) {
            throw new BizException("链接识别失败");
        }

    }
    private static String readQRCode(BufferedImage bufferedImage) {
        try {
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            Result result = new MultiFormatReader().decode(binaryBitmap, hints);//解码
            return result.getText();
        } catch (NotFoundException e) {
            throw new BizException("识别失败");
        }
    }
    public static String createQRCode(String contents, int width, int height, BufferedImage logo) {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 2);

        try {
            BitMatrix bitMatrix = (new MultiFormatWriter()).encode(contents, BarcodeFormat.QR_CODE, width, height, hints);
            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (logo != null) {
                image = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_sRGB), null).filter(image, null);
                Graphics2D graphics2D = image.createGraphics();
                int logoWith = 40,logoHeight = 40;
                int x = (250-logoWith)/2, y = (250 - logoHeight)/2;
                graphics2D.drawImage(logo,x,y,logoWith,logoHeight,null);
                image.flush();
                logo.flush();
            }
            ImageIO.write(image, "png", baos);
            return FileUpload2Qiniu.uploadToFileAutoDeleteAfterOneDay(baos.toByteArray());
        } catch (Exception var9) {
            var9.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println(readQRCode(""));
    }
}
