package com.loser.sharefile.dao.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name = "tb_tag", indexes = {@Index(name = "file_template_num_index", columnList ="file_template_num")})
public class Tag extends BaseEntity{
    @Column(name = "name")
    private String name;
    @Column(name = "file_template_num")
    private String fileTemplateNum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileTemplateNum() {
        return fileTemplateNum;
    }

    public void setFileTemplateNum(String fileTemplateNum) {
        this.fileTemplateNum = fileTemplateNum;
    }
}

