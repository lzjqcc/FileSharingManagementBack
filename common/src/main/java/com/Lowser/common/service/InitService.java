package com.Lowser.common.service;

import com.Lowser.common.dao.domain.AppConfig;
import com.Lowser.common.dao.domain.StorageConfig;
import com.Lowser.common.dao.repository.AppConfigRepository;
import com.Lowser.common.dao.repository.StorageConfigReposiroty;
import com.Lowser.common.enums.WebEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
@Service
public class InitService {
    @Autowired
    private StorageConfigReposiroty storageConfigReposiroty;
    @Autowired
    private AppConfigRepository appConfigRepository;

    @PostConstruct
    public void init() {
        initAppConfig();
        initStorageConfig();
    }

    private void initAppConfig() {
        WebEnum[] webEnums = WebEnum.values();
        for (WebEnum webEnum : webEnums) {
            AppConfig appConfig = appConfigRepository.findByAppKey(webEnum.getAppConfig().getAppKey());
            if (appConfig == null) {
                appConfigRepository.save(webEnum.getAppConfig());
            }
        }
    }
    private void initStorageConfig() {
        StorageConfig fileConfig = new StorageConfig();
        fileConfig.setTokenExpireTime(0L);
        fileConfig.setAccessToken("");
        fileConfig.setAppConfigId(WebEnum.QI_NIU.getAppConfig().getId());
        fileConfig.setBucket("file");
        fileConfig.setDomain("http://file.shouzan.top/");
        if (storageConfigReposiroty.findByBucket(fileConfig.getBucket()) == null) {
            storageConfigReposiroty.save(fileConfig);
        }
        StorageConfig filmConfig = new StorageConfig();
        filmConfig.setTokenExpireTime(0L);
        filmConfig.setAccessToken("");
        filmConfig.setAppConfigId(WebEnum.QI_NIU.getAppConfig().getId());
        filmConfig.setBucket("film");
        filmConfig.setDomain("http://film.shouzan.top/");
        if (storageConfigReposiroty.findByBucket(filmConfig.getBucket()) == null) {
            storageConfigReposiroty.save(filmConfig);
        }
    }
}
