package org.necros.settings;

import org.necros.settings.PreferenceException;

public class CascadingPreferenceService extends AbstractPreferenceService {
	private CascadingService<AbstractPreferenceService> services;

	@Override
	protected Preference doGetPreference(String key) throws PreferenceException {
		for (AbstractPreferenceService svc: services.getServices()) {
			Preference p = svc.doGetPreference(key);
			if (p != null) return p;
		}
		return null;
	}

	public void setServices(CascadingService<AbstractPreferenceService> services) {
		this.services = services;
	}
}
