package org.necros.dict;

import java.util.List;

import org.necros.paging.Pager;

public interface CategoryManager {
	public abstract Category add(Category category) throws DictionaryException;
	public abstract Category update(Category category) throws DictionaryException;
	public abstract Category remove(String name) throws DictionaryException;
	public abstract Category get(String name);
	public abstract List<Category> all();
	public abstract int countAll();
	public abstract Pager<Category> pageAll(Pager<Category> page);
	public abstract List<Category> filtered(String filterText);
	public abstract int countFiltered(String filterText);
	public abstract Pager<Category> pageFiltered(String filterText, Pager<Category> page);
}
