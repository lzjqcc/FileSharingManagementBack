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
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.geom.RoundRectangle2D;
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
            //BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
            BufferedImage bufferedImage = ImageUtils.resizeAndToImage(bytes, 200, 1, "jpg");
            ImageIO.write(bufferedImage, "jpg", new File("D:\\pptImage\\d.jpg"));
            return readQRCode(bufferedImage);
        } catch (Throwable e) {
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
    public static String readQRCode(BufferedImage bufferedImage) {
        try {
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(DecodeHintType.TRY_HARDER, true);
            hints.put(DecodeHintType.PURE_BARCODE, true);
            MultiFormatReader reader = new MultiFormatReader();
            Result result = reader.decode(binaryBitmap, hints);//解码
            return result.getText();
        } catch (NotFoundException e) {
            e.printStackTrace();
            throw new BizException("识别失败");
        }
    }
    public static byte[] createQRCode(String contents, int width, int height, String logoUrl, String backgroundImageUrl, Color qrCodeColor) {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 2);

        try {
            BitMatrix matrix = (new MultiFormatWriter()).encode(contents, BarcodeFormat.QR_CODE, width, height, hints);
            BufferedImage backgroundImage = null;
            if (backgroundImageUrl != null) {
                backgroundImage = ImageIO.read(new URL(backgroundImageUrl));
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                Thumbnails.of(backgroundImage).scale(width / backgroundImage.getWidth(null)).outputQuality(0.8).outputFormat("png").toOutputStream(outputStream);
                backgroundImage = ImageIO.read(new ByteArrayInputStream(outputStream.toByteArray()));
            }else {
                backgroundImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            }
            int[] rowPixels = new int[width];
            BitArray row = new BitArray(width);
            for (int y = 0; y < height; y++) {
                row = matrix.getRow(y, row);
                for (int x = 0; x < width; x++) {
                    rowPixels[x] = row.get(x) ? qrCodeColor.getRGB() : backgroundImageUrl == null? Color.WHITE.getRGB() : backgroundImage.getRGB(x,y);
                }
                backgroundImage.setRGB(0, y, width, 1, rowPixels, 0, width);
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (logoUrl != null) {
                BufferedImage logo = ImageIO.read(new URL(logoUrl));
                backgroundImage = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_sRGB), null).filter(backgroundImage, null);
                Graphics2D graphics2D = backgroundImage.createGraphics();
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2D.setColor(Color.WHITE);
                int x = (width-60)/2, y = (height - 60)/2;
                graphics2D.drawImage(logo.getScaledInstance(60,60, Image.SCALE_SMOOTH),x,y,60,60,null);
                graphics2D.dispose();
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
