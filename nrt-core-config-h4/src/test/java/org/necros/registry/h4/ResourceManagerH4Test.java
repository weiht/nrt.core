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
		//----------------------------
		n = new ResourceNode();
		n.setParentPath("/org/");
		n.setName("xml");
		n.setNodeType(NodeType.Folder);
		n.setType(ContentType.None);
		resourceManager.add(n);
		xmlId = n.getId();
		//----------------------------
		n = new ResourceNode();
		n.setName("tv");
		n.setNodeType(NodeType.Folder);
		n.setType(ContentType.None);
		resourceManager.add(n);
		tvId = n.getId();
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
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.necros.registry.h4.ResourceManagerH4#root()}.
	 */
	@Test @Transactional
	public void testRoot() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.necros.registry.h4.ResourceManagerH4#filteredRoot(java.lang.String)}.
	 */
	@Test @Transactional
	public void testFilteredRoot() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.necros.registry.h4.ResourceManagerH4#countRoot()}.
	 */
	@Test @Transactional
	public void testCountRoot() {
		fail("Not yet implemented");
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
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.necros.registry.h4.ResourceManagerH4#pageFilteredRoot(java.lang.String, org.necros.paging.Pager)}.
	 */
	@Test @Transactional
	public void testPageFilteredRoot() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.necros.registry.h4.ResourceManagerH4#childrenOf(org.necros.registry.ResourceNode)}.
	 */
	@Test @Transactional
	public void testChildrenOf() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.necros.registry.h4.ResourceManagerH4#filteredChildrenOf(java.lang.String, org.necros.registry.ResourceNode)}.
	 */
	@Test @Transactional
	public void testFilteredChildrenOf() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.necros.registry.h4.ResourceManagerH4#countChildrenOf(org.necros.registry.ResourceNode)}.
	 */
	@Test @Transactional
	public void testCountChildrenOf() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.necros.registry.h4.ResourceManagerH4#pageChildrenOf(org.necros.registry.ResourceNode, org.necros.paging.Pager)}.
	 */
	@Test @Transactional
	public void testPageChildrenOf() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.necros.registry.h4.ResourceManagerH4#countFilteredChildrenOf(java.lang.String, org.necros.registry.ResourceNode)}.
	 */
	@Test @Transactional
	public void testCountFilteredChildrenOf() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.necros.registry.h4.ResourceManagerH4#pageFilteredChildrenOf(java.lang.String, org.necros.registry.ResourceNode, org.necros.paging.Pager)}.
	 */
	@Test @Transactional
	public void testPageFilteredChildrenOf() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.necros.registry.h4.ResourceManagerH4#children(java.lang.String)}.
	 */
	@Test @Transactional
	public void testChildren() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.necros.registry.h4.ResourceManagerH4#filteredChildren(java.lang.String, java.lang.String)}.
	 */
	@Test @Transactional
	public void testFilteredChildren() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.necros.registry.h4.ResourceManagerH4#countChildren(java.lang.String)}.
	 */
	@Test @Transactional
	public void testCountChildren() {
		fail("Not yet implemented");
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
		fail("Not yet implemented");
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
	public void testAdd() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.necros.registry.h4.ResourceManagerH4#remove(java.lang.String)}.
	 */
	@Test @Transactional
	public void testRemove() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.necros.registry.h4.ResourceManagerH4#replaceContent(org.necros.registry.ResourceNode)}.
	 */
	@Test @Transactional
	public void testReplaceContent() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.necros.registry.h4.ResourceManagerH4#rename(java.lang.String, java.lang.String)}.
	 */
	@Test @Transactional
	public void testRename() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.necros.registry.h4.ResourceManagerH4#move(java.lang.String, java.lang.String)}.
	 */
	@Test @Transactional
	public void testMove() {
		fail("Not yet implemented");
	}
}
