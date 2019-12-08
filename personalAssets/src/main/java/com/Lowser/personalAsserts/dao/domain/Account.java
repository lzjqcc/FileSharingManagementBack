package com.Lowser.personalAsserts.dao.domain;

import com.Lowser.common.dao.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tb_account")
public class Account extends BaseEntity{
    private String name;
    private String email;
    private String password;
    /**
     * 初始资金
     */
    @Column(name = "init_amount")
    private Integer initAmount;
    @Column(name = "target_amount")
    private Integer targetAmount;
    @Column(name = "target_year")
    private Integer targetYear;
    /**
     * 目标年化收益
     */
    @Column(name = "target_return_rate")
    private Double targetReturnRate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getInitAmount() {
        return initAmount;
    }

    public void setInitAmount(Integer initAmount) {
        this.initAmount = initAmount;
    }

    public Integer getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(Integer targetAmount) {
        this.targetAmount = targetAmount;
    }

    public Integer getTargetYear() {
        return targetYear;
    }

    public void setTargetYear(Integer targetYear) {
        this.targetYear = targetYear;
    }

    public Double getTargetReturnRate() {
        return targetReturnRate;
    }

    public void setTargetReturnRate(Double targetReturnRate) {
        this.targetReturnRate = targetReturnRate;
    }
}
