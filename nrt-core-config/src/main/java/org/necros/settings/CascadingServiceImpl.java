/**
 * 
 */
package org.necros.settings;

import java.util.ArrayList;
import java.util.List;

/**
 * @author weiht
 *
 */
public class CascadingServiceImpl<T> implements CascadingService<T> {
	private List<T> services;
	private List<Integer> zIndices;
	private Integer maxZIndex = 0;
	private List<T> serviceList;

	@Override
	public void injectImplementer(Integer zIndex, T impl) {
		if (services == null) {
			services = new ArrayList<T>();
			zIndices = new ArrayList<Integer>();
		}
		Integer z = zIndex == null ? 0 : zIndex;
		if (z < 1) {
			services.add(impl);
			zIndices.add(z);
		} else if (z >= maxZIndex) {
			services.add(0, impl);
			zIndices.add(0, z);
			maxZIndex = z;
		} else {
			for (int i = 0; i < zIndices.size(); i ++) {
				Integer sz = zIndices.get(i);
				if (sz < z) {
					zIndices.add(i, z);
					services.add(i, impl);
					break;
				}
			}
		}
		serviceList = null;
	}

	public List<T> getServices() {
		synchronized (this) {
			if (serviceList == null) {
				if (services == null) return null;
				serviceList = new ArrayList<T>(services);
			}
		}
		return serviceList;
	}
}
