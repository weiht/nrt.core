package org.necros.data.h4;

import java.io.IOException;
import java.util.List;

import org.hibernate.SessionFactory;
import org.necros.data.GeneralDataSchema;
import org.necros.data.MetaClass;
import org.necros.data.MetaDataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeneralDataSchemaH4 implements GeneralDataSchema {
	private static final Logger logger = LoggerFactory.getLogger(GeneralDataSchemaH4.class);
	
	private RebuildableSessionFactory rebuildable;
	private SessionFactoryHelper helper;
	private MappingGenerator mappingGenerator;
	
	@SuppressWarnings("unchecked")
	private List<MetaClass> loadAllMetaClasses() {
		return helper.getSession().createCriteria(MetaClass.class)
				.list();
	}
	
	private void rebuildSessionFactory() {
		rebuildable.rebuild();
	}

	@Override
	public void apply(MetaClass clazz) throws MetaDataAccessException {
		if (clazz != null) {
			try {
				mappingGenerator.generateMapping(clazz);
			} catch (IOException e) {
				logger.warn("Error generating mapping file: {}", clazz);
				throw new MetaDataAccessException("Error generating mapping file.");
			}
			rebuildSessionFactory();
		}
	}

	@Override
	public void applyBatch(MetaClass[] clazz) throws MetaDataAccessException {
		if (clazz != null && clazz.length > 0) {
			for (MetaClass mc: clazz) {
				try {
					mappingGenerator.generateMapping(mc);
				} catch (IOException e) {
					logger.warn("Error generating mapping file: {}", mc);
					throw new MetaDataAccessException("Error generating mapping file.");
				}
			}
			rebuildSessionFactory();
		}
	}

	@Override
	public void applyAll() throws MetaDataAccessException {
		List<MetaClass> clazz = loadAllMetaClasses();
		if (clazz != null && clazz.size() > 0) {
			for (MetaClass mc: clazz) {
				try {
					mappingGenerator.generateMapping(mc);
				} catch (IOException e) {
					logger.warn("Error generating mapping file: {}", mc);
					throw new MetaDataAccessException("Error generating mapping file.");
				}
			}
			rebuildSessionFactory();
		}
	}

	public void setRebuildable(RebuildableSessionFactory rebuildable) {
		this.rebuildable = rebuildable;
	}
	
	public void setMetaClassSessionFactory(SessionFactory sessionFactory) {
		this.helper = SessionFactoryHelper.getInstance(sessionFactory);
	}

	public void setMappingGenerator(MappingGenerator mappingGenerator) {
		this.mappingGenerator = mappingGenerator;
	}
}
