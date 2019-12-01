package com.Loser.controller.param;

public class AccountFundParam {
    private String name;
    private Integer parentId;
    private Integer initAmount;
    private Integer currentAmount;
    private Integer accountfundTypeId;

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

    public Integer getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(Integer currentAmount) {
        this.currentAmount = currentAmount;
    }

    public Integer getAccountfundTypeId() {
        return accountfundTypeId;
    }

    public void setAccountfundTypeId(Integer accountfundTypeId) {
        this.accountfundTypeId = accountfundTypeId;
    }
}
