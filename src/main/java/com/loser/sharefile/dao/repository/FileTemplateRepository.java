package com.loser.sharefile.dao.repository;

import com.loser.sharefile.dao.domain.FileTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileTemplateRepository extends JpaRepository<FileTemplate, Integer> {
    FileTemplate findByFileNum(String fileNum);

    PageImpl<FileTemplate> findByGroupIdOrderByInsertTimeDesc(Integer groupId, Pageable pageable);

    PageImpl<FileTemplate> findByGroupIdInOrderByInsertTimeDesc(List<Integer> groupIds, Pageable pageable);

}
