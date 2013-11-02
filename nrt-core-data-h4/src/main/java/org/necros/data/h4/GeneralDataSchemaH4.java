package org.necros.data.h4;

import java.util.List;

import org.necros.data.GeneralDataSchema;
import org.necros.data.MetaClass;
import org.necros.data.MetaDataAccessException;

public class GeneralDataSchemaH4 implements GeneralDataSchema {
	private RebuildableSessionFactory rebuildable;
	
	private void generateTable(MetaClass clazz) {
		throw new RuntimeException("Not yet implemented.");
	}
	
	private List<MetaClass> loadAllMetaClasses() {
		throw new RuntimeException("Not yet implemented.");
	}
	
	private void rebuildSessionFactory() {
		rebuildable.rebuild();
	}

	@Override
	public void apply(MetaClass clazz) throws MetaDataAccessException {
		if (clazz != null) {
			generateTable(clazz);
			rebuildSessionFactory();
		}
	}

	@Override
	public void applyBatch(MetaClass[] clazz) throws MetaDataAccessException {
		if (clazz != null && clazz.length > 0) {
			for (MetaClass mc: clazz) {
				generateTable(mc);
			}
			rebuildSessionFactory();
		}
	}

	@Override
	public void applyAll() throws MetaDataAccessException {
		List<MetaClass> clazz = loadAllMetaClasses();
		if (clazz != null && clazz.size() > 0) {
			for (MetaClass mc: clazz) {
				generateTable(mc);
			}
			rebuildSessionFactory();
		}
	}

	public void setRebuildable(RebuildableSessionFactory rebuildable) {
		this.rebuildable = rebuildable;
	}
}
