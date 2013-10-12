package org.necros.settings;

import java.util.List;

public interface CascadingService<T> {
	public abstract void injectImplementer(Integer zIndex, T impl);
	public abstract List<T> getServices();
}
