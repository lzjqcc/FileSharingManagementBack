package com.Lowser.tool.dao.repository;

import com.Lowser.tool.dao.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LimitRepository extends JpaRepository<Limit, Integer> {
     Limit findByTypeAndAction(String type, String action);
}
