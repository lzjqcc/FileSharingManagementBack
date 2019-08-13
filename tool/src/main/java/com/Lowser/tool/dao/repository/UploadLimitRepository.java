package com.Lowser.tool.dao.repository;

import com.Lowser.tool.dao.domain.UploadLimit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface UploadLimitRepository extends JpaRepository<UploadLimit, Integer>{
    UploadLimit findByLimitIdAndIp(Integer limitId, String ip);

    void deleteUploadLimitByInsertTimeBefore(Date date);
}
