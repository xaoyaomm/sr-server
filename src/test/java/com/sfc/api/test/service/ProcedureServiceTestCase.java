package com.sfc.api.test.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.store.api.mysql.entity.procedure.OperationArea;
import com.store.api.mysql.entity.procedure.VeVehicleInfoDetail;
import com.store.api.mysql.service.ProcedureService;

public class ProcedureServiceTestCase extends BaseServiceTestCase{
    
    @Autowired
    ProcedureService service;
    
    @Test
    public void testGetVehicleDetailByCarId(){
        VeVehicleInfoDetail info=service.getVehicleDetailByCarId(2l);
        Assert.assertTrue(null!=info);
    }
    
    @Test
    public void testFindOperationArea(){
        OperationArea area=service.findOperationArea("13413313313", 2);
        Assert.assertTrue(null!=area);
    }

}
