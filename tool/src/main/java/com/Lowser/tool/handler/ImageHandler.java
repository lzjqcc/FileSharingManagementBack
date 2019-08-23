package com.Lowser.tool.handler;

import com.Lowser.common.service.FileUpload2QiniuService;
import com.Lowser.tool.annotations.MethodParams;
import com.Lowser.tool.utils.QRCodeUtils;
import com.alibaba.fastjson.JSONObject;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

@Component
public class ImageHandler extends AbstractHandler {
    @Autowired
    private FileUpload2QiniuService fileUpload2QiniuService;
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
        return fileUpload2QiniuService.uploadToFileAutoDeleteAfterOneDay(outputStream.toByteArray());
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

    public static void main(String[] args) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://upload-images.jianshu.io/upload_images/12208532-ce8515a526ae3964.png";
        ResponseEntity<byte[]> entity = restTemplate.getForEntity(url, byte[].class);
        QRCodeUtils.readQRCode(entity.getBody());
//        BufferedImage bi = ImageIO.read(new URL("https://upload-images.jianshu.io/upload_images/12208532-d2f1ca7192fdfcd1.png"));
//        // 获取当前图片的高,宽,ARGB
//        int h = bi.getHeight();
//        int w = bi.getWidth();
//        int rgb = bi.getRGB(0, 0);
//        int arr[][] = new int[w][h];
//
//        // 获取图片每一像素点的灰度值
//        for (int i = 0; i < w; i++) {
//            for (int j = 0; j < h; j++) {
//                // getRGB()返回默认的RGB颜色模型(十进制)
//                arr[i][j] = getImageRgb(bi.getRGB(i, j));//该点的灰度值
//            }
//
//        }
//
//        BufferedImage bufferedImage=new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);//  构造一个类型为预定义图像类型之一的 BufferedImage，TYPE_BYTE_BINARY（表示一个不透明的以字节打包的 1、2 或 4 位图像。）
//        int FZ=130;
//        for (int i = 0; i < w; i++) {
//            for (int j = 0; j < h; j++) {
//                if(getGray(arr,i,j,w,h)>FZ){
//                    int black=new Color(255,255,255).getRGB();
//                    bufferedImage.setRGB(i, j, black);
//                }else{
//                    int white=new Color(0,0,0).getRGB();
//                    bufferedImage.setRGB(i, j, white);
//                }
//            }
//
//        }
//        ImageIO.write(bufferedImage, "jpg", new File("D:"+File.separator+"new123.jpg"));
    }

    private static int getImageRgb(int i) {
        String argb = Integer.toHexString(i);// 将十进制的颜色值转为十六进制
        // argb分别代表透明,红,绿,蓝 分别占16进制2位
        int r = Integer.parseInt(argb.substring(2, 4),16);//后面参数为使用进制
        int g = Integer.parseInt(argb.substring(4, 6),16);
        int b = Integer.parseInt(argb.substring(6, 8),16);
        int result=(int)((r+g+b)/3);
        return result;
    }



    //自己加周围8个灰度值再除以9，算出其相对灰度值
    public static int  getGray(int gray[][], int x, int y, int w, int h)
    {
        int rs = gray[x][y]
                + (x == 0 ? 255 : gray[x - 1][y])
                + (x == 0 || y == 0 ? 255 : gray[x - 1][y - 1])
                + (x == 0 || y == h - 1 ? 255 : gray[x - 1][y + 1])
                + (y == 0 ? 255 : gray[x][y - 1])
                + (y == h - 1 ? 255 : gray[x][y + 1])
                + (x == w - 1 ? 255 : gray[x + 1][ y])
                + (x == w - 1 || y == 0 ? 255 : gray[x + 1][y - 1])
                + (x == w - 1 || y == h - 1 ? 255 : gray[x + 1][y + 1]);
        return rs / 9;
    }
    @Override
    public String handlerType() {
        return "handleImage";
    }
}
