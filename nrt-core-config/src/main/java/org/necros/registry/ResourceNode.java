package org.necros.registry;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ResourceNode implements Serializable {
	public static final String SEPARATOR = "/";
	private String id;
	private String parentPath;
	private String path;
	private NodeType nodeType;
	private String name;
	private String description;
	private Integer size;
	private ContentType type = ContentType.None;
	private Long longValue;
	private Double doubleValue;
	private String stringValue;
	private String longText;
	private byte[] binValue;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public NodeType getNodeType() {
		return nodeType;
	}

	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public ContentType getType() {
		return type;
	}

	public void setType(ContentType type) {
		this.type = type;
	}

	public Long getLongValue() {
		return longValue;
	}

	public void setLongValue(Long longValue) {
		this.longValue = longValue;
	}

	public Double getDoubleValue() {
		return doubleValue;
	}

	public void setDoubleValue(Double doubleValue) {
		this.doubleValue = doubleValue;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public String getLongText() {
		return longText;
	}

	public void setLongText(String longText) {
		this.longText = longText;
	}

	public byte[] getBinValue() {
		return binValue;
	}

	public void setBinValue(byte[] binValue) {
		this.binValue = binValue;
	}
}
