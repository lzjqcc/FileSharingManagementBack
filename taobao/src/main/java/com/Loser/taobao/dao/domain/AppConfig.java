package com.Loser.taobao.dao.domain;

import com.Loser.common.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tb_app_config")
public class AppConfig extends BaseEntity {
    @Column(name = "app_key")
    private String appKey;
    @Column(name = "app_secret")
    private String appSecret;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
}
