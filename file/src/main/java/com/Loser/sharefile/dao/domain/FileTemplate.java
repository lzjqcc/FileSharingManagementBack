package com.Loser.sharefile.dao.domain;

import com.Loser.common.domain.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "tb_file_template", indexes = {@Index(name = "group_id_index", columnList = "group_id")
                                            ,@Index(name = "file_num_index", columnList = "file_num")})
public class FileTemplate extends BaseEntity {

    /**
     * 文件编号
     */
    @Column(name = "file_num", unique = true)
    private String fileNum;
    private String name;
    private String url;
    private String url2;
    /**
     * 下载次数
     */
    @Column(name = "download_nums", columnDefinition = "int default 0")
    private Integer downloadNums;
    /**
     * 排序字段
     */
    @Column(name = "sequence", columnDefinition = "int default 0")
    private Integer sequence;
    /**
     * 点赞
     */
    @Column(name = "thumbs_up", columnDefinition = "int default 0")
    private Integer thumbsUp;

    private String description;
    /**
     * 文件格式
     */
    @Column(name = "file_format")
    private String fileFormat;
    /**
     * 文件大小 kb
     */
    @Column(name = "file_size", columnDefinition = "int default 0")
    private Integer fileSize;
    /**
     * 查看次数
     */
    @Column(name = "view_nums", columnDefinition = "int default 0")
    private Integer viewNums;
    @Column(name = "group_id")
    private Integer groupId;

    public String getUrl2() {
        return url2;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }

    public String getFileNum() {
        return fileNum;
    }

    public void setFileNum(String fileNum) {
        this.fileNum = fileNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getDownloadNums() {
        return downloadNums;
    }

    public void setDownloadNums(Integer downloadNums) {
        this.downloadNums = downloadNums;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Integer getThumbsUp() {
        return thumbsUp;
    }

    public void setThumbsUp(Integer thumbsUp) {
        this.thumbsUp = thumbsUp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getViewNums() {
        return viewNums;
    }

    public void setViewNums(Integer viewNums) {
        this.viewNums = viewNums;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }


}
