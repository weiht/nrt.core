package org.necros.util;

public interface ServiceStore<T> {
	public abstract T get(String key);
}
