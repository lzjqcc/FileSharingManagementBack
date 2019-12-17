package com.Lowser.personalAsserts.controller.vo;

import java.util.Date;

public class AccountFundDetailsVO {
    private Integer accountFundId;
    private Integer addCash;
    private Integer addInterest;
    private Integer currentCash;
    private Integer currentInterest;
    private Integer currentAmount;
    private Date insertTime;

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public Integer getAccountFundId() {
        return accountFundId;
    }

    public void setAccountFundId(Integer accountFundId) {
        this.accountFundId = accountFundId;
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

    public Integer getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(Integer currentAmount) {
        this.currentAmount = currentAmount;
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
