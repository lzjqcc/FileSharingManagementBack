package com.Lowser.common.dao.repository;

import com.Lowser.common.dao.domain.StorageConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageConfigReposiroty extends JpaRepository<StorageConfig, Integer>{
    StorageConfig findByBucket(String bucket);
}
