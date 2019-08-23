package com.Lowser.tool.handler;

import com.Lowser.common.error.BizException;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class AbstractHandler implements Handler {
    @Override
    public Object handler(String action, Object needHandler, JSONObject jsonObject) {
        try {
            Method method = null;
            if (jsonObject == null) {
                 method = this.getClass().getDeclaredMethod(action, needHandler.getClass());
            } else {
                method = this.getClass().getDeclaredMethod(action, needHandler.getClass(), jsonObject.getClass());
            }
            if (method.getName().equals("handler") || method.getName().equals("handleType")) {
                throw new BizException("不支持处理");
            }
            if (jsonObject == null) {
                return method.invoke(this, needHandler);
            }
            return  method.invoke(this, needHandler, jsonObject);
        } catch (NoSuchMethodException e) {
            throw new BizException("不支持处理" + action);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new BizException("执行方法错误 action = " + action);
        } catch (InvocationTargetException ex) {
            if (ex.getTargetException() != null) {
                throw new BizException(ex.getTargetException().getMessage());
            }
            throw new BizException("执行方法错误 action = " + action);
        }catch (BizException b) {
            throw b;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new BizException("执行错误");
        }
    }

}
