package com.store.api.mongo.service;

import java.util.List;

import com.store.api.mongo.entity.UploadGpsGroup;
import com.store.api.mongo.entity.VeUploadGpsMG;

public interface VeUploadGpsMGService {

    public VeUploadGpsMG findOne(Long id);
    
    public void save(VeUploadGpsMG entity);
    
    /**
     * 按开始与结束时间过滤数据，按用户ID分组查询最早与最晚的坐标上传时间。
     * @param firstDate
     * @param lastDate
     * @return
     */
    public List<UploadGpsGroup> groupUploadGpsWithId(Long firstDate,Long lastDate);
    
    
    /**
     * 查询时间段内GPS记录,按ID顺序排列
     * @param veModel
     * @param freTime
     * @param arrTime
     * @return
     */
    public List<VeUploadGpsMG> findByUserIdAndUptimeBetweenOrderByIdAsc(Long userId, Long freTime, Long arrTime);
    
    /**
     * 批量保存
     * @param items
     */
    public void save(List<VeUploadGpsMG> items);

    /**
     * 查询一段时间内的GPS记录 按时间降序排列
     * @param veModel
     * @param freTime
     * @param arrTime
     * @return
     */
    public List<VeUploadGpsMG> findByUserIdAndUptimeBetweenOrderByUptimeDesc(Long userId,Long freTime, Long arrTime);
    
    /**
     *  按开始与结束时间过滤数据，按用户ID  查询最早与最晚的坐标上传时间。
     * @param firstDate
     * @param lastDate
     * @param userId
     * @return
     */
     
    public List<UploadGpsGroup> getUploadGpsGroupByIdAndTime(Long firstDate,Long lastDate,Long userId);
    
    
    
}


