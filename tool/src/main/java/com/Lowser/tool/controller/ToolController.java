package com.Lowser.tool.controller;

import com.Lowser.common.error.BizException;
import com.Lowser.tool.enums.HandlerTypeEnum;
import com.Lowser.tool.handler.Handler;
import com.Lowser.tool.handler.ImageHandler;
import com.Lowser.tool.handler.TextHandler;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/tool")
public class ToolController {
    @Autowired
    private TextHandler textHandler;
    @Autowired
    private ImageHandler imageHandler;
    @PostMapping(value = "/handleString")
    public Object handleString(String action,@RequestBody Map<String, Object> needHandlerMap) {

        JSONObject jsonObject = null;
        if (!StringUtils.isEmpty(needHandlerMap.get("ext"))) {
            jsonObject = JSONObject.parseObject(JSONObject.toJSONString(needHandlerMap.get("ext")), JSONObject.class);
        }
        return textHandler.handler(action, needHandlerMap.get("text"), jsonObject);
    }
    @PostMapping(value = "/handleImage")
    public Object handleImage(@RequestParam(value = "image") MultipartFile file,
                              @RequestParam("action") String action,
                              @RequestParam(name = "ext", required = false) String ext) throws IOException {
        if (!file.getContentType().contains("image")) {
            throw new BizException("必须是图片");
        }
        JSONObject jsonObject = null;
        if (!StringUtils.isEmpty(ext)) {
            jsonObject = JSONObject.parseObject(ext, JSONObject.class);
        }
        return imageHandler.handler(action, file.getBytes(), jsonObject);
    }
}
