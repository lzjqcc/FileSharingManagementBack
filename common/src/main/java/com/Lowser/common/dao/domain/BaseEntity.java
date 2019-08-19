package com.Lowser.common.dao.domain;

import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(generator="increment", strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "is_delete", columnDefinition = "tinyint default 0")
    private Boolean delete = false;
    @Column(name = "insert_time", columnDefinition = "datetime")
    @Temporal(TemporalType.DATE)
    private Date insertTime ;
    @Temporal(TemporalType.DATE)
    @Column(name = "update_time", columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP")
    @UpdateTimestamp
    private Date updateTime;
    public BaseEntity() {

    }
    public BaseEntity(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getDelete() {
        if (delete == null) {
            return false;
        }
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    public Date getInsertTime() {
        if (insertTime == null) {
            return new Date();
        }
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
