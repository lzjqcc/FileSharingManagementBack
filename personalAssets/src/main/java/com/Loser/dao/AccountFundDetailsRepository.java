package com.Loser.dao;

import com.Loser.dao.domain.AccountFundDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface AccountFundDetailsRepository extends JpaRepository<AccountFundDetails, Integer> {
    List<AccountFundDetails> findByAccountFundIdAndCreateDate(Integer accountFundId, Date createDate);
}
