package org.necros.authz;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Role implements Serializable {
	private String id;
	private String name;
	private String displayText;
	private String status;
	private String description;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	public String getDisplayText() {
		return this.displayText;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return this.status;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}
}
