package org.necros.res;

import java.io.InputStream;

public interface ResourceProvider {
	public abstract InputStream read(String name);
}
