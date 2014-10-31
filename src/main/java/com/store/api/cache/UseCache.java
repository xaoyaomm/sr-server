package com.store.api.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 缓存注解，可作用于接口或是实现方法
 * 
 * Revision History
 * 
 * 2014年7月28日,vincent,created it
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UseCache {
    /**
     * 过期时间,单位:秒
     * @return
     */
    long expire() default 60;
}
