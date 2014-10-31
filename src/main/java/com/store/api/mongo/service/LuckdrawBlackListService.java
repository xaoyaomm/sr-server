package com.store.api.mongo.service;

import java.util.List;
import java.util.Set;

import com.store.api.cache.CacheConstant;
import com.store.api.cache.UseCache;
import com.store.api.mongo.entity.LuckydrawBlackList;

public interface LuckdrawBlackListService {
    
    @UseCache(expire=CacheConstant.CACHE_EXPIRE_HOUR_2)
    public Set<Long> findAllUserId();
    
    public List<LuckydrawBlackList> findAll();
    
    public void save(LuckydrawBlackList entity);
    
    public void delete(LuckydrawBlackList entity);

}
