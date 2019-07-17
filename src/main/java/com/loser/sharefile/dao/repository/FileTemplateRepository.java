package com.loser.sharefile.dao.repository;

import com.loser.sharefile.dao.domain.FileTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileTemplateRepository extends JpaRepository<FileTemplate, Integer> {
    FileTemplate findByFileNum(String fileNum);

    PageImpl<FileTemplate> findByGroupIdOrderByInsertTime(Integer groupId, Pageable pageable);

    PageImpl<FileTemplate> findByGroupIdAndTagIdOrderByInsertTime(Integer groupId, Integer tagId, Pageable pageable);
}
