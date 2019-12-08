package com.Lowser.personalAsserts.dao.domain;

import com.Lowser.common.dao.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tb_account_fund")
public class AccountFund extends BaseEntity{
    @Column(name = "account_id")
    private Integer accountId;
    @Column(name = "parent_id")
    private Integer parentId;
    /**
     * 资金账户名称
     */
    private String name;
    /**
     * 资金账户类型
     */
    @Column(name = "account_fund_type_id")
    private int accountfundTypeId;
    @Column(name = "init_amount")
    private int initAmount;
    @Column(name = "total_cash")
    private Integer totalCash;
    @Column(name = "total_interest")
    private Integer totalInterest;
    @Column(name = "total_amount")
    private Integer totalAmount;

    public Integer getTotalCash() {
        return totalCash;
    }

    public void setTotalCash(Integer totalCash) {
        this.totalCash = totalCash;
    }

    public Integer getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(Integer totalInterest) {
        this.totalInterest = totalInterest;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAccountfundTypeId() {
        return accountfundTypeId;
    }

    public void setAccountfundTypeId(int accountfundTypeId) {
        this.accountfundTypeId = accountfundTypeId;
    }

    public int getInitAmount() {
        return initAmount;
    }

    public void setInitAmount(int initAmount) {
        this.initAmount = initAmount;
    }
}
