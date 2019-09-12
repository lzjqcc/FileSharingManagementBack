package com.Lowser.common.utils;

import com.Lowser.common.dao.domain.BaseEntity;
import com.Lowser.common.error.SystemError;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
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
    public static void copyPropertiesNotIncludeNull(Object source, Object target) {
        List<String> nullFieldLists = getNullFields(source);
        String[] nullFields = new String[nullFieldLists.size()];
        nullFieldLists.toArray(nullFields);
        BeanUtils.copyProperties(source, target, nullFields);
    }
    private static List<String> getNullFields(Object ob) {
        Field[] fields = ob.getClass().getFields();
        List<String> fieldLists = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(ob);
                if (value instanceof Collection && (value == null || CollectionUtils.isEmpty((Collection<?>) value))) {
                    fieldLists.add(field.getName());
                }
                if (value instanceof String && StringUtils.isEmpty(value)) {
                    fieldLists.add(field.getName());
                }
                if (value == null) {
                    fieldLists.add(field.getName());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return fieldLists;
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
