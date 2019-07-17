package com.loser.sharefile.dao.repository;

import com.loser.sharefile.dao.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    List<Image> findByFileTemplateNum(String fileTemplateNum);
    @Query(value = "select * from tb_image where file_template_num = 1? limit 1", nativeQuery = true)
    Image findImagesByFileTemplateNumLimit(String fileTemplateNum);
}
