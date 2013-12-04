/**
 * 
 */
package org.necros.settings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author weiht
 *
 */
public abstract class AbstractPreferenceService implements PreferenceService {
	private static final Logger logger = LoggerFactory.getLogger(AbstractPreferenceService.class);
	private Pattern placeholderPattern = Pattern.compile("(\\$\\{[\\w,/]+})");
	
	@Override
	public Preference getPreference(String key) throws PreferenceException {
		Preference itm = getRawPreference(key);
		if (itm != null && !Preference.ITEM_TYPE_FOLDER.equals(itm.getItemType())) {
			itm.setValue(formatValue(itm.getValue(), key));
			return itm;
		}
		//目录不作为参数返回。
		return null;
	}
	
	@Override
	public String stringValue(String key, String defaultValue) {
		Preference p;
		try {
			p = getPreference(key);
		} catch (PreferenceException e) {
			return defaultValue;
		}
		if (p != null) {
			String v = p.getValue();
			if (v != null) {
				return v;
			}
		}
		return defaultValue;
	}
	
	@Override
	public Integer intValue(String key, Integer defaultValue) {
		Preference p;
		try {
			p = getPreference(key);
		} catch (PreferenceException e) {
			logger.warn("Error retrieving preference with key: {}", key);
			logger.warn("", e);
			logger.warn("Using the default value: {}", defaultValue);
			return defaultValue;
		}
		if (p != null) {
			String v = p.getValue();
			if (v != null) {
				try {
					return Integer.valueOf(v);
				} catch (NumberFormatException e) {
					logger.warn("Error retrieving preference with key: {}", key);
					logger.warn("", e);
					logger.warn("Using the default value: {}", defaultValue);
				}
			}
		}
		return defaultValue;
	}
	
	@Override
	public Double floatValue(String key, Double defaultValue) {
		Preference p;
		try {
			p = getPreference(key);
		} catch (PreferenceException e) {
			logger.warn("Error retrieving preference with key: {}", key);
			logger.warn("", e);
			logger.warn("Using the default value: {}", defaultValue);
			return defaultValue;
		}
		if (p != null) {
			String v = p.getValue();
			if (v != null) {
				try {
					return Double.valueOf(v);
				} catch (NumberFormatException e) {
					logger.warn("Error retrieving preference with key: {}", key);
					logger.warn("", e);
					logger.warn("Using the default value: {}", defaultValue);
				}
			}
		}
		return defaultValue;
	}

	private String formatValue(String sentence, String baseKey) throws PreferenceException {
		logger.debug("Formatting '{}', with value [{}]...", baseKey, sentence);
		String value = sentence;
		Matcher m = null;;
		while((m = placeholderPattern.matcher(value)).find()) {
			logger.debug("Matches: {}", m.groupCount());
			String g = m.group(1);
			String k = g.substring(2, g.length() - 1);
			String fk = k.startsWith(Preference.splitter) ? k : baseKey.replaceAll("/([\\w])*$", "/" + k);
			Preference p = getPreference(fk);
			String v = null;
			if (p == null) {
				throw new PreferenceException("找不到名为“" + fk + "”的参数");
			} else {
				v = p.getValue();
			}
			logger.debug("Preference '{}' is [{}]", g, v);
			String replace = "(\\$\\{" + k + "})";
			logger.debug("Replacing [{}] with [{}]...", replace, v);
			value = value.replaceAll(replace, v);
			logger.debug("Preference formatted to: [{}].", value);
		}
		return value;
	}
}
