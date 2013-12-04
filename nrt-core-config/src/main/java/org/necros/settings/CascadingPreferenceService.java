package org.necros.settings;

import org.necros.settings.PreferenceException;

public class CascadingPreferenceService extends AbstractPreferenceService {
	private CascadingService<PreferenceService> services;

	@Override
	public Preference getRawPreference(String key) throws PreferenceException {
		for (PreferenceService svc: services.getServices()) {
			Preference p = svc.getRawPreference(key);
			if (p != null) return p;
		}
		return null;
	}

	public void setServices(CascadingService<PreferenceService> services) {
		this.services = services;
	}
}
