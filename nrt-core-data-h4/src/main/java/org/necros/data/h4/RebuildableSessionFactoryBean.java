package org.necros.data.h4;

import org.hibernate.SessionFactory;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

public class RebuildableSessionFactoryBean extends LocalSessionFactoryBean {
	private ServiceRegistry serviceRegistry;
	
	@Override
	public SessionFactory getObject() {
		if (serviceRegistry == null) {
			serviceRegistry = new ServiceRegistryBuilder().applySettings(getConfiguration().getProperties()).buildServiceRegistry();
		}
		MetaClassSessionFactory mcSessionFactory = new MetaClassSessionFactory();
		mcSessionFactory.setServiceRegistry(serviceRegistry);
		mcSessionFactory.setConfiguration(getConfiguration());
		return mcSessionFactory;
	}

	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}
}
