package org.necros.cache.directive;

import org.necros.cache.key.KeyGenerator;
import org.necros.cache.provider.Provider;

public interface CacheDirective {
	Provider getProvider();

	KeyGenerator getKeyGenerator();
}
