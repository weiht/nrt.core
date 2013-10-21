package org.necros.auth.h4;

import static org.junit.Assert.*;

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

import org.necros.auth.LoginManager;

/**
 * @author weiht
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:META-INF/*-beans.xml")
@TransactionConfiguration(defaultRollback=true)
public class LoginManagerH4Test {
	private static final Logger logger = LoggerFactory.getLogger(LoginManagerH4Test.class);

	@Resource
	private LoginManager loginManager;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test @Transactional
	public void testGet() {
		fail("Not yet implemented.");
	}

	@Test @Transactional
	public void testGetWithName() {
		fail("Not yet implemented.");
	}

	@Test @Transactional
	public void testAdd() {
		fail("Not yet implemented.");
	}

	@Test @Transactional
	public void testRemove() {
		fail("Not yet implemented.");
	}

	@Test @Transactional
	public void testEnable() {
		fail("Not yet implemented.");
	}

	@Test @Transactional
	public void testDisable() {
		fail("Not yet implemented.");
	}

	@Test @Transactional
	public void testResetPassword() {
		fail("Not yet implemented.");
	}
}
