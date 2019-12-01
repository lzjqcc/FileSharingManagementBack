package com.Loser.dao;

import com.Loser.dao.domain.AccountFundDetails;
import com.Loser.dao.domain.AccountFundTargetDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountFundTargetDetailsRepository extends JpaRepository<AccountFundTargetDetails, Integer> {

}
