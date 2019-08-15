package com.Lowser.common.service;

import com.Lowser.common.dao.domain.AppConfig;
import com.Lowser.common.dao.domain.StorageConfig;
import com.Lowser.common.dao.repository.AppConfigRepository;
import com.Lowser.common.dao.repository.StorageConfigReposiroty;
import com.Lowser.common.error.BizException;
import com.Lowser.common.utils.GenerateNum;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FileUpload2QiniuService {
    @Autowired
    private StorageConfigReposiroty storageConfigReposiroty;
    @Autowired
    private AppConfigRepository appConfigRepository;
    private final static String FILE_BUCKET = "file";
    public  String uploadToFile(byte[] bytes) {
        return  upload(FILE_BUCKET, bytes, null);
    }
    public  String uploadToFileAutoDeleteAfterOneDay(byte[] bytes) {

        return upload(FILE_BUCKET, bytes, "oneday/" + System.currentTimeMillis() + GenerateNum.getRandom(3));
    }
    private  String upload(String bucket, byte[] bytes, String key) {
        StorageConfig storageConfig = getStorageConfig(bucket);
        Configuration cfg = new Configuration(Zone.zone0());
        UploadManager uploadManager = new UploadManager(cfg);
        try {
            Response response = uploadManager.put(bytes, key, storageConfig.getAccessToken());
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return storageConfig.getDomain() + putRet.key;
        } catch (QiniuException ex) {
            throw new BizException("上传文件到七牛失败 e = " + ex.toString());
        }
    }
    private  StorageConfig getStorageConfig(String bucket) {
        StorageConfig storageConfig = storageConfigReposiroty.findByBucket(bucket);
        if (storageConfig == null) {
            throw new BizException("没有storage配置" + bucket);
        }
        if (storageConfig.getTokenExpireTime() > System.currentTimeMillis() - 1000 * 60) {
            return storageConfig;
        }
        Optional<AppConfig> appConfigOptional = appConfigRepository.findById(storageConfig.getAppConfigId());
        if (!appConfigOptional.isPresent()) {
            throw new BizException("没有app配置" + storageConfig.getAppConfigId());
        }
        AppConfig appConfig = appConfigOptional.get();

        Auth auth = Auth.create(appConfig.getAppKey(), appConfig.getAppSecret());
        // 过期
        storageConfig.setAccessToken(auth.uploadToken(bucket));
        storageConfig.setTokenExpireTime(System.currentTimeMillis() + 3600 * 1000);
        storageConfigReposiroty.save(storageConfig);
        return storageConfig;
    }
}
