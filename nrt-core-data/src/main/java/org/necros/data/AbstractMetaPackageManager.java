package org.necros.data;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public abstract class AbstractMetaPackageManager implements MetaPackageManager {
	private static final Logger logger = LoggerFactory.getLogger(AbstractMetaPackageManager.class);
	private Pattern pathPattern = Pattern.compile("^([\\w]+" + MetaPackage.SEPARATOR_REGEX + ")*([\\w])+$");

	protected IdGenerator idGenerator;
	protected MetaClassManager metaClassManager;

	protected abstract MetaPackage doGet(String path);
	protected abstract MetaPackage doAdd(MetaPackage pkg);
	protected abstract MetaPackage doUpdate(MetaPackage pkg);
	protected abstract MetaPackage doRemove(MetaPackage pkg);

	protected boolean validatePath(String path) {
		if (!StringUtils.hasText(path)) return false;
		Matcher m = pathPattern.matcher(path);
		return m.matches();
	}

	protected void mkdirs(String path) {
		String[] parts = path.split(MetaPackage.SEPARATOR_REGEX);
		logger.debug("Making MetaPackage dirs, path: [{}], parts: [{}]", path, (Object)parts);
		String dir = "", pdir = null;
		for (int i = 0; i < parts.length - 1; i ++) {
			if (dir.length() > 0) dir += MetaPackage.SEPARATOR;
			dir += parts[i];
			MetaPackage pkg = doGet(dir);
			if (pkg == null) {
				String name = parts[i];
				logger.debug("Making MetaPackage dir: path=[{}], parent=[{}], name=[{}]",
					dir, pdir, name);
				pkg = new MetaPackage();
				pkg.setPath(dir);
				pkg.setParentPath(pdir);
				pkg.setName(name);
				pkg.setId((String)idGenerator.generate());
				doAdd(pkg);
			}
			pdir = dir;
		}
	}

	protected void adjustPath(MetaPackage pkg) {
		String path = pkg.getPath();
		String ppath = null;
		String name = path;
		int ix = path.lastIndexOf(MetaPackage.SEPARATOR);
		if (ix > 0) {
			ppath = path.substring(0, ix);
			name = path.substring(ix + 1);
		}
		pkg.setName(name);
		pkg.setParentPath(ppath);
	}

	public MetaPackage get(String path) {
		if (!StringUtils.hasText(path)) return null;
		return doGet(path);
	}

	public MetaPackage add(MetaPackage pkg) throws MetaDataAccessException {
		if (pkg == null) throw new MetaDataAccessException("Saving null MetaPackage.");
		String path = pkg.getPath();
		if (!StringUtils.hasText(path)) throw new MetaDataAccessException("No path specified for MetaPackage.");
		if (!validatePath(path)) throw new MetaDataAccessException("Invlaid MetaPackage path.");
		MetaPackage orig = doGet(path);
		if (orig != null) throw new MetaDataAccessException("MetaPackage already exists: [" + path + "].");
		mkdirs(path);
		adjustPath(pkg);
		pkg.setId((String)idGenerator.generate());
		logger.debug("Do adding MetaPackage: path=[{}], parent=[{}], name=[{}]",
			pkg.getPath(), pkg.getParentPath(), pkg.getName());
		return doAdd(pkg);
	}

	public MetaPackage updateDescription(MetaPackage pkg) throws MetaDataAccessException {
		MetaPackage origPkg = get(pkg.getPath());
		if (origPkg == null) throw new MetaDataAccessException("Invlaid MetaPackage.");

		origPkg.setDescription(pkg.getDescription());
		doUpdate(origPkg);
		return origPkg;
	}

	protected void moveClasses(String path) {
		if (metaClassManager != null) {
			for (MetaClass metaClazz: metaClassManager.all(path)) {
				metaClazz.setMetaPackage(null);
				try {
					metaClassManager.moveTo(metaClazz.getId(), null);
				} catch (Exception ex) {
					logger.warn("Error moving MetaClass to null. \n{}", ex);
				}
			}
		}
	}

	protected void removeChildren(String path) {
		logger.debug("Removing children for [{}]...", path);
		List<MetaPackage> pkgs = children(path);
		logger.debug("Children found: {}", pkgs);
		for (MetaPackage pkg: pkgs) {
			removeTree(pkg);
		}
	}

	protected void removeTree(MetaPackage pkg) {
		String path = pkg.getPath();
		removeChildren(path);
		moveClasses(path);
		doRemove(pkg);
	}

	public MetaPackage remove(String path) throws MetaDataAccessException {
		MetaPackage origPkg = get(path);
		if (origPkg == null) throw new MetaDataAccessException("Invlaid MetaPackage.");

		removeTree(origPkg);
		return origPkg;
	}

	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	public void setMetaClassManager(MetaClassManager metaClassManager) {
		this.metaClassManager = metaClassManager;
	}
}
