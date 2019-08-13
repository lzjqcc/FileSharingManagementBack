package com.Lowser.common.utils;

import com.Lowser.common.error.BizException;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class FileUpload2Qiniu {
    private final static String ACCESS_KEY = "AdCwqzqvYskN7WjK7HsuxYnd87-FkMnWxYdFaEk6";
    private final static String SECRETKEY = "ln51kzu1sRMymE-zDckMr-scADmKof6EkhepLJIR";
    private final static String DOMAIN = "http://file.shouzan.top/";
    private final static String FILE_BUCKET = "file";
    private final static String FILE_TOKEN = FILE_BUCKET + "_token";
    private static Map<String, Token> cache = new ConcurrentHashMap<>();
    private static Map<String, String> map = new HashMap<>();
    static {
        map.put(FILE_BUCKET, FILE_TOKEN);
        cache.put(FILE_TOKEN, new Token(0L, ""));
    }
    public static String uploadToFile(byte[] bytes) {
        return DOMAIN + upload(FILE_BUCKET, bytes, null);
    }
    public static String uploadToFileAutoDeleteAfterOneDay(byte[] bytes) {

        return DOMAIN + upload(FILE_BUCKET, bytes, "oneday/" + System.currentTimeMillis() + GenerateNum.getRandom(3));
    }
    private static String upload(String bucket, byte[] bytes, String key) {
        String upToken = getToken(bucket);
        Configuration cfg = new Configuration(Zone.zone0());
        UploadManager uploadManager = new UploadManager(cfg);
        try {
            Response response = uploadManager.put(bytes, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return putRet.key;
        } catch (QiniuException ex) {
            throw new BizException("上传文件到七牛失败 e = " + ex.toString());
        }
    }
    private static String getToken(String bucket) {
        Auth auth = Auth.create(ACCESS_KEY, SECRETKEY);
        String fileTokenKey = map.get(bucket);
        if (StringUtils.isNullOrEmpty(fileTokenKey)) {
            throw new BizException(bucket + "不存在");
        }
        // 秒
        Long expaires = cache.get(fileTokenKey).expairs;
        // 过期
        if (System.currentTimeMillis() / 1000 > expaires -  1 * 60 * 5) {
            String token = auth.uploadToken(bucket);
            cache.put(fileTokenKey, new Token(System.currentTimeMillis()/1000 + 3600L, token));
            return token;
        }
        return cache.get(fileTokenKey).tokenString;
    }
    private static class Token {
        //秒
        private Long expairs;
        private String tokenString;

        Token(Long expairs, String tokenString) {
            this.expairs = expairs;
            this.tokenString = tokenString;
        }
    }
}
