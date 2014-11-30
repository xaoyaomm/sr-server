package com.store.api.mongo.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.api.mongo.dao.SequenceRepository;
import com.store.api.mongo.entity.Sequence;
import com.store.api.mongo.service.SequenceService;

@Service
public class SequenceServiceImpl  implements SequenceService {
	@Autowired
	private SequenceRepository sequenceRepository=null;

	/**
	 * 针对Mongo的序列服务实现
	 */
	@Override
	synchronized public long getNextSequence(String name) {
		 Sequence sequence=this.sequenceRepository.getSequenceByName(name);
		return sequence.getSeq();
	}

	/**
     * 针对Mongo的序列服务实现
     */
    @Override
    synchronized public <T extends Serializable> long getNextSequence(T entity) {
        Sequence sequence=this.sequenceRepository.getSequenceByName(entity.getClass().getSimpleName());
        return sequence.getSeq();
    }

    @Override
    public void SetNextSequence(String sequenceName, long next) {
        Sequence sequence=this.sequenceRepository.getSequenceByName(sequenceName);
        if(null==sequence){
            sequence=new Sequence();
            sequence.setId(System.currentTimeMillis());
            sequence.setName(sequenceName);
            sequence.setSeq(next);
            sequence.setStep(new Long(1));
        }else{
            sequence.setSeq(next);
        }
        sequenceRepository.save(sequence);
    }

}
