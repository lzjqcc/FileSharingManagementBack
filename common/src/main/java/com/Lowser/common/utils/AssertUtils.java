package com.Lowser.common.utils;

import com.Lowser.common.error.BizException;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class AssertUtils extends Assert {
    public static void notEmpty(String content, String msg) {
        if (StringUtils.isEmpty(content)) {
            throw new BizException(msg);
        }
    }
}
