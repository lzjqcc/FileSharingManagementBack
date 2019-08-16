package com.Lowser.tool.dao.domain;

import com.Lowser.common.dao.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "tb_limit")
@Entity
public class Limit extends BaseEntity{
    /**
     *@see com.Lowser.tool.enums.HandlerTypeEnum
     */
    private String type;
    /**
     * 具体的方法
     * @see com.Lowser.tool.handler.Handler
     */
    private String action;
    /**
     * 限制
     */
    @Column(name = "limit_times")
    private Integer limitTimes;
    @Column(name = "is_limit", columnDefinition = "tinyint default 0")
    private Boolean limit = false;
    private String description;
    private String ext;
    public Limit(String type, String action, Integer limits) {
        this.type = type;
        this.action = action;
        this.limitTimes = limits;
    }
    public Limit() {

    }

    public Boolean getLimit() {
        return limit;
    }

    public void setLimit(Boolean limit) {
        this.limit = limit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getLimitTimes() {
        return limitTimes;
    }

    public void setLimitTimes(Integer limitTimes) {
        this.limitTimes = limitTimes;
    }
}
