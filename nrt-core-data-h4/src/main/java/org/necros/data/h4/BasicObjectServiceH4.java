/**
 * 
 */
package org.necros.data.h4;

import java.sql.Timestamp;
import java.util.Calendar;

import org.hibernate.SessionFactory;

import org.necros.data.BasicObject;
import org.necros.data.BasicObjectService;
import org.necros.data.AbstractBasicObjectService;
import org.necros.data.ObjectTouchLog;
import org.necros.data.h4.SessionFactoryHelper;

/**
 * @author weiht
 *
 */
public class BasicObjectServiceH4 extends AbstractBasicObjectService {
	private SessionFactoryHelper helper;
	
	protected BasicObject getObj(String id) {
		return (BasicObject) helper.getSession().get(BasicObject.class, id);
	}

	protected BasicObject addObj(BasicObject obj) {
		helper.getSession().save(obj);
		return obj;
	}

	protected BasicObject updateObj(BasicObject obj) {
		helper.getSession().update(obj);
		return obj;
	}

	protected ObjectTouchLog saveLog(ObjectTouchLog log) {
		helper.getSession().save(log);
		return log;
	}

	protected ObjectTouchLog generateLog(ObjectTouchLog log) {
		//TODO Generate log text.
		return log;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.helper = SessionFactoryHelper.getInstance(sessionFactory);
	}
}
