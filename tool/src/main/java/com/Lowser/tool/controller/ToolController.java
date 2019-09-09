package com.Lowser.tool.controller;

import com.Lowser.common.error.BizException;
import com.Lowser.tool.enums.HandlerTypeEnum;
import com.Lowser.tool.handler.Handler;
import com.Lowser.tool.handler.ImageHandler;
import com.Lowser.tool.handler.TextHandler;
import com.alibaba.fastjson.JSONObject;
import com.github.junrar.exception.RarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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

    public static void main(String[] args) throws UnsupportedEncodingException, ParseException, InterruptedException {
        for (int i =0;i<5;i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        post();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }

    }
    public static void post() throws ParseException, InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        postParameters.add("name", "瞿超超");
        postParameters.add("tel", "15879169004");
        postParameters.add("sn", "36042419940916498X");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        HttpEntity<MultiValueMap<String, Object>> r = new HttpEntity<>(postParameters, headers);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");

        while (true) {
            Date date = format.parse("2019-09-09 13:59:53");
            if (System.currentTimeMillis() >= date.getTime()) {
                Thread.sleep(1);
                System.out.println("开始预约");
                String responseMessage = null;
                try {
                    responseMessage = restTemplate.postForObject("http://3.duotucms.com/index.php/index/order", r, String.class);
                }catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("出现异常 " );
                    continue;
                }
                JSONObject jsonObject = JSONObject.parseObject(responseMessage, JSONObject.class);
                System.out.println(responseMessage + "："+ jsonObject.getString("message"));
                Date end = format.parse("2019-09-09 14:03:00");
                if (System.currentTimeMillis() >= end.getTime()) {
                    break;
                }
            }

        }
    }
}
