package com.store.api.mongo.dao;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.store.api.mongo.entity.VeUploadGpsMG;

public interface VeUploadGpsMGRepository extends PagingAndSortingRepository<VeUploadGpsMG, Long>{
    
    /**
     * 查询时间段内的坐标上传记录，按ID顺序排列
     * @param userId
     * @param freTime
     * @param arrTime
     * @return
     */
    public List<VeUploadGpsMG> findByUserIdAndUptimeBetweenOrderByIdAsc(Long userId, Long freTime, Long arrTime);
    
    /**
     * 查询时间段内的坐标上传记录，按时间倒顺排列
     * @param userId
     * @param freTime
     * @param arrTime
     * @return
     */
    public List<VeUploadGpsMG> findByUserIdAndUptimeBetweenOrderByUptimeDesc(Long userId,Long freTime, Long arrTime);


    
}
