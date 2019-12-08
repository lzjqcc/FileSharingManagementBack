package com.Lowser.personalAsserts.controller.vo;

import java.util.Date;

public class AccountDetailsVO {
    private Integer addCash;
    private Integer addInterest;
    private String addCashRemarks;
    private String addInterestRemarks;
    private Date createDate;

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

    public String getAddCashRemarks() {
        return addCashRemarks;
    }

    public void setAddCashRemarks(String addCashRemarks) {
        this.addCashRemarks = addCashRemarks;
    }

    public String getAddInterestRemarks() {
        return addInterestRemarks;
    }

    public void setAddInterestRemarks(String addInterestRemarks) {
        this.addInterestRemarks = addInterestRemarks;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
