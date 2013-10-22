package org.necros.data;

import java.util.List;

public interface MetaPackageManager {
	public abstract MetaPackage get(String path);
	public abstract MetaPackage add(MetaPackage pkg) throws MetaDataAccessException;
	public abstract MetaPackage updateDescription(MetaPackage pkg) throws MetaDataAccessException;
	public abstract MetaPackage remove(String path) throws MetaDataAccessException;
	public abstract List<MetaPackage> root();
	public abstract List<MetaPackage> children(String path);
}
