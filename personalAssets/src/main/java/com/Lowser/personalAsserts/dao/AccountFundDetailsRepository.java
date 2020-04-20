package com.Lowser.personalAsserts.dao;

import com.Lowser.personalAsserts.dao.domain.AccountFund;
import com.Lowser.personalAsserts.dao.domain.AccountFundDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
@Component
public interface AccountFundDetailsRepository extends JpaRepository<AccountFundDetails, Integer> {

    List<AccountFundDetails> findByAccountFundIdAndCreateDate(Integer accountFundId, Date createDate);
    void deleteByAccountFundIdIn(List<Integer> accountFundIds);

    List<AccountFundDetails> findByIdIn(List<Integer> ids);

    List<AccountFundDetails> findByAccountIdAndAccountFundIdIn(Integer accountId, List<Integer> accountFundIds);

    List<AccountFundDetails> findByAccountFundId(Integer accountFundId);


}
