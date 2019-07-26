package com.Lowser.taobao.dao.repository;

import com.Lowser.taobao.dao.domain.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessTokenRepository extends JpaRepository<AccessToken, Integer>{
    AccessToken findByUserNick(String userNick);
}
