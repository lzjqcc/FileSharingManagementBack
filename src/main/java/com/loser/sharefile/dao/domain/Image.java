package com.loser.sharefile.dao.domain;

import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Date;
@Entity
@Table(name = "tb_image", indexes = {@Index(name = "file_template_num_index", columnList = "file_template_num")})
public class Image extends BaseEntity{
    private String url;

    /**
     *
     */
    @Column(name = "file_template_num")
    private Integer fileTemplateNum;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getFileTemplateNum() {
        return fileTemplateNum;
    }

    public void setFileTemplateNum(Integer fileTemplateNum) {
        this.fileTemplateNum = fileTemplateNum;
    }
}
