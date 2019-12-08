package com.Lowser.personalAsserts.comment.controller.result;

import java.util.List;

public class CommentResult {
    private Integer id;
    private String content;
    private String fromName;
    private String toName;
    public List<CommentResult> childs;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public List<CommentResult> getChilds() {
        return childs;
    }

    public void setChilds(List<CommentResult> childs) {
        this.childs = childs;
    }
}
