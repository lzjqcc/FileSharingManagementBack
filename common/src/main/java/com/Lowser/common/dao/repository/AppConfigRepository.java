package com.Lowser.common.dao.repository;

import com.Lowser.common.dao.domain.AppConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppConfigRepository extends JpaRepository<AppConfig, Integer> {
    AppConfig findByAppKey(String appKey);
}
