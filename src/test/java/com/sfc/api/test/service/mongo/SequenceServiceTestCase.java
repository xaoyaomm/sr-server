package com.sfc.api.test.service.mongo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.sfc.api.test.service.BaseServiceTestCase;
import com.store.api.mongo.service.SequenceService;
/**
 * 序列测试
 * @author @haipenge 
 * haipenge@gmail.com
*  Create Date:2014年8月13日
 */
public class SequenceServiceTestCase extends BaseServiceTestCase {

	@Autowired
	private SequenceService sequenceServcie=null;
	
	@Test
	public void testGetNextSequence() throws Exception{
		int loopISum=0;
		long seqSum=0;
		for(int i=0;i<10;i++){
			loopISum+=(i+1);
			Long nextSeq=this.sequenceServcie.getNextSequence("test");
			seqSum+=nextSeq;
		}
		Assert.isTrue(loopISum==new Long(seqSum).intValue());
	}
}
