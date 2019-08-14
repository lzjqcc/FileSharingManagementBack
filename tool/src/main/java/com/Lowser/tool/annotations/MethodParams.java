package com.Lowser.tool.annotations;

import com.google.zxing.common.detector.MathUtils;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodParams {
    boolean limit() default false;

    int limitTimes() default 10000;
    String ext() default ""; //"0 非必传,1 必传"

    String description() default "";
}
