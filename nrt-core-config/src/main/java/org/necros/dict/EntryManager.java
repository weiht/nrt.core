package org.necros.dict;

import java.util.List;

import org.necros.paging.Pager;

public interface EntryManager {
	public abstract Entry add(Entry entry);
	public abstract Entry update(Entry entry);
	public abstract Entry remove(String category, String value);
}
