package com.store.api.mongo.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.store.api.mongo.entity.Contact;
import com.store.api.mongo.entity.enumeration.UserType;

public interface ContactsRepository extends MongoRepository<Contact, Long>{
    
    public List<Contact> findByUserIdAndUserType (Long Userid,UserType Type);

}
