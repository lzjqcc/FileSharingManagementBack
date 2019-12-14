package com.Lowser.personalAsserts.controller.vo;

import java.util.List;

public class AccountFundVo {
    private Integer id;
    private String fundType;
    private String name;
    private int initAmount;
    private Integer totalCash;
    private Integer totalInterest;
    private Integer totalAmount;
    private String remarks;
    private List<AccountFundVo> childAccountFunds;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFundType() {
        return fundType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInitAmount() {
        return initAmount;
    }

    public void setInitAmount(int initAmount) {
        this.initAmount = initAmount;
    }

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

    public List<AccountFundVo> getChildAccountFunds() {
        return childAccountFunds;
    }

    public void setChildAccountFunds(List<AccountFundVo> childAccountFunds) {
        this.childAccountFunds = childAccountFunds;
    }
}
