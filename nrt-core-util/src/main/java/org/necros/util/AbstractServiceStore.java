package org.necros.util;

import java.util.Map;
import java.util.HashMap;

public abstract class AbstractServiceStore<T> {
	private Map<String, T> store = new HashMap<String, T>();

	public synchronized T get(String key) {
		if (store.containsKey(key)) {
			return store.get(key);
		} else {
			T svc = buildService(key);
			store.put(key, svc);
			return svc;
		}
	}

	protected abstract T buildService(String key);
}
