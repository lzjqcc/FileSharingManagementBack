package com.Loser.comment.dao.domain;

import com.Lowser.common.dao.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;


public class Comment extends BaseEntity {
    private String comment;
    /**
     *
     */
    private Integer appId;
    private String fromName;
    private String toName;
}
