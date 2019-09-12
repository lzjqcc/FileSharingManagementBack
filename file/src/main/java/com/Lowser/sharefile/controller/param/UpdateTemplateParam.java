package com.Lowser.sharefile.controller.param;

import javax.validation.constraints.NotNull;

public class UpdateTemplateParam {
    @NotNull
    private String fileNum;
    private Integer downloadNums;
    private Integer thumbsUp;

    public String getFileNum() {
        return fileNum;
    }

    public void setFileNum(String fileNum) {
        this.fileNum = fileNum;
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
}
