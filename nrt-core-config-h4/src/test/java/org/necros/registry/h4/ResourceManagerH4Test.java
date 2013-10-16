package org.necros.registry.h4;

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

import org.necros.registry.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:META-INF/*-beans.xml")
@TransactionConfiguration(defaultRollback=true)
public class ResourceManagerH4Test {
	private static final Logger logger = LoggerFactory.getLogger(ResourceManagerH4Test.class);

	@Resource(name="resourceManager")
	private ResourceManager resourceManager;
	private String orgId, xmlId, tvId;

	private void debugNode(ResourceNode n) {
		if (!logger.isDebugEnabled()) return;
		logger.debug("---------------Resource node---------------");
		logger.debug("id: " + n.getId());
		logger.debug("parentPath: " + n.getParentPath());
		logger.debug("name: " + n.getName());
		logger.debug("path: " + n.getPath());
		logger.debug("-----------End of Resource node------------");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		ResourceNode n;
		n = new ResourceNode();
		n.setName("org");
		n.setNodeType(NodeType.Folder);
		n.setType(ContentType.None);
		resourceManager.add(n);
		orgId = n.getId();
		debugNode(n);
		//----------------------------
		n = new ResourceNode();
		n.setParentPath("/org/");
		n.setName("xml");
		n.setNodeType(NodeType.Folder);
		n.setType(ContentType.String);
		n.setStringValue("<?xml version=\"1.1\"?><root></root>");
		resourceManager.add(n);
		xmlId = n.getId();
		debugNode(n);
		//----------------------------
		n = new ResourceNode();
		n.setName("tv");
		n.setNodeType(NodeType.Folder);
		n.setType(ContentType.None);
		resourceManager.add(n);
		tvId = n.getId();
		debugNode(n);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.necros.registry.h4.ResourceManagerH4#get(java.lang.String)}.
	 */
	@Test @Transactional
	public void testGet() {
		ResourceNode n;
		n = resourceManager.get(orgId);
		assertNotNull(n);
		assertEquals(n.getParentPath(), ResourceNode.SEPARATOR);
		assertEquals(n.getPath(), ResourceNode.SEPARATOR + n.getName());
		n = resourceManager.get("nothing");
		assertNull(n);
		n = resourceManager.get(null);
		assertNull(n);
	}

	/**
	 * Test method for {@link org.necros.registry.h4.ResourceManagerH4#getWithPath(java.lang.String)}.
	 */
	@Test @Transactional
	public void testGetWithPath() {
		ResourceNode n;
		n = resourceManager.getWithPath("/org");
		assertNotNull(n);
		assertEquals(n.getParentPath(), ResourceNode.SEPARATOR);
		n = resourceManager.getWithPath("/org/xml");
		assertNotNull(n);
		assertEquals(n.getParentPath(), "/org/");
		n = resourceManager.getWithPath("nothing");
		assertNull(n);
		n = resourceManager.getWithPath(null);
		assertNull(n);
	}

	/**
	 * Test method for {@link org.necros.registry.h4.ResourceManagerH4#root()}.
	 */
	@Test @Transactional
	public void testRoot() {
		List<ResourceNode> root;
		root = resourceManager.root();
		assertNotNull(root);
		assertEquals(2, root.size());
	}

	/**
	 * Test method for {@link org.necros.registry.h4.ResourceManagerH4#filteredRoot(java.lang.String)}.
	 */
	@Test @Transactional
	public void testFilteredRoot() {
		List<ResourceNode> root;
		root = resourceManager.filteredRoot("o");
		assertNotNull(root);
		assertEquals(1, root.size());
		root = resourceManager.filteredRoot("v");
		assertNotNull(root);
		assertEquals(1, root.size());
		root = resourceManager.filteredRoot("x");
		assertNotNull(root);
		assertEquals(0, root.size());
	}

	/**
	 * Test method for {@link org.necros.registry.h4.ResourceManagerH4#countRoot()}.
	 */
	@Test @Transactional
	public void testCountRoot() {
		int cnt = 0;
		cnt = resourceManager.countRoot();
		assertEquals(2, cnt);
	}

	/**
	 * Test method for {@link org.necros.registry.h4.ResourceManagerH4#pageRoot(org.necros.paging.Pager)}.
	 */
	@Test @Transactional
	public void testPageRoot() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.necros.registry.h4.ResourceManagerH4#countFilteredRoot(java.lang.String)}.
	 */
	@Test @Transactional
	public void testCountFilteredRoot() {
		int cnt = 0;
		cnt = resourceManager.countFilteredRoot("o");
		assertEquals(1, cnt);
		cnt = resourceManager.countFilteredRoot("v");
		assertEquals(1, cnt);
		cnt = resourceManager.countFilteredRoot("x");
		assertEquals(0, cnt);
	}

	/**
	 * Test method for {@link org.necros.registry.h4.ResourceManagerH4#pageFilteredRoot(java.lang.String, org.necros.paging.Pager)}.
	 */
	@Test @Transactional
	public void testPageFilteredRoot() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.necros.registry.h4.ResourceManagerH4#children(java.lang.String)}.
	 */
	@Test @Transactional
	public void testChildren() {
		String p;
		List<ResourceNode> children;
		p = "/";
		children = resourceManager.children(p);
		assertNotNull(children);
		logger.debug("Children of [{}]: {}", p, children.size());
		assertEquals(2, children.size());
		p = "/org";
		children = resourceManager.children(p);
		assertNotNull(children);
		logger.debug("Children of [{}]: {}", p, children.size());
		assertEquals(1, children.size());
		p = "/org/";
		children = resourceManager.children(p);
		assertNotNull(children);
		logger.debug("Children of [{}]: {}", p, children.size());
		assertEquals(1, children.size());
		p = "/org/xml/";
		children = resourceManager.children(p);
		assertNotNull(children);
		logger.debug("Children of [{}]: {}", p, children.size());
		assertEquals(0, children.size());
		p = "/tv/";
		children = resourceManager.children(p);
		assertNotNull(children);
		logger.debug("Children of [{}]: {}", p, children.size());
		assertEquals(0, children.size());
		p = "nothing";
		children = resourceManager.children(p);
		assertNotNull(children);
		logger.debug("Children of [{}]: {}", p, children.size());
		assertEquals(0, children.size());
	}

	/**
	 * Test method for {@link org.necros.registry.h4.ResourceManagerH4#filteredChildren(java.lang.String, java.lang.String)}.
	 */
	@Test @Transactional
	public void testFilteredChildren() {
		String p;
		List<ResourceNode> children;
		p = "/";
		children = resourceManager.filteredChildren("o", p);
		assertNotNull(children);
		assertEquals(1, children.size());
		p = "/";
		children = resourceManager.filteredChildren("v", p);
		assertNotNull(children);
		assertEquals(1, children.size());
		p = "/";
		children = resourceManager.filteredChildren("x", p);
		assertNotNull(children);
		assertEquals(0, children.size());
		p = "/org/";
		children = resourceManager.filteredChildren("o", p);
		assertNotNull(children);
		assertEquals(0, children.size());
		p = "/org/";
		children = resourceManager.filteredChildren("x", p);
		assertNotNull(children);
		assertEquals(1, children.size());
	}

	/**
	 * Test method for {@link org.necros.registry.h4.ResourceManagerH4#countChildren(java.lang.String)}.
	 */
	@Test @Transactional
	public void testCountChildren() {
		String p;
		int cnt = -1;
		p = "/";
		cnt = resourceManager.countChildren(p);
		assertEquals(2, cnt);
		p = "/org";
		cnt = resourceManager.countChildren(p);
		assertEquals(1, cnt);
		p = "/org/";
		cnt = resourceManager.countChildren(p);
		assertEquals(1, cnt);
		p = "/org/xml/";
		cnt = resourceManager.countChildren(p);
		assertEquals(0, cnt);
		p = "/tv/";
		cnt = resourceManager.countChildren(p);
		assertEquals(0, cnt);
		p = "nothing";
		cnt = resourceManager.countChildren(p);
		assertEquals(0, cnt);
	}

	/**
	 * Test method for {@link org.necros.registry.h4.ResourceManagerH4#pageChildren(java.lang.String, org.necros.paging.Pager)}.
	 */
	@Test @Transactional
	public void testPageChildren() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.necros.registry.h4.ResourceManagerH4#countFilteredChildren(java.lang.String, java.lang.String)}.
	 */
	@Test @Transactional
	public void testCountFilteredChildren() {
		String p;
		int cnt = -1;
		p = "/";
		cnt = resourceManager.countFilteredChildren("o", p);
		assertEquals(1, cnt);
		p = "/";
		cnt = resourceManager.countFilteredChildren("v", p);
		assertEquals(1, cnt);
		p = "/";
		cnt = resourceManager.countFilteredChildren("x", p);
		assertEquals(0, cnt);
		p = "/org/";
		cnt = resourceManager.countFilteredChildren("o", p);
		assertEquals(0, cnt);
		p = "/org/";
		cnt = resourceManager.countFilteredChildren("x", p);
		assertEquals(1, cnt);
	}

	/**
	 * Test method for {@link org.necros.registry.h4.ResourceManagerH4#pageFilteredChildren(java.lang.String, java.lang.String, org.necros.paging.Pager)}.
	 */
	@Test @Transactional
	public void testPageFilteredChildren() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.necros.registry.h4.ResourceManagerH4#add(org.necros.registry.ResourceNode)}.
	 */
	@Test @Transactional
	public void testAdd() throws RegistryAccessException {
		int cnt1, cnt2;
		String id, ppath;
		ResourceNode n, nn;
		cnt1 = resourceManager.countRoot();
		//--------------------------
		n = new ResourceNode();
		n.setParentPath("/");
		n.setName("info");
		nn = resourceManager.add(n);
		assertNotNull(nn);
		id = nn.getId();
		assertNotNull(id);
		cnt2 = resourceManager.countRoot();
		assertEquals(cnt1 + 1, cnt2);
		ppath = nn.getPath();
		n = new ResourceNode();
		n.setParentPath(ppath);
		n.setName("security");
		n.setNodeType(NodeType.Resource);
		n.setType(ContentType.Long);
		n.setLongValue(Long.MAX_VALUE);
		nn = resourceManager.add(n);
		id = nn.getId();
		assertNotNull(id);
		cnt2 = resourceManager.countChildren(ppath);
		assertEquals(1, cnt2);
	}

	@Test(expected=RegistryAccessException.class) @Transactional
	public void testAddDuplicated() throws RegistryAccessException {
		ResourceNode n;
		n = new ResourceNode();
		n.setParentPath("/");
		n.setName("org");
		resourceManager.add(n);
	}

	/**
	 * Test method for {@link org.necros.registry.h4.ResourceManagerH4#remove(java.lang.String)}.
	 */
	@Test @Transactional
	public void testRemove() throws RegistryAccessException {
		ResourceNode removed;
		int cnt, rcnt;
		cnt = resourceManager.countRoot();
		removed = resourceManager.remove("nothing");
		assertNull(removed);
		rcnt = resourceManager.countRoot();
		assertEquals(cnt, rcnt);
		removed = resourceManager.remove(orgId);
		assertNotNull(removed);
		assertTrue(orgId.equals(removed.getId()));
		rcnt = resourceManager.countRoot();
		assertEquals(cnt - 1, rcnt);
	}

	/**
	 * Test method for {@link org.necros.registry.h4.ResourceManagerH4#replaceContent(org.necros.registry.ResourceNode)}.
	 */
	@Test @Transactional
	public void testReplaceContent() throws RegistryAccessException {
		ResourceNode n, nn;
		n = resourceManager.getWithPath("/org/xml");
		debugNode(n);
		nn = new ResourceNode();
		nn.setType(ContentType.Double);
		nn.setDoubleValue(3.1415926d);
		nn.setId(n.getId());
		debugNode(nn);
		nn = resourceManager.replaceContent(nn);
		debugNode(nn);
		assertNotNull(nn);
		assertNotNull(nn.getPath());
		assertTrue(nn.getPath().equals(n.getPath()));
	}

	@Test(expected=RegistryAccessException.class) @Transactional
	public void testReplaceContentNotFound() throws RegistryAccessException {
		ResourceNode n, nn;
		n = resourceManager.getWithPath("/org/xml");
		debugNode(n);
		resourceManager.remove(n.getId());
		nn = new ResourceNode();
		nn.setType(ContentType.Double);
		nn.setDoubleValue(3.1415926d);
		nn.setId(n.getId());
		debugNode(nn);
		nn = resourceManager.replaceContent(nn);
	}

	/**
	 * Test method for {@link org.necros.registry.h4.ResourceManagerH4#rename(java.lang.String, java.lang.String)}.
	 */
	@Test @Transactional
	public void testRename() throws RegistryAccessException {
		ResourceNode n, nn;
		String id, name, oname;
		name = "html";
		n = resourceManager.getWithPath("/org/xml");
		id = n.getId();
		oname = n.getName();
		nn = resourceManager.rename(id, name);
		debugNode(nn);
		assertNotNull(nn);
		assertNotNull(nn.getName());
		assertTrue(nn.getName().equals(name));
		assertFalse(nn.getName().equals(oname));
		assertTrue(nn.getId().equals(id));
		nn = resourceManager.rename(id, name);
		debugNode(nn);
		assertNotNull(nn);
		assertNotNull(nn.getName());
		assertTrue(nn.getName().equals(name));
		assertTrue(nn.getId().equals(id));
	}

	@Test(expected=RegistryAccessException.class) @Transactional
	public void testRenameNotFound() throws RegistryAccessException {
		resourceManager.rename("nothing", "html");
	}

	@Test(expected=RegistryAccessException.class) @Transactional
	public void testRenameDuplicated() throws RegistryAccessException {
		ResourceNode n;
		n = resourceManager.getWithPath("/org");
		resourceManager.rename(n.getId(), "tv");
	}

	/**
	 * Test method for {@link org.necros.registry.h4.ResourceManagerH4#move(java.lang.String, java.lang.String)}.
	 */
	@Test @Transactional
	public void testMove() throws RegistryAccessException {
		ResourceNode n, nn;
		String id, p, path, name;
		n = resourceManager.getWithPath("/org/xml");
		p = "/tv/";
		id = n.getId();
		path = n.getPath();
		name = n.getName();
		nn = resourceManager.move(id, p);
		debugNode(nn);
		assertNotNull(nn);
		assertNotNull(nn.getParentPath());
		assertTrue(nn.getParentPath().equals(p));
		assertTrue(nn.getName().equals(name));
		assertTrue(nn.getId().equals(id));
		assertFalse(nn.getPath().equals(path));
		nn = resourceManager.move(id, p);
		debugNode(nn);
		assertNotNull(nn);
		assertNotNull(nn.getParentPath());
		assertTrue(nn.getParentPath().equals(p));
		assertTrue(nn.getId().equals(id));
	}

	@Test(expected=RegistryAccessException.class) @Transactional
	public void testMoveNotFound() throws RegistryAccessException {
		resourceManager.move("nothing", "/tv");
	}

	@Test(expected=RegistryAccessException.class) @Transactional
	public void testMoveDuplicated() throws RegistryAccessException {
		ResourceNode n;
		n = resourceManager.getWithPath("/tv");
		n = resourceManager.rename(n.getId(), "xml");
		debugNode(n);
		resourceManager.move(n.getId(), "/org");
	}
}
