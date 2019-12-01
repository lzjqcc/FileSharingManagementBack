package com.Loser.dao;

import com.Loser.dao.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer>{
    Account findByEmailAndPassword(String email, String password);
}
