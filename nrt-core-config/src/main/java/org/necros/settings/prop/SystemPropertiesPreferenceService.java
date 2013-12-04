package org.necros.settings.prop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.necros.settings.PreferenceException;
import org.necros.settings.AbstractPreferenceService;
import org.necros.settings.Preference;

public class SystemPropertiesPreferenceService extends AbstractPreferenceService {
	private static final Logger logger = LoggerFactory.getLogger(SystemPropertiesPreferenceService.class);
	
	private String mutateKey(String key) {
		if (key == null) return key;
		String k = key;
		k = k.replaceAll(Preference.splitter, ".");
		return k.charAt(0) == '.' ? k.substring(1) : k;
	}

	@Override
	public Preference getRawPreference(String key) throws PreferenceException {
		logger.trace(key);
		String k = mutateKey(key);
		String value = System.getProperty(k);
		if (value == null) return null;
		Preference p = new Preference();
		p.setKey(key);
		p.setValue(value);
		p.setItemType(Preference.ITEM_TYPE_SETTING);
		return p;
	}
}
