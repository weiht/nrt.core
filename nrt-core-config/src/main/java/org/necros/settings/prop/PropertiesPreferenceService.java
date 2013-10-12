/**
 * 
 */
package org.necros.settings.prop;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import org.necros.settings.PreferenceException;
import org.necros.settings.AbstractPreferenceService;
import org.necros.settings.Preference;

/**
 * @author weiht
 *
 */
public class PropertiesPreferenceService extends AbstractPreferenceService {
	private static final Logger logger = LoggerFactory.getLogger(PropertiesPreferenceService.class);
	
	private Properties propsDb;
	private String propertiesLocation;
	private PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
	
	public void init() {
		Resource[] resources;
		try {
			resources = resolver.getResources(propertiesLocation);
		} catch (IOException e) {
			logger.error("Error initializing properties as system preferences.", e);
			return;
		}
		propsDb = new Properties();
		for (Resource res: resources) {
			logger.trace("Loading resource for system preferences: {}", res);
			try {
				propsDb = extractProperties(propsDb, res);
			} catch (IOException e) {
				logger.warn("Error retrieving resources for system preferences.", e);
			}
		}
		logger.trace("{}", propsDb);
	}

	private Properties extractProperties(Properties defProps, Resource res)
			throws IOException {
		Properties props = new Properties(defProps);
		InputStream ins = res.getInputStream();
		try {
			props.load(ins);
		} finally {
			ins.close();
		}
		return props;
	}

	private String mutateKey(String key) {
		if (key == null) return key;
		String k = key;
		k = k.replaceAll(Preference.splitter, ".");
		return k.charAt(0) == '.' ? k.substring(1) : k;
	}

	@Override
	protected Preference doGetPreference(String key) throws PreferenceException {
		logger.trace(key);
		if (propsDb == null) return null;
		String k = mutateKey(key);
		String value = propsDb.getProperty(k);
		if (logger.isTraceEnabled()) {
			logger.trace("Key: {}", k);
			logger.trace("value: {}", value);
		}
		if (value == null) return null;
		Preference p = new Preference();
		p.setKey(key);
		p.setValue(value);
		p.setItemType(Preference.ITEM_TYPE_SETTING);
		return p;
	}

	public void setPropertiesLocation(String propertiesLocation) {
		this.propertiesLocation = propertiesLocation;
	}
}
