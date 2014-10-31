package com.sfc.api.test.service.mongo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import com.sfc.api.test.service.BaseServiceTestCase;
import com.store.api.mongo.entity.CargoAddress;
import com.store.api.mongo.service.CargoAddressService;

public class CargoAddressServiceTestCase extends BaseServiceTestCase{
    
    @Autowired
    private CargoAddressService service=null;
    
    @Test
    public void testSave(){
        CargoAddress address=new CargoAddress();
        address.setAddressDetail("塘长路南山智园3C");
        address.setAddress(null);
        address.setAddressShort("");
        address.setCargoId(213L);
        address.setLat(22.602652);
        address.setLng(114.011892);
        address.setUseCount(3);
        address.setType(1);
        service.save(address);
    }
    
    @Test
    public void testFind(){
//        CargoAddress address=service.findByCargoIdAndLngAndLat(2,325L, 113.993504, 23.065587);
//        address.setUseCount(address.getUseCount()+1);
//        service.save(address);
//        System.out.println(address.getAddress());
        Page<CargoAddress> list=service.findByCargoId(2,325L);
        for (CargoAddress add : list.getContent()) {
            System.out.println(add.getId());
        }
//        Assert.assertNotNull(address);
    }
    
    @Test
    public void testFindByAddress(){
        CargoAddress address=service.findByTypeAndCargoIdAndAddress(2, 325L, "test", "", "");
        System.out.println(address.getAddress());
    }

}
