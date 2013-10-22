package org.necros.data;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.springframework.util.StringUtils;

public abstract class AbstractMetaPackageManager implements MetaPackageManager {
	private Pattern pathPattern = Pattern.compile("^([\\d]+\\.)([\\d])+");

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
		String[] parts = path.split(MetaPackage.SEPARATOR);
		String dir = "", pdir = null;
		for (int i = 0; i < parts.length - 1; i ++) {
			if (dir.length() > 0) dir += MetaPackage.SEPARATOR;
			dir += parts[i];
			MetaPackage pkg = doGet(dir);
			if (pkg == null) {
				pkg = new MetaPackage();
				pkg.setPath(dir);
				pkg.setParentPath(pdir);
				pkg.setName(parts[i]);
				doAdd(pkg);
			}
			pdir = dir;
		}
	}

	protected void adjustPath(MetaPackage pkg) {
		String path = pkg.getPath();
		String ppath = null;
		String name = path;
		int ix = path.indexOf(MetaPackage.SEPARATOR);
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
		mkdirs(path);
		adjustPath(pkg);
		return doAdd(pkg);
	}

	public MetaPackage updateDescription(MetaPackage pkg) throws MetaDataAccessException {
		MetaPackage origPkg = get(pkg.getPath());
		if (origPkg == null) throw new MetaDataAccessException("Invlaid MetaPackage.");

		origPkg.setDescription(pkg.getDescription());
		doUpdate(origPkg);
		return origPkg;
	}

	public MetaPackage remove(String path) throws MetaDataAccessException {
		MetaPackage origPkg = get(path);
		if (origPkg == null) throw new MetaDataAccessException("Invlaid MetaPackage.");

		doRemove(origPkg);
		return origPkg;
	}
}
