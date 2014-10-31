package com.store.api.mongo.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

import com.mongodb.CommandResult;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.store.api.mongo.dao.VeUploadGpsMGDao;
import com.store.api.mongo.entity.UploadGpsGroup;
import com.store.api.utils.JsonUtils;

@Repository
public class VeUploadGpsMgDaoImpl implements VeUploadGpsMGDao {

    @Autowired
    private MongoOperations mongoOps;

    @Override
    public List<UploadGpsGroup> groupUploadGpsWithId(Long firstDate, Long lastDate) {
        StringBuffer jsonSb = new StringBuffer();
        jsonSb.append("{group:{ns:'veUploadGpsMG',key:{userId:1,mobile:1},");
        jsonSb.append("cond:{uptime:{$gte:");
        jsonSb.append(firstDate).append(",$lte:");
        jsonSb.append(lastDate).append("}},");
        jsonSb.append("$reduce:'function(curr,result){" + "result.total+=1;" + "if(result.maxUptime===0){" + "result.maxUptime=curr.uptime;" + "}else{"
                + "if(result.maxUptime<curr.uptime){" + "result.maxUptime=curr.uptime;}}" + "if(result.minUptime===0){" + "result.minUptime=curr.uptime;" + "}else{"
                + "if(result.minUptime>curr.uptime){" + "result.minUptime=curr.uptime;}}}',");

        jsonSb.append("initial:{total:0,maxUptime:0,minUptime:0}}}");
        DBObject command = (DBObject) JSON.parse(jsonSb.toString());
        CommandResult result = mongoOps.executeCommand(command);
        Object obj = result.get("retval");
        List list = (List) JsonUtils.json2Object(obj.toString(), List.class);
        List<UploadGpsGroup> gpsList=new ArrayList<UploadGpsGroup>();
        if (null != list && list.size() > 0) {
            for (Object object : list) {
                Map<String, Object> map=(Map<String, Object>) object;
                UploadGpsGroup gps=new UploadGpsGroup();
                gps.setFirst(MapUtils.getLong(map, "minUptime"));
                gps.setLast(MapUtils.getLong(map, "maxUptime"));
                gps.setUserId(MapUtils.getLong(map, "userId"));
                gps.setMobile(MapUtils.getString(map, "mobile"));
                gpsList.add(gps);
            }
        }
        return gpsList;
    }

    @Override
	public List<UploadGpsGroup> getUploadGpsGroupByIdAndTime(Long firstDate,
			Long lastDate, Long userId) {
		 StringBuffer jsonSb = new StringBuffer();
	        jsonSb.append("{group:{ns:'veUploadGpsMG',key:{userId:1,mobile:1},");
	        jsonSb.append("cond:{").append("userId:").append(userId+",").append(" uptime:{$gte:");
	        jsonSb.append(firstDate).append(",$lte:");
	        jsonSb.append(lastDate).append("}},");
	        jsonSb.append("$reduce:'function(curr,result){" + "result.total+=1;" + "if(result.maxUptime===0){" + "result.maxUptime=curr.uptime;" + "}else{"
	                + "if(result.maxUptime<curr.uptime){" + "result.maxUptime=curr.uptime;}}" + "if(result.minUptime===0){" + "result.minUptime=curr.uptime;" + "}else{"
	                + "if(result.minUptime>curr.uptime){" + "result.minUptime=curr.uptime;}}}',");

	        jsonSb.append("initial:{total:0,maxUptime:0,minUptime:0}}}");
	        DBObject command = (DBObject) JSON.parse(jsonSb.toString());
	        CommandResult result = mongoOps.executeCommand(command);
	        Object obj = result.get("retval");
	        List list = (List) JsonUtils.json2Object(obj.toString(), List.class);
	        List<UploadGpsGroup> gpsList=new ArrayList<UploadGpsGroup>();
	        if (null != list && list.size() > 0) {
	            for (Object object : list) {
	                Map<String, Object> map=(Map<String, Object>) object;
	                UploadGpsGroup gps=new UploadGpsGroup();
	                gps.setFirst(MapUtils.getLong(map, "minUptime"));
	                gps.setLast(MapUtils.getLong(map, "maxUptime"));
	                gps.setUserId(MapUtils.getLong(map, "userId"));
	                gps.setMobile(MapUtils.getString(map, "mobile"));
	                gpsList.add(gps);
	            }
	        }
	        return gpsList;
	}
}
