package com.store.api.test.controller;

import javax.servlet.http.Cookie;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.store.api.session.SessionFilter;

/**
 * 
 * 
 * Revision History
 * 
 * 2014年4月25日,vincent,created it
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-context.xml","classpath:/spring-web.xml" })
@WebAppConfiguration
public class BaseActionTestCase {
    protected Logger log = LoggerFactory.getLogger(this.getClass());
    protected MockMvc mockMvc;
    @Autowired
    protected WebApplicationContext wac;
    protected Cookie cookie;
    protected HttpHeaders headers=new HttpHeaders();
    

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(new SessionFilter(), "/*").build(); 
    }
}
