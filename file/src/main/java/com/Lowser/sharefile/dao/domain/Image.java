package com.Lowser.sharefile.dao.domain;

import com.Lowser.common.dao.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "tb_image", indexes = {@Index(name = "file_template_num_index", columnList = "file_template_num")})
public class Image extends BaseEntity {
    private String url;
    private String url2;
    /**
     *
     */
    @Column(name = "file_template_num")
    private String fileTemplateNum;

    public String getUrl() {
        return url;
    }

    public String getUrl2() {
        return url2;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileTemplateNum() {
        return fileTemplateNum;
    }

    public void setFileTemplateNum(String fileTemplateNum) {
        this.fileTemplateNum = fileTemplateNum;
    }
}
