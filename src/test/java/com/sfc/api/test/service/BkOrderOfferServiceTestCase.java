package com.sfc.api.test.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.store.api.mysql.entity.BkOrderOffer;
import com.store.api.mysql.service.BkOrderOfferService;

public class BkOrderOfferServiceTestCase extends BaseServiceTestCase {
    
    @Autowired
    BkOrderOfferService service;
    
    @Test
    public void testFindByOrderIdAndVeMobile(){
        List<BkOrderOffer> list=service.findByOrderIdAndVeMobile(1L, "");
        Assert.assertTrue(null!=list);
    }
    
    @Test
    public void testFindbyOrderId(){
        List<BkOrderOffer> list=service.getByOrderId(1L, "price");
        for (BkOrderOffer offer : list) {
            System.out.println(offer.getPrice());
        }
    }

}
