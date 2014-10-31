package com.sfc.api.test.service.mongo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.sfc.api.test.service.BaseServiceTestCase;
import com.store.api.mongo.entity.Contact;
import com.store.api.mongo.entity.enumeration.UserType;
import com.store.api.mongo.service.ContactsService;

public class PlContactsServiceTestCase extends BaseServiceTestCase{
    
    @Autowired
    private ContactsService service;
    
    @Test
    public void testFindByUserIdAndType(){
        Set<String> set=service.findByUserIdAndType(1L, UserType.cargo);
        System.out.println(set.size());
    }
    
    @Test
    public void testSave(){
        Contact con=new Contact();
        con.setMobile("12345678");
        con.setName("li师傅");
        con.setCreateDate(new Date().getTime());
        con.setUserId(1L);
        con.setUserType(UserType.cargo);
        
        Contact con1=new Contact();
        con1.setMobile("87654321");
        con1.setName("w师傅");
        con1.setCreateDate(new Date().getTime());
        con1.setUserId(1L);
        con1.setUserType(UserType.cargo);
        
        Contact con2=new Contact();
        con2.setMobile("1234321");
        con2.setName("z师傅");
        con2.setCreateDate(new Date().getTime());
        con2.setUserId(2L);
        con2.setUserType(UserType.cargo);
        
        List<Contact> list=new ArrayList<Contact>();
        list.add(con);
        list.add(con1);
        list.add(con2);
        service.save(list);
    }

}
