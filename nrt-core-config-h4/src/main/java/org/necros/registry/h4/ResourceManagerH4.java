package org.necros.registry.h4;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
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
		return getSession().createCriteria(clazz);
	}

	private Criteria ordered(Criteria c) {
		return c
				.addOrder(Order.asc("path"));
	}

	private Criteria filtered(Criteria c, String filterText) {
		return c
			.add(Restrictions.or(
				Restrictions.like("name", filterText, MatchMode.ANYWHERE),
				Restrictions.like("description", filterText, MatchMode.ANYWHERE))
			);
	}

	private int doCount(Criteria c) {
		return ((Long)c.setProjection(Projections.rowCount())
			.uniqueResult()).intValue();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Pager doPage(Criteria c, Pager p) {
		p.setResult(ordered(c).setFirstResult(p.getQueryFirst())
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
			.add(Restrictions.eq("parentPath", ResourceNode.SEPARATOR));
	}

	@SuppressWarnings("unchecked")
	public List<ResourceNode> root() {
		return ordered(rootCriteria(createCriteria()))
			.list();
	}

	@SuppressWarnings("unchecked")
	public List<ResourceNode> filteredRoot(String filterText) {
		return ordered(filtered(rootCriteria(createCriteria()), filterText))
			.list();
	}
	
	public int countRoot() {
		return doCount(rootCriteria(createCriteria()));
	}
	
	@SuppressWarnings("unchecked")
	public Pager<ResourceNode> pageRoot(Pager<ResourceNode> page) {
		page.setRecordCount(countRoot());
		return doPage(rootCriteria(createCriteria()), page);
	}
	
	public int countFilteredRoot(String filterText) {
		return doCount(filtered(rootCriteria(createCriteria()), filterText));
	}
	
	@SuppressWarnings("unchecked")
	public Pager<ResourceNode> pageFilteredRoot(String filterText, Pager<ResourceNode> page) {
		page.setRecordCount(countFilteredRoot(filterText));
		return doPage(filtered(rootCriteria(createCriteria()), filterText), page);
	}

	private Criteria childrenCriteria(Criteria c, String parentPath) {
		if (!StringUtils.hasText(parentPath))
			return rootCriteria(c);
		String p = parentPath;
		if (!p.startsWith(ResourceNode.SEPARATOR)) p = ResourceNode.SEPARATOR + p;
		if (!p.endsWith(ResourceNode.SEPARATOR)) p = p + ResourceNode.SEPARATOR;
		return c
			.add(Restrictions.eq("parentPath", p));
	}
	
	@SuppressWarnings("unchecked")
	public List<ResourceNode> children(String path) {
		return ordered(childrenCriteria(createCriteria(), path))
			.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<ResourceNode> filteredChildren(String filterText, String path) {
		return ordered(filtered(childrenCriteria(createCriteria(), path), filterText))
			.list();
	}
	
	public int countChildren(String path) {
		return doCount(childrenCriteria(createCriteria(), path));
	}
	
	@SuppressWarnings("unchecked")
	public Pager<ResourceNode> pageChildren(String path, Pager<ResourceNode> page) {
		page.setRecordCount(countChildren(path));
		return doPage(childrenCriteria(createCriteria(), path), page);
	}
	
	public int countFilteredChildren(String filterText, String path) {
		return doCount(filtered(childrenCriteria(createCriteria(), path), filterText));
	}
	
	@SuppressWarnings("unchecked")
	public Pager<ResourceNode> pageFilteredChildren(String filterText, String path, Pager<ResourceNode> page) {
		page.setRecordCount(countFilteredChildren(filterText, path));
		return doPage(filtered(childrenCriteria(createCriteria(), path), filterText), page);
	}
	
	public ResourceNode add(ResourceNode node) throws RegistryAccessException {
		if (node == null) throw new RegistryAccessException("You cannot save an empty node.");
		node.setId((String)idGenerator.generate());
		if (!StringUtils.hasText(node.getParentPath())) {
			node.setParentPath(ResourceNode.SEPARATOR);
		}
		String ppath = node.getParentPath();
		if (!ppath.startsWith(ResourceNode.SEPARATOR)) {
			ppath = ResourceNode.SEPARATOR + ppath;
		}
		if (!ppath.endsWith(ResourceNode.SEPARATOR)) {
			ppath = ppath + ResourceNode.SEPARATOR;
		}
		node.setParentPath(ppath);

		String path = ppath + node.getName();
		if (getWithPath(path) != null) throw new RegistryAccessException("Duplicated node path: " + path);

		node.setPath(path);

		switch (node.getType()) {
			case Long:
				node.setSize(8);
				break;
			case Double:
				node.setSize(8);
				break;
			case String:
				node.setSize(StringUtils.hasText(node.getStringValue())
					? node.getStringValue().length()
					: 0);
				break;
			case Clob:
				node.setSize(StringUtils.hasText(node.getLongText())
					? node.getLongText().length()
					: 0);
				break;
			case Blob:
				node.setSize(node.getBinValue() != null
					? node.getBinValue().length
					: 0);
				break;
			case Url:
				node.setSize(StringUtils.hasText(node.getStringValue())
					? node.getStringValue().length()
					: 0);
				break;
			case File:
				node.setSize(node.getBinValue() != null
					? node.getBinValue().length
					: 0);
				break;
			case Serialized:
				node.setSize(node.getBinValue() != null
					? node.getBinValue().length
					: 0);
				break;
			default:
				break;
		}

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
		if (origNode == null) throw new RegistryAccessException("No such node found: " + fromId);
		
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
		if (origNode == null) throw new RegistryAccessException("No such node found: " + fromId);

		String p = toPath;
		if (!StringUtils.hasText(p)) p = ResourceNode.SEPARATOR;
		if (!p.endsWith(ResourceNode.SEPARATOR)) p += ResourceNode.SEPARATOR;
		String op = origNode.getParentPath();
		if (op.equals(p)) return origNode;

		String fp = p + origNode.getName();
		ResourceNode node = getWithPath(fp);
		if (node != null) throw new RegistryAccessException("Duplicated node path: " + fp);

		origNode.setParentPath(p);
		origNode.setPath(fp);
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
