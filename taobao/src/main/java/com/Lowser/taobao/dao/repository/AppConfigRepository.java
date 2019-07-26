package com.Lowser.taobao.dao.repository;

import com.Lowser.taobao.dao.domain.AppConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppConfigRepository extends JpaRepository<AppConfig, Integer> {
    AppConfig findByAppKey(String appKey);
}
