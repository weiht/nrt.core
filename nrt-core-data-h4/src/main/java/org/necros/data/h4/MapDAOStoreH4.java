package org.necros.data.h4;

import org.hibernate.SessionFactory;

import org.necros.data.MapDAO;
import org.necros.util.AbstractServiceStore;
import org.necros.data.IdGenerator;

public class MapDAOStoreH4<MapDAO> extends AbstractServiceStore {
	private SessionFactory sessionFactory;
	private IdGenerator idGenerator;

	@Override
	protected MapDAO buildService(String key) {
		MapDAOH4 dao = new MapDAOH4();
		dao.setSessionFactory(this.sessionFactory);
		dao.setIdGenerator(this.idGenerator);
		return (MapDAO)dao;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}
}
