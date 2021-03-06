package org.necros.data;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MetaProperty implements Serializable {
	private String id;
	private MetaClass metaClass;
	private String name;
	private String displayName;
	private String columnName;
	private String inputHint;
	private String displayType;
	private String dataType;
	private String description;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public MetaClass getMetaClass() {
		return metaClass;
	}

	public void setMetaClass(MetaClass metaClass) {
		this.metaClass = metaClass;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	public String getInputHint() {
		return inputHint;
	}

	public void setInputHint(String inputHint) {
		this.inputHint = inputHint;
	}
	
	public String getDisplayType() {
		return displayType;
	}

	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}
	
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		StringBuilder buff = new StringBuilder();
		buff.append("{'class': '").append(MetaProperty.class);
		buff.append("', 'id': '").append(id);
		buff.append("', 'metaClass': '").append(metaClass);
		buff.append("', 'name': '").append(name);
		buff.append("', 'displayName': '").append(displayName);
		buff.append("', 'columnName': '").append(columnName);
		buff.append("', 'inputHint': '").append(inputHint);
		buff.append("', 'displayType': '").append(displayType);
		buff.append("', 'dataType': '").append(dataType);
		buff.append("', 'description': '").append(description);
		buff.append("}");
		return buff.toString();
	}
}
