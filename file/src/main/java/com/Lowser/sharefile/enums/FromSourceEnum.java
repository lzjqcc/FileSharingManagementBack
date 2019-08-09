package com.Lowser.sharefile.enums;

public enum FromSourceEnum {
    BAI_DU_WANGP(0, "百度网盘"),
    QI_NIU(1, "七牛"),
    OTHER_WEB(3, "其他网站");
    private int code;
    private String desc;

    FromSourceEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
