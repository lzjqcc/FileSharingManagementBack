package com.Lowser.personalAsserts.dao.domain;

import com.Lowser.common.dao.domain.BaseEntity;
import com.Lowser.common.utils.DateUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "tb_account_fund_details")
public class AccountFundDetails extends BaseEntity{
    @Column(name = "account_id")
    private Integer accountId;
    @Column(name = "account_fund_id")
    private Integer accountFundId;
    /**
     * 增加的现金
     */
    @Column(name = "add_cash")
    private Integer addCash;
    /**
     * 增加的收益
     */
    @Column(name = "add_interest")
    private Integer addInterest;
    @Column(name = "current_cash")
    private Integer currentCash;
    @Column(name = "current_inerest")
    private Integer currentInterest;
    @Column(name = "create_date")
    private Date createDate = DateUtils.getBeginTimeOfDay();

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getCurrentCash() {
        return currentCash;
    }

    public void setCurrentCash(Integer currentCash) {
        this.currentCash = currentCash;
    }

    public Integer getCurrentInterest() {
        return currentInterest;
    }

    public void setCurrentInterest(Integer currentInterest) {
        this.currentInterest = currentInterest;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getAccountFundId() {
        return accountFundId;
    }

    public void setAccountFundId(Integer accountFundId) {
        this.accountFundId = accountFundId;
    }

    public Integer getAddCash() {
        return addCash;
    }

    public void setAddCash(Integer addCash) {
        this.addCash = addCash;
    }

    public Integer getAddInterest() {
        return addInterest;
    }

    public void setAddInterest(Integer addInterest) {
        this.addInterest = addInterest;
    }

}
