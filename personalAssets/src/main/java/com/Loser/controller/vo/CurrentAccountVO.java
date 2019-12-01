package com.Loser.controller.vo;

import java.util.List;

public class CurrentAccountVO {
    private Integer totalAmount;
    private Integer totalCash;
    private Integer totalInterest;
    /**
     * 总收益
     */
    private Integer returnRate;
    private Integer yearReturnRate;
    private String name;
    private String fundType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    private List<CurrentAccountVO> childs;

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

    public Integer getReturnRate() {
        return returnRate;
    }

    public void setReturnRate(Integer returnRate) {
        this.returnRate = returnRate;
    }

    public List<CurrentAccountVO> getChilds() {
        return childs;
    }

    public void setChilds(List<CurrentAccountVO> childs) {
        this.childs = childs;
    }
}
