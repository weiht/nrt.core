package org.necros.settings;

import java.util.List;

import org.necros.settings.PreferenceException;

public interface PreferenceManager extends PreferenceService {
	public abstract Preference get(String key) throws PreferenceException;
	public abstract List<Preference> root() throws PreferenceException;
	public abstract List<Preference> childPaths(String parentKey) throws PreferenceException;
	public abstract List<Preference> Preferences(String parentKey) throws PreferenceException;
	public abstract List<Preference> children(String parentKey) throws PreferenceException;
	public abstract List<Preference> allPlain() throws PreferenceException;
	
	public abstract Preference create(Preference itm) throws PreferenceException;
	public abstract Preference update(Preference itm) throws PreferenceException;
	public abstract Preference remove(Preference itm) throws PreferenceException;
}
