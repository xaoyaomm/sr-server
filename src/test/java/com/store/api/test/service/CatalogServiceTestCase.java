package com.store.api.test.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.store.api.mongo.dao.AreaRepository;
import com.store.api.mongo.entity.Area;
import com.store.api.mongo.entity.Catalog;
import com.store.api.mongo.service.CatalogService;

public class CatalogServiceTestCase extends BaseServiceTestCase {

    @Autowired
    private CatalogService service;
    
    @Autowired
    private AreaRepository dao;
    
    @Test
    public void testSave(){
        Catalog catalog=new Catalog();
        catalog.setName("零食");
        catalog.setOrder(1L);
//        service.save(catalog);
    }

}
