package com.store.api.mongo.service;

import java.util.List;
import java.util.Set;

import com.store.api.mongo.entity.Contact;
import com.store.api.mongo.entity.enumeration.UserType;


public interface ContactsService {
    
    /** 保存联系人 **/
    public void save(Contact entity);
    
    /** 批量保存联系人 **/
    public void save(List<Contact> entitys);
    
    /**
     * 按用户ID与用户类型查找联系人
     * @param Userid
     * @param type
     * @return 联系人手机号Set
     */
    public Set<String> findByUserIdAndType(Long Userid, UserType type);
    
    public void deleteAll();

}
