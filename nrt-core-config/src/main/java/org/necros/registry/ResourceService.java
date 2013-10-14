package org.necros.registry;

import java.util.List;

import org.necros.paging.Pager;

public interface ResourceService {
	public abstract ResourceNode add(ResourceNode node) throws RegistryAccessException;
	public abstract ResourceNode remove(ResourceNode node) throws RegistryAccessException;
	public abstract ResourceNode get(String id);
	public abstract ResourceNode getWithPath(String path);
	public abstract ResourceNode replaceContent(ResourceNode newNode) throws RegistryAccessException;
	public abstract ResourceNode rename(String fromId, String toName) throws RegistryAccessException;
	public abstract ResourceNode move(String fromPath, String toPath) throws RegistryAccessException;
	public abstract List<ResourceNode> root();
	public abstract List<ResourceNode> filteredRoot(String filterText);
	public abstract int countRoot();
	public abstract Pager<ResourceNode> pageRoot(Pager<ResourceNode> page);
	public abstract int countFilteredRoot(String filterText);
	public abstract Pager<ResourceNode> pageFilteredRoot(String filterText, Pager<ResourceNode> page);
	public abstract List<ResourceNode> children(ResourceNode parent);
	public abstract List<ResourceNode> filteredChildren(String filterText, ResourceNode parent);
	public abstract int countChildren(ResourceNode parent);
	public abstract Pager<ResourceNode> pageChildren(ResourceNode parent, Pager<ResourceNode> page);
	public abstract int countFilteredChildren(String filterText, ResourceNode parent);
	public abstract Pager<ResourceNode> pageFilteredChildren(String filterText, ResourceNode parent, Pager<ResourceNode> page);
	public abstract List<ResourceNode> children(String path);
	public abstract List<ResourceNode> filteredChildren(String filterText, String path);
	public abstract int countChildren(String path);
	public abstract Pager<ResourceNode> pageChildren(String path, Pager<ResourceNode> page);
	public abstract int countFilteredChildren(String filterText, String path);
	public abstract Pager<ResourceNode> pageFilteredChildren(String filterText, String path, Pager<ResourceNode> page);
}
