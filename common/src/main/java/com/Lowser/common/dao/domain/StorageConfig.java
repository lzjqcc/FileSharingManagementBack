package com.Lowser.common.dao.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Date;

/**
 * 七牛对象存储配置
 */
@Table(name = "tb_storage_config",indexes = {@Index(name = "bucket_index", columnList = "bucket")})
@Entity
public class StorageConfig extends BaseEntity{
    private String domain;
    private String bucket;
    @Column(name = "access_token")
    private String accessToken;
    @Column(name = "token_expire_time")
    private Long tokenExpireTime;
    @Column(name = "app_config_id")
    private Integer appConfigId;
    public Integer getAppConfigId() {
        return appConfigId;
    }

    public void setAppConfigId(Integer appConfigId) {
        this.appConfigId = appConfigId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getTokenExpireTime() {
        return tokenExpireTime;
    }

    public void setTokenExpireTime(Long tokenExpireTime) {
        this.tokenExpireTime = tokenExpireTime;
    }
}
