/**
 * 
 */
package org.necros.dict.h4;

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

import org.necros.dict.*;

/**
 * @author weiht
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:META-INF/*-beans.xml")
@TransactionConfiguration(defaultRollback=true)
public class EntryManagerH4Test {
	private static final Logger logger = LoggerFactory.getLogger(EntryManagerH4Test.class);

	private static final String CATEGORY_NAME = "gender";

	@Resource(name="entryManagerH4")
	private EntryManager entryManager;
	@Resource(name="categoryManagerH4")
	private CategoryManager categoryManager;
	@Resource(name="dictionaryServiceH4")
	private DictionaryService dictionaryService;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		logger.debug("Initializing categories and entries...");
		Category cat = new Category();
		cat.setName(CATEGORY_NAME);
		cat.setValueType(ValueType.String);
		cat.setDescription("Gender of a person.");
		categoryManager.add(cat);
		Entry e = new Entry();
		e.setCategoryName(CATEGORY_NAME);
		e.setValue("n");
		e.setDisplayOrder(2);
		e.setDisplayText("Not provided.");
		entryManager.add(e);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.necros.dict.h4.EntryManagerH4#add(org.necros.dict.Entry)}.
	 */
	@Test(expected=DictionaryException.class) @Transactional
	public void testAdd() throws DictionaryException {
		Entry e;
		e = new Entry();
		e.setCategoryName(CATEGORY_NAME);
		e.setValue("m");
		e.setDisplayOrder(1);
		e.setDisplayText("Male");
		e.setDescription("Gentlemen.");
		e = entryManager.add(e);
		assertNotNull(e);
		assertEquals(2, dictionaryService.countAllEntries(CATEGORY_NAME));
		e = new Entry();
		e.setCategoryName(CATEGORY_NAME);
		e.setValue("f");
		e.setDisplayOrder(2);
		e.setDisplayText("Female");
		e.setDescription("Ladies.");
		entryManager.add(e);
		assertNotNull(e);
		assertEquals(3, dictionaryService.countAllEntries(CATEGORY_NAME));
		e = new Entry();
		e.setCategoryName(CATEGORY_NAME);
		e.setValue("f");
		e.setDisplayOrder(3);
		e.setDisplayText("Not provided.");
		entryManager.add(e);
		assertNotNull(e);
	}

	/**
	 * Test method for {@link org.necros.dict.h4.EntryManagerH4#update(org.necros.dict.Entry)}.
	 */
	@Test(expected=DictionaryException.class) @Transactional
	public void testUpdate() throws DictionaryException {
		Entry e, oe;
		e = new Entry();
		e.setCategoryName(CATEGORY_NAME);
		e.setValue("n");
		e.setDisplayText("Not applicable");
		e.setDisplayOrder(-1);
		e.setStatus("disabled");
		oe = entryManager.update(e);
		assertNotNull(oe);
		assertFalse(e == oe);
		assertTrue(e.getDisplayText().equals(oe.getDisplayText()));
		assertEquals(e.getDisplayOrder(), oe.getDisplayOrder());
		assertNull(oe.getStatus());
		e = new Entry();
		e.setCategoryName(CATEGORY_NAME);
		e.setValue("1");
		entryManager.update(e);
	}

	/**
	 * Test method for {@link org.necros.dict.h4.EntryManagerH4#remove(java.lang.String, java.lang.String)}.
	 */
	@Test @Transactional
	public void testRemove() throws DictionaryException {
		assertEquals(1, dictionaryService.countAllEntries(CATEGORY_NAME));
		entryManager.remove(CATEGORY_NAME, "m");
		assertEquals(1, dictionaryService.countAllEntries(CATEGORY_NAME));
		entryManager.remove(CATEGORY_NAME, "n");
		assertEquals(0, dictionaryService.countAllEntries(CATEGORY_NAME));
	}

	/**
	 * Test method for {@link org.necros.dict.h4.EntryManagerH4#disable(java.lang.String, java.lang.String)}.
	 */
	@Test @Transactional
	public void testDisable() throws DictionaryException {
		Entry e;
		entryManager.enable(CATEGORY_NAME, "n");
		entryManager.disable(CATEGORY_NAME, "n");
		e = dictionaryService.getEntry(CATEGORY_NAME, "n");
		assertNotNull(e);
		assertNotNull(e.getStatus());
		assertTrue(Entry.STATUS_DISABLED.equals(e.getStatus()));
		e = entryManager.disable(CATEGORY_NAME, "m");
		assertNull(e);
	}

	/**
	 * Test method for {@link org.necros.dict.h4.EntryManagerH4#enable(java.lang.String, java.lang.String)}.
	 */
	@Test @Transactional
	public void testEnable() throws DictionaryException {
		Entry e;
		entryManager.disable(CATEGORY_NAME, "n");
		entryManager.enable(CATEGORY_NAME, "n");
		e = dictionaryService.getEntry(CATEGORY_NAME, "n");
		assertNotNull(e);
		assertNull(e.getStatus());
		e = entryManager.disable(CATEGORY_NAME, "m");
		assertNull(e);
	}
}
