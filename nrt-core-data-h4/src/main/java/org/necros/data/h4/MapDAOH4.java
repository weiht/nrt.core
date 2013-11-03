package org.necros.data.h4;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.StringUtils;

import org.necros.data.MapDAO;
import org.necros.data.IdGenerator;
import org.necros.data.MapKeys;
import org.necros.data.UsableStatuses;
import org.necros.data.GeneralDataAccessException;
import org.necros.paging.Pager;

public class MapDAOH4 implements MapDAO {
	private String entityName;
	private SessionFactoryHelper helper;
	private IdGenerator idGenerator;

	private Criteria createCriteria() {
		return helper.getSession().createCriteria(entityName)
			.add(Restrictions.ne(MapKeys.KEY_STATUS, UsableStatuses.DELETED));
	}

	public Map<String, Object> get(String id) {
		return (Map<String, Object>) createCriteria()
			.add(Restrictions.eq(MapKeys.KEY_ID, id))
			.uniqueResult();
	}

	public Map<String, Object> add(Map<String, Object> data)
	throws GeneralDataAccessException {
		data.put(MapKeys.KEY_ID, idGenerator.generate());
		data.put(MapKeys.KEY_ENTITY, entityName);
		helper.getSession().save(entityName, data);
		return data;
	}

	private void modifyProperties(Map<String, Object> toObj, Map<String, Object> fromObj,
			String[] updatableFieilds) {
		for (String f: updatableFieilds) {
			toObj.put(f, fromObj.get(f));
		}
	}

	public Map<String, Object> update(Map<String, Object> data, String[] updatableFieilds)
	throws GeneralDataAccessException {
		String id = (String)data.get(MapKeys.KEY_ID);
		Map<String, Object> orig = get(id);
		if (orig == null) throw new GeneralDataAccessException("No entity with id [" + id + "] found.");
		modifyProperties(orig, data, updatableFieilds);
		helper.getSession().update(entityName, orig);
		return orig;
	}

	public Map<String, Object> remove(String id, boolean permanent)
	throws GeneralDataAccessException {
		Map<String, Object> orig = get(id);
		if (orig == null) return null;
		if (permanent) {
			helper.getSession().delete(entityName, orig);
		} else {
			orig.put(MapKeys.KEY_STATUS, UsableStatuses.DELETED);
			helper.getSession().update(entityName, orig);
		}
		return orig;
	}

	public List<Map<String, Object>> all() {
		return createCriteria()
			.list();
	}

	public int countAll() {
		return helper.count(createCriteria());
	}

	public Pager<Map<String, Object>> pageAll(Pager<Map<String, Object>> page) {
		page.setRecordCount(countAll());
		return helper.page(createCriteria(), page);
	}

	
	public List<Map<String, Object>> filtered(String filterText, String[] filterFields) {
		return helper.filterOnFieldArray(
				createCriteria(),
				filterText,
				filterFields
			)
			.list();
	}

	public int countFiltered(String filterText, String[] filterFields) {
		return helper.count(helper.filterOnFieldArray(
				createCriteria(),
				filterText,
				filterFields
			));
	}

	public Pager<Map<String, Object>> pageFiltered(String filterText,
			String[] filterFields, Pager<Map<String, Object>> page) {
		page.setRecordCount(countFiltered(filterText, filterFields));
		return helper.page(
				helper.filterOnFieldArray(
					createCriteria(),
					filterText,
					filterFields
				), page);
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.helper = SessionFactoryHelper.getInstance(sessionFactory);
	}

	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}
}
