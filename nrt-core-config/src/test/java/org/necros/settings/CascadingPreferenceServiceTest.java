package org.necros.settings;

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

import org.necros.settings.PreferenceException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:META-INF/*-beans.xml")
public class CascadingPreferenceServiceTest {
	private static final Logger logger = LoggerFactory.getLogger(CascadingPreferenceServiceTest.class);
	
	@Resource(name="preferenceService")
	private PreferenceService service;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testGetPreference() throws PreferenceException {
		Preference p = service.getPreference(PreferenceServiceTestConstants.KEY_TEST_CP_FILE);
		logger.trace("{}", p);
		assertNotNull(p);
		String v = p.getValue();
		logger.trace("{}", v);
		assertNotNull(v);
		assertTrue(v.equals("test.properties"));
		p = service.getPreference(PreferenceServiceTestConstants.KEY_TEST_NOTHING);
		logger.trace("{}", p);
		assertNull(p);
	}
	
	@Test
	public void testStringValue() {
		String v = service.stringValue(PreferenceServiceTestConstants.KEY_TEST_CP_FILE, null);
		logger.trace("{}", v);
		assertNotNull(v);
		assertTrue(v.equals("test.properties"));
		v = service.stringValue(PreferenceServiceTestConstants.KEY_TEST_NOTHING, null);
		logger.trace("{}", v);
		assertNull(v);
	}
	
	@Test
	public void testIntValue() {
		Integer v = service.intValue(PreferenceServiceTestConstants.KEY_TEST_VAL_INT, null);
		logger.trace("{}", v);
		assertNotNull(v);
		assertTrue(v == 100);
		v = service.intValue(PreferenceServiceTestConstants.KEY_TEST_NOTHING, null);
		logger.trace("{}", v);
		assertNull(v);
	}
	
	@Test
	public void testFloatValue() {
		Double v = service.floatValue(PreferenceServiceTestConstants.KEY_TEST_VAL_FLOAT, null);
		logger.trace("{}", v);
		assertNotNull(v);
		assertTrue(v == 314d);
		v = service.floatValue(PreferenceServiceTestConstants.KEY_TEST_NOTHING, null);
		logger.trace("{}", v);
		assertNull(v);
	}
}
