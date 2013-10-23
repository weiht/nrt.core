package org.necros.data;

import java.util.List;

public interface MetaClassManager {
	public abstract MetaClass get(String id);
	public abstract MetaClass getWithName(String pkg, String name);
	public abstract MetaClass add(MetaClass clazz) throws MetaDataAccessException;
	public abstract MetaClass updateInfo(MetaClass clazz) throws MetaDataAccessException;
	public abstract MetaClass remove(String id) throws MetaDataAccessException;
	public abstract MetaClass moveTo(String id, String pkg) throws MetaDataAccessException;
	public abstract MetaProperty addProperty(String clazzId, MetaProperty prop) throws MetaDataAccessException;
	public abstract MetaProperty removeProperty(String clazzId, String propId) throws MetaDataAccessException;
	public abstract MetaProperty updateProperty(MetaProperty prop) throws MetaDataAccessException;

	public abstract List<MetaClass> all(String pkg);
	public abstract List<MetaClass> search(String filterText);
	public abstract List<MetaClass> searchInPackage(String pkg, String filterText);
}
