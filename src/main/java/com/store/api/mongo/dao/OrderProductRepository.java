package com.store.api.mongo.dao;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.store.api.mongo.entity.OrderProduct;

/**
 * 
 * Revision History
 * 
 * 2014年11月15日,vincent,created it
 */
public interface OrderProductRepository extends MongoRepository<OrderProduct, Long> {
    
    public List<OrderProduct> findByOrderId(Long orderId);

}
