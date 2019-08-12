package com.Lowser.sharefile.helper;

import com.Lowser.common.error.BizException;
import com.Lowser.sharefile.controller.param.FileTemplateParam;
import com.Lowser.sharefile.utils.PPTUtils;
import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.SeekableInMemoryByteChannel;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * ppt or pptx to image
 */
@Component
public class ImageHelper {

    public List<String> getImages(FileTemplateParam fileTemplateParam) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, Object>> httpEntity = null;
        if (!StringUtils.isEmpty(fileTemplateParam.getCookie())) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("cookie", fileTemplateParam.getCookie());
            httpEntity = new HttpEntity(httpHeaders);
        }
        restTemplate.exchange(fileTemplateParam.getUrl(), HttpMethod.GET, httpEntity, byte[].class);
        ResponseEntity<byte[]> entity = restTemplate.getForEntity(fileTemplateParam.getUrl(), byte[].class);
        if (entity.getStatusCode() == HttpStatus.OK) {
            File2Image file2Image = FileTypeEnum.getFile2Image(fileTemplateParam.getFileFormat());
            try {
                return file2Image.toImageUrl(entity.getBody());
            } catch (IOException | RarException e) {
                throw new BizException("转换图片失败，文件类型" + fileTemplateParam.getFileFormat(), entity.toString());
            }
        }
        throw new BizException("请求失败：status = "+ entity.getStatusCode() + "\n" +entity.toString());
    }

    /**
     * 获取文件头类型
     *
     * @param bytes
     * @return
     */
    private static String getFileType(byte[] bytes) {
        byte[] fileType = Arrays.copyOf(bytes, 4);
        return byteToHexString(fileType);

    }

    private static String byteToHexString(byte[] b) {
        StringBuilder stringBuilder = new StringBuilder();
        if (b == null || b.length <= 0) {
            return null;
        }
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    private static interface File2Image {
        List<String> toImageUrl(byte[] bytes) throws IOException, RarException;
    }

    private static class ZipFile2Image implements File2Image {

        @Override
        public List<String> toImageUrl(byte[] bytes) throws IOException {
            SeekableInMemoryByteChannel inMemoryByteChannel = new SeekableInMemoryByteChannel(bytes);
            ZipFile zipFile = new ZipFile(inMemoryByteChannel);
            while (zipFile.getEntries().hasMoreElements()) {
                ZipArchiveEntry zipArchiveEntry = zipFile.getEntries().nextElement();
                if (inFileTypeEnum(zipArchiveEntry.getName())) {
                    ByteArrayOutputStream outputStream = null;
                    InputStream zipFileInputStream = null;
                    try {
                        zipFileInputStream = zipFile.getInputStream(zipArchiveEntry);
                        outputStream = new ByteArrayOutputStream();
                        IOUtils.copy(zipFileInputStream, outputStream);
                        return PPTUtils.ppt2Png(outputStream.toByteArray());
                    }finally {
                        IOUtils.closeQuietly(outputStream);
                        IOUtils.closeQuietly(zipFileInputStream);
                    }
                }
            }
            throw new BizException("压缩文件中不存在文件");
        }
    }

    private static boolean inFileTypeEnum(String name) {
        FileTypeEnum[] fileTypeEnums = FileTypeEnum.values();
        for (FileTypeEnum fileTypeEnum : fileTypeEnums) {
            if (fileTypeEnum.singleFile && name.endsWith(fileTypeEnum.description)) {
                return true;
            }
        }
        return false;
    }

    private static class RaRFile2Image implements File2Image {

        @Override
        public List<String> toImageUrl(byte[] bytes) throws IOException, RarException {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            ByteArrayOutputStream outputStream = null;
            try {
                Archive archive = new Archive(inputStream);
                List<FileHeader> fileHeaders = archive.getFileHeaders();
                if (fileHeaders == null) {
                    throw new BizException("压缩文件中不存在文件");
                }
                if (CollectionUtils.isEmpty(fileHeaders)) {
                    throw new BizException("解压rar文件失败: 请确认压缩文件中只存在一个文件");
                }
                for (FileHeader fileHeader : fileHeaders) {
                    if (inFileTypeEnum(fileHeader.getFileNameString())) {
                        outputStream = new ByteArrayOutputStream();
                        archive.extractFile(fileHeader, outputStream);
                        return PPTUtils.ppt2Png(outputStream.toByteArray());
                    }
                }
            } finally {
                IOUtils.closeQuietly(outputStream);
                IOUtils.closeQuietly(inputStream);
            }
            return null;
        }
    }

    private static class PPTFile2Image implements File2Image {

        @Override
        public List<String> toImageUrl(byte[] bytes) {
            return PPTUtils.ppt2Png(bytes);
        }
    }

    private static enum FileTypeEnum {
        RAR("52617221", "rar", false, new RaRFile2Image()),
        ZIP("504B0304", "zip", false, new ZipFile2Image()),
        PPT("255044462D312E", "ppt", true, new PPTFile2Image()),
        PPTX("504B0304", "pptx",true, new PPTFile2Image());
        private String type;
        private String description;
        private File2Image file2Image;
        private boolean singleFile;
        FileTypeEnum(String type, String description,boolean singleFile, File2Image file2Image) {
            this.type = type;
            this.description = description;
            this.file2Image = file2Image;
            this.singleFile = singleFile;
        }

        public boolean isSingleFile() {
            return singleFile;
        }

        public static File2Image getFile2Image(String type) {
            FileTypeEnum[] fileTypeEnums = FileTypeEnum.values();
            for (FileTypeEnum fileTypeEnum : fileTypeEnums) {
                if (fileTypeEnum.description.equalsIgnoreCase(type)) {
                    return fileTypeEnum.file2Image;
                }
            }
            throw new BizException("文件类型不支持", type);
        }

        public String getType() {
            return type;
        }

        public String getDescription() {
            return description;
        }

        public File2Image getFile2Image() {
            return file2Image;
        }
    }

    public static void main(String[] args) throws IOException, RarException {
        File file = new File("/home/li/Downloads/zip.zip");
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bytes = IOUtils.toByteArray(fileInputStream);
        FileTypeEnum.getFile2Image("zip").toImageUrl(bytes);

    }


}
