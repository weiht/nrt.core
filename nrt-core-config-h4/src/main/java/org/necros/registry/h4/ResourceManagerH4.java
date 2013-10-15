package org.necros.registry;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import org.necros.data.IdGenerator;
import org.necros.paging.Pager;
import org.necros.registry.ResourceNode;
import org.necros.registry.ResourceManager;
import org.necros.registry.RegistryAccessException;

public class ResourceManagerH4 implements ResourceManager {
	private static final Class<?> clazz = ResourceNode.class;

	private IdGenerator idGenerator;
	private SessionFactory sessionFactory;

	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	private Criteria createCriteria() {
		return getSession().createCriteria(clazz)
				.addOrder(Order.asc("path"));
	}

	private Criteria filtered(Criteria c, String filterText) {
		return c
			.add(Restrictions.like("path", filterText, MatchMode.ANYWHERE))
			.add(Restrictions.like("description", filterText, MatchMode.ANYWHERE));
	}

	private int doCount(Criteria c) {
		return ((Long)c.setProjection(Projections.rowCount())
			.uniqueResult()).intValue();
	}

	@SuppressWarnings("rawtypes")
	public Pager doPage(Criteria c, Pager p) {
		p.setResult(c.setFirstResult(p.getQueryFirst())
			.setFetchSize(p.getPageSize())
			.setMaxResults(p.getPageSize())
			.list());
		return p;
	}

	public ResourceNode get(String id) {
		return (ResourceNode) createCriteria()
			.add(Restrictions.eq("id", id))
			.uniqueResult();
	}

	public ResourceNode getWithPath(String path) {
		return (ResourceNode) createCriteria()
			.add(Restrictions.eq("path", path))
			.uniqueResult();
	}

	private Criteria rootCriteria(Criteria c) {
		return c
			.add(Restrictions.eqOrIsNull("parentPath", ResourceNode.SEPARATOR));
	}

	@SuppressWarnings("unckecked")
	public List<ResourceNode> root() {
		return rootCriteria(createCriteria())
			.list();
	}

	@SuppressWarnings("unckecked")
	public List<ResourceNode> filteredRoot(String filterText) {
		return filtered(rootCriteria(createCriteria()), filterText)
			.list();
	}
	
	public int countRoot() {
		return doCount(rootCriteria(createCriteria()));
	}
	
	@SuppressWarnings("unckecked")
	public Pager<ResourceNode> pageRoot(Pager<ResourceNode> page) {
		page.setRecordCount(countRoot());
		return doPage(rootCriteria(createCriteria()), page);
	}
	
	public int countFilteredRoot(String filterText) {
		return doCount(filtered(rootCriteria(createCriteria()), filterText));
	}
	
	@SuppressWarnings("unckecked")
	public Pager<ResourceNode> pageFilteredRoot(String filterText, Pager<ResourceNode> page) {
		page.setRecordCount(countFilteredRoot(filterText));
		return doPage(filtered(rootCriteria(createCriteria()), filterText), page);
	}

	private Criteria childrenCriteria(Criteria c, String parentPath) {
		return c
			.add(Restrictions.eq("parentPath", parentPath));
	}
	
	@SuppressWarnings("unckecked")
	public List<ResourceNode> childrenOf(ResourceNode parent) {
		return children(parent.getPath());
	}
	
	@SuppressWarnings("unckecked")
	public List<ResourceNode> filteredChildrenOf(String filterText, ResourceNode parent) {
		return filteredChildren(parent.getPath(), filterText);
	}
	
	public int countChildrenOf(ResourceNode parent) {
		return countChildren(parent.getPath());
	}
	
	@SuppressWarnings("unckecked")
	public Pager<ResourceNode> pageChildrenOf(ResourceNode parent, Pager<ResourceNode> page) {
		return pageChildren(parent.getPath(), page);
	}
	
	public int countFilteredChildrenOf(String filterText, ResourceNode parent) {
		return countFilteredChildren(filterText, parent.getPath());
	}
	
	@SuppressWarnings("unckecked")
	public Pager<ResourceNode> pageFilteredChildrenOf(String filterText, ResourceNode parent, Pager<ResourceNode> page) {
		return pageFilteredChildren(filterText, parent.getPath(), page);
	}
	
	@SuppressWarnings("unckecked")
	public List<ResourceNode> children(String path) {
		return childrenCriteria(createCriteria(), path)
			.list();
	}
	
	@SuppressWarnings("unckecked")
	public List<ResourceNode> filteredChildren(String filterText, String path) {
		return filtered(childrenCriteria(createCriteria(), path), filterText)
			.list();
	}
	
	public int countChildren(String path) {
		return doCount(childrenCriteria(createCriteria(), path));
	}
	
	@SuppressWarnings("unckecked")
	public Pager<ResourceNode> pageChildren(String path, Pager<ResourceNode> page) {
		page.setRecordCount(countChildren(path));
		return doPage(childrenCriteria(createCriteria(), path), page);
	}
	
	public int countFilteredChildren(String filterText, String path) {
		return doCount(filtered(childrenCriteria(createCriteria(), path), filterText));
	}
	
	@SuppressWarnings("unckecked")
	public Pager<ResourceNode> pageFilteredChildren(String filterText, String path, Pager<ResourceNode> page) {
		page.setRecordCount(countChildren(path));
		return doPage(filtered(childrenCriteria(createCriteria(), path), filterText), page);
	}
	
	public ResourceNode add(ResourceNode node) throws RegistryAccessException {
		if (node == null) throw new RegistryAccessException("You cannot save an empty node.");
		node.setId((String)idGenerator.generate());
		if (!StringUtils.hasText(node.getParentPath())) {
			node.setParentPath(ResourceNode.SEPARATOR);
		}
		String ppath = node.getParentPath();
		if (!ppath.endsWith(ResourceNode.SEPARATOR)) {
			ppath = ppath + ResourceNode.SEPARATOR;
			node.setParentPath(ppath);
		}

		String path = ppath + node.getName();
		if (getWithPath(path) != null) throw new RegistryAccessException("Duplicated node path: " + path);

		node.setPath(path);
		getSession().save(node);
		return node;
	}
	
	public ResourceNode remove(String nodeId) throws RegistryAccessException {
		ResourceNode node = get(nodeId);
		if (node != null) {
			getSession().delete(node);
		}
		return node;
	}
	
	public ResourceNode replaceContent(ResourceNode newNode) throws RegistryAccessException {
		ResourceNode origNode = get(newNode.getId());
		if (origNode == null) throw new RegistryAccessException("No such node found: " + newNode);
		origNode.setDescription(newNode.getDescription());
		origNode.setType(newNode.getType());
		switch (origNode.getType()) {
			case Long:
				origNode.setLongValue(newNode.getLongValue());
				origNode.setSize(8);
				break;
			case Double:
				origNode.setDoubleValue(newNode.getDoubleValue());
				origNode.setSize(8);
				break;
			case String:
				origNode.setStringValue(newNode.getStringValue());
				origNode.setSize(StringUtils.hasText(origNode.getStringValue())
					? origNode.getStringValue().length()
					: 0);
				break;
			case Clob:
				origNode.setLongText(newNode.getLongText());
				origNode.setSize(StringUtils.hasText(origNode.getLongText())
					? origNode.getLongText().length()
					: 0);
				break;
			case Blob:
				origNode.setBinValue(newNode.getBinValue());
				origNode.setSize(origNode.getBinValue() != null
					? origNode.getBinValue().length
					: 0);
				break;
			case Url:
				origNode.setStringValue(newNode.getStringValue());
				origNode.setSize(StringUtils.hasText(origNode.getStringValue())
					? origNode.getStringValue().length()
					: 0);
				break;
			case File:
				origNode.setStringValue(newNode.getStringValue());
				origNode.setBinValue(newNode.getBinValue());
				origNode.setSize(origNode.getBinValue() != null
					? origNode.getBinValue().length
					: 0);
				break;
			case Serialized:
				origNode.setBinValue(newNode.getBinValue());
				origNode.setSize(origNode.getBinValue() != null
					? origNode.getBinValue().length
					: 0);
				break;
			default:
				break;

		}
		getSession().update(origNode);
		return origNode;
	}
	
	public ResourceNode rename(String fromId, String toName) throws RegistryAccessException {
		ResourceNode origNode = get(fromId);
		String n = origNode.getName();
		if (n.equals(toName)) return origNode;

		String p = origNode.getParentPath() + toName;
		ResourceNode node = getWithPath(p);
		if (node != null) throw new RegistryAccessException("Duplicated node path: " + p);

		origNode.setName(toName);
		origNode.setPath(p);
		getSession().update(origNode);
		return origNode;
	}
	
	public ResourceNode move(String fromId, String toPath) throws RegistryAccessException {
		ResourceNode origNode = get(fromId);
		String op = origNode.getPath();
		if (op.equals(toPath)) return origNode;

		String p = toPath;
		if (!StringUtils.hasText(p)) p = ResourceNode.SEPARATOR;
		if (!p.endsWith(ResourceNode.SEPARATOR)) p += ResourceNode.SEPARATOR;
		String fp = p + origNode.getName();

		ResourceNode node = getWithPath(fp);
		if (node != null) throw new RegistryAccessException("Duplicated node path: " + fp);

		origNode.setPath(p);
		getSession().update(origNode);
		return origNode;
	}

	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
