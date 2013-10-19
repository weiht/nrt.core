package org.necros.auth;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:META-INF/*-beans.xml")
@TransactionConfiguration(defaultRollback=true)
public class AuthenticationServiceImplTest {
	@Resource
	private AuthenticationService authService;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAuthenticate() {
		Login admin;
		admin = authService.authenticate("admin", null);
		assertNull(admin);
		admin = authService.authenticate("admin", "none");
		assertNull(admin);
		admin = authService.authenticate("admin", "nimda");
		assertNotNull(admin);
		Login none;
		none = authService.authenticate("none", "none");
		assertNull(none);
	}

	@Test
	public void testChangePassword() {
		Login admin;
		admin = authService.authenticate("admin", "nimda");
		assertNotNull(admin);
		assertNull(authService.changePassword(admin.getLoginName(), "admin", "admin"));
		assertNull(authService.changePassword(admin.getLoginName(), admin.getPassword(), "admin"));
		admin = authService.changePassword(admin.getLoginName(), "nimda", "admin");
		assertNotNull(admin);
		admin = authService.authenticate("admin", "admin");
		assertNotNull(admin);
	}
}
