/**
 * 
 */
package org.necros.data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author weiht
 *
 */
@SuppressWarnings("serial")
public class ObjectTouchLog implements Serializable {
	private Long id;
	private String objectId;
	private String entity;
	private String updatePersonId;
	private String updatePersonName;
	private Timestamp updateTime;
	private String log;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getUpdatePersonId() {
		return updatePersonId;
	}

	public void setUpdatePersonId(String updatePersonId) {
		this.updatePersonId = updatePersonId;
	}

	public String getUpdatePersonName() {
		return updatePersonName;
	}

	public void setUpdatePersonName(String updatePersonName) {
		this.updatePersonName = updatePersonName;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}
}
