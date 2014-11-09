package com.store.api.session;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.store.api.mongo.entity.enumeration.UserType;
import com.store.api.utils.Utils;

/**
 * session服务类
 * 
 * Revision History
 *
 * @author vincent,2014年11月6日 created it
 */
public class SessionService {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private int expire = 3600*24; // 单位为秒,过期踢出的用户缓存中保存24小时

    private static SessionService instance = null;

    public static synchronized SessionService getInstance() {
        if (instance == null) {
            instance = new SessionService();
        }
        return instance;
    }

    private SessionService() {

    }

    protected Map getSession(String id) {
        try {
            return RedisService.getInstance().hGetAll(id);
        } catch (Exception e) {
            LOG.error("getSession error,sessionId:" + id, e);
        }
        return new HashMap();
    }

    protected void saveSession(String id, Map session) {
        try {
            RedisService.getInstance().save(id, session);
        } catch (Exception e) {
            LOG.error("saveSession error,sessionId:" + id, e);
        }
    }
    
    protected void removeSession(String id) {
        try {
            RedisService.getInstance().remove(id);
        } catch (Exception e) {
            LOG.error("removeSession error,sessionId:" + id, e);
        }
    }
    
    protected void removeField(String id,String field) {
        try {
            RedisService.getInstance().removeField(id,field);
        } catch (Exception e) {
            LOG.error("removeField error,sessionId:" + id, e);
        }
    }

    private String createSessionId(String id, String type, boolean needRandom) {
        String separator = "_";
        StringBuffer sb = new StringBuffer();
        if (needRandom) {
            int random = (int) (Math.random() * 8999 + 1000);
            String str=System.currentTimeMillis()+"";
            sb.append(type).append(separator).append(id).append(separator).append(random);
            sb.append(str.substring(str.length()-3));
            return sb.toString();
        } else {
            sb.append(type).append(separator).append(id).append(separator);
            return sb.toString();
        }
    }

    /**
     * 创建session(不创建session对象，只将用户信息存入redis)
     * 
     * @param id
     *            用户ID
     * @param type
     *            用户类型
     * @param map
     *            sessionMap(对象必须转成JSON格式)
     * @return sessionId
     */
    public String createSession(String id, UserType type, Map<String, String> map,String oldSessionId) throws Exception {
        String sessionId = createSessionId(id, type.toString(), false);
//        String pattern = createSessionId(id, type, false) + "*";
        try {
//            Set<String> keys = RedisService.getInstance().findByPattern(pattern.toString());
//            if (null != keys && keys.size() > 0) {
//                for (String key : keys) {
//                    Map redisMap = RedisService.getInstance().hGetAll(key);
//                    redisMap.put(Constant.SESSION_INVALID_KEY, Constant.SESSION_INVALID_VALUE);
//                    RedisService.getInstance().save(key, redisMap, expire);
//                }
//            }
            RedisService.getInstance().save(sessionId, map);
            if(!Utils.isEmpty(oldSessionId))
                RedisService.getInstance().remove(oldSessionId);
            
        } catch (Exception e) {
            LOG.error("createSession error", e);
            throw new Exception("createSession error", e);
        }
        return sessionId;
    }

}
