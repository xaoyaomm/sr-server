package com.sfc.api.test.dao;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.util.Assert;

import com.store.api.mysql.dao.UserCargoFavoriteRepository;
import com.store.api.mysql.entity.UsersCargoFavorite;

/**
 * 示例
 * 
 * Revision History
 * 
 * 2014年4月25日,vincent,created it
 */
public class UserCargoFavoriteRepositoryTestCase extends BaseDaoTestCase {

    @Autowired
    UserCargoFavoriteRepository res;
    
    @Test
    public void testFindByUserCargoId(){
        List<UsersCargoFavorite> fav=res.findByUserCargoId(2558l);
        log.info(fav.get(0).getVehicleInfoId()+"");
        Assert.notNull(fav);
        Assert.isTrue(fav.size()>0);
    }
    
    @Test
    public void testFindByUserCargoIdPage(){
        Pageable pr = new PageRequest(0, 5, Direction.DESC, "collectTime");
        Page<UsersCargoFavorite> page=res.findByUserCargoId(2558L, pr);
        System.out.println(page.getTotalElements());
    }
}
