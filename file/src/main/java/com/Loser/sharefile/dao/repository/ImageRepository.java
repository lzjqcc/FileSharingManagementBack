package com.Loser.sharefile.dao.repository;

import com.Loser.sharefile.dao.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface ImageRepository extends JpaRepository<Image, Integer> {
    List<Image> findByFileTemplateNum(String fileTemplateNum);
    Image findTop1ByFileTemplateNum(String fileTemplateNum);

}
