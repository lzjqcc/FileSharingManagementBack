package com.loser.sharefile.dao.repository;

import com.loser.sharefile.dao.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    List<Tag> findTagByFileTemplateNum(String fileTemplateNum);
}
