package com.Lowser.tool.init;

import com.Lowser.tool.annotations.MethodParams;
import com.Lowser.tool.dao.domain.Limit;
import com.Lowser.tool.dao.repository.LimitRepository;
import com.Lowser.tool.enums.HandlerTypeEnum;
import com.Lowser.tool.handler.Handler;
import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InitService {
    @Autowired
    private LimitRepository limitRepository;
    @PostConstruct
    public void init() {
        HandlerTypeEnum[] typeEnums = HandlerTypeEnum.values();
        for (HandlerTypeEnum handlerTypeEnum : typeEnums) {
            Handler handler = handlerTypeEnum.getStringHandler();
            Method[] methods = handler.getClass().getMethods();
            List<String> notIncludeActions = notActions();
            List<Method> methodsLists = Lists.newArrayList(methods).stream().filter(t-> !notIncludeActions.contains(t.getName())).collect(Collectors.toList());
            for (Method method : methodsLists) {
                Limit limit = limitRepository.findByTypeAndAction(handlerTypeEnum.getType(), method.getName());
                MethodParams extParams = method.getAnnotation(MethodParams.class);
                if (limit == null ) {
                    limit = new Limit();
                    String ext = null;
                    if (extParams != null) {
                        limit.setDescription(extParams.description());
                        limit.setExt(extParams.ext());
                        limit.setLimitTimes(extParams.limitTimes());
                        limit.setLimit(extParams.limit());
                    }
                    limit.setAction(method.getName());
                    limit.setType(handlerTypeEnum.getType());
                    //limitRepository.save(limit);
                    System.out.println(JSONObject.toJSON(limit));
                }
            }
        }
    }
    private List<String> notActions() {
        Method[] methods = Object.class.getMethods();
        List<String> list = new ArrayList<>();
        for (Method method : methods) {
            list.add(method.getName());
        }
        list.add("handler");
        return list;
    }
}
