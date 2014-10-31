package com.sfc.api.test.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.store.api.mysql.entity.UsersCargoFavorite;
import com.store.api.mysql.service.UserFavoriteService;

/**
 * 示例
 * 
 * Revision History
 * 
 * 2014年4月25日,vincent,created it
 */
public class UserCargoFavoriteServiceTestCase extends BaseServiceTestCase {
    
    @Autowired
    UserFavoriteService favService;
    
    @Test
    public void testFindByUserCargoId(){
        List<UsersCargoFavorite> fav=favService.findByUserCargoId(2558l);
        log.info(fav.get(0).getVehicleInfoId()+"");
        Assert.notNull(fav);
        Assert.isTrue(fav.size()>0);
    }
}
