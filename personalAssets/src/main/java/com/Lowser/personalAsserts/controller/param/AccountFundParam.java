package com.Lowser.personalAsserts.controller.param;

public class AccountFundParam {
    private String name;
    private Integer parentId;
    private Integer initAmount;
    private Integer totalAmount;
    private Integer totalCash;
    private Integer totalInterest;
    private String fundType;

    public String getFundType() {
        return fundType;
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

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getInitAmount() {
        return initAmount;
    }

    public void setInitAmount(Integer initAmount) {
        this.initAmount = initAmount;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
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

}
