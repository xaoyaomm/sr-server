package com.sfc.api.test.service.mongo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.sfc.api.test.service.BaseServiceTestCase;
import com.store.api.Migration.service.ConvertService;
import com.store.api.mongo.entity.ConverterBean;

public class ConverterServiceTestCase extends BaseServiceTestCase {
    
    @Autowired
    private ConvertService service;
    
    @Test
    public void testSave(){
        ConverterBean bean=new ConverterBean();
        bean.setDesc("联系人");
        bean.setDone(false);
        bean.setName("contact");
        service.save(bean);
    }

}
