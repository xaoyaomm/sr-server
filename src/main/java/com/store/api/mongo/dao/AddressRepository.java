package com.store.api.mongo.dao;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.store.api.mongo.entity.Address;

public interface AddressRepository extends MongoRepository<Address, Long> {
    
    public List<Address> findByUserId(Long userId);

}
