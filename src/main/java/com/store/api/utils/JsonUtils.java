package com.store.api.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.api.mongo.entity.Product;
import com.store.api.mongo.entity.subdocument.OrderProduct;

public class JsonUtils {
    private static final Logger LOG = LoggerFactory.getLogger(JsonUtils.class);
    private static ObjectMapper objectMapper = null;
    
    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

	/**
	 * Object 转化成json
	 * @param o
	 * @return
	 */
	public static String object2Json(Object o){  
        Writer w = new StringWriter();  
        String json = null;  
        try {  
            objectMapper.writeValue(w, o);  
            json = w.toString();  
            w.close();  
        } catch (IOException e) {  
            // 错误处理  
        	return null;
        }  
        return json;  
    }  
	
	/**
     * JSON转化为对像
     * 
     * @param json
     * @param clazz
     * @return
     */
    public static Object json2Object(String json, Class clazz) {
        Object o = null;
        try {
            o = objectMapper.readValue(json, clazz);
        } catch (JsonParseException e) {
            LOG.error(">>E:" + e);
        } catch (JsonMappingException e) {
            LOG.error(">>E:" + e);
        } catch (IOException e) {
            LOG.error(">>E:" + e);
        }
        return o;
    }
    
    /**
     * JSON转化为对象
     * 
     * @param json
     * @param clazz
     * @return
     */
    public static Map<String,String> json2Map(String json) {
        Map<String,String> o = null;
        try {
            o = objectMapper.readValue(json, new TypeReference<Map<String,String>>(){});
        } catch (JsonParseException e) {
            LOG.error(">>E:" + e);
        } catch (JsonMappingException e) {
            LOG.error(">>E:" + e);
        } catch (IOException e) {
            LOG.error(">>E:" + e);
        }
        return o;
    }
    
    public static List<OrderProduct> json2OrderProduct(String json) {
    	List<OrderProduct> o = null;
        try {
            o = objectMapper.readValue(json, new TypeReference<List<OrderProduct>>(){});
        } catch (JsonParseException e) {
            LOG.error(">>E:" + e);
        } catch (JsonMappingException e) {
            LOG.error(">>E:" + e);
        } catch (IOException e) {
            LOG.error(">>E:" + e);
        }
        return o;
    }
	
	/**
	 * 处理返回的json模式
	 */
	public static String resultJson(int errorcode ,String info ,Object dataList){
		LinkedHashMap<Object,Object> map =new LinkedHashMap<Object,Object>();
		map.put("errorcode", errorcode+"");
		if(null != info){
			map.put("info", info);
		}
		
		if(null != dataList){
			map.put("data", dataList);
		}
		return JsonUtils.object2Json(map);
	}
	
}  

