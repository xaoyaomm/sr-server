package com.store.api.mongo.dao;

import java.util.List;

import com.store.api.mongo.entity.UploadGpsGroup;

public interface VeUploadGpsMGDao {
    
    /**
     * 按开始与结束时间过滤数据，按用户ID分组查询最早与最晚的坐标上传时间。
     * @param firstDate
     * @param lastDate
     * @return
     */
    public List<UploadGpsGroup> groupUploadGpsWithId(Long firstDate,Long lastDate);
    
    /**
     *  按开始与结束时间过滤数据，按用户ID  查询最早与最晚的坐标上传时间。
     * @param firstDate
     * @param lastDate
     * @param userId
     * @return
     */
     
    public List<UploadGpsGroup> getUploadGpsGroupByIdAndTime(Long firstDate,Long lastDate,Long userId);

}
