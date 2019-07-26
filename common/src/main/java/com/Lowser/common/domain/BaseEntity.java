package com.Lowser.common.domain;

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
    private Boolean delete;
    @Column(name = "insert_time", columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.DATE)
    private Date insertTime;
    @Temporal(TemporalType.DATE)
    @Column(name = "update_time", columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP")
    @UpdateTimestamp
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    public Date getInsertTime() {
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
