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

import org.necros.data.MetaPackage;
import org.necros.data.MetaPackageManager;
import org.necros.data.MetaDataAccessException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:META-INF/*-beans.xml")
@TransactionConfiguration(defaultRollback=true)
public class MetaPackageManagerH4Test {
	private static final Logger logger = LoggerFactory.getLogger(MetaPackageManagerH4Test.class);

	@Resource
	private MetaPackageManager manager;

	private void initPackages() throws MetaDataAccessException {
		MetaPackage pkg;
		pkg = new MetaPackage();
		pkg.setPath("net.sf.jsonlib");
		manager.add(pkg);
		pkg = new MetaPackage();
		pkg.setPath("net.21cn");
		manager.add(pkg);
		pkg = new MetaPackage();
		pkg.setPath("com.microsoft.web");
		manager.add(pkg);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		try {
			initPackages();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test @Transactional
	public void testGet() {
		MetaPackage pkg;
		String path;
		logger.debug("MetaPackageManager get method test:");
		path = "net";
		pkg = manager.get(path);
		logger.debug("MetaPackage with path [{}]: {}", path, pkg);
		assertNotNull(pkg);
		path = "net.sf.jsonlib";
		pkg = manager.get(path);
		logger.debug("MetaPackage with path [{}]: {}", path, pkg);
		assertNotNull(pkg);
		path = "com";
		pkg = manager.get(path);
		logger.debug("MetaPackage with path [{}]: {}", path, pkg);
		assertNotNull(pkg);
		path = null;
		pkg = manager.get(path);
		logger.debug("MetaPackage with path [{}]: {}", path, pkg);
		assertNull(pkg);
		path = "";
		pkg = manager.get(path);
		logger.debug("MetaPackage with path [{}]: {}", path, pkg);
		assertNull(pkg);
		path = "\n";
		pkg = manager.get(path);
		logger.debug("MetaPackage with path [{}]: {}", path, pkg);
		assertNull(pkg);
	}

	@Test @Transactional
	public void testAdd() throws MetaDataAccessException {
		MetaPackage pkg;
		String path;
		String name;
		logger.debug("Testing MetaPackageManager.add ...");
		path = "org";
		pkg = new MetaPackage();
		pkg.setPath(path);
		pkg = manager.add(pkg);
		assertNotNull(pkg);
		assertNotNull(pkg.getName());
		assertTrue(pkg.getName().equals(path));
		assertNull(pkg.getParentPath());
		path = "org.necros.portal";
		pkg = new MetaPackage();
		pkg.setPath(path);
		pkg = manager.add(pkg);
		assertNotNull(pkg);
		assertNotNull(pkg.getName());
		assertFalse(pkg.getName().equals(path));
		assertNotNull(pkg.getParentPath());
		assertTrue(pkg.getPath().equals(pkg.getParentPath() + MetaPackage.SEPARATOR + pkg.getName()));
		path = "cn.gov.hebei";
		pkg = new MetaPackage();
		pkg.setPath(path);
		manager.add(pkg);
		pkg = manager.get("cn");
		assertNotNull(pkg);
	}

	@Test(expected=MetaDataAccessException.class) @Transactional
	public void testAddDuplicated() throws MetaDataAccessException {
		MetaPackage pkg;
		pkg = new MetaPackage();
		pkg.setPath("com.microsoft");
		manager.add(pkg);
	}

	@Test(expected=MetaDataAccessException.class) @Transactional
	public void testAddInvalidPath() throws MetaDataAccessException {
		MetaPackage pkg;
		pkg = new MetaPackage();
		pkg.setPath("com.micro/soft");
		manager.add(pkg);
	}

	@Test @Transactional
	public void testUpdateDescription() throws MetaDataAccessException {
		MetaPackage pkg, orig;
		String path, desc;
		path = "net"; desc = "something";
		pkg = new MetaPackage();
		pkg.setPath(path);
		pkg.setDescription(desc);
		orig = manager.updateDescription(pkg);
		assertNotNull(orig);
		assertFalse(pkg == orig);
		assertNotNull(orig.getDescription());
		assertTrue(desc.equals(orig.getDescription()));
		pkg = new MetaPackage();
		pkg.setPath(path);
		orig = manager.updateDescription(pkg);
		assertNotNull(orig);
		assertFalse(pkg == orig);
		assertNull(orig.getDescription());
	}

	@Test(expected=MetaDataAccessException.class) @Transactional
	public void testUpdateDescriptionInvalid() throws MetaDataAccessException {
		MetaPackage pkg;
		pkg = new MetaPackage();
		pkg.setPath("none");
		manager.updateDescription(pkg);
	}

	@Test @Transactional
	public void testRemove() throws MetaDataAccessException {
		MetaPackage pkg, pkgGot;
		String path;
		logger.debug("Testing MetaPackageManager.remove ...");
		path = "net.sf.jsonlib";
		pkgGot = manager.get(path);
		logger.debug("MetaPackage got for [{}]: [{}]", path, pkgGot);
		assertNotNull(pkgGot);
		path = "net.sf";
		pkgGot = manager.get(path);
		logger.debug("MetaPackage got for [{}]: [{}]", path, pkgGot);
		assertNotNull(pkgGot);
		pkg = manager.remove(path);
		assertNotNull(pkg);
		path = "net.sf.jsonlib";
		pkgGot = manager.get(path);
		logger.debug("MetaPackage got for [{}]: [{}]", path, pkgGot);
		assertNull(pkgGot);
		path = "net.sf";
		pkgGot = manager.get(path);
		logger.debug("MetaPackage got for [{}]: [{}]", path, pkgGot);
		assertNull(pkgGot);
		path = "net";
		pkgGot = manager.get(path);
		logger.debug("MetaPackage got for [{}]: [{}]", path, pkgGot);
		assertNotNull(pkgGot);
		path = "net.21cn";
		pkgGot = manager.get(path);
		logger.debug("MetaPackage got for [{}]: [{}]", path, pkgGot);
		assertNotNull(pkgGot);
		path = "net";
		pkg = manager.remove(path);
		assertNotNull(pkg);
		path = "net.21cn";
		pkgGot = manager.get(path);
		logger.debug("MetaPackage got for [{}]: [{}]", path, pkgGot);
		assertNull(pkgGot);
		path = "net";
		pkgGot = manager.get(path);
		logger.debug("MetaPackage got for [{}]: [{}]", path, pkgGot);
		assertNull(pkgGot);
	}

	@Test(expected=MetaDataAccessException.class) @Transactional
	public void testRemoveInvalid() throws MetaDataAccessException {
		manager.remove("org");
	}

	@Test @Transactional
	public void testRoot() throws MetaDataAccessException {
		List<MetaPackage> root;
		root = manager.root();
		assertNotNull(root);
		assertEquals(2, root.size());
		manager.remove("net");
		root = manager.root();
		assertNotNull(root);
		assertEquals(1, root.size());
		manager.remove("com");
		root = manager.root();
		assertNotNull(root);
		assertEquals(0, root.size());
	}

	@Test @Transactional
	public void testChildren() {
		List<MetaPackage> children;
		String path;
		path = "net";
		children = manager.children(path);
		assertNotNull(children);
		assertEquals(2, children.size());
		path = "com";
		children = manager.children(path);
		assertNotNull(children);
		assertEquals(1, children.size());
		path = "net.sf";
		children = manager.children(path);
		assertNotNull(children);
		assertEquals(1, children.size());
		assertTrue("jsonlib".equals(children.get(0).getName()));
		path = "org";
		children = manager.children(path);
		assertNotNull(children);
		assertEquals(0, children.size());
	}
}
