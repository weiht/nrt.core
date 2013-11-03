package org.necros.util;

import java.util.Map;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractServiceStore<T> implements ServiceStore {
	private static final Logger logger = LoggerFactory.getLogger(AbstractServiceStore.class);

	private Map<String, T> store = new HashMap<String, T>();

	public synchronized T get(String key) {
		logger.debug("Checking whether there is a key [{}] in store...", key);
		if (store.containsKey(key)) {
			logger.debug("There is a key [{}] in store.", key);
			return store.get(key);
		} else {
			logger.debug("No key [{}] in store. Building service...", key);
			T svc = buildService(key);
			store.put(key, svc);
			return svc;
		}
	}

	protected abstract T buildService(String key);
}
