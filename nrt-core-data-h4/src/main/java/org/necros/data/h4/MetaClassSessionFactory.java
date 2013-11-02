/**
 * 
 */
package org.necros.data.h4;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;
import javax.naming.Reference;

import org.hibernate.Cache;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionBuilder;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.StatelessSessionBuilder;
import org.hibernate.TypeHelper;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.FilterDefinition;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.stat.Statistics;

/**
 * @author weiht
 *
 */
@SuppressWarnings("serial")
public class MetaClassSessionFactory implements RebuildableSessionFactory {
	private SessionFactory sessionFactory;
	private Configuration configuration;
	private ServiceRegistry serviceRegistry;

	private void buildSessionFactory() {
		this.sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	}

	public SessionFactoryOptions getSessionFactoryOptions() {
		return sessionFactory.getSessionFactoryOptions();
	}

	public SessionBuilder withOptions() {
		return sessionFactory.withOptions();
	}

	public Session openSession() throws HibernateException {
		return sessionFactory.openSession();
	}

	public Session getCurrentSession() throws HibernateException {
		return sessionFactory.getCurrentSession();
	}

	public StatelessSessionBuilder withStatelessOptions() {
		return sessionFactory.withStatelessOptions();
	}

	public StatelessSession openStatelessSession() {
		return sessionFactory.openStatelessSession();
	}

	public StatelessSession openStatelessSession(Connection connection) {
		return sessionFactory.openStatelessSession(connection);
	}

	@SuppressWarnings("rawtypes")
	public ClassMetadata getClassMetadata(Class entityClass) {
		return sessionFactory.getClassMetadata(entityClass);
	}

	public ClassMetadata getClassMetadata(String entityName) {
		return sessionFactory.getClassMetadata(entityName);
	}

	public CollectionMetadata getCollectionMetadata(String roleName) {
		return sessionFactory.getCollectionMetadata(roleName);
	}

	public Map<String, ClassMetadata> getAllClassMetadata() {
		return sessionFactory.getAllClassMetadata();
	}

	@SuppressWarnings("rawtypes")
	public Map getAllCollectionMetadata() {
		return sessionFactory.getAllCollectionMetadata();
	}

	public Statistics getStatistics() {
		return sessionFactory.getStatistics();
	}

	public void close() throws HibernateException {
		sessionFactory.close();
	}

	public boolean isClosed() {
		return sessionFactory.isClosed();
	}

	public Cache getCache() {
		return sessionFactory.getCache();
	}

	@SuppressWarnings("rawtypes")
	@Deprecated
	public void evict(Class persistentClass) throws HibernateException {
		sessionFactory.evict(persistentClass);
	}

	@SuppressWarnings("rawtypes")
	@Deprecated
	public void evict(Class persistentClass, Serializable id)
			throws HibernateException {
		sessionFactory.evict(persistentClass, id);
	}

	@Deprecated
	public void evictEntity(String entityName) throws HibernateException {
		sessionFactory.evictEntity(entityName);
	}

	@Deprecated
	public void evictEntity(String entityName, Serializable id)
			throws HibernateException {
		sessionFactory.evictEntity(entityName, id);
	}

	@Deprecated
	public void evictCollection(String roleName) throws HibernateException {
		sessionFactory.evictCollection(roleName);
	}

	@Deprecated
	public void evictCollection(String roleName, Serializable id)
			throws HibernateException {
		sessionFactory.evictCollection(roleName, id);
	}

	@Deprecated
	public void evictQueries(String cacheRegion) throws HibernateException {
		sessionFactory.evictQueries(cacheRegion);
	}

	@Deprecated
	public void evictQueries() throws HibernateException {
		sessionFactory.evictQueries();
	}

	@SuppressWarnings("rawtypes")
	@Deprecated
	public Set getDefinedFilterNames() {
		return sessionFactory.getDefinedFilterNames();
	}

	public FilterDefinition getFilterDefinition(String filterName)
			throws HibernateException {
		return sessionFactory.getFilterDefinition(filterName);
	}

	public boolean containsFetchProfileDefinition(String name) {
		return sessionFactory.containsFetchProfileDefinition(name);
	}

	public Reference getReference() throws NamingException {
		return sessionFactory.getReference();
	}

	public TypeHelper getTypeHelper() {
		return sessionFactory.getTypeHelper();
	}

	@Override
	public void rebuild() {
		buildSessionFactory();
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
		buildSessionFactory();
	}

	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}
}
