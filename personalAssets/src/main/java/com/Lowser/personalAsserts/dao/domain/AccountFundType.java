package com.Lowser.personalAsserts.dao.domain;

import com.Lowser.common.dao.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 账户类型
 */
@Entity
@Table(name = "tb_account_fund_type")
public class AccountFundType extends BaseEntity{
    @Column(name = "account_id")
    private Integer accountId;
    private String name;
    private String remarks;



    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


}
