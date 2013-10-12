package org.necros.settings;

public class Preference {
	public static final String splitter = "/";
	public static Integer ITEM_TYPE_FOLDER = 0;
	public static Integer ITEM_TYPE_SETTING = 1;
	
	private String parentPath;
	private Integer itemType;
	private String key;
	private String value;
	private String description;
	
	public Preference() {
	}

	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}

	public Integer getItemType() {
		return itemType;
	}

	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(Preference.class.getName()).append("\n");
		b.append("parentPath = [").append(parentPath).append("]\n");
		b.append("itemType = [").append(itemType).append("]\n");
		b.append("key = [").append(key).append("]\n");
		b.append("value = [").append(value).append("]\n");
		b.append("description = [").append(description).append("]\n");
		return b.toString();
	}
}
