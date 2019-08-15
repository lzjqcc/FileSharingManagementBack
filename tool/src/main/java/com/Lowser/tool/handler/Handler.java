package com.Lowser.tool.handler;

import com.alibaba.fastjson.JSONObject;

public interface Handler {
    Object handler(String action, Object needHandler, JSONObject ext);
    String handlerType();
}
