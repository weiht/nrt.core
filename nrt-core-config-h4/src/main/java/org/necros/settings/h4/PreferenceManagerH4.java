package org.necros.settings.h4;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import org.necros.settings.PreferenceException;
import org.necros.settings.AbstractPreferenceService;
import org.necros.settings.Preference;
import org.necros.settings.PreferenceManager;

@Transactional
public class PreferenceManagerH4 extends AbstractPreferenceService implements PreferenceManager {
	private static final Class<?> clazz = Preference.class;
	
	private static final String HQL_REMOVE_SUBTREE = "delete Preference i where i.key like ?";
	
	private SessionFactory sessionFactory;

	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	private Criteria createCriteria() {
		return getSession().createCriteria(clazz)
				.addOrder(Order.asc("itemType"))
				.addOrder(Order.asc("key"));
	}
	
	private Criteria addParent(Criteria c, String parentPath) {
		return c.add(Restrictions.eq("parentPath", parentPath));
	}
	
	private Criteria addItemType(Criteria c, Integer itype) {
		return c.add(Restrictions.eq("itemType", itype));
	}

	@Override
	public Preference get(String key) throws PreferenceException {
		return getRawPreference(key);
	}
	
	@Override
	public Preference getRawPreference(String key) throws PreferenceException {
		return (Preference) getSession().get(clazz, key);
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Preference> rootPaths() {
		return addItemType(createCriteria().add(Restrictions.isNull("parentPath")),
					Preference.ITEM_TYPE_FOLDER)
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Preference> rootPreferences() {
		return addItemType(createCriteria().add(Restrictions.isNull("parentPath")),
					Preference.ITEM_TYPE_SETTING)
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Preference> childPaths(String parentKey) {
		return  addItemType(addParent(createCriteria(), parentKey), Preference.ITEM_TYPE_FOLDER)
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Preference> childPreferences(String parentKey) {
		return  addItemType(addParent(createCriteria(), parentKey), Preference.ITEM_TYPE_SETTING)
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Preference> children(String parentKey) {
		return addParent(createCriteria(), parentKey)
				.list();
	}

	@Override
	public Preference create(Preference itm) {
		if (itm.getItemType() == null) {
			if (itm.getValue() == null) {
				itm.setItemType(Preference.ITEM_TYPE_FOLDER);
			} else {
				itm.setItemType(Preference.ITEM_TYPE_SETTING);
			}
		}
		String k = itm.getKey();
		if (k.indexOf(Preference.splitter) != 0) {
			k = Preference.splitter + k;
			itm.setKey(k);
		}
		int ix = k.lastIndexOf(Preference.splitter);
		if (ix == 0) {
			itm.setParentPath(null);
		} else {
			itm.setParentPath(k.substring(0, ix));
		}
		getSession().save(itm);
		return itm;
	}

	@Override
	public Preference update(Preference itm) throws PreferenceException {
		Preference orig;
		try {
			orig = get(itm.getKey());
		} catch (PreferenceException e) {
			throw new PreferenceException(e);
		}
		if (orig != null) {
			orig.setDescription(itm.getDescription());
			orig.setValue(itm.getValue());
			getSession().update(orig);
		}
		return orig;
	}

	@Override
	public Preference remove(Preference itm) throws PreferenceException {
		Preference orig;
		try {
			orig = get(itm.getKey());
		} catch (PreferenceException e) {
			throw new PreferenceException(e);
		}
		if (orig != null) {
			getSession().delete(orig);
			getSession().createQuery(HQL_REMOVE_SUBTREE)
				.setString(0, itm.getKey() + Preference.splitter + "%")
				.executeUpdate();
		}
		return itm;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Preference> allPlain() {
		return createCriteria()
				.list();
	}
}
