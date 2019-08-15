package com.Lowser.tool.utils;

import com.Lowser.common.error.BizException;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
    public static byte[] createQRCode(String contents, int width, int height, String logoUrl, String backgroundImageUrl, int r, int g, int b) {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 2);

        try {
            BitMatrix matrix = (new MultiFormatWriter()).encode(contents, BarcodeFormat.QR_CODE, width, height, hints);
            BufferedImage backgroundImage = null;
            if (backgroundImageUrl != null) {
                backgroundImage = ImageIO.read(new URL(backgroundImageUrl));
            }else {
                backgroundImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            }
            int[] rowPixels = new int[width];
            BitArray row = new BitArray(width);
            for (int y = 0; y < height; y++) {
                row = matrix.getRow(y, row);
                for (int x = 0; x < width; x++) {
                    rowPixels[x] = row.get(x) ? new Color(r, g, b).getRGB() : backgroundImage.getRGB(x,y);
                }
                backgroundImage.setRGB(0, y, width, 1, rowPixels, 0, width);
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (logoUrl != null) {
                BufferedImage logo = ImageIO.read(new URL(logoUrl));
                backgroundImage = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_sRGB), null).filter(backgroundImage, null);
                Graphics2D graphics2D = backgroundImage.createGraphics();
                graphics2D.setColor(Color.WHITE);
                int logoWith = 40,logoHeight = 40;
                int x = (width-logoWith)/2, y = (height - logoHeight)/2;
                graphics2D.drawImage(logo,x,y,logoWith,logoHeight,null);
                backgroundImage.flush();
                logo.flush();
            }
            ImageIO.write(backgroundImage, "png", baos);
            return baos.toByteArray();
        } catch (Exception var9) {
            var9.printStackTrace();
            return null;
        }
    }
}
