package org.necros.res;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.necros.res.repo.RepositoryLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileResourceProvider
implements ResourceProvider {
	private static final Logger logger = LoggerFactory.getLogger(FileResourceProvider.class);
	
	private RepositoryLocator locator;
	
	@Override
	public InputStream read(String name) {
		File[] fs = locator.getRepositories();
		for (File f: fs) {
			File res = new File(f, name);
			if (res.exists() && res.isFile()) {
				try {
					logger.trace("File resource found for {}: {}", name, res);
					return new FileInputStream(res);
				} catch (FileNotFoundException e) {
					//This exception won't be thrown, so it won't be handled.
				}
			}
		}
		return null;
	}

	public void setLocator(RepositoryLocator locator) {
		this.locator = locator;
	}
}
