package com.store.api.cache;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Redis缓存管理类，提供基本管理操作
 * 
 * Revision History
 * 
 * 2014年7月28日,vincent,created it
 */
public class RedisCachedImpl implements ICached {
    public RedisCachedImpl() {

    }

    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public String deleteCached(final String sessionId) throws Exception {
        redisTemplate.execute(new RedisCallback<Object>() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.del(sessionId.toString().getBytes());
                return null;
            }
        });

        return null;
    }

    @Override
    public String saveCached(final String key, final Object session, final long expire) throws Exception {
        String cacheKey = (String) redisTemplate.execute(new RedisCallback<Object>() {
            public String doInRedis(final RedisConnection connection) throws DataAccessException {
                connection.set(key.getBytes(), SerializeUtil.serialize(session));
                connection.expire(key.getBytes(), expire);
                return key;
            }
        });
        return cacheKey;

    }

    @Override
    public Object getCached(final String sessionId) throws Exception {
        return redisTemplate.execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] bs = connection.get(sessionId.getBytes());
                return SerializeUtil.unserialize(bs);
            }
        });

    }

    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

}
