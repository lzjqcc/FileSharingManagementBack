package com.Lowser.sharefile.controller.result;

import java.util.List;

public class FileTemplateResult {
    private List<ImageResult> images;
    private String name;
    private String url;
    private Integer downloadNums;
    private Integer thumbsUp;
    private String description;
    private String fileFormat;
    private String fileNum;
    private Integer fileSize;
    private Integer viewNums;
    private String code;
    private List<TagResult> tagResults;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<TagResult> getTagResults() {
        return tagResults;
    }

    public void setTagResults(List<TagResult> tagResults) {
        this.tagResults = tagResults;
    }

    public String getFileNum() {
        return fileNum;
    }

    public void setFileNum(String fileNum) {
        this.fileNum = fileNum;
    }

    public List<ImageResult> getImages() {
        return images;
    }

    public void setImages(List<ImageResult> images) {
        this.images = images;
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
}
