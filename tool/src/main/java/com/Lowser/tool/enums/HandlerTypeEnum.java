package com.Lowser.tool.enums;

import com.Lowser.tool.handler.ImageHandler;
import com.Lowser.tool.handler.TextHandler;
import com.Lowser.tool.handler.Handler;

public enum HandlerTypeEnum {
    Text_HANDLER("text", new TextHandler(), "字符编码"),
    Image_HANDLER("image", new ImageHandler(), "图片处理")
    ;
    private String type;
    private Handler stringHandler;
    private String descr;

    HandlerTypeEnum(String type, Handler stringHandler, String descr) {
        this.type = type;
        this.stringHandler = stringHandler;
        this.descr = descr;
    }

    public String getType() {
        return type;
    }

    public Handler getStringHandler() {
        return stringHandler;
    }

    public String getDescr() {
        return descr;
    }
}
