package com.Lowser.taobao.dao.domain;

import com.Lowser.common.domain.BaseEntity;

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
    @Column(name = "redirect_url")
    private String redirectUrl;
    public String getAppKey() {
        return appKey;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
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
