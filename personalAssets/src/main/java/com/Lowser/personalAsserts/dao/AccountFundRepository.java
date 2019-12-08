package com.Lowser.personalAsserts.dao;

import com.Lowser.personalAsserts.dao.domain.AccountFund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
public interface AccountFundRepository extends JpaRepository<AccountFund, Integer>{
    List<AccountFund> findByAccountId(Integer accountId);
}
