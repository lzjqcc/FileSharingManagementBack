package com.Loser.dao;

import com.Loser.dao.domain.AccountFundType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountFundTypeRepository extends JpaRepository<AccountFundType, Integer>{
    List<AccountFundType> findByIdIn(List<Integer> ids);
}
