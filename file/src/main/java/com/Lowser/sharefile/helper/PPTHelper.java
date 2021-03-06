package com.Lowser.sharefile.helper;

import com.Lowser.common.error.BizException;
import com.Lowser.common.service.FileUpload2QiniuService;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.hslf.usermodel.HSLFTextParagraph;
import org.apache.poi.hslf.usermodel.HSLFTextRun;
import org.apache.poi.hslf.usermodel.HSLFTextShape;
import org.apache.poi.sl.usermodel.ShapeContainer;
import org.apache.poi.sl.usermodel.Slide;
import org.apache.poi.sl.usermodel.SlideShow;
import org.apache.poi.sl.usermodel.SlideShowFactory;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
@Component
public class PPTHelper {
    @Autowired
    private FileUpload2QiniuService fileUpload2QiniuService;
    public List<String> ppt2Png(byte[] pptByte) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pptByte);
        return toPng(byteArrayInputStream);
    }
    private  List<String> toPng(InputStream inputStream) {
        SlideShow slideShow = null;
        try {
            slideShow = SlideShowFactory.create(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BizException("ppt 转图片错误");
        }
        Dimension pgsize = slideShow.getPageSize();
        List<String> imageUrls = new ArrayList<>();
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
            graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
            // render
            slide.draw(graphics);
            // save the output
            ByteArrayOutputStream out = null;
            try {
                out = new ByteArrayOutputStream();
                javax.imageio.ImageIO.write(img, "png", out);
                imageUrls.add(fileUpload2QiniuService.uploadToFile(out.toByteArray()));
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                IOUtils.closeQuietly(out);
            }

        }
        return imageUrls;
    }
}
