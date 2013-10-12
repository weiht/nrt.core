package org.necros.cache.directive;

import org.necros.cache.key.KeyGenerator;
import org.necros.cache.provider.Provider;
 
public class CacheRemovalDirectiveImpl implements CacheRemovalDirective {
 
  private Provider provider;
 
  private KeyGenerator keyGenerator;
 
  public Provider getProvider() {
    return provider;
  }
 
  public void setProvider(final Provider provider) {
    this.provider = provider;
  }
 
  public KeyGenerator getKeyGenerator() {
    return keyGenerator;
  }
 
  public void setKeyGenerator(final KeyGenerator keyGenerator) {
    this.keyGenerator = keyGenerator;
  }
}
