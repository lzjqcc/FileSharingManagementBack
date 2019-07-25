package com.Loser.common.utils;

import com.Loser.common.domain.BaseEntity;
import com.Loser.common.error.SystemError;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class ModelUtils {
    public static  <S extends BaseEntity, T> List<T> toTargets(List<S> source, Class<T> targetClass) {
        List<Object> targetObjects = new ArrayList<>();
        for (BaseEntity baseEntity : source) {
            if (baseEntity.getDelete() != null && !baseEntity.getDelete()) {
                targetObjects.add(toTarget(baseEntity, targetClass));
            }
        }
        return (List<T>) targetObjects;
    }
    public static <S extends BaseEntity, T> T toTarget(S source, Class<T> targetClass) {
        Object o = newInstance(targetClass);
        BeanUtils.copyProperties(source, o);
        return (T) o;
    }
    private static <T> T newInstance(Class<T> tClass) {
        try {
            Object object = tClass.newInstance();
            return (T) object;
        } catch (InstantiationException e) {
            throw new SystemError(e, "创建对象失败",tClass.getName());
        } catch (IllegalAccessException e) {
            throw new SystemError(e, "创建对象失败",tClass.getName());
        }

    }
}
