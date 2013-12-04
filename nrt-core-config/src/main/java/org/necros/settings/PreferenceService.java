package org.necros.settings;

import org.necros.settings.PreferenceException;

public interface PreferenceService {
	/**
	 * 根据参数key获取参数值
	 * @param key
	 * @return 找到的参数；如果未找到，返回null
	 */
	public abstract Preference getPreference(String key) throws PreferenceException;
	/**
	 * 根据参数key获取参数值
	 * @param key
	 * @return 找到的参数；如果未找到，返回null
	 */
	public abstract Preference getRawPreference(String key) throws PreferenceException;
	/**
	 * 根据参数key获取参数值
	 * @param key
	 * @param defaultValue
	 * @return 找到的参数；如果未找到，返回null
	 */
	public abstract Integer intValue(String key, Integer defaultValue);
	/**
	 * 根据参数key获取参数值
	 * @param key
	 * @param defaultValue
	 * @return 找到的参数；如果未找到，返回null
	 */
	public abstract String stringValue(String key, String defaultValue);
	/**
	 * 根据参数key获取参数值
	 * @param key
	 * @param defaultValue
	 * @return 找到的参数；如果未找到，返回null
	 */
	public abstract Double floatValue(String key, Double defaultValue);
}
