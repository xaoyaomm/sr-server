package com.store.api.cache;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * 缓存拦截器，对使用了UseCache注册的方法进行拦截，将方法执行结果存入缓存
 * 
 * Revision History
 * 
 * 2014年7月28日,vincent,created it
 */
public class MethodCacheInterceptor implements MethodInterceptor, InitializingBean {

    private ICached cacheManager;

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    public void setCacheManager(final ICached cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(cacheManager, "A cacheManager is required. Use setCacheManager(cacheManager) method.");
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String targetName = invocation.getThis().getClass().getName();
        String methodName = invocation.getMethod().getName();
        Object[] arguments = invocation.getArguments();
        UseCache uc = invocation.getMethod().getAnnotation(UseCache.class);
        long expire = uc.expire(); // 过期时间（单位）
        Object result = null; // 返回结果
        String key = getCacheKey(targetName, methodName, arguments);
        try {
            result = cacheManager.getCached(key);
        } catch (Exception e) {
            LOG.error("get data from cache fail:" + key, e);
        }
        if (null != result) {// 缓存获取结果
            LOG.info("useing cache data. key:" + key);
            return result;
        } else {
            try {// 缓存中没有则执行目标方法
                result = invocation.proceed();
            } catch (Exception e) {
                result = null;
                throw e;
            }
            if (null != result) {// 返回值不为null,写入缓存
                try {
                    cacheManager.saveCached(key, result, expire);
                    LOG.info("cache data. key:" + key);
                    return result;
                } catch (Exception e) {
                    result = null;
                    LOG.error("save data to cache fail:" + key, e);
                }

            }
        }
        return null;
    }

    private String getCacheKey(final String targetName, final String methodName, final Object[] arguments) {
        StringBuffer sb = new StringBuffer();
        // 因为服务层与DAO层使用代理方式，classname中会带有代理对象名称，为让KEY值能一致，去掉代理对象名称信息
        if (targetName.contains("$"))
            sb.append(targetName.substring(0, targetName.indexOf("$")));
        else
            sb.append(targetName);
        sb.append("_").append(methodName);
        if ((arguments != null) && (arguments.length != 0)) {
            for (int i = 0; i < arguments.length; i++) {
                sb.append("_").append(arguments[i]);
            }
        }
        return sb.toString();
    }

}
