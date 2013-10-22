package org.necros.data.h4;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import org.necros.data.MetaPackage;
import org.necros.data.AbstractMetaPackageManager;

public class MetaPackageManagerH4 extends AbstractMetaPackageManager {
	private SessionFactoryHelper helper;

	protected MetaPackage doGet(String path) {
		return (MetaPackage)helper.createCriteria(MetaPackage.class)
			.add(Restrictions.eq("path", path))
			.uniqueResult();
	}

	protected MetaPackage doAdd(MetaPackage pkg) {
		helper.getSession().save(pkg);
		return pkg;
	}

	protected MetaPackage doUpdate(MetaPackage pkg) {
		helper.getSession().update(pkg);
		return pkg;
	}

	protected MetaPackage doRemove(MetaPackage pkg) {
		helper.getSession().delete(pkg);
		return pkg;
	}

	@SuppressWarnings("unchecked")
	public List<MetaPackage> root() {
		return helper.createCriteria(MetaPackage.class)
			.add(Restrictions.isNull("parentPath"))
			.list();
	}

	@SuppressWarnings("unchecked")
	public List<MetaPackage> children(String path) {
		return helper.createCriteria(MetaPackage.class)
			.add(Restrictions.eq("parentPath", path))
			.list();
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.helper = SessionFactoryHelper.getInstance(sessionFactory);
	}
}
