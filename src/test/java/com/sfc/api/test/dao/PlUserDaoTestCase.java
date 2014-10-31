package com.sfc.api.test.dao;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.store.api.mysql.dao.PlUsersDao;
import com.store.api.mysql.entity.PlUsers;
import com.store.api.utils.JsonUtils;

public class PlUserDaoTestCase extends BaseDaoTestCase{
    @Autowired
    PlUsersDao plUsersDao;
    @Test
    public void test() {
        List<PlUsers> puList = null;
        try {
//            puList=plUsersDao.findPlUsersByUserId(3L);
        	puList = plUsersDao.findByMobileAndIsvalid("13760301170", "N");
            System.out.println(JsonUtils.object2Json(puList));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
