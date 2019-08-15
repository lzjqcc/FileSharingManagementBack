package com.Lowser.common.enums;

import com.Lowser.common.dao.domain.AppConfig;

import java.util.Map;

public enum WebEnum {
    QI_NIU(new AppConfig("AdCwqzqvYskN7WjK7HsuxYnd87-FkMnWxYdFaEk6","ln51kzu1sRMymE-zDckMr-scADmKof6EkhepLJIR",0,"七牛", 1));
    private AppConfig appConfig;
    WebEnum(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    public AppConfig getAppConfig() {
        return appConfig;
    }
}
