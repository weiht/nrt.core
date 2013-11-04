package org.necros.authz;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RoleMember implements Serializable {
	private String id;
	private String roleId;
	private String loginId;


	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleId() {
		return this.roleId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getLoginId() {
		return this.loginId;
	}
}
