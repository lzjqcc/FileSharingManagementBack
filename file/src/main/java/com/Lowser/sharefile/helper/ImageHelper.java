package com.Lowser.sharefile.helper;

import com.Lowser.common.error.BizException;
import com.Lowser.sharefile.controller.param.FileTemplateParam;
import com.Lowser.sharefile.dao.domain.FileTemplate;
import com.Lowser.sharefile.utils.PPTUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class ImageHelper {
    public List<String> getImages(FileTemplateParam fileTemplateParam) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, Object>> httpEntity = null;
        if (!StringUtils.isEmpty(fileTemplateParam.getCookie())) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("cookie", fileTemplateParam.getCookie());
            httpEntity = new HttpEntity(httpHeaders);
        }
        restTemplate.exchange(fileTemplateParam.getUrl(), HttpMethod.GET, httpEntity, byte[].class);
        ResponseEntity<byte[]> entity =  restTemplate.getForEntity(fileTemplateParam.getUrl(), byte[].class);
        if (entity.getStatusCode() == HttpStatus.OK) {
            return PPTUtils.ppt2Png(entity.getBody());
        }
        throw new BizException(entity.toString());
    }

}
