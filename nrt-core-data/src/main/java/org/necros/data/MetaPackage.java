package org.necros.data;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MetaPackage implements Serializable {
	public static final String SEPARATOR = ".";
	public static final String SEPARATOR_REGEX = "\\.";
	
	private String id;
	private String parentPath;
	private String path;
	private String name;
	private String description;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getParentPath() {
		return this.parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		StringBuilder buff = new StringBuilder();
		buff.append("{'class': '").append(MetaPackage.class);
		buff.append("', 'id': '").append(id);
		buff.append("', 'path': '").append(path);
		buff.append("', 'parentPath': '").append(parentPath);
		buff.append("', 'name': '").append(name);
		buff.append("', 'description': '").append(description);
		buff.append("'}");
		return buff.toString();
	}
}
