package com.Lowser.personalAsserts.dao;

import com.Lowser.personalAsserts.dao.domain.AccountFundType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
@Component
public interface AccountFundTypeRepository extends JpaRepository<AccountFundType, Integer>{
    List<AccountFundType> findByIdIn(List<Integer> ids);

    List<AccountFundType> findByAccountId(Integer accountId);

    AccountFundType findByAccountIdAndName(Integer accountId, String fundTypeName);
}
