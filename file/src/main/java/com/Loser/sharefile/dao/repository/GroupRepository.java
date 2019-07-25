package com.Loser.sharefile.dao.repository;

import com.Loser.sharefile.dao.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Integer> {
    List<Group> findByParentId(Integer parentId);
}
