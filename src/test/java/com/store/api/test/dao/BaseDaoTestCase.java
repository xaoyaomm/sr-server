package com.store.api.test.dao;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * 
 * 
 * Revision History
 * 
 * 2014年4月25日,vincent,created it
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-dataSourceJDBC.xml","classpath:/spring-jpa.xml"})
@TransactionConfiguration(defaultRollback=true)
public class BaseDaoTestCase extends AbstractTransactionalJUnit4SpringContextTests{
    protected Logger log = LoggerFactory.getLogger(this.getClass());
}
