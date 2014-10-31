package com.sfc.api.test.dao;


import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.store.api.mysql.dao.ProcedureDao;
import com.store.api.mysql.entity.procedure.VehicleComment;
import com.store.api.utils.JsonUtils;

public class ProcedureDaoTestCase extends BaseDaoTestCase{
	 @Autowired
	 ProcedureDao procedureDao;
	 @Test
	    public void test() {
	     try {
	    	List<VehicleComment> list = procedureDao.getVehicleCommentList("13413313313", 1, "0", "10", null);
	    	System.out.println(JsonUtils.object2Json(list));
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	    }
}
