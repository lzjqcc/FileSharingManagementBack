package com.Loser.sharefile.dao.repository;

import com.Loser.sharefile.dao.domain.FileTemplate;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileTemplateRepository extends JpaRepository<FileTemplate, Integer> {
    FileTemplate findByFileNum(String fileNum);

    PageImpl<FileTemplate> findByGroupId(Integer groupId, Pageable pageable);

    PageImpl<FileTemplate> findByGroupIdIn(List<Integer> groupIds, Pageable pageable);

}
