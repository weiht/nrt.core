package org.necros.data.h4;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.StringUtils;

public class SessionFactoryHelper {
	private SessionFactory factory;

	private SessionFactoryHelper() {}

	public SessionFactoryHelper getInstance(SessionFactory factory) {
		SessionFactoryHelper instance = new SessionFactoryHelper();
		instance.factory = factory;
		return instance;
	}

	public Session getSession() {
		return factory.getCurrentSession();
	}

	public Criteria createCriteria(Class<?> clazz) {
		return getSession().createCriteria(clazz);
	}

	public Criteria createCriteriaForEntity(String entityName) {
		return getSession().createCriteria(entityName);
	}

	public int count(Criteria c) {
		return ((Long)c.setProjection(Projections.rowCount())
			.uniqueResult()).intValue();
	}

	public Criteria filterOnFieldArray(Criteria c, String filterText, String[] fields) {
		if (!StringUtils.hasText(filterText)) return c;
		if (fields == null || fields.length == 0) return c;

		Criterion[] orClause = new Criterion[fields.length];
		for (int i = 0; i < fields.length; i ++) {
			orClause[i] = Restrictions.like(fields[i], filterText, MatchMode.ANYWHERE);
		}
		c.add(Restrictions.or(orClause));
		return c;
	}

	public Criteria filterOnFieldList(Criteria c, String filterText, List<String> fields) {
		if (!StringUtils.hasText(filterText)) return c;
		if (fields == null || fields.isEmpty()) return c;

		Criterion[] orClause = new Criterion[fields.size()];
		for (int i = 0; i < fields.size(); i ++) {
			orClause[i] = Restrictions.like(fields.get(i), filterText, MatchMode.ANYWHERE);
		}
		c.add(Restrictions.or(orClause));
		return c;
	}
}
