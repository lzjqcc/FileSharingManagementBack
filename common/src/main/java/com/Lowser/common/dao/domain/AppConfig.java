package com.Lowser.common.dao.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.net.Inet4Address;

@Table(name = "tb_app_config")
@Entity
public class AppConfig extends BaseEntity {
    @Column(name = "app_key")
    private String appKey;
    @Column(name = "app_secret")
    private String appSecret;
    /**
     * @see com.Lowser.common.enums.WebEnum
     */
    private Integer web;
    private String name;
    @Column(name = "redirect_url")
    private String redirectUrl;

    public AppConfig(String appKey, String appSecret, Integer web, String name, Integer id) {
        super(id);
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.web = web;
        this.name = name;

    }

    public AppConfig() {

    }
    public Integer getWeb() {
        return web;
    }

    public void setWeb(Integer web) {
        this.web = web;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

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
