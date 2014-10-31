package com.store.api.mongo.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.service.spi.ServiceException;
import org.springframework.data.domain.Page;
/**
 * 基础服务类
 * @author @haipenge 
 * haipenge@gmail.com
*  Create Date:2014年8月13日
 * @param <T>
 * @param <ID>
 */
public interface BaseService<T, ID extends Serializable> {
	/**
	 * 保存数据
	 * 
	 * @param entity
	 * @throws Exception
	 * @Date:Create Date:2014年3月19日
	 * @Author @haipenge
	 */
	public void save(T entity) ;
	
	/**
	 * 批量保存
	 * @todo
	 * @param entities
	 * @throws ServiceException
	 * @author:@haipenge
	 * haipenge@gmail.com
	 * 2014年5月20日
	 */
	
	public void save(Iterable<T> entities);
	
	/**
	 * 
	 * @todo
	 * @param entity
	 * @throws ServiceException
	 * @author:@haipenge
	 * haipenge@gmail.com
	 * 2014年5月20日
	 */
	public void saveAndFlush(T entity) ;

	

	/**
	 * 删除
	 * 
	 * @param id
	 * @throws Exception
	 * @Date:Create Date:2014年3月19日
	 * @Author @haipenge
	 */
	public void remove(ID id);
	
	/**
	 * 删除
	 * @todo
	 * @param entity
	 * @throws ServiceException
	 * @author:@haipenge
	 * haipenge@gmail.com
	 * 2014年5月19日
	 */
	public void remove(T entity) ;
	
	/**
	 * 
	 * @todo
	 * @throws ServiceException
	 * @author:@haipenge
	 * haipenge@gmail.com
	 * 2014年5月20日
	 */
	public void removeAll();
	
	/**
	 * 
	 * @todo
	 * @throws ServiceException
	 * @author:@haipenge
	 * haipenge@gmail.com
	 * 2014年5月20日
	 */
	public void removeAllInBatch() ;
	
	/**
	 * 批量删除
	 * @todo
	 * @param entities
	 * @throws ServiceException
	 * @author:@haipenge
	 * haipenge@gmail.com
	 * 2014年5月20日
	 */
	public void removeInBatch(Iterable<T> entities);
	
	
	/**
	 * 根据主键进行数据查询
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 * @Date:Create Date:2014年3月19日
	 * @Author @haipenge
	 */
	public T get(ID id) ;
	
	
	/**
	 * 
	 * @todo
	 * @return
	 * @throws ServiceException
	 * @author:@haipenge
	 * haipenge@gmail.com
	 * 2014年5月20日
	 */
    public List<T> getAll() ;
    
    /**
     * 查询ID列表的值
     * @todo
     * @param ids
     * @return
     * @throws ServiceException
     * @author:@haipenge
     * haipenge@gmail.com
     * 2014年5月20日
     */
    public List<T> getAll(Iterable<ID> ids);
    /**
     * 分页查询
     * @todo
     * @param pageable
     * @return
     * @throws ServiceException
     * @author:@haipenge
     * haipenge@gmail.com
     * 2014年5月20日
     */
    public Page<T> getPage(Map<String,Object> searchParams,int page,int size);
}
