/**
 * 
 */
package org.necros.settings.h4;

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

import org.necros.settings.PreferenceException;
import org.necros.settings.Preference;
import org.necros.settings.PreferenceManager;

/**
 * @author weiht
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:META-INF/*-beans.xml")
@TransactionConfiguration(defaultRollback=true)
public class PreferenceManagerH4Test {
	private static final Logger logger = LoggerFactory.getLogger(PreferenceManagerH4Test.class);

	private static final String KEY_ADDR = "/org/necros/addr";

	private static final String RAW_ADDR = "石家庄市友谊南大街${port}号";
	private static final String RAW_BALANCE = "5200.32";
	private static final String RAW_PORT = "42";
	private static final String COMPILED_ADDR = RAW_ADDR.replace("${port}", RAW_PORT);
	private static final int INT_PORT = Integer.parseInt(RAW_PORT);

	@Resource(name="preferenceManager")
	private PreferenceManager manager;
	
	private Preference insertPreference(String key, String value) throws PreferenceException {
		Preference p = new Preference();
		p.setKey(key);
		p.setValue(value);
		p.setItemType(Preference.ITEM_TYPE_SETTING);
		manager.create(p);
		return p;
	}

	private Preference insertFolder(String key) throws PreferenceException {
		Preference p = new Preference();
		p.setKey(key);
		p.setItemType(Preference.ITEM_TYPE_FOLDER);
		manager.create(p);
		return p;
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		logger.debug("Compiled addr: {}", COMPILED_ADDR);
		logger.trace("Initializing Preference manager test...");
		insertFolder("/org");
		insertFolder("/org/necros");
		insertPreference(KEY_ADDR, RAW_ADDR);
		insertPreference("/org/necros/port", RAW_PORT);
		insertPreference("/org/necros/balance", RAW_BALANCE);
		logger.trace("Initialized.");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.necros.settings.h4.PreferenceManagerH4#get(java.lang.String)}.
	 * @throws PreferenceException 
	 */
	@Test @Transactional
	public void testGet() throws PreferenceException {
		Preference p = manager.get("none");
		assertNull(p);
		p = manager.get("/org");
		assertNotNull(p);
	}

	/**
	 * Test method for {@link org.necros.settings.h4.PreferenceManagerH4#rootPaths()}.
	 * @throws PreferenceException 
	 */
	@Test @Transactional
	public void testRootPaths() throws PreferenceException {
		List<Preference> root = manager.rootPaths();
		assertNotNull(root);
		assertEquals(1, root.size());
	}

	/**
	 * Test method for {@link org.necros.settings.h4.PreferenceManagerH4#childPaths(java.lang.String)}.
	 * @throws PreferenceException 
	 */
	@Test @Transactional
	public void testChildPaths() throws PreferenceException {
		List<Preference> children = manager.childPaths("/org");
		assertNotNull(children);
		assertEquals(1, children.size());
		children = manager.childPaths("/net");
		assertNotNull(children);
		assertEquals(0, children.size());
	}

	/**
	 * Test method for {@link org.necros.settings.h4.PreferenceManagerH4#childPreferences(java.lang.String)}.
	 * @throws PreferenceException 
	 */
	@Test @Transactional
	public void testChildPreferences() throws PreferenceException {
		List<Preference> plist = manager.childPreferences("/org");
		assertNotNull(plist);
		assertEquals(0, plist.size());
		plist = manager.childPreferences("/org/necros");
		assertNotNull(plist);
		assertEquals(3, plist.size());
		plist = manager.childPreferences("/org/hyper");
		assertNotNull(plist);
		assertEquals(0, plist.size());
	}

	/**
	 * Test method for {@link org.necros.settings.h4.PreferenceManagerH4#children(java.lang.String)}.
	 * @throws PreferenceException 
	 */
	@Test @Transactional
	public void testChildren() throws PreferenceException {
		List<Preference> plist = manager.children("/org");
		assertNotNull(plist);
		assertEquals(1, plist.size());
		plist = manager.children("/org/necros");
		assertNotNull(plist);
		assertEquals(3, plist.size());
		plist = manager.children("/org/hyper");
		assertNotNull(plist);
		assertEquals(0, plist.size());
	}

	/**
	 * Test method for {@link org.necros.settings.h4.PreferenceManagerH4#create(org.necros.settings.Preference)}.
	 * @throws PreferenceException 
	 */
	@Test @Transactional
	public void testCreate() throws PreferenceException {
		List<Preference> root = manager.rootPaths();
		assertNotNull(root);
		assertEquals(1, root.size());
		insertFolder("/net");
		root = manager.rootPaths();
		assertNotNull(root);
		assertEquals(2, root.size());
	}

	/**
	 * Test method for {@link org.necros.settings.h4.PreferenceManagerH4#update(org.necros.settings.Preference)}.
	 * @throws PreferenceException 
	 */
	@Test @Transactional
	public void testUpdate() throws PreferenceException {
		String newVal = "I don't know.";
		Preference p = manager.get(KEY_ADDR);
		assertNotNull(p);
		assertFalse(newVal.equals(p.getValue()));
		p.setValue(newVal);
		manager.update(p);
		p = manager.get(KEY_ADDR);
		assertNotNull(p);
		assertTrue(newVal.equals(p.getValue()));
	}

	/**
	 * Test method for {@link org.necros.settings.h4.PreferenceManagerH4#remove(org.necros.settings.Preference)}.
	 * @throws PreferenceException 
	 */
	@Test @Transactional
	public void testRemove() throws PreferenceException {
		List<Preference> root = manager.rootPaths();
		assertNotNull(root);
		assertEquals(1, root.size());
		manager.remove(root.get(0));
		root = manager.rootPaths();
		assertNotNull(root);
		assertEquals(0, root.size());
	}

	/**
	 * Test method for {@link org.necros.settings.h4.PreferenceManagerH4#allPlain()}.
	 * @throws PreferenceException 
	 */
	@Test @Transactional
	public void testAllPlain() throws PreferenceException {
		List<Preference> plain = manager.allPlain();
		assertNotNull(plain);
		assertEquals(5, plain.size());
	}

	/**
	 * Test method for {@link org.necros.settings.AbstractPreferenceService#getPreference(java.lang.String)}.
	 * @throws PreferenceException 
	 */
	@Test @Transactional
	public void testGetPreference() throws PreferenceException {
		//TODO 如果是folder，应该返回空还是直接返回？
		Preference p = manager.getPreference(KEY_ADDR);
		assertNotNull(p);
		assertFalse(RAW_ADDR.equals(p.getValue()));
		assertTrue(COMPILED_ADDR.equals(p.getValue()));
		p = manager.getPreference("none");
		assertNull(p);
	}

	/**
	 * Test method for {@link org.necros.settings.AbstractPreferenceService#stringValue(java.lang.String, java.lang.String)}.
	 */
	@Test @Transactional
	public void testStringValue() {
		String v = manager.stringValue(KEY_ADDR, null);
		assertNotNull(v);
		assertFalse(RAW_ADDR.equals(v));
		assertTrue(COMPILED_ADDR.equals(v));
		v = manager.stringValue("none", null);
		assertNull(v);
		v = manager.stringValue("none", "none");
		assertNotNull(v);
		assertTrue("none".equals(v));
	}

	/**
	 * Test method for {@link org.necros.settings.AbstractPreferenceService#intValue(java.lang.String, java.lang.Integer)}.
	 */
	@Test @Transactional
	public void testIntValue() {
		Integer v = manager.intValue("/org/necros/port", null);
		assertNotNull(v);
		assertEquals(INT_PORT, v.intValue());
		v = manager.intValue("none", null);
		assertNull(v);
		v = manager.intValue("none", Integer.MAX_VALUE);
		assertNotNull(v);
		assertEquals(Integer.MAX_VALUE, v.intValue());
		v = manager.intValue(KEY_ADDR, null);
		assertNull(v);
		v = manager.intValue(KEY_ADDR, Integer.MAX_VALUE);
		assertNotNull(v);
		assertEquals(Integer.MAX_VALUE, v.intValue());
	}

	/**
	 * Test method for {@link org.necros.settings.AbstractPreferenceService#floatValue(java.lang.String, java.lang.Double)}.
	 */
	@Test @Transactional
	public void testFloatValue() {
		Double v = manager.floatValue("/org/necros/balance", null);
		assertNotNull(v);
		assertTrue(v.toString().equals(RAW_BALANCE));
		v = manager.floatValue("none", null);
		assertNull(v);
		v = manager.floatValue("none", Double.MAX_VALUE);
		assertNotNull(v);
		assertTrue(Double.toString(Double.MAX_VALUE).equals(v.toString()));
		v = manager.floatValue(KEY_ADDR, null);
		assertNull(v);
		v = manager.floatValue(KEY_ADDR, Double.MAX_VALUE);
		assertNotNull(v);
		assertTrue(Double.toString(Double.MAX_VALUE).equals(v.toString()));
	}
}
