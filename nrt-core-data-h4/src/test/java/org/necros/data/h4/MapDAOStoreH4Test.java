package org.necros.data.h4;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.necros.data.MapDAO;
import org.necros.util.ServiceStore;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:META-INF/*-beans.xml")
public class MapDAOStoreH4Test {
	private static final Logger logger = LoggerFactory.getLogger(MapDAOStoreH4Test.class);

	@Resource(name="mapDAOH4Store")
	private ServiceStore<MapDAO> store;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		logger.debug("MapDAO store: [{}]", store);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGet() {
		MapDAO dao;
		String key;
		key = "abc";
		logger.debug("Retrieving DAO for [{}]", key);
		dao = store.get(key);
		assertNotNull(dao);
	}

	@Test(expected=HibernateException.class)
	public void testGetNoEntity() {
		MapDAO dao;
		String key;
		key = "abc";
		logger.debug("Retrieving DAO for [{}]", key);
		dao = store.get(key);
		//No mapping, a HibernateException thrown.
		dao.get("something");
	}
}
