package com.store.api.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

/**
 * 查询结果数组处理工具
 * @author @haipenge 
 * haipenge@gmail.com
*  Create Date:2014年6月26日
 */
public class ArrayUtil {
	private static Logger logger=LoggerFactory.getLogger(ArrayUtil.class);
	/**
	 * 将Array 结构根据给定的KEY,V结构，转化为Map结构
	 * @todo
	 * @param arg0
	 * @param keys
	 * @return
	 * @author:@haipenge
	 * haipenge@gmail.com
	 * 2014年6月26日
	 */
    public static Map<String,Object> array2Map(Object [] arg0,String[] keys){
    	Map<String,Object> result=new HashMap<String,Object>();
    	if(arg0!=null && keys!=null && arg0.length==keys.length){
    		for(int i=0;i<arg0.length;i++){
    			Object o=arg0[i];
    			String k=keys[i];
    			result.put(k,o);
    		}
    	}else{
    	   logger.debug(">>object array or keys is null,or arry's length not eq keys length.");
    	}
    	return result;
    }
    /**
     * 将一个数组组成的List,转化为一个Map组成的List
     * @todo
     * @param items
     * @param keys
     * @return
     * @author:@haipenge
     * haipenge@gmail.com
     * 2014年6月26日
     */
    public static List<Map<String,Object>> array2Map(List<Object[]> items,String [] keys){
    	List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
    	if(CollectionUtils.isNotEmpty(items)){
    		for(Object[] obj:items){
    			Map<String,Object> map=array2Map(obj,keys);
    			result.add(map);
    		}
    	}
    	return result;
    }
    /**
     * 将Map 组成的List,转化成Bean组成的List
     * @todo
     * @param items
     * @param keys
     * @param clazz
     * @return
     * @author:@haipenge
     * haipenge@gmail.com
     * 2014年6月26日
     */
    public static List<?> map2Bean(List<Map<String,Object>>items,Class clazz){
    	List result=new ArrayList(0);
        for(Map<String,Object> map:items){
        	Object obj=map2Bean(map,clazz);
        	result.add(obj);
        }
    	return result;
    }
    /**
     * 将单个Map转化为Bean
     * @todo
     * @param map
     * @param clazz
     * @return
     * @author:@haipenge
     * haipenge@gmail.com
     * 2014年6月26日
     */
    public static Object map2Bean(Map<String,Object> map,Class clazz){
    	Object result=null;
    	result=BeanUtils.instantiateClass(clazz);
    	BeanUtils.copyProperties(map, result);
    	return result;
    }
}
