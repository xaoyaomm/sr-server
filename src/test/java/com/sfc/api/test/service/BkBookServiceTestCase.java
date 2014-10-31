package com.sfc.api.test.service;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import com.store.api.mysql.entity.BkBooking;
import com.store.api.mysql.service.BkBookingService;

public class BkBookServiceTestCase extends BaseServiceTestCase {

    @Autowired
    private BkBookingService service;

    // @Test
    // public void testFindPageByUserIdAndStatus(){
    // Page<BkBooking> page=service.findByBookingToUsersIdAndBookingStatus(3l,
    // 1l, 1, 10);
    // Assert.assertNotNull(page.getContent());
    // }

    @Test
    public void testFindBkBookingByBookingNo() {
        Long bookingNo = 1252L;
        for (int i = 10; i < 100000000; i++) {
            Date a=new Date();
            BkBooking bkBooking = service.findOne(bookingNo);
            Date b=new Date();
            long c=b.getTime()-a.getTime();
            if(c>50)
                System.out.println(c);
            Assert.assertNotNull(bkBooking);
        }
    }
    
    @Test
    public void testFindByUserCargoId(){
        for(int i=0;i<=7;i++){
            Page<BkBooking> bs=service.findByUserCargoId(199l, i, 50,1L);
            System.out.println(i+1+" has:"+bs.hasContent()+": page:"+bs.getTotalPages()+"  el:  "+bs.getTotalElements()+"  size:"+bs.getContent().size());
        }
    }
    
    @Test
    public void testQueryBkBookingIsvalidCount(){
    	int count = service.queryBkBookingIsvalidCount(192L);
    	System.out.println(count);
    }
}
