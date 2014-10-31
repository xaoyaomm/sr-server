package com.store.api.mongo.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.api.mongo.dao.LuckdrawBlackListRepository;
import com.store.api.mongo.entity.LuckydrawBlackList;
import com.store.api.mongo.service.LuckdrawBlackListService;
import com.store.api.mongo.service.SequenceService;

@Service
public class LuckdrawBlackListServiceImpl implements LuckdrawBlackListService {
    
    @Autowired
    private SequenceService sequenceService=null;
    
    @Autowired
    private LuckdrawBlackListRepository repository;
    
    public Set<Long> findAllUserId(){
        List<LuckydrawBlackList> list=repository.findAll();
        Set<Long> set=new HashSet<Long>();
        if(null!=list && list.size()>0){
            for(int i=0;i<list.size();i++){
                set.add(list.get(i).getUserId());
            }
        }
        return set;
    }

    @Override
    public void save(LuckydrawBlackList entity) {
        if(null==entity.getId()){
            entity.setId(this.sequenceService.getNextSequence(entity));
        }
        repository.save(entity);
    }

    @Override
    public void delete(LuckydrawBlackList entity) {
        repository.delete(entity);
    }

    @Override
    public List<LuckydrawBlackList> findAll() {
        return repository.findAll();
    }
}
