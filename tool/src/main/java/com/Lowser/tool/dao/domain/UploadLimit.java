package com.Lowser.tool.dao.domain;

import com.Lowser.common.dao.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "tb_upload_limit")
@Entity
public class UploadLimit extends BaseEntity{
    @Column(name = "upload_times")
    private Integer uploadTimes;
    @Column(name = "limit_id")
    private Integer limitId;
    private String ip;

    public Integer getUploadTimes() {
        return uploadTimes;
    }

    public void setUploadTimes(Integer uploadTimes) {
        this.uploadTimes = uploadTimes;
    }

    public Integer getLimitId() {
        return limitId;
    }

    public void setLimitId(Integer limitId) {
        this.limitId = limitId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
