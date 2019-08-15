package com.Lowser.sharefile.dao.domain;


import com.Lowser.common.dao.domain.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "tb_group")
public class Group extends BaseEntity {

    /**
     *
     */
    @Column(name = "name")
    private String name;
    @Column(name = "parent_id" , columnDefinition = "int default 0")
    private Integer parentId;

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
