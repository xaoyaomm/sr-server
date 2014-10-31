package com.sfc.api.test.dao;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.store.api.mysql.dao.TbPrizeLogRepository;
import com.store.api.mysql.dao.VeStatusDao;
import com.store.api.mysql.dao.VeUploadGpsRepository;
import com.store.api.mysql.entity.TbPrizeLog;
import com.store.api.mysql.entity.VeStatus;
import com.store.api.mysql.service.TbPrizeLogService;
import com.store.api.utils.JsonUtils;
import com.store.api.utils.TimeUtil;


public class VestatusDaoTestCase extends BaseDaoTestCase {
	@Autowired
    VeStatusDao ves;
	@Autowired
	VeUploadGpsRepository upload;
	
	@Autowired
	TbPrizeLogRepository  tbPrizeLogRepository;
//	
//	@Test
//    public void test1() throws ParseException{
//        List<TbPrizeLog> list = tbPrizeLogRepository.findByCreateDtAfterAndCreateDtBefore(TimeUtil.getMondayOfThisWeek(), new Date());
//        System.out.println(JsonUtils.object2Json(list));
//    }
    
    @Test
    public void testFindByUserCargoId(){
        List<VeStatus> lv= ves.findByUserId(1L);
        System.out.println(JsonUtils.object2Json(lv));
        
    }
    
    @Test
    public void testInsertVeStatus() throws RuntimeException{
    	Long user_id = 1l;
    	VeStatus v=ves.findOne(2L);
    	System.out.println(v.getVehicleInfoId()+"<--->"+v.getUserId());
    	VeStatus  vestatus =new VeStatus();
    	vestatus.setVeStatus(1L);
		vestatus.setModelId(1L);
		vestatus.setUpdateDt(new Date());
		vestatus.setIsvalid("Y");
		vestatus.setVehicleInfoId(1L);
		vestatus.setUserId(user_id);
		ves.save(vestatus);
//		throw new RuntimeException("test");
		
    }
   
}
