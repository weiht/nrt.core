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

import org.necros.auth.AuthException;
import org.necros.auth.LoginManager;
import org.necros.auth.Login;
import org.necros.data.UsableStatuses;

/**
 * @author weiht
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:META-INF/*-beans.xml")
@TransactionConfiguration(defaultRollback=true)
public class LoginManagerH4Test {
	private static final Logger logger = LoggerFactory.getLogger(LoginManagerH4Test.class);
	private static final String DEF_PASSWORD = "123456";

	@Resource
	private LoginManager loginManager;

	private String rootId, adminId;

	private String addLogin(String name, String status) throws AuthException {
		Login l = new Login();
		l.setLoginName(name);
		l.setPassword(DEF_PASSWORD);
		l.setStatus(status);
		loginManager.add(l);
		return l.getId();
	}

	@Before
	public void setUp() throws Exception {
		rootId = addLogin("root", null);
		adminId = addLogin("admin", UsableStatuses.DISABLED);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test @Transactional
	public void testGet() {
		Login l;
		l = loginManager.get(rootId);
		assertNotNull(l);
		assertTrue("root".equals(l.getLoginName()));
		l = loginManager.get(adminId);
		assertNotNull(l);
		assertTrue("admin".equals(l.getLoginName()));
	}

	@Test @Transactional
	public void testGetWithName() {
		Login l;
		l = loginManager.getWithName("root");
		assertNotNull(l);
		assertTrue("root".equals(l.getLoginName()));
		l = loginManager.getWithName("admin");
		assertNotNull(l);
		assertTrue("admin".equals(l.getLoginName()));
	}

	@Test @Transactional
	public void testAdd() throws AuthException {
		Login l, newL;
		String ln, lpwd, lid;
		ln = "test"; lpwd = "testpwd";
		l = new Login();
		l.setLoginName(ln);
		l.setPassword(lpwd);
		newL = loginManager.add(l);
		assertNotNull(newL);
		assertTrue(ln.equals(newL.getLoginName()));
		assertFalse(lpwd.equals(newL.getPassword()));
	}

	@Test(expected=AuthException.class) @Transactional
	public void testAddDuplicated() throws AuthException {
		Login l;
		l = new Login();
		l.setLoginName("root");
		loginManager.add(l);
	}

	@Test @Transactional
	public void testRemove() throws AuthException {
		Login l, rm;
		l = loginManager.getWithName("root");
		assertNotNull(l);
		rm = loginManager.remove(l.getId());
		assertNotNull(rm);
		l = loginManager.getWithName("root");
		assertNull(l);
	}

	@Test @Transactional
	public void testEnable() throws AuthException {
		Login l, dl;
		l = loginManager.getWithName("admin");
		assertNotNull(l);
		assertNotNull(l.getStatus());
		dl = loginManager.disable(l.getId());
		assertNotNull(dl);
		assertTrue(dl.getId().equals(l.getId()));
		assertNull(dl.getStatus());
	}

	@Test @Transactional
	public void testDisable() throws AuthException {
		Login l, el;
		l = loginManager.getWithName("root");
		assertNotNull(l);
		assertNull(l.getStatus());
		el = loginManager.enable(l.getId());
		assertNotNull(el);
		assertTrue(el.getId().equals(l.getId()));
		assertNotNull(el.getStatus());
	}

	@Test @Transactional
	public void testResetPassword() {
		Login l;
		String pwd, pwd1, pwd2;
		l = loginManager.getWithName("root");
		assertNotNull(l);
		pwd = l.getPassword();
		assertNotNull(pwd);
		pwd1 = loginManager.resetPassword("root");
		assertNotNull(pwd1);
		l = loginManager.getWithName("root");
		assertNotNull(l);
		assertTrue(pwd1.equals(l.getPassword()));
		assertFalse(pwd1.equals(pwd));
		pwd2 = loginManager.resetPassword("root");
		assertNotNull(pwd2);
		l = loginManager.getWithName("root");
		assertNotNull(l);
		assertTrue(pwd2.equals(l.getPassword()));
		assertFalse(pwd2.equals(pwd1));
		assertFalse(pwd2.equals(pwd));
	}
}
