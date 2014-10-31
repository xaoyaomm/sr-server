package com.store.api.session.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用户验证，参数type指定存放在SESSION的用户对象KEY名称
 * 
 * Revision History
 * 
 * 2014年6月8日,vincent,created it
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Authorization {
    String type();
}
