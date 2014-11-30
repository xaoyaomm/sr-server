package com.store.api.mongo.service;

import java.io.Serializable;



/**
 * 序列服务，当前为mogo提供序列服务
 */
public interface SequenceService  {
  public <T extends Serializable> long getNextSequence(T entity);
  
  public long getNextSequence(String name);
  
  public void SetNextSequence(String sequenceName,long next);
}
