package com.store.api.mongo.dao;

import com.store.api.mongo.entity.Sequence;

public interface SequenceRepository  {
	
	public Sequence getSequenceByName(String name);
	
	public void save(Sequence sequence);
	
}
