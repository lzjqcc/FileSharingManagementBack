package com.Lowser.personalAsserts.dao.domain;

import com.Lowser.common.dao.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "tb_account_fund_target_details",indexes = {
        @Index(columnList = "account_id",name = "account_id_index")
})
public class AccountFundTargetDetails extends BaseEntity {
    @Column(name = "add_cash")
    private int addCash;
    @Column(name = "add_interest")
    private int addInterest;
    @Column(name = "current_cash")
    private int currentCash;
    @Column(name = "current_interest")
    private int currentInterest;
    @Column(name = "current_amount")
    private int currentAmount;
    @Column(name = "target_date")
    private Date targetDate;
    @Column(name = "account_id")
    private Integer accountId;

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public int getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(int currentAmount) {
        this.currentAmount = currentAmount;
    }

    public Date getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(Date targetDate) {
        this.targetDate = targetDate;
    }

    public int getAddCash() {
        return addCash;
    }

    public void setAddCash(int addCash) {
        this.addCash = addCash;
    }

    public int getAddInterest() {
        return addInterest;
    }

    public void setAddInterest(int addInterest) {
        this.addInterest = addInterest;
    }

    public int getCurrentCash() {
        return currentCash;
    }

    public void setCurrentCash(int currentCash) {
        this.currentCash = currentCash;
    }

    public int getCurrentInterest() {
        return currentInterest;
    }

    public void setCurrentInterest(int currentInterest) {
        this.currentInterest = currentInterest;
    }
}
