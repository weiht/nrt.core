package org.necros.data;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import org.necros.data.IdGenerator;

public abstract class AbstractMetaClassManager implements MetaClassManager {
	private static final Logger logger = LoggerFactory.getLogger(AbstractMetaClassManager.class);
	private static final Pattern nameValidationPattern = Pattern.compile("^[a-z,A-Z]\\w*$");

	private MetaPackageManager metaPackageManager;
	private IdGenerator idGenerator;

	protected abstract MetaClass doGet(String id);
	protected abstract MetaClass doGetWithName(String pkg, String name);
	protected abstract boolean clazzExists(String pkg, String name);
	protected abstract MetaClass doAdd(MetaClass clazz);
	protected abstract MetaClass doUpdate(MetaClass clazz);
	protected abstract MetaClass doRemove(MetaClass clazz);
	protected abstract MetaProperty doGetProperty(String propId);
	protected abstract MetaProperty doAddProperty(MetaClass clazz, MetaProperty prop);
	protected abstract MetaProperty doRemoveProperty(MetaProperty prop);
	protected abstract MetaProperty doUpdateProperty(MetaProperty prop);
	protected abstract List<MetaClass> doFindAll(String pkg);
	protected abstract List<MetaClass> doSearchInPackage(String pkg, String filterText);

	protected boolean validateName(String name) {
		if (!StringUtils.hasText(name)) return false;
		Matcher m = nameValidationPattern.matcher(name);
		return m.matches();
	}

	protected void makePropertyIds(MetaClass clazz) {
		for (MetaProperty p: clazz.getProperties()) {
			if (p != null && p.getId() == null) {
				p.setId((String)idGenerator.generate());
				p.setMetaClass(clazz);
			}
		}
	}

	public MetaClass get(String id) {
		if (!StringUtils.hasText(id)) return null;
		return doGet(id);
	}

	public MetaClass getWithName(String pkg, String name) {
		if (!StringUtils.hasText(name)) return null;
		return doGetWithName(pkg, name);
	}

	public MetaClass add(MetaClass clazz) throws MetaDataAccessException {
		if (clazz == null) throw new MetaDataAccessException("Saving null MetaClass");
		String pkg = clazz.getMetaPackage();
		if (StringUtils.hasText(pkg) && metaPackageManager.get(pkg) == null)
			throw new MetaDataAccessException("Package doesn't exist: " + pkg);
		String name = clazz.getName();
		if (!validateName(name)) throw new MetaDataAccessException("Invalid class name.");
		if(clazzExists(pkg, name)) throw new MetaDataAccessException("MetaClass already exist.");

		clazz.setId((String)idGenerator.generate());
		makePropertyIds(clazz);
		doAdd(clazz);
		return clazz;
	}

	public MetaClass updateInfo(MetaClass clazz) throws MetaDataAccessException {
		MetaClass orig = get(clazz.getId());
		if (orig == null) throw new MetaDataAccessException("Invalid MetaClass.");
		orig.setDisplayName(clazz.getDisplayName());
		orig.setDescription(clazz.getDescription());
		doUpdate(orig);
		return orig;
	}

	public MetaClass remove(String id) throws MetaDataAccessException {
		MetaClass orig = get(id);
		if (orig == null) throw new MetaDataAccessException("Invlaid MetaClass.");

		doRemove(orig);
		return orig;
	}

	public MetaClass moveTo(String id, String pkg) throws MetaDataAccessException {
		if (StringUtils.hasText(pkg) && metaPackageManager.get(pkg) == null)
			throw new MetaDataAccessException("Package doesn't exist: " + pkg);
		MetaClass clazz = get(id);
		if (clazz == null) throw new MetaDataAccessException("Invalid MetaClass.");
		if(clazzExists(pkg, clazz.getName())) throw new MetaDataAccessException("MetaClass already exist.");
		clazz.setMetaPackage(StringUtils.hasText(pkg) ? pkg : null);
		doUpdate(clazz);
		return clazz;
	}

	public MetaProperty addProperty(String clazzId, MetaProperty prop) throws MetaDataAccessException {
		if (prop == null) throw new MetaDataAccessException("Saving null MetaProperty.");
		MetaClass clazz = get(clazzId);
		if (clazz == null) throw new MetaDataAccessException("Invalid MetaClass.");
		if (!validateName(prop.getName())) throw new MetaDataAccessException("Invalid name.");

		prop.setId((String)idGenerator.generate());
		prop.setMetaClass(clazz);
		return doAddProperty(clazz, prop);
	}

	public MetaProperty removeProperty(String clazzId, String propId) throws MetaDataAccessException {
		MetaProperty prop = doGetProperty(propId);
		if (prop == null) throw new MetaDataAccessException("Invalid MetaProperty.");
		if (prop.getMetaClass() == null || prop.getMetaClass().getId() == null
			|| !prop.getMetaClass().getId().equals(clazzId))
			throw new MetaDataAccessException("This porperty does not belong to the specified class.");
		return doRemoveProperty(prop);
	}

	public MetaProperty updateProperty(MetaProperty prop) throws MetaDataAccessException {
		MetaProperty orig = doGetProperty(prop.getId());
		if (orig == null) throw new MetaDataAccessException("Invalid MetaProperty.");
		orig.setDisplayName(prop.getDisplayName());
		orig.setInputHint(prop.getInputHint());
		orig.setDisplayType(prop.getDisplayType());
		orig.setDescription(prop.getDescription());
		return doUpdateProperty(orig);
	}

	public List<MetaClass> all(String pkg) {
		if (StringUtils.hasText(pkg) && metaPackageManager.get(pkg) == null)
			return new ArrayList<MetaClass>();
		return doFindAll(pkg);
	}

	public List<MetaClass> searchInPackage(String pkg, String filterText) {
		if (StringUtils.hasText(pkg) && metaPackageManager.get(pkg) == null)
			return new ArrayList<MetaClass>();
		return doSearchInPackage(pkg, filterText);
	}


	public void setMetaPackageManager(MetaPackageManager metaPackageManager) {
		this.metaPackageManager = metaPackageManager;
	}

	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}
}
