package com.Lowser.personalAsserts.controller.param;

public class AccountFundDetailsParam {
    private int addCash;
    private int addInterest;
    private String addCashRemarks;
    private String addInterestRemarks;
    private Integer accountFundId;

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

    public Integer getAccountFundId() {
        return accountFundId;
    }

    public void setAccountFundId(Integer accountFundId) {
        this.accountFundId = accountFundId;
    }
}
