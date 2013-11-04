package org.necros.authz;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ObjectPermission implements Serializable {
	private String id;
	private String objectId;
	private String objectType;
	private String permissionOwnerId;
	private String permissionOwnerType;
	private Long permission;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getObjectId() {
		return this.objectId;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getObjectType() {
		return this.objectType;
	}

	public void setPermissionOwnerId(String permissionOwnerId) {
		this.permissionOwnerId = permissionOwnerId;
	}

	public String getPermissionOwnerId() {
		return this.permissionOwnerId;
	}

	public void setPermissionOwnerType(String permissionOwnerType) {
		this.permissionOwnerType = permissionOwnerType;
	}

	public String getPermissionOwnerType() {
		return this.permissionOwnerType;
	}

	public void setPermission(Long permission) {
		this.permission = permission;
	}

	public Long getPermission() {
		return this.permission;
	}
}
