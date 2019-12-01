package com.Loser.controller.vo;

public class TargetAccountVO {
    /**
     * 初始资金
     */
    private Integer initAmount;
    private Integer targetAmount;
    private Integer targetYear;
    /**
     * 目标年化收益
     */
    private Integer targetReturnRate;

    public Integer getInitAmount() {
        return initAmount;
    }

    public void setInitAmount(Integer initAmount) {
        this.initAmount = initAmount;
    }

    public Integer getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(Integer targetAmount) {
        this.targetAmount = targetAmount;
    }

    public Integer getTargetYear() {
        return targetYear;
    }

    public void setTargetYear(Integer targetYear) {
        this.targetYear = targetYear;
    }

    public Integer getTargetReturnRate() {
        return targetReturnRate;
    }

    public void setTargetReturnRate(Integer targetReturnRate) {
        this.targetReturnRate = targetReturnRate;
    }
}
