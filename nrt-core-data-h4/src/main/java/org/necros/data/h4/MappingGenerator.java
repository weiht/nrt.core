package org.necros.data.h4;

import java.io.IOException;

import org.necros.data.MetaClass;

public interface MappingGenerator {
	public abstract String generateMapping(MetaClass clazz) throws IOException;
}
