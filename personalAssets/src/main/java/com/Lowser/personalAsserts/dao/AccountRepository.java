package com.Lowser.personalAsserts.dao;

import com.Lowser.personalAsserts.dao.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface AccountRepository extends JpaRepository<Account, Integer>{
    Account findByEmailAndPassword(String email, String password);
}
