package com.loser.sharefile.dao.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name = "tb_tag", indexes = {@Index(name = "group_id_index", columnList ="group_id")})
public class Tag extends BaseEntity{
    @Column(name = "name")
    private String name;
    @Column(name = "group_id")
    private Integer groupId;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {

        this.groupId = groupId;
    }

}

