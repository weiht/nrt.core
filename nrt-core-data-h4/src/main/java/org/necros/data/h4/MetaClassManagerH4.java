package org.necros.data.h4;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.StringUtils;

import org.necros.data.MetaClass;
import org.necros.data.MetaProperty;
import org.necros.data.AbstractMetaClassManager;

public class MetaClassManagerH4 extends AbstractMetaClassManager {
	private static Class<?> CLS_CLASS = MetaClass.class;
	private static Class<?> CLS_PROOPERTY = MetaProperty.class;
	private static String[] filterFields = {"name", "displayName", "metaPackage", "tableName", "description"};

	private SessionFactoryHelper helper;

	private Criteria classCriteria() {
		return helper.getSession().createCriteria(CLS_CLASS);
	}

	private Criteria globalClassCriteria() {
		return classCriteria().add(Restrictions.isNull("metaPackage"));
	}

	private Criteria packageClassCriteria(String pkg) {
		return classCriteria().add(Restrictions.eq("metaPackage", pkg));
	}

	private Criteria propertyCriteria() {
		return helper.getSession().createCriteria(CLS_PROOPERTY);
	}

	protected MetaClass doGet(String id) {
		return (MetaClass) helper.getSession().get(CLS_CLASS, id);
	}

	protected MetaClass doGetWithName(String pkg, String name) {
		if (!StringUtils.hasText(pkg)) {
			return (MetaClass) globalClassCriteria()
				.add(Restrictions.eq("name", name))
				.uniqueResult();
		} else {
			return (MetaClass) packageClassCriteria(pkg)
				.add(Restrictions.eq("name", name))
				.uniqueResult();
		}
	}

	protected boolean clazzExists(String pkg, String name) {
		return doGetWithName(pkg, name) != null;
	}

	protected MetaClass doAdd(MetaClass clazz) {
		helper.getSession().save(clazz);
		return clazz;
	}

	protected MetaClass doUpdate(MetaClass clazz) {
		helper.getSession().update(clazz);
		return clazz;
	}

	protected MetaClass doRemove(MetaClass clazz) {
		helper.getSession().delete(clazz);
		return clazz;
	}

	protected MetaProperty doGetProperty(String propId) {
		return (MetaProperty) helper.getSession().get(CLS_PROOPERTY, propId);
	}

	protected MetaProperty doAddProperty(MetaClass clazz, MetaProperty prop) {
		for (MetaProperty p: clazz.getProperties()) {
			if (p.getName().equals(prop.getName()))
				return p;
		}
		clazz.getProperties().add(prop);
		prop.setMetaClass(clazz);
		helper.getSession().save(prop);
		helper.getSession().update(clazz);
		return prop;
	}

	protected MetaProperty doRemoveProperty(MetaProperty prop) {
		MetaClass clazz = prop.getMetaClass();
		if (clazz != null) {
			for (MetaProperty p: clazz.getProperties()) {
				if (p.getName().equals(prop.getName())) {
					clazz.getProperties().remove(p);
					helper.getSession().delete(p);
					break;
				}
			}
		}
		helper.getSession().delete(prop);
		return prop;
	}

	protected MetaProperty doUpdateProperty(MetaProperty prop) {
		helper.getSession().update(prop);
		return prop;
	}

	@SuppressWarnings("unchecked")
	protected List<MetaClass> doFindAll(String pkg) {
		if (StringUtils.hasText(pkg)) {
			return packageClassCriteria(pkg)
				.list();
		} else {
			return globalClassCriteria()
				.list();
		}
	}

	@SuppressWarnings("unchecked")
	public List<MetaClass> search(String filterText) {
		return helper.filterOnFieldArray(classCriteria(), filterText, filterFields)
			.list();
	}

	@SuppressWarnings("unchecked")
	protected List<MetaClass> doSearchInPackage(String pkg, String filterText) {
		Criteria c = StringUtils.hasText(pkg) ? packageClassCriteria(pkg) : globalClassCriteria();
		return helper.filterOnFieldArray(c, filterText, filterFields)
			.list();
	}


	public void setSessionFactory(SessionFactory sessionFactory) {
		this.helper = SessionFactoryHelper.getInstance(sessionFactory);
	}
}
