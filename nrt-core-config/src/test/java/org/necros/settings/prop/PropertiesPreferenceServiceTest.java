package org.necros.settings.prop;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.necros.settings.PreferenceException;
import org.necros.settings.Preference;
import org.necros.settings.PreferenceServiceTestConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesPreferenceServiceTest {
	private static final Logger logger = LoggerFactory.getLogger(PropertiesPreferenceServiceTest.class);
	
	private PropertiesPreferenceService cpService, tmpService;
	
	private PropertiesPreferenceService initService(String location) {
		PropertiesPreferenceService svc = new PropertiesPreferenceService();
		logger.trace("Loading properties located at: {}", location);
		svc.setPropertiesLocation(location);
		svc.init();
		logger.trace("Properties loaded.");
		return svc;
	}

	private String initTempProps() throws IOException {
		File f = File.createTempFile("test-", ".properties");
		Properties props = new Properties();
		props.put(PreferenceServiceTestConstants.STORE_KEY_TEST_TMP_DIR,
				PreferenceServiceTestConstants.VAL_TEST_TMP_DIR);
		props.put(PreferenceServiceTestConstants.STORE_KEY_TEST_TIMES,
				PreferenceServiceTestConstants.VAL_TEST_TIMES);
		FileOutputStream fos = new FileOutputStream(f);
		props.store(fos, "");
		String fn = "file:" + f.getAbsolutePath();
		return fn;
	}

	@Before
	public void setUp() throws Exception {
		cpService = initService("classpath*:META-INF/*-preferences.properties");
		String location = initTempProps();
		tmpService = initService(location);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testValRetrieving() throws PreferenceException {
		String k = PreferenceServiceTestConstants.KEY_TEST_CP_FILE;
		String strVal = cpService.stringValue(k, null);
		logger.trace("String value of key [{}]: [{}]", k, strVal);
		assertNotNull(strVal);
		k = PreferenceServiceTestConstants.KEY_TEST_VAL_INT;
		Integer ival = cpService.intValue(k, null);
		logger.trace("String value of key [{}]: [{}]", k, ival);
		assertNotNull(ival);
		k = PreferenceServiceTestConstants.KEY_TEST_VAL_FLOAT;
		Double dval = cpService.floatValue(k, null);
		logger.trace("String value of key [{}]: [{}]", k, dval);
		assertNotNull(dval);
	}

	@Test
	public void testClassPathRes() throws PreferenceException {
		Preference p = cpService.getPreference(PreferenceServiceTestConstants.KEY_TEST_CP_FILE);
		logger.trace("{}", p);
		assertNotNull(p);
		String vf = p.getValue();
		assertNotNull(vf);
		p = cpService.getPreference(PreferenceServiceTestConstants.KEY_TEST_CP_DIR);
		logger.trace("{}", p);
		assertNotNull(p);
		String vd = p.getValue();
		assertNotNull(vd);
		assertFalse(vf.equals(vd));
		p = cpService.getPreference(PreferenceServiceTestConstants.KEY_TEST_NOTHING);
		assertNull(p);
	}
	
	@Test
	public void testFileRes() throws PreferenceException {
		Preference p = tmpService.getPreference(PreferenceServiceTestConstants.KEY_TEST_TMP_DIR);
		logger.trace("{}", p);
		assertNotNull(p);
		String vd = p.getValue();
		assertNotNull(vd);
		assertTrue(vd.equals(PreferenceServiceTestConstants.VAL_TEST_TMP_DIR));
		p = tmpService.getPreference(PreferenceServiceTestConstants.KEY_TEST_TIMES);
		logger.trace("{}", p);
		assertNotNull(p);
		String vt = p.getValue();
		assertNotNull(vt);
		assertTrue(vt.equals(PreferenceServiceTestConstants.VAL_TEST_TIMES));
	}
}
