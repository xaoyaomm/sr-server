package com.sfc.api.test.service.mongo;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.sfc.api.test.service.BaseServiceTestCase;
import com.store.api.mongo.entity.VeUploadGpsMG;
import com.store.api.mongo.service.VeUploadGpsMGService;

public class UploadGpsTestCase extends BaseServiceTestCase {
    
    @Autowired
    private VeUploadGpsMGService service;
    
    @Test
    public void testSave(){
        VeUploadGpsMG gps=new VeUploadGpsMG();
//        gps.setVeUploadId(2L);
        gps.setLat("111.1111");
        gps.setLng("222.2222");
        gps.setUserId(112L);
        gps.setVehicleId(123L);
        gps.setMobile("13333333333");
        gps.setUptime(new Date().getTime());
        service.save(gps);
    }
    
    @Test
    public void testFindByVeModelAndUptimeAfter(){
        List<VeUploadGpsMG> gps=service.findByUserIdAndUptimeBetweenOrderByUptimeDesc(116L, 1408449837132L,1408450077731L);
//        VeUploadGpsMG gps=service.findOne(1L);
        Assert.assertNotNull(gps);
    }
    
    @Test
    public void testGroup(){
        service.groupUploadGpsWithId(1408177126245L, 1408183050212L);
    }
}
