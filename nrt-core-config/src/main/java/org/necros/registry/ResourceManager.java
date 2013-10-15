package org.necros.registry;

public interface ResourceManager extends ResourceService {
	public abstract ResourceNode add(ResourceNode node) throws RegistryAccessException;
	public abstract ResourceNode remove(String nodeId) throws RegistryAccessException;
	public abstract ResourceNode replaceContent(ResourceNode newNode) throws RegistryAccessException;
	public abstract ResourceNode rename(String fromId, String toName) throws RegistryAccessException;
	public abstract ResourceNode move(String fromPath, String toPath) throws RegistryAccessException;
}
