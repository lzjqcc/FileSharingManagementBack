package com.Lowser.sharefile.utils;

import com.Lowser.common.error.BizException;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.poi.hslf.usermodel.HSLFTextParagraph;
import org.apache.poi.hslf.usermodel.HSLFTextRun;
import org.apache.poi.hslf.usermodel.HSLFTextShape;
import org.apache.poi.sl.usermodel.*;
import org.apache.poi.sl.usermodel.Shape;
import org.apache.poi.xslf.usermodel.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PPTUtils {
    public static List<String> ppt2Png(byte[] pptByte) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pptByte);
        return toPng(byteArrayInputStream);
    }
    private static List<String> toPng(InputStream inputStream) {
        SlideShow slideShow = null;
        try {
            slideShow = SlideShowFactory.create(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BizException("ppt 转图片错误");
        }
        Dimension pgsize = slideShow.getPageSize();

        for (Object object : slideShow.getSlides()) {
            Slide slide = null;
            if (object instanceof Slide) {
                slide = (Slide) object;
            }
            ShapeContainer container = null;
            if (object instanceof ShapeContainer) {
                container = (ShapeContainer) object;
                for (Object xslfShape : container.getShapes()) {
                    if (xslfShape instanceof XSLFTextShape) {
                        XSLFTextShape tsh = (XSLFTextShape) xslfShape;
                        for (XSLFTextParagraph p : tsh) {
                            for (XSLFTextRun r : p) {
                                r.setFontFamily("宋体");
                            }
                        }
                    }
                    if (xslfShape instanceof HSLFTextShape) {
                        HSLFTextShape textShape = (HSLFTextShape) xslfShape;
                        for (HSLFTextParagraph paragraph : textShape) {
                            for (HSLFTextRun run : paragraph) {
                                run.setFontFamily("宋体");
                            }
                        }
                    }
                }
            }

            BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = img.createGraphics();
            graphics.setPaint(Color.white);
            //graphics.setPaint(slide.getBackground().getFillColor());
            graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
            // render
            slide.draw(graphics);
            // save the output
            FileOutputStream out = null;
            try {
                out = new FileOutputStream("/home/li/Downloads/pptImage/" + slide.getSlideName() + ".png");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                javax.imageio.ImageIO.write(img, "png", out);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }
    public static void main(String[] args) throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream(new File("/home/li/Downloads/c.ppt"));
        toPng(inputStream);
    }
}
