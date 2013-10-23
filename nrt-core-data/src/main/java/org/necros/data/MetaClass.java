package org.necros.data;

import java.io.Serializable;
import java.util.List;

public class MetaClass implements Serializable {
	private String id;
	private String metaPackage;
	private String name;
	private String displayName;
	private String tableName;
	private String revision;
	private String remarks;
	private List<MetaProperty> properties;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getMetaPackage() {
		return metaPackage;
	}

	public void setMetaPackage(String metaPackage) {
		this.metaPackage = metaPackage;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}
	
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
		
	public List<MetaProperty> getProperties() {
		return properties;
	}

	public void setProperties(List<MetaProperty> properties) {
		this.properties = properties;
	}	
}
