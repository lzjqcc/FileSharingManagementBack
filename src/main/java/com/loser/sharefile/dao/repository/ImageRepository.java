package com.loser.sharefile.dao.repository;

import com.loser.sharefile.dao.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
public interface ImageRepository extends JpaRepository<Image, Integer> {
    List<Image> findByFileTemplateNum(String fileTemplateNum);
    Image findTop1ByFileTemplateNum(String fileTemplateNum);

}
