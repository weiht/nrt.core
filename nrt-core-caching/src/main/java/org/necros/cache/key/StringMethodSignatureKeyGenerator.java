package org.necros.cache.key;

public class StringMethodSignatureKeyGenerator extends
		AbstractMethodSignatureKeyGenerator {
	@Override
	protected Key createKey(final String keyValue) {
		return new StringKey(keyValue);
	}
}
