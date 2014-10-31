package com.sfc.api.test.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.store.api.mysql.service.SearchEngineService;

public class SearchEngineServiceTestCase extends BaseServiceTestCase{
    
    @Autowired
    public SearchEngineService searchEngineService;

    @Test
    public void testUpdateVehicleLevel(){
        boolean flag=searchEngineService.updateVehicleLevel(105L, 100L);
        Assert.assertTrue(flag);
    }
    
    @Test
    public void testUpdateVehicleVisible(){
        boolean flag=searchEngineService.updateVehicleVisible(105L, true);
        Assert.assertTrue(flag);
    }
}
