package com.store.api.mongo.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.api.mongo.dao.ContactsRepository;
import com.store.api.mongo.entity.Contact;
import com.store.api.mongo.entity.enumeration.UserType;
import com.store.api.mongo.service.ContactsService;
import com.store.api.mongo.service.SequenceService;

@Service
public class ContactsServiceImpl implements ContactsService {

    @Autowired
    private SequenceService sequenceService = null;

    @Autowired
    private ContactsRepository repository;

    @Override
    public void save(Contact entity) {
        if (null == entity.getId()) {
            entity.setId(sequenceService.getNextSequence(entity));
        }
        repository.save(entity);
    }

    @Override
    public void save(List<Contact> entitys) {
        for (Contact entity : entitys) {
            if (null == entity.getId()) {
                entity.setId(sequenceService.getNextSequence(entity));
            }
        }
        repository.save(entitys);
    }

    @Override
    public Set<String> findByUserIdAndType(Long Userid, UserType type) {
        Set<String> set = new HashSet<String>();
        List<Contact> list = repository.findByUserIdAndUserType(Userid, type);
        if (list.size() > 0) {
            for (Contact contact : list) {
                set.add(contact.getMobile());
            }
        }
        return set;
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

}
