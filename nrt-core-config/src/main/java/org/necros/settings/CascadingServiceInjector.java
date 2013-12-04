/**
 * 
 */
package org.necros.settings;

/**
 * @author weiht
 *
 */
public class CascadingServiceInjector<T> {
	private Integer zIndex = 0;
	private T service;
	private CascadingService<T> cascadingService;
	
	public void doInject() {
		if (cascadingService != null && service != null) {
			cascadingService.injectImplementer(zIndex, service);
		}
	}

	public void setzIndex(Integer zIndex) {
		this.zIndex = zIndex;
	}

	public void setService(T service) {
		this.service = service;
	}

	public void setCascadingService(CascadingService<T> cascadingService) {
		this.cascadingService = cascadingService;
	}
}
