/**
 * 
 */
package org.necros.data;

import java.sql.Timestamp;
import java.util.Calendar;

import org.necros.data.BasicObject;
import org.necros.data.BasicObjectService;
import org.necros.data.ObjectTouchLog;
/**
 * @author weiht
 *
 */
public abstract class AbstractBasicObjectService implements BasicObjectService {
	
	protected abstract BasicObject getObj(String id);
	protected abstract BasicObject addObj(BasicObject obj);
	protected abstract BasicObject updateObj(BasicObject obj);
	protected abstract ObjectTouchLog saveLog(ObjectTouchLog log);
	protected abstract ObjectTouchLog generateLog(ObjectTouchLog log);

	public BasicObject touch(String id, String entity, String login) {
		BasicObject obj = getObj(id);
		if (obj == null) {
			obj = new BasicObject();
			obj.setId(id);
			obj.setEntity(entity);
			obj.setOwnerId(login);
			obj.setCreatorId(login);
			obj.setCreateTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
			doTouch(obj, login);
			addObj(obj);
		} else {
			doTouch(obj, login);
			updateObj(obj);
		}
		return obj;
	}

	/**
	 * @param obj
	 * @param Login
	 * @param personName
	 */
	private void doTouch(BasicObject obj, String login) {
		obj.setUpdaterId(login);
		obj.setUpdateTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		ObjectTouchLog log = new ObjectTouchLog();
		log.setObjectId(obj.getId());
		log.setEntity(obj.getEntity());
		log.setUpdaterId(login);
		log.setUpdateTime(obj.getUpdateTime());
		generateLog(log);
		saveLog(log);
	}
}
