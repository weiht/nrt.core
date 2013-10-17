package org.necros.dict;

import java.util.List;

import org.necros.paging.Pager;

public interface DictionaryService {
	public abstract List<Entry> allEntries(String category);
	public abstract int countAllEntries(String category);
	public abstract Pager<Entry> pageAllEntries(String category, Pager<Entry> page);
	public abstract List<Entry> filterEntries(String category, String filterText);
	public abstract int countFilteredEntries(String category, String filterText);
	public abstract Pager<Entry> pageFilteredEntries(String category, String filterText, Pager<Entry> page);
}
