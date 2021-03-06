package com.Lowser.personalAsserts.dao.domain;

import com.Lowser.common.dao.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "tb_account_fund",indexes =
        {@Index(columnList = "account_id",name = "account_id_parent_id_index"),
        @Index(columnList = "parent_id", name = "account_id_parent_id_index")})
public class AccountFund extends BaseEntity{
    @Column(name = "account_id")
    private Integer accountId;
    @Column(name = "parent_id")
    private Integer parentId;
    /**
     * 资金账户名称
     */
    private String name;
    /**
     * 资金账户类型
     */
    @Column(name = "account_fund_type_id")
    private Integer accountfundTypeId;
    @Column(name = "init_amount")
    private Integer initAmount;
    @Column(name = "total_cash")
    private Integer totalCash;
    @Column(name = "total_interest")
    private Integer totalInterest;
    @Column(name = "total_amount")
    private Integer totalAmount;

    public Integer getTotalCash() {
        if (totalCash == null) {
            return 0;
        }
        return totalCash;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAccountfundTypeId() {
        return accountfundTypeId;
    }

    public void setAccountfundTypeId(Integer accountfundTypeId) {
        this.accountfundTypeId = accountfundTypeId;
    }

    public Integer getInitAmount() {
        return initAmount;
    }

    public void setInitAmount(Integer initAmount) {
        this.initAmount = initAmount;
    }

    public void setTotalCash(Integer totalCash) {
        this.totalCash = totalCash;
    }

    public Integer getTotalInterest() {
        if (totalInterest == null) {
            return 0;
        }
        return totalInterest;
    }

    public void setTotalInterest(Integer totalInterest) {
        this.totalInterest = totalInterest;
    }

    public Integer getTotalAmount() {
        if (totalAmount == null) {
            return 0;
        }
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof AccountFund)) {
            return false;
        }
        AccountFund accountFund = (AccountFund) obj;
        return accountFund.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return this.getId();
    }
}
