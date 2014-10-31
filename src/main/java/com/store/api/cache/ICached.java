package com.store.api.cache;

import java.util.Date;

public interface ICached {

    /**
     * 删除 缓存
     * @param key
     * @return
     * @throws Exception
     */
    String deleteCached(String key)throws Exception;
    /**
     * 更新 缓存
     * @param key
     * @param value
     * @param expire
     * @return
     * @throws Exception
     */
    Object saveCached(String key,Object value,long expire)throws Exception;
    /**
     * 获取缓存
     * @param key
     * @return
     * @throws Exception
     */
    Object getCached(String key)throws Exception;
    
    
}
