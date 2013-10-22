package org.necros.data.h4;

import static org.junit.Assert.*;

import java.util.List;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import org.necros.data.BasicObject;
import org.necros.data.BasicObjectService;
import org.necros.data.IdGenerator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:META-INF/*-beans.xml")
@TransactionConfiguration(defaultRollback=true)
public class BasicObjectServiceH4Test {
	private static final Logger logger = LoggerFactory.getLogger(BasicObjectServiceH4Test.class);
	private static final String ENTITY_NAME = "Person";
	private static final String LOGIN_NAME = "root";
	private static final String ALT_LOGIN_NAME = "admin";

	@Resource
	private BasicObjectService objService;
	@Resource
	private IdGenerator idGenerator;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test @Transactional
	public void testTouch() {
		BasicObject obj;
		String id;
		long createTime, updateTime;
		id = (String)idGenerator.generate();
		obj = objService.touch(id, ENTITY_NAME, LOGIN_NAME);
		assertNotNull(obj);
		assertTrue(id.equals(obj.getId()));
		assertTrue(LOGIN_NAME.equals(obj.getCreatorId()));
		assertTrue(LOGIN_NAME.equals(obj.getOwnerId()));
		assertTrue(LOGIN_NAME.equals(obj.getUpdaterId()));
		assertNotNull(obj.getCreateTime());
		assertNotNull(obj.getUpdateTime());
		createTime = obj.getCreateTime().getTime();
		updateTime = obj.getUpdateTime().getTime();
		obj = objService.touch(id, ENTITY_NAME, ALT_LOGIN_NAME);
		assertNotNull(obj);
		assertTrue(id.equals(obj.getId()));
		assertTrue(LOGIN_NAME.equals(obj.getCreatorId()));
		assertTrue(LOGIN_NAME.equals(obj.getOwnerId()));
		assertTrue(ALT_LOGIN_NAME.equals(obj.getUpdaterId()));
		assertNotNull(obj.getCreateTime());
		assertNotNull(obj.getUpdateTime());
		assertEquals(createTime, obj.getCreateTime().getTime());
		assertFalse(updateTime == obj.getUpdateTime().getTime());
	}
}