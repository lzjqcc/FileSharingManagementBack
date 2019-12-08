package com.Lowser.personalAsserts.dao;

import com.Lowser.personalAsserts.dao.domain.AccountFundTargetDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface AccountFundTargetDetailsRepository extends JpaRepository<AccountFundTargetDetails, Integer> {
    Integer countByAccountId(Integer accountId);

    void removeByAccountId(Integer accountId);

    List<AccountFundTargetDetails> findByAccountId(Integer accountId);
}
