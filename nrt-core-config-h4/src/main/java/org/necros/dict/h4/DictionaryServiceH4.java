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
import org.necros.dict.DictionaryService;
import org.necros.data.h4.SessionFactoryHelper;

public class DictionaryServiceH4 implements DictionaryService {
	private SessionFactory sessionFactory;
	private SessionFactoryHelper helper;
	private String[] filterFields = {"value", "displayText", "description"};

	private Criteria createCriteria(String category) {
		return helper.getSession().createCriteria(Entry.class)
			.add(Restrictions.eq("categoryName", category));
	}

	public Entry getEntry(String category, String value) {
		return (Entry) createCriteria(category).add(Restrictions.eq("value", value))
			.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<Entry> allEntries(String category) {
		return helper.displayOrder(createCriteria(category))
			.list();
	}

	public int countAllEntries(String category) {
		return helper.count(createCriteria(category));
	}

	@SuppressWarnings("unchecked")
	public Pager<Entry> pageAllEntries(String category, Pager<Entry> page) {
		page.setRecordCount(countAllEntries(category));
		return helper.page(helper.displayOrder(createCriteria(category)), page);
	}

	@SuppressWarnings("unchecked")
	public List<Entry> filterEntries(String category, String filterText) {
		return helper.filterOnFieldArray(
				helper.displayOrder(createCriteria(category)),
				filterText,
				filterFields
			)
			.list();
	}

	public int countFilteredEntries(String category, String filterText) {
		return helper.count(helper.filterOnFieldArray(createCriteria(category), filterText, filterFields));
	}

	@SuppressWarnings("unchecked")
	public Pager<Entry> pageFilteredEntries(String category, String filterText, Pager<Entry> page) {
		page.setRecordCount(countFilteredEntries(category, filterText));
		return helper.page(
				helper.displayOrder(helper.filterOnFieldArray(createCriteria(category), filterText, filterFields)),
				page
			);
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.helper = SessionFactoryHelper.getInstance(sessionFactory);
	}
}
