package org.necros.data;

public interface GeneralDataSchema {
	public abstract void apply(MetaClass clazz) throws MetaDataAccessException;
	public abstract void applyBatch(MetaClass[] clazz) throws MetaDataAccessException;
	public abstract void applyAll() throws MetaDataAccessException;
}
