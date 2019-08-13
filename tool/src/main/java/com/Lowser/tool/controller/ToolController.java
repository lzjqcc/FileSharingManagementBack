package com.Lowser.tool.controller;

import com.Lowser.common.error.BizException;
import com.Lowser.tool.enums.HandlerTypeEnum;
import com.Lowser.tool.handler.Handler;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/tool")
public class ToolController {
    @PostMapping(value = "/handleString")
    public Object handleString(String type, String action,@RequestBody Map<String, Object> needHandlerMap) {
        Handler stringHandler = getHandler(type);
        JSONObject jsonObject = null;
        if (!StringUtils.isEmpty(needHandlerMap.get("ext"))) {
            jsonObject = JSONObject.parseObject(JSONObject.toJSONString(needHandlerMap.get("ext")), JSONObject.class);
        }
        return stringHandler.handler(action, needHandlerMap.get("text"), jsonObject);
    }
    @PostMapping(value = "/handleImage")
    public Object handleImage(@RequestParam(value = "image") MultipartFile file, @RequestParam("type") String type,
                              @RequestParam("action") String action,
                              @RequestParam(name = "ext", required = false) String ext) throws IOException {
        Handler stringHandler = getHandler(type);
        if (!file.getContentType().contains("image")) {
            throw new BizException("必须是图片");
        }
        JSONObject jsonObject = null;
        if (!StringUtils.isEmpty(ext)) {
            jsonObject = JSONObject.parseObject(ext, JSONObject.class);
        }
        return stringHandler.handler(action, file.getBytes(), jsonObject);
    }

    private Handler getHandler(String type) {
        HandlerTypeEnum[] typeEnums = HandlerTypeEnum.values();
        for (HandlerTypeEnum handlerTypeEnum : typeEnums) {
            if (handlerTypeEnum.getType().equals(type)) {
                return handlerTypeEnum.getStringHandler();
            }
        }
        throw new BizException("没有这个功能");
    }
}
