package org.necros.data.h4;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.necros.data.MapDAO;
import org.necros.util.AbstractServiceStore;
import org.necros.data.IdGenerator;

public class MapDAOStoreH4 extends AbstractServiceStore<MapDAO> {
	private static final Logger logger = LoggerFactory.getLogger(MapDAOStoreH4.class);

	private SessionFactory sessionFactory;
	private IdGenerator idGenerator;

	@Override
	protected MapDAO buildService(String key) {
		logger.debug("Building MapDAO for key [{}]...", key);
		MapDAOH4 dao = new MapDAOH4();
		dao.setSessionFactory(this.sessionFactory);
		dao.setIdGenerator(this.idGenerator);
		return dao;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}
}
