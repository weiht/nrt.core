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

import org.necros.data.MetaClass;
import org.necros.data.MetaClassManager;
import org.necros.data.MetaPackageManager;
import org.necros.data.MetaPackage;
import org.necros.data.MetaProperty;
import org.necros.data.MetaDataAccessException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:META-INF/*-beans.xml")
@TransactionConfiguration(defaultRollback=true)
public class MetaClassManagerH4Test {
	private static final Logger logger = LoggerFactory.getLogger(MetaClassManagerH4Test.class);

	@Resource
	private MetaClassManager manager;
	@Resource
	private MetaPackageManager pkgMgr;

	private String globalId, arrId, arrPropId;

	private void initPackages() throws MetaDataAccessException {
		MetaPackage pkg;
		pkg = new MetaPackage();
		pkg.setPath("net.sf.jsonlib");
		pkgMgr.add(pkg);
		pkg = new MetaPackage();
		pkg.setPath("net.21cn");
		pkgMgr.add(pkg);
		pkg = new MetaPackage();
		pkg.setPath("com.microsoft.web");
		pkgMgr.add(pkg);
	}

	private void initClasses() throws MetaDataAccessException {
		MetaClass cls;
		MetaProperty prop;
		prop = new MetaProperty();
		prop.setName("name");
		prop.setColumnName("arr_name");
		cls = new MetaClass();
		cls.setMetaPackage("net.sf.jsonlib");
		cls.setName("JsonArray");
		cls.setTableName("clazz_json_array");
		cls.getProperties().add(prop);
		cls = manager.add(cls);
		arrId = cls.getId();
		arrPropId = cls.getProperties().iterator().next().getId();
		cls = new MetaClass();
		cls.setName("Global");
		cls.setTableName("clazz_global");
		cls = manager.add(cls);
		globalId = cls.getId();
	}

	@Before
	public void setUp() throws Exception {
		initPackages();
		initClasses();
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test @Transactional
	public void testGet() {
		MetaClass cls;
		cls = manager.get(globalId);
		assertNotNull(cls);
		assertNotNull(cls.getProperties());
		assertEquals(0, cls.getProperties().size());
		cls = manager.get(arrId);
		assertNotNull(cls);
		assertNotNull(cls.getProperties());
		assertEquals(1, cls.getProperties().size());
		cls = manager.get("none");
		assertNull(cls);
	}

	@Test @Transactional
	public void testGetWithName() {
		MetaClass cls;
		String pkg, name;
		pkg = null; name = "Global";
		logger.debug("Retrieving with name: [{}].[{}]", pkg, name);
		cls = manager.getWithName(pkg, name);
		assertNotNull(cls);
		assertNotNull(cls.getProperties());
		assertEquals(0, cls.getProperties().size());
		pkg = ""; name = "Global";
		logger.debug("Retrieving with name: [{}].[{}]", pkg, name);
		cls = manager.getWithName(pkg, name);
		assertNotNull(cls);
		assertNotNull(cls.getProperties());
		assertEquals(0, cls.getProperties().size());
		pkg = "net.sf.jsonlib"; name = "JsonArray";
		logger.debug("Retrieving with name: [{}].[{}]", pkg, name);
		cls = manager.getWithName(pkg, name);
		assertNotNull(cls);
		assertNotNull(cls.getProperties());
		assertEquals(1, cls.getProperties().size());
		pkg = "net.sf.jsonlib"; name = "Global";
		logger.debug("Retrieving with name: [{}].[{}]", pkg, name);
		cls = manager.getWithName(pkg, name);
		assertNull(cls);
		pkg = "none"; name = "Global";
		logger.debug("Retrieving with name: [{}].[{}]", pkg, name);
		cls = manager.getWithName(pkg, name);
		assertNull(cls);
		pkg = null; name = "JsonArray";
		logger.debug("Retrieving with name: [{}].[{}]", pkg, name);
		cls = manager.getWithName(pkg, name);
		assertNull(cls);
		pkg = null; name = null;
		logger.debug("Retrieving with name: [{}].[{}]", pkg, name);
		cls = manager.getWithName(pkg, name);
		assertNull(cls);
	}

	@Test @Transactional
	public void testAdd() throws MetaDataAccessException {
		MetaClass cls;
		logger.debug("Testing adding global MetaClass...");
		cls = new MetaClass();
		cls.setName("Golbal2");
		cls.setTableName("clazz_global_2");
		cls = manager.add(cls);
		assertNotNull(cls);
		assertNotNull(cls.getProperties());
		assertEquals(0, cls.getProperties().size());
		assertNotNull(cls.getId());
		logger.debug("Testing adding packaged MetaClass...");
		cls = new MetaClass();
		cls.setMetaPackage("net.sf.jsonlib");
		cls.setName("JsonObject");
		cls.setTableName("clazz_json_object");
		cls = manager.add(cls);
		assertNotNull(cls);
		assertNotNull(cls.getProperties());
		assertEquals(0, cls.getProperties().size());
		assertNotNull(cls.getId());
		logger.debug("Testing adding packaged MetaClass...");
		cls = new MetaClass();
		cls.setMetaPackage("net.sf.jsonlib");
		cls.setName("Global");
		cls.setTableName("clazz_global");
		cls = manager.add(cls);
		assertNotNull(cls);
		assertNotNull(cls.getProperties());
		assertEquals(0, cls.getProperties().size());
		assertNotNull(cls.getId());
	}

	@Test(expected=MetaDataAccessException.class) @Transactional
	public void testAddInvalidPackage() throws MetaDataAccessException {
		MetaClass cls;
		logger.debug("Testing adding duplicated global MetaClass...");
		cls = new MetaClass();
		cls.setMetaPackage("Global");
		cls.setName("Global");
		cls = manager.add(cls);
	}

	@Test(expected=MetaDataAccessException.class) @Transactional
	public void testAddInvalidName() throws MetaDataAccessException {
		MetaClass cls;
		logger.debug("Testing adding duplicated global MetaClass...");
		cls = new MetaClass();
		cls.setName("Global.1");
		cls = manager.add(cls);
	}

	@Test(expected=MetaDataAccessException.class) @Transactional
	public void testAddDuplicatedGlobal() throws MetaDataAccessException {
		MetaClass cls;
		logger.debug("Testing adding duplicated global MetaClass...");
		cls = new MetaClass();
		cls.setName("Global");
		cls = manager.add(cls);
	}

	@Test(expected=MetaDataAccessException.class) @Transactional
	public void testAddDuplicatedPackaged() throws MetaDataAccessException {
		MetaClass cls;
		logger.debug("Testing adding duplicated packaged MetaClass...");
		cls = new MetaClass();
		cls.setMetaPackage("net.sf.jsonlib");
		cls.setName("JsonArray");
		cls.setTableName("clazz_json_array");
		cls = manager.add(cls);
	}

	@Test @Transactional
	public void testUpdateInfo() throws MetaDataAccessException {
		MetaClass cls;
		cls = new MetaClass();
		cls.setId(arrId);
		cls.setMetaPackage("none");
		cls.setName("invalid..");
		cls.setDisplayName("displayName");
		cls.setDescription("description");
		cls = manager.updateInfo(cls);
		assertNotNull(cls);
		assertTrue(arrId.equals(cls.getId()));
		assertTrue("displayName".equals(cls.getDisplayName()));
		assertTrue("description".equals(cls.getDescription()));
		assertFalse("none".equals(cls.getMetaPackage()));
		assertFalse("invalid..".equals(cls.getName()));
	}

	@Test(expected=MetaDataAccessException.class) @Transactional
	public void testUpdateInfoInvalidId() throws MetaDataAccessException {
		MetaClass cls;
		cls = new MetaClass();
		cls.setId(arrId + "abc");
		cls = manager.updateInfo(cls);
	}

	@Test @Transactional
	public void testRemove() throws MetaDataAccessException {
		MetaClass cls;
		logger.debug("Removing existing MetaClass...");
		cls = manager.remove(arrId);
		assertNotNull(cls);
		assertNull(manager.get(arrId));
	}

	@Test(expected=MetaDataAccessException.class) @Transactional
	public void testRemoveInvalid() throws MetaDataAccessException {
		MetaClass cls;
		logger.debug("Removing non-existing MetaClass...");
		cls = manager.remove(arrId + "abc");
	}

	@Test @Transactional
	public void testMoveTo() throws MetaDataAccessException {
		MetaClass cls;
		String pkg;
		pkg = null;
		cls = manager.moveTo(arrId, pkg);
		assertNotNull(cls);
		assertNull(cls.getMetaPackage());
		pkg = "net.sf";
		cls = manager.moveTo(arrId, pkg);
		assertNotNull(cls);
		assertNotNull(cls.getMetaPackage());
		assertTrue("net.sf".equals(cls.getMetaPackage()));
	}
	
	@Test(expected=MetaDataAccessException.class) @Transactional
	public void testMoveToInvalid() throws MetaDataAccessException {
		MetaClass cls;
		String pkg;
		pkg = "org.necros";
		cls = manager.moveTo(arrId, pkg);
	}

	@Test @Transactional
	public void testAddProperty() throws MetaDataAccessException {
		MetaProperty prop;
		prop = new MetaProperty();
		prop.setName("prop");
		prop = manager.addProperty(arrId, prop);
		assertNotNull(prop);
		assertNotNull(prop.getId());
		assertNotNull(prop.getMetaClass());
		assertNotNull(prop.getMetaClass().getId());
		assertTrue(arrId.equals(prop.getMetaClass().getId()));
		assertNotNull(prop.getMetaClass().getProperties());
		assertEquals(2, prop.getMetaClass().getProperties().size());
		//Duplicated name
		prop = new MetaProperty();
		prop.setName("prop");
		prop = manager.addProperty(arrId, prop);
		assertNotNull(prop);
		assertNotNull(prop.getId());
		assertNotNull(prop.getMetaClass());
		assertNotNull(prop.getMetaClass().getId());
		assertTrue(arrId.equals(prop.getMetaClass().getId()));
		assertNotNull(prop.getMetaClass().getProperties());
		assertEquals(2, prop.getMetaClass().getProperties().size());
	}
	
	@Test(expected=MetaDataAccessException.class) @Transactional
	public void testAddPropertyInvalidClass() throws MetaDataAccessException {
		MetaProperty prop;
		prop = new MetaProperty();
		prop.setName("prop");
		prop = manager.addProperty(arrId + "abc", prop);
	}
	
	@Test(expected=MetaDataAccessException.class) @Transactional
	public void testAddPropertyInvalidName() throws MetaDataAccessException {
		MetaProperty prop;
		prop = new MetaProperty();
		prop.setName("prop.abc");
		prop = manager.addProperty(arrId, prop);
	}

	@Test @Transactional
	public void testRemoveProperty() throws MetaDataAccessException {
		MetaProperty prop;
		MetaClass cls;
		String clsId, propId;
		cls = manager.get(arrId);
		prop = cls.getProperties().iterator().next();
		assertNotNull(prop);
		assertNotNull(prop.getMetaClass());
		clsId = cls.getId();
		propId = prop.getId();
		prop = manager.removeProperty(clsId, propId);
		assertNotNull(prop);
		cls = manager.get(arrId);
		assertNotNull(cls);
		assertNotNull(cls.getProperties());
		assertEquals(0, cls.getProperties().size());
	}

	@Test(expected=MetaDataAccessException.class) @Transactional
	public void testRemovePropertyInvalidProp() throws MetaDataAccessException {
		MetaProperty prop;
		MetaClass cls;
		String clsId, propId;
		cls = manager.get(arrId);
		prop = cls.getProperties().iterator().next();
		clsId = cls.getId();
		propId = prop.getId() + "abc";
		prop = manager.removeProperty(clsId, propId);
	}

	@Test(expected=MetaDataAccessException.class) @Transactional
	public void testRemovePropertyInvalidClazz() throws MetaDataAccessException {
		MetaProperty prop;
		MetaClass cls;
		String clsId, propId;
		cls = manager.get(arrId);
		prop = cls.getProperties().iterator().next();
		clsId = globalId;
		propId = prop.getId();
		prop = manager.removeProperty(clsId, propId);
	}

	@Test @Transactional
	public void testUpdateProperty() throws MetaDataAccessException {
		MetaProperty prop;
		prop = new MetaProperty();
		prop.setId(arrPropId);
		prop.setName("another");
		prop.setDisplayName("dispName");
		prop = manager.updateProperty(prop);
		assertNotNull(prop);
		assertTrue(arrPropId.equals(prop.getId()));
		assertFalse("another".equals(prop.getName()));
		assertTrue("dispName".equals(prop.getDisplayName()));
	}

	@Test(expected=MetaDataAccessException.class) @Transactional
	public void testUpdatePropertyInvalid() throws MetaDataAccessException {
		MetaProperty prop;
		prop = new MetaProperty();
		prop.setId(arrPropId + "abc");
		manager.updateProperty(prop);
	}

	@Test @Transactional
	public void testAll() {
		List<MetaClass> lst;
		lst = manager.all(null);
		assertNotNull(lst);
		assertEquals(1, lst.size());
		lst = manager.all("");
		assertNotNull(lst);
		assertEquals(1, lst.size());
		lst = manager.all("com");
		assertNotNull(lst);
		assertEquals(0, lst.size());
		lst = manager.all("net.sf");
		assertNotNull(lst);
		assertEquals(0, lst.size());
		lst = manager.all("net.sf.jsonlib");
		assertNotNull(lst);
		assertEquals(1, lst.size());
		lst = manager.all("none");
		assertNotNull(lst);
		assertEquals(0, lst.size());
	}

	@Test @Transactional
	public void testSearch() {
		List<MetaClass> lst;
		String filterText;
		filterText = null;
		logger.debug("Searching MetaClass: [{}]", filterText);
		lst = manager.search(filterText);
		assertNotNull(lst);
		assertEquals(2, lst.size());
		filterText = "";
		logger.debug("Searching MetaClass: [{}]", filterText);
		lst = manager.search(filterText);
		assertNotNull(lst);
		assertEquals(2, lst.size());
		filterText = "net";
		logger.debug("Searching MetaClass: [{}]", filterText);
		lst = manager.search(filterText);
		assertNotNull(lst);
		assertEquals(1, lst.size());
		filterText = "l";
		logger.debug("Searching MetaClass: [{}]", filterText);
		lst = manager.search(filterText);
		assertNotNull(lst);
		assertEquals(2, lst.size());
		filterText = "f3209";
		logger.debug("Searching MetaClass: [{}]", filterText);
		lst = manager.search(filterText);
		assertNotNull(lst);
		assertEquals(0, lst.size());
	}

	@Test @Transactional
	public void testSearchInPackage() {
		List<MetaClass> lst;
		String pkg, filterText;
		pkg = null; filterText = null;
		logger.debug("Searching MetaClass in package [{}]: [{}]", pkg, filterText);
		lst = manager.searchInPackage(pkg, filterText);
		assertNotNull(lst);
		assertEquals(1, lst.size());
		pkg = ""; filterText = "";
		logger.debug("Searching MetaClass in package [{}]: [{}]", pkg, filterText);
		lst = manager.searchInPackage(pkg, filterText);
		assertNotNull(lst);
		assertEquals(1, lst.size());
		pkg = null; filterText = "none";
		logger.debug("Searching MetaClass in package [{}]: [{}]", pkg, filterText);
		lst = manager.searchInPackage(pkg, filterText);
		assertNotNull(lst);
		assertEquals(0, lst.size());
		pkg = null; filterText = "o";
		logger.debug("Searching MetaClass in package [{}]: [{}]", pkg, filterText);
		lst = manager.searchInPackage(pkg, filterText);
		assertNotNull(lst);
		assertEquals(1, lst.size());
		pkg = "net"; filterText = null;
		logger.debug("Searching MetaClass in package [{}]: [{}]", pkg, filterText);
		lst = manager.searchInPackage(pkg, filterText);
		assertNotNull(lst);
		assertEquals(0, lst.size());
		pkg = "net.sf.jsonlib"; filterText = null;
		logger.debug("Searching MetaClass in package [{}]: [{}]", pkg, filterText);
		lst = manager.searchInPackage(pkg, filterText);
		assertNotNull(lst);
		assertEquals(1, lst.size());
		pkg = "net.sf.jsonlib"; filterText = "";
		logger.debug("Searching MetaClass in package [{}]: [{}]", pkg, filterText);
		lst = manager.searchInPackage(pkg, filterText);
		assertNotNull(lst);
		assertEquals(1, lst.size());
		pkg = "net.sf.jsonlib"; filterText = "nA";
		logger.debug("Searching MetaClass in package [{}]: [{}]", pkg, filterText);
		lst = manager.searchInPackage(pkg, filterText);
		assertNotNull(lst);
		assertEquals(1, lst.size());
		pkg = "net.sf.jsonlib"; filterText = "none";
		logger.debug("Searching MetaClass in package [{}]: [{}]", pkg, filterText);
		lst = manager.searchInPackage(pkg, filterText);
		assertNotNull(lst);
		assertEquals(0, lst.size());
	}
}
