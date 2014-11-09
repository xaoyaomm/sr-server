package com.store.api.test.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.store.api.mongo.entity.Address;
import com.store.api.mongo.service.AddressService;

public class AddressServiceTestCase extends BaseServiceTestCase {
    
    @Autowired
    private AddressService service;
    
    @Test
    public void testSave(){
        Address addr=new Address();
        addr.setAddress("深圳市福田区彩田北路天威花园XXXX号");
        addr.setUserId(6L);
        addr.setLocation(new Double[]{0D,0D});
        service.save(addr);
        Assert.assertNotNull(addr.getId());
    }
}
