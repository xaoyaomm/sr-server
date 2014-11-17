package com.store.api.test.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.store.api.mongo.entity.Product;
import com.store.api.mongo.service.ProductService;

public class ProductServiceTestCase extends BaseServiceTestCase {

    @Autowired
    private ProductService service;

    @Test
    public void testSave() {
        List<Product> list=new ArrayList<Product>();
        for (int i = 1; i < 21; i++) {
            Product pro = new Product();
            pro.setAreaId(51);
            pro.setCatalogId(1L);
            pro.setImgUrl("http://img12.360buyimg.com/da/jfs/t628/307/22725097/9518/83a5caa7/545c2434Nd9d42d5d.jpg");
            pro.setName("零食"+i);
            DecimalFormat dcmFmt = new DecimalFormat("0.0");
            Random rand = new Random();
            Double f = rand.nextDouble() * 100;
            pro.setPrice(Double.valueOf(dcmFmt.format(f)));
            pro.setStatus(1);
            pro.setOrder(Long.valueOf(i));
            list.add(pro);
        }
        service.save(list);
        
    }
    
    @Test
    public void testFind(){
    	List<Long> ids=new ArrayList<Long>();
    	ids.add(16L);
    	ids.add(23L);
    	ids.add(45L);
    	
    	Map<Long, Product> map=service.findByIds(ids);
    	System.out.println(map.get(16L).getName());
    }

}
