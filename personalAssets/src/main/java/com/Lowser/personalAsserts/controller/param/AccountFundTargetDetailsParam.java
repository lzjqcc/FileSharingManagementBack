package com.Lowser.personalAsserts.controller.param;

public class AccountFundTargetDetailsParam {
    private Integer targetAmount;
    private Double targetYearRate;
    private Integer targetYear;

    public Integer getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(Integer targetAmount) {
        this.targetAmount = targetAmount;
    }

    public Double getTargetYearRate() {
        return targetYearRate;
    }

    public void setTargetYearRate(Double targetYearRate) {
        this.targetYearRate = targetYearRate;
    }

    public Integer getTargetYear() {
        return targetYear;
    }

    public void setTargetYear(Integer targetYear) {
        this.targetYear = targetYear;
    }
}