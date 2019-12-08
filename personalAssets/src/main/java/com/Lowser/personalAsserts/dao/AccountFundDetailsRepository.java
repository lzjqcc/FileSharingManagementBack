package com.Lowser.personalAsserts.dao;

import com.Lowser.personalAsserts.dao.domain.AccountFundDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
@Component
public interface AccountFundDetailsRepository extends JpaRepository<AccountFundDetails, Integer> {
    List<AccountFundDetails> findByAccountFundIdAndInsertTime(Integer accountFundId, Date insertTime);
}
