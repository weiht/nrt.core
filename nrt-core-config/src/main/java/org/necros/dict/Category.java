package org.necros.dict;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Category implements Serializable {
	private String name;
	private ValueType valueType;
	private String description;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
	
	public void setValueType(ValueType valueType) {
		this.valueType = valueType;
	}

	public ValueType getValueType() {
		return this.valueType;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}
}
