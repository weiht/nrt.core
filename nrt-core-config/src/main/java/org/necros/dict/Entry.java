package org.necros.dict;

public class Entry {
	private String categoryName;
	private String value;
	private String displayText;
	private String description;

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryName() {
		return this.categoryName;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	public String getDisplayText() {
		return this.displayText;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}
}
