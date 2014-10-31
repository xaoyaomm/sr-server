package com.store.api.mongo.service;

import java.io.Serializable;



/**
 * 序列服务，当前为mogo提供序列服务
 * @author @haipenge 
 * haipenge@gmail.com
*  Create Date:2014年8月10日
 */
public interface SequenceService  {
  public <T extends Serializable> Long getNextSequence(T entity);
  
  public void SetNextSequence(String sequenceName,Long next);
}
