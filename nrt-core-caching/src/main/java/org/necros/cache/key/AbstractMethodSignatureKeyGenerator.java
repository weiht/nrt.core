package org.necros.cache.key;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractMethodSignatureKeyGenerator implements
		MethodSignatureKeyGenerator {
	private static final Logger logger = LoggerFactory.getLogger(AbstractMethodSignatureKeyGenerator.class);
	
	private MethodSignatureKeyValueStrategy keyValueStrategy;

	public Key generateKey(final Object target, final String targetMethod,
			final Object[] arguments) {
		final String keyValue = keyValueStrategy.generateKeyValue(target,
				targetMethod, arguments);
		Key k = createKey(keyValue);
		if (logger.isDebugEnabled()) {
			logger.debug("Key generated: " + k);
		}
		return k;
	}

	protected abstract Key createKey(String keyValue);

	public void setKeyValueStrategy(
			final MethodSignatureKeyValueStrategy keyValueStrategy) {
		this.keyValueStrategy = keyValueStrategy;
	}
}
