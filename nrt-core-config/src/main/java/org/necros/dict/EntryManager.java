package org.necros.dict;

public interface EntryManager {
	public abstract Entry add(Entry entry) throws DictionaryException;
	public abstract Entry update(Entry entry) throws DictionaryException;
	public abstract Entry remove(String category, String value) throws DictionaryException;
	public abstract Entry disable(String category, String value) throws DictionaryException;
	public abstract Entry enable(String category, String value) throws DictionaryException;
}
