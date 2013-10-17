package org.necros.dict.h4;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.StringUtils;

import org.necros.paging.Pager;
import org.necros.dict.Category;
import org.necros.dict.ValueType;
import org.necros.dict.Entry;
import org.necros.dict.CategoryManager;
import org.necros.dict.EntryManager;
import org.necros.dict.DictionaryException;
import org.necros.dict.DictionaryService;
import org.necros.data.h4.SessionFactoryHelper;

public class CategoryManagerH4 implements CategoryManager {
	private SessionFactory sessionFactory;
	private SessionFactoryHelper helper;
	private EntryManager entryManager;
	private DictionaryService dictionaryService;
	private String[] filterFields = {"name", "description"};

	public Category add(Category category) throws DictionaryException {
		Category orig = get(category.getName());
		if (orig != null) throw new DictionaryException("Category already exists.");

		helper.getSession().save(category);
		return category;
	}

	public Category update(Category category) throws DictionaryException {
		Category orig = get(category.getName());
		if (orig == null) throw new DictionaryException("No such category.");

		orig.setDescription(category.getDescription());

		helper.getSession().update(orig);
		return orig;
	}

	public Category remove(String name) throws DictionaryException {
		Category orig = get(name);
		if (orig == null) return null;

		for (Entry e: dictionaryService.allEntries(name)) {
			entryManager.remove(name, e.getValue());
		}
		helper.getSession().delete(orig);
		return orig;
	}

	public Category get(String name) {
		return (Category) helper.getSession().get(Category.class, name);
	}

	@SuppressWarnings("unchecked")
	public List<Category> all() {
		return helper.createCriteria(Category.class)
			.list();
	}

	public int countAll() {
		return helper.count(helper.createCriteria(Category.class));
	}

	@SuppressWarnings("unchecked")
	public Pager<Category> pageAll(Pager<Category> page) {
		page.setRecordCount(countAll());
		return helper.page(helper.createCriteria(Category.class), page);
	}

	@SuppressWarnings("unchecked")
	public List<Category> filtered(String filterText) {
		return helper.filterOnFieldArray(
				helper.createCriteria(Category.class),
				filterText,
				filterFields
			)
			.list();
	}

	public int countFiltered(String filterText) {
		return helper.count(helper.filterOnFieldArray(
				helper.createCriteria(Category.class),
				filterText,
				filterFields
			));
	}

	@SuppressWarnings("unchecked")
	public Pager<Category> pageFiltered(String filterText, Pager<Category> page) {
		page.setRecordCount(countFiltered(filterText));
		return helper.page(
				helper.filterOnFieldArray(
					helper.createCriteria(Category.class),
					filterText,
					filterFields
				), page);
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.helper = SessionFactoryHelper.getInstance(sessionFactory);
	}

	public void setEntryManager(EntryManager entryManager) {
		this.entryManager = entryManager;
	}

	public void setDictionaryService(DictionaryService dictionaryService) {
		this.dictionaryService = dictionaryService;
	}
}
