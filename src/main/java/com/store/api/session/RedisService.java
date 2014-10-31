package com.store.api.session;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

import com.store.api.utils.PropertiesUtil;

/**
 * redis服务类
 * 
 * Revision History
 * 
 * 2014年7月8日,vincent,created it
 */
public class RedisService {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private int expire = 7776000; // 单位为秒

    private static RedisService instance = null;

    private JedisPool jedisPool = null;

    public static synchronized RedisService getInstance() throws Exception {
        if (instance == null) {
            instance = new RedisService();
        }
        return instance;
    }

    private RedisService() throws Exception {
        InputStream infile = this.getClass().getResourceAsStream("/conf/sessionConfig.properties");
        Properties props = new Properties();
        try {
            props.load(infile);
        } catch (IOException ex) {
            LOG.error("load SessionConfig file fail:" + ex.getMessage());
        }
        expire = PropertiesUtil.getIntProperty(props, "sessionTimeout", 7776000);
        initSockIOPool(props);
    }

    private void initSockIOPool(Properties props) throws Exception {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(PropertiesUtil.getIntProperty(props, "maxActive", 20));
        config.setMaxIdle(PropertiesUtil.getIntProperty(props, "maxIdle", 5));
        config.setMinIdle(PropertiesUtil.getIntProperty(props, "minIdle", 1));
        config.setMaxWaitMillis(PropertiesUtil.getLongProperty(props, "maxWait", 1000 * 3));
        config.setTestOnBorrow(PropertiesUtil.getBooleanProperty(props, "testOnBorrow", true));
        config.setTestOnReturn(PropertiesUtil.getBooleanProperty(props, "testOnReturn", true));
        config.setTestWhileIdle(PropertiesUtil.getBooleanProperty(props, "testWhileIdle", true));
        config.setMinEvictableIdleTimeMillis(PropertiesUtil.getLongProperty(props, "minEvictableIdleTimeMillis", 1000 * 30));
        config.setTimeBetweenEvictionRunsMillis(PropertiesUtil.getLongProperty(props, "timeBetweenEvictionRunsMillis", 1000 * 30));
        String host = props.getProperty("host", "");
        int port = PropertiesUtil.getIntProperty(props, "port", 6379);
        int timeout = PropertiesUtil.getIntProperty(props, "timeout", 1000 * 3);
        String password = props.getProperty("password", null);
        try {
            if (!StringUtils.isEmpty(password))
                jedisPool = new JedisPool(config, host, port, timeout, password);
            else
                jedisPool = new JedisPool(config, host, port, timeout);
            LOG.debug("JedisPool init success");
        } catch (Exception e) {
            LOG.error("jedisPool init fail:" + e.getMessage());
            throw new RuntimeException("jedisPool init fail.");
        }
    }

    /**
     * 从REDIS获取
     * @param id
     * @return
     */
    protected Map hGetAll(String id) {
        Jedis jedis = null;
        boolean isBroken = false;
        Map map = null;
        try {
            jedis = getJedis();
            if (jedis.exists(id)) {
                map = jedis.hgetAll(id);
                LOG.debug("jedis get success:" + id);
            }
        } catch (Exception e) {
            isBroken = true;
            LOG.error("jedis get fail:" + e.getMessage());
        } finally {
            release(jedis, isBroken); // 返还到池
        }
        return map;
    }
    
    /**
     * 从REDIS获取
     * @param id
     * @return
     */
    public String get(String id) {
        Jedis jedis = null;
        boolean isBroken = false;
        String result = null;
        try {
            jedis = getJedis();
            if (jedis.exists(id)) {
                result = jedis.get(id);
                LOG.debug("jedis get success:" + id);
            }
        } catch (Exception e) {
            isBroken = true;
            LOG.error("jedis get fail:" + e.getMessage());
        } finally {
            release(jedis, isBroken); // 返还到池
        }
        return result;
    }
    
    /**
     * 从REDIS中获取指定域的值
     * @param id
     * @param field
     * @return
     */
    protected String hGet(String id,String field) {
        Jedis jedis = null;
        boolean isBroken = false;
        String value = null;
        try {
            jedis = getJedis();
            if (jedis.exists(id)) {
                value= jedis.hget(id,field);
                LOG.debug("jedis hget success,id:" + id+" field:"+field);
            }
        } catch (Exception e) {
            isBroken = true;
            LOG.error("jedis hget fail:" + e.getMessage());
        } finally {
            release(jedis, isBroken); // 返还到池
        }
        return value;
    }
    
    /**
     * 存储到REDIS,默认过期时间90天
     * @param id
     * @param session
     */
    protected void save(String id, Map session) {
        save(id,session,0);
    }

    /**
     * 存储到REDIS
     * @param id
     * @param session
     * @param expire 单位：秒
     */
    protected void save(String id, Map session, int expire) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            jedis.hmset(id, session);
            if(expire>0)
                jedis.expire(id, expire);
            LOG.debug("jedis save success:" + id);
        } catch (Exception e) {
            isBroken = true;
            LOG.error("jedis save fail:" + e.getMessage());
        } finally {
            release(jedis, isBroken); // 返还到池
        }
    }
    
    /**
     * 存储到REDIS
     * @param id
     * @param session
     * @param expire 单位：秒
     */
    public void set(String key, String value, int expire) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            jedis.set(key, value);
            if(expire>0)
                jedis.expire(key, expire);
            LOG.debug("jedis save success:" + key);
        } catch (Exception e) {
            isBroken = true;
            LOG.error("jedis save fail:" + e.getMessage());
        } finally {
            release(jedis, isBroken); // 返还到池
        }
    }

    /**
     * 从REDIS中删除
     * @param id
     */
    public void remove(String id) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            jedis.del(id);
            LOG.debug("jedis remove success:" + id);
        } catch (Exception e) {
            isBroken = true;
            LOG.error("jedis remove fail:" + e.getMessage());
        } finally {
            release(jedis, isBroken); // 返还到池
        }
    }
    
    /**
     * 从REDIS中删除对应key的field域
     * @param id
     * @param field
     */
    protected void removeField(String id,String field) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            jedis.hdel(id,field);
            LOG.debug("jedis removeField success,id:" + id+" field:"+field);
        } catch (Exception e) {
            isBroken = true;
            LOG.error("jedis removeField fail:" + e.getMessage());
        } finally {
            release(jedis, isBroken); // 返还到池
        }
    }

    private Jedis getJedis() throws JedisException {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
        } catch (JedisException e) {
            LOG.error("jedisPool getResource failed", e);
            throw e;
        }
        return jedis;
    }

    private void release(Jedis jedis, boolean isBroken) {
        if (jedis != null) {
            if (isBroken)
                jedisPool.returnBrokenResource(jedis);
            jedisPool.returnResource(jedis);
        }
    }
    
    /**
     * 从REDIS中按pattern查找key列表
     * @param pattern
     * @return
     */
    protected Set<String> findByPattern(String pattern){
        Jedis jedis = null;
        boolean isBroken = false;
        Set<String> keys=null;
        try {
            jedis = getJedis();
            keys= jedis.keys(pattern);
            LOG.debug("jedis findByPattern success:" + pattern);
        } catch (Exception e) {
            isBroken = true;
            LOG.error("jedis findByPattern fail:" + e.getMessage());
        } finally {
            release(jedis, isBroken); // 返还到池
        }
        return keys;
    }
    
    /**
     * 查询KEY的过期时间
     * @param pattern
     * @return
     */
    protected Long Ttl(String key){
        Jedis jedis = null;
        boolean isBroken = false;
        Long expire=null;
        try {
            jedis = getJedis();
            expire= jedis.ttl(key);
            LOG.debug("jedis ttl success:" + key);
        } catch (Exception e) {
            isBroken = true;
            LOG.error("jedis ttl fail:" + e.getMessage());
        } finally {
            release(jedis, isBroken); // 返还到池
        }
        return expire;
    }
    
    /**
     * 设置缓存过期时间，单位秒
     * @param id
     * @param expire
     */
    protected void expire(String id,int expire){
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            jedis.expire(id, expire);
            LOG.debug("jedis expire success,id:" + id+" expire:"+expire);
        } catch (Exception e) {
            isBroken = true;
            LOG.error("jedis expire fail:" + e.getMessage());
        } finally {
            release(jedis, isBroken); // 返还到池
        }
    }
    
    /**
     * 清除指定KEY的过期时间
     * @param id
     */
    protected void persist(String id){
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            jedis.persist(id);
            LOG.debug("jedis persist success,id:" + id);
        } catch (Exception e) {
            isBroken = true;
            LOG.error("jedis persist fail:" + e.getMessage());
        } finally {
            release(jedis, isBroken); // 返还到池
        }
    }

    /**
     * REDIS中缓存更名
     * @param key
     * @param newKey
     */
    protected void reName(String key,String newKey){
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            jedis.rename(key, newKey);
            LOG.debug("jedis reName success:" + key);
        } catch (Exception e) {
            isBroken = true;
            LOG.error("jedis reName fail:" + e.getMessage());
        } finally {
            release(jedis, isBroken); // 返还到池
        }
    }

}
