package org.necros.dict.h4;

import org.hibernate.SessionFactory;

import org.necros.dict.Category;
import org.necros.dict.Entry;
import org.necros.dict.EntryManager;
import org.necros.dict.CategoryManager;
import org.necros.dict.DictionaryService;
import org.necros.dict.DictionaryException;
import org.necros.data.h4.SessionFactoryHelper;

public class EntryManagerH4 implements EntryManager {
	private CategoryManager categoryManager;
	private DictionaryService dictionaryService;
	private SessionFactoryHelper helper;

	public Entry add(Entry entry) throws DictionaryException {
		Category cat = categoryManager.get(entry.getCategoryName());
		if (cat == null) throw new DictionaryException("No such category.");

		Entry origEntry = dictionaryService.getEntry(cat.getName(), entry.getValue());
		if (origEntry != null) throw new DictionaryException("Entry already exists.");

		helper.getSession().save(entry);
		return entry;
	}

	public Entry update(Entry entry) throws DictionaryException {
		Entry origEntry = dictionaryService.getEntry(entry.getCategoryName(), entry.getValue());
		if (origEntry == null) throw new DictionaryException("No such entry.");

		origEntry.setDisplayText(entry.getDisplayText());
		origEntry.setDisplayOrder(entry.getDisplayOrder());
		origEntry.setValue(entry.getValue());

		helper.getSession().update(origEntry);
		return origEntry;
	}

	public Entry remove(String category, String value) throws DictionaryException {
		Entry origEntry = dictionaryService.getEntry(category, value);
		if (origEntry == null) return null;

		helper.getSession().delete(origEntry);
		return origEntry;
	}

	public Entry disable(String category, String value) throws DictionaryException {
		Entry origEntry = dictionaryService.getEntry(category, value);
		if (origEntry == null) return null;

		origEntry.setStatus(Entry.STATUS_DISABLED);
		helper.getSession().update(origEntry);
		return origEntry;
	}

	public Entry enable(String category, String value) throws DictionaryException {
		Entry origEntry = dictionaryService.getEntry(category, value);
		if (origEntry == null) return null;

		origEntry.setStatus(null);
		helper.getSession().update(origEntry);
		return origEntry;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.helper = SessionFactoryHelper.getInstance(sessionFactory);
	}

	public void setCategoryManager(CategoryManager categoryManager) {
		this.categoryManager = categoryManager;
	}

	public void setDictionaryService(DictionaryService dictionaryService) {
		this.dictionaryService = dictionaryService;
	}
}
