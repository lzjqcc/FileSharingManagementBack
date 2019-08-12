package com.Lowser.sharefile.helper;

import com.Lowser.common.error.BizException;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;

public class FileUpload2Qiniu {
    private final static String ACCESS_KEY = "AdCwqzqvYskN7WjK7HsuxYnd87-FkMnWxYdFaEk6";
    private final static String SECRETKEY = "ln51kzu1sRMymE-zDckMr-scADmKof6EkhepLJIR";
    private final static String DOMAIN = "http://file.shouzan.top/";

    public static String uploadToFile(byte[] bytes) {
        return DOMAIN + upload("file", bytes);
    }

    private static String upload(String bucket, byte[] bytes) {
        Auth auth = Auth.create(ACCESS_KEY, SECRETKEY);
        String upToken = auth.uploadToken(bucket);
        System.out.println(upToken);
        Configuration cfg = new Configuration(Zone.zone0());
        UploadManager uploadManager = new UploadManager(cfg);
        try {
            Response response = uploadManager.put(bytes, null, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return putRet.key;
        } catch (QiniuException ex) {
            throw new BizException("上传文件到七牛失败 e = " + ex.toString());
        }
    }

    public void test() {
        String accessKey = "";
        String secretKey = "";
        String bucket = "file";

    }
}
