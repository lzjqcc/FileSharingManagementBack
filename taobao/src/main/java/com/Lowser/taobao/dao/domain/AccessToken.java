package com.Lowser.taobao.dao.domain;

import com.Lowser.common.dao.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "tb_access_token", indexes = {@Index(columnList = "access_token", unique = true, name = "access_token_index")})
public class AccessToken extends BaseEntity{
    @Column(name = "access_token")
    private String accessToken;
    /**
     * 用于刷新accessToken
     */
    @Column(name = "expire_time")
    private Long expireTime;
    @Column(name = "refresh_token")
    private  String refreshToken;

    /**
     * refreshToken几秒后过期
     */
    @Column(name = "user_nick")
    private String userNick;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "sub_user_id")
    private String subUserId;
    @Column(name = "sub_user_nick")
    private String subUserNick;

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }



    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }



    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSubUserId() {
        return subUserId;
    }

    public void setSubUserId(String subUserId) {
        this.subUserId = subUserId;
    }

    public String getSubUserNick() {
        return subUserNick;
    }

    public void setSubUserNick(String subUserNick) {
        this.subUserNick = subUserNick;
    }
}
