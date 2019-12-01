package com.Loser.dao;

import com.Loser.dao.domain.AccountFund;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountFundRepository extends JpaRepository<AccountFund, Integer>{
    List<AccountFund> findByAccountId(Integer accountId);
}
