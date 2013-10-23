package org.necros.data;

import java.util.List;

public interface MetaClassManager {
	public abstract MetaClass get(String id);
	public abstract MetaClass getWithName(String pkg, String name);
	public abstract MetaClass add(MetaClass clazz);
	public abstract MetaClass updateInfo(MetaClass clazz);
	public abstract MetaClass remove(String id);
	public abstract MetaProperty addProperty(String clazzId, MetaProperty prop);
	public abstract MetaProperty removeProperty(String clazzId, String propId);
	public abstract MetaProperty updateProperty(MetaProperty prop);

	public abstract List<MetaClass> all(String pkg);
	public abstract List<MetaClass> search(String filterText);
	public abstract List<MetaClass> searchInPackage(String pkg, String filterText);
}
