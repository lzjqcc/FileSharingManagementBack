package com.Lowser.sharefile.dao.repository;

import com.Lowser.sharefile.dao.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Integer> {
    List<Group> findByParentId(Integer parentId);
}
