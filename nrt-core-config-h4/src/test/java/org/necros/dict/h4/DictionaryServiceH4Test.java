/**
 * 
 */
package org.necros.dict.h4;

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

import org.necros.paging.Pager;
import org.necros.dict.*;

/**
 * @author weiht
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:META-INF/*-beans.xml")
@TransactionConfiguration(defaultRollback=true)
public class DictionaryServiceH4Test {
	private static final Logger logger = LoggerFactory.getLogger(DictionaryServiceH4Test.class);

	private static final String CATEGORY_NAME = "gender";

	@Resource(name="entryManagerH4")
	private EntryManager entryManager;
	@Resource(name="categoryManagerH4")
	private CategoryManager categoryManager;
	@Resource(name="dictionaryServiceH4")
	private DictionaryService dictionaryService;

	private void generateGenders() throws DictionaryException {
		for (int i = 1; i < 50; i ++) {
			Entry e = new Entry();
			e.setCategoryName(CATEGORY_NAME);
			e.setValue("" + i);
			e.setDisplayOrder(50 - i);
			entryManager.add(e);
		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		logger.debug("Initializing dictionary...");
		Category cat = new Category();
		cat.setName(CATEGORY_NAME);
		cat.setValueType(ValueType.String);
		cat.setDescription("Gender of 1 person.");
		categoryManager.add(cat);
		generateGenders();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		categoryManager.remove(CATEGORY_NAME);
	}

	/**
	 * Test method for {@link org.necros.dict.h4.DictionaryServiceH4#getEntry(java.lang.String, java.lang.String)}.
	 */
	@Test @Transactional
	public void testGetEntry() {
		Entry e;
		e = dictionaryService.getEntry(CATEGORY_NAME, "1");
		assertNotNull(e);
		e = dictionaryService.getEntry(CATEGORY_NAME, "100");
		assertNull(e);
		e = dictionaryService.getEntry("nothing", "1");
		assertNull(e);
	}

	/**
	 * Test method for {@link org.necros.dict.h4.DictionaryServiceH4#allEntries(java.lang.String)}.
	 */
	@Test @Transactional
	public void testAllEntries() {
		List<Entry> entries;
		entries = dictionaryService.allEntries(CATEGORY_NAME);
		assertNotNull(entries);
		assertEquals(49, entries.size());
	}

	/**
	 * Test method for {@link org.necros.dict.h4.DictionaryServiceH4#countAllEntries(java.lang.String)}.
	 */
	@Test @Transactional
	public void testCountAllEntries() {
		int cnt = -1;
		cnt = dictionaryService.countAllEntries(CATEGORY_NAME);
		assertEquals(49, cnt);
	}

	/**
	 * Test method for {@link org.necros.dict.h4.DictionaryServiceH4#pageAllEntries(java.lang.String, org.necros.paging.Pager)}.
	 */
	@Test @Transactional
	public void testPageAllEntries() {
		List<Entry> result;
		Pager<Entry> page;
		page = new Pager<Entry>();
		page = dictionaryService.pageAllEntries(CATEGORY_NAME, page);
		assertNotNull(page);
		result = page.getResult();
		assertNotNull(result);
		assertEquals(10, result.size());
		assertEquals(5, page.getPageCount());
		page.setPageNum(5);
		page = dictionaryService.pageAllEntries(CATEGORY_NAME, page);
		assertNotNull(page);
		result = page.getResult();
		assertNotNull(result);
		assertEquals(9, result.size());
		page.setPageNum(6);
		page = dictionaryService.pageAllEntries(CATEGORY_NAME, page);
		assertNotNull(page);
		result = page.getResult();
		assertNotNull(result);
		assertEquals(0, result.size());
		page.setPageNum(-1);
		page = dictionaryService.pageAllEntries(CATEGORY_NAME, page);
		assertNotNull(page);
		result = page.getResult();
		assertNotNull(result);
		assertEquals(10, result.size());
		assertEquals(1, page.getPageNum());

		// Minimal page size is 2.
		page.setPageSize(1);
		page = dictionaryService.pageAllEntries(CATEGORY_NAME, page);
		assertNotNull(page);
		assertEquals(25, page.getPageCount());
		result = page.getResult();
		assertNotNull(result);
		assertEquals(2, result.size());
		page.setPageNum(25);
		page = dictionaryService.pageAllEntries(CATEGORY_NAME, page);
		assertNotNull(page);
		result = page.getResult();
		assertNotNull(result);
		assertEquals(1, result.size());
	}

	/**
	 * Test method for {@link org.necros.dict.h4.DictionaryServiceH4#filterEntries(java.lang.String, java.lang.String)}.
	 */
	@Test @Transactional
	public void testFilterEntries() {
		List<Entry> entries;
		String filterText;
		filterText = "";
		entries = dictionaryService.filterEntries(CATEGORY_NAME, filterText);
		assertNotNull(entries);
		assertEquals(49, entries.size());
		filterText = "1";
		entries = dictionaryService.filterEntries(CATEGORY_NAME, filterText);
		assertNotNull(entries);
		assertEquals(14, entries.size());
		filterText = "0";
		entries = dictionaryService.filterEntries(CATEGORY_NAME, filterText);
		assertNotNull(entries);
		assertEquals(4, entries.size());
		filterText = "x";
		entries = dictionaryService.filterEntries(CATEGORY_NAME, filterText);
		assertNotNull(entries);
		assertEquals(0, entries.size());
	}

	/**
	 * Test method for {@link org.necros.dict.h4.DictionaryServiceH4#countFilteredEntries(java.lang.String, java.lang.String)}.
	 */
	@Test @Transactional
	public void testCountFilteredEntries() {
		int cnt = -1;
		String filterText;
		filterText = "";
		cnt = dictionaryService.countFilteredEntries(CATEGORY_NAME, filterText);
		assertEquals(49, cnt);
		filterText = "1";
		cnt = dictionaryService.countFilteredEntries(CATEGORY_NAME, filterText);
		assertEquals(14, cnt);
		filterText = "0";
		cnt = dictionaryService.countFilteredEntries(CATEGORY_NAME, filterText);
		assertEquals(4, cnt);
		filterText = "x";
		cnt = dictionaryService.countFilteredEntries(CATEGORY_NAME, filterText);
		assertEquals(0, cnt);
	}

	/**
	 * Test method for {@link org.necros.dict.h4.DictionaryServiceH4#pageFilteredEntries(java.lang.String, java.lang.String, org.necros.paging.Pager)}.
	 */
	@Test @Transactional
	public void testPageFilteredEntries() {
		List<Entry> result;
		Pager<Entry> page;
		String filterText;
		filterText = "1";
		page = new Pager<Entry>();
		page = dictionaryService.pageFilteredEntries(CATEGORY_NAME, filterText, page);
		assertNotNull(page);
		result = page.getResult();
		assertNotNull(result);
		assertEquals(10, result.size());
		assertEquals(2, page.getPageCount());
		page.setPageNum(2);
		page = dictionaryService.pageFilteredEntries(CATEGORY_NAME, filterText, page);
		assertNotNull(page);
		result = page.getResult();
		assertNotNull(result);
		assertEquals(4, result.size());
		page.setPageNum(3);
		page = dictionaryService.pageFilteredEntries(CATEGORY_NAME, filterText, page);
		assertNotNull(page);
		result = page.getResult();
		assertNotNull(result);
		assertEquals(0, result.size());
		page.setPageNum(-1);
		page = dictionaryService.pageFilteredEntries(CATEGORY_NAME, filterText, page);
		assertNotNull(page);
		result = page.getResult();
		assertNotNull(result);
		assertEquals(10, result.size());
		assertEquals(1, page.getPageNum());

		// Minimal page size is 2.
		filterText = "5";
		page.setPageSize(1);
		page = dictionaryService.pageFilteredEntries(CATEGORY_NAME, filterText, page);
		assertNotNull(page);
		assertEquals(3, page.getPageCount());
		result = page.getResult();
		assertNotNull(result);
		assertEquals(2, result.size());
		page.setPageNum(3);
		page = dictionaryService.pageFilteredEntries(CATEGORY_NAME, filterText, page);
		assertNotNull(page);
		result = page.getResult();
		assertNotNull(result);
		assertEquals(1, result.size());
	}

}
