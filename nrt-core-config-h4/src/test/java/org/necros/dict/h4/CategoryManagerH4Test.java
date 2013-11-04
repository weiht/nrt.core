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
public class CategoryManagerH4Test {
	private static final Logger logger = LoggerFactory.getLogger(CategoryManagerH4Test.class);

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
		logger.debug("Initializing category...");
		Category cat = new Category();
		cat.setName("gender");
		cat.setValueType(ValueType.String);
		cat.setDescription("Gender of 1 person.");
		categoryManager.add(cat);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.necros.dict.h4.CategoryManagerH4#add(org.necros.dict.Category)}.
	 */
	@Test @Transactional
	public void testAdd() throws DictionaryException {
		Category cat;
		cat = new Category();
		cat.setName("race");
		cat.setValueType(ValueType.String);
		cat.setDescription("Race of a person.");
		cat = categoryManager.add(cat);
		assertNotNull(cat);
	}

	@Test(expected=DictionaryException.class) @Transactional
	public void testAddDuplicated() throws DictionaryException {
		Category cat;
		cat = new Category();
		cat.setName("gender");
		cat.setValueType(ValueType.String);
		cat.setDescription("Race of a person.");
		cat = categoryManager.add(cat);
	}

	/**
	 * Test method for {@link org.necros.dict.h4.CategoryManagerH4#update(org.necros.dict.Category)}.
	 */
	@Test @Transactional
	public void testUpdate() throws DictionaryException {
		Category cat, ocat;
		ocat = categoryManager.get("gender");
		String desc = ocat.getDescription();
		ValueType t = ocat.getValueType();
		cat = new Category();
		cat.setName("gender");
		cat.setValueType(ValueType.Long);
		cat.setDescription("Race of a person.");
		cat = categoryManager.update(cat);
		assertNotNull(cat);
		assertNotNull(cat.getDescription());
		assertFalse(cat.getDescription().equals(desc));
		assertTrue(t.equals(cat.getValueType()));
	}

	@Test(expected=DictionaryException.class) @Transactional
	public void testUpdateNotFound() throws DictionaryException {
		Category cat;
		cat = new Category();
		cat.setName("race");
		cat.setValueType(ValueType.Long);
		cat.setDescription("Race of a person.");
		cat = categoryManager.update(cat);
	}

	/**
	 * Test method for {@link org.necros.dict.h4.CategoryManagerH4#remove(java.lang.String)}.
	 */
	@Test @Transactional
	public void testRemove() throws DictionaryException {
		Category cat;
		cat = categoryManager.get("gender");
		assertNotNull(cat);
		cat = categoryManager.remove("gender");
		assertNotNull(cat);
		cat = categoryManager.remove("gender");
		assertNull(cat);
	}

	/**
	 * Test method for {@link org.necros.dict.h4.CategoryManagerH4#get(java.lang.String)}.
	 */
	@Test @Transactional
	public void testGet() {
		Category cat;
		cat = categoryManager.get("gender");
		assertNotNull(cat);
	}

	private void generateCategories() throws DictionaryException {
		for (int i = 1; i <= 48; i ++) {
			Category cat = new Category();
			cat.setName("category_" + i);
			categoryManager.add(cat);
		}
	}

	/**
	 * Test method for {@link org.necros.dict.h4.CategoryManagerH4#all()}.
	 */
	@Test @Transactional
	public void testAll() throws DictionaryException {
		List<Category> cats;
		Category cat;
		cats = categoryManager.all();
		assertNotNull(cats);
		assertEquals(1, cats.size());

		cat = new Category();
		cat.setName("race");
		cat.setValueType(ValueType.String);
		cat.setDescription("Race of a person.");
		cat = categoryManager.add(cat);
		assertNotNull(cat);
		cats = categoryManager.all();
		assertNotNull(cats);
		assertEquals(2, cats.size());

		generateCategories();
		cats = categoryManager.all();
		assertNotNull(cats);
		assertEquals(50, cats.size());
	}

	/**
	 * Test method for {@link org.necros.dict.h4.CategoryManagerH4#countAll()}.
	 */
	@Test @Transactional
	public void testCountAll() throws DictionaryException {
		int cnt = -1;
		Category cat;
		cnt = categoryManager.countAll();
		assertEquals(1, cnt);

		cat = new Category();
		cat.setName("race");
		cat.setValueType(ValueType.String);
		cat.setDescription("Race of a person.");
		cat = categoryManager.add(cat);
		assertNotNull(cat);
		cnt = categoryManager.countAll();
		assertEquals(2, cnt);

		generateCategories();
		cnt = categoryManager.countAll();
		assertEquals(50, cnt);
	}

	/**
	 * Test method for {@link org.necros.dict.h4.CategoryManagerH4#pageAll(org.necros.paging.Pager)}.
	 */
	@Test @Transactional
	public void testPageAll() throws DictionaryException {
		generateCategories();
		
		List<Category> result;
		Pager<Category> page;
		page = new Pager<Category>();
		page = categoryManager.pageAll(page);
		assertNotNull(page);
		result = page.getResult();
		assertNotNull(result);
		assertEquals(10, result.size());
		assertEquals(5, page.getPageCount());
		page.setPageNum(5);
		page = categoryManager.pageAll(page);
		assertNotNull(page);
		result = page.getResult();
		assertNotNull(result);
		assertEquals(9, result.size());
		page.setPageNum(6);
		page = categoryManager.pageAll(page);
		assertNotNull(page);
		result = page.getResult();
		assertNotNull(result);
		assertEquals(0, result.size());
		page.setPageNum(-1);
		page = categoryManager.pageAll(page);
		assertNotNull(page);
		result = page.getResult();
		assertNotNull(result);
		assertEquals(10, result.size());
		assertEquals(1, page.getPageNum());

		// Minimal page size is 2.
		page.setPageSize(1);
		page = categoryManager.pageAll(page);
		assertNotNull(page);
		assertEquals(25, page.getPageCount());
		result = page.getResult();
		assertNotNull(result);
		assertEquals(2, result.size());
		page.setPageNum(25);
		page = categoryManager.pageAll(page);
		assertNotNull(page);
		result = page.getResult();
		assertNotNull(result);
		assertEquals(1, result.size());
	}

	/**
	 * Test method for {@link org.necros.dict.h4.CategoryManagerH4#filtered(java.lang.String)}.
	 */
	@Test @Transactional
	public void testFiltered() throws DictionaryException {
		List<Category> cats;

		generateCategories();
		cats = categoryManager.filtered("cat");
		assertNotNull(cats);
		assertEquals(48, cats.size());
		cats = categoryManager.filtered("1");
		assertNotNull(cats);
		assertEquals(15, cats.size());
	}

	/**
	 * Test method for {@link org.necros.dict.h4.CategoryManagerH4#countFiltered(java.lang.String)}.
	 */
	@Test @Transactional
	public void testCountFiltered() throws DictionaryException {
		int cnt = -1;

		generateCategories();
		cnt = categoryManager.countFiltered("cat");
		assertEquals(48, cnt);
		cnt = categoryManager.countFiltered("1");
		assertEquals(15, cnt);
	}

	/**
	 * Test method for {@link org.necros.dict.h4.CategoryManagerH4#pageFiltered(java.lang.String, org.necros.paging.Pager)}.
	 */
	@Test @Transactional
	public void testPageFiltered() throws DictionaryException {
		generateCategories();
		
		List<Category> result;
		Pager<Category> page;
		String filterText = "1";
		page = new Pager<Category>();
		page = categoryManager.pageFiltered(filterText, page);
		assertNotNull(page);
		result = page.getResult();
		assertNotNull(result);
		assertEquals(10, result.size());
		assertEquals(2, page.getPageCount());
		page.setPageNum(2);
		page = categoryManager.pageFiltered(filterText, page);
		assertNotNull(page);
		result = page.getResult();
		assertNotNull(result);
		assertEquals(5, result.size());
		page.setPageNum(3);
		page = categoryManager.pageFiltered(filterText, page);
		assertNotNull(page);
		result = page.getResult();
		assertNotNull(result);
		assertEquals(0, result.size());
		page.setPageNum(-1);
		page = categoryManager.pageFiltered(filterText, page);
		assertNotNull(page);
		result = page.getResult();
		assertNotNull(result);
		assertEquals(10, result.size());
		assertEquals(1, page.getPageNum());

		// Minimal page size is 2.
		page.setPageSize(1);
		page = categoryManager.pageFiltered(filterText, page);
		assertNotNull(page);
		assertEquals(8, page.getPageCount());
		result = page.getResult();
		assertNotNull(result);
		assertEquals(2, result.size());
		page.setPageNum(8);
		page = categoryManager.pageFiltered(filterText, page);
		assertNotNull(page);
		result = page.getResult();
		assertNotNull(result);
		assertEquals(1, result.size());
	}
}
