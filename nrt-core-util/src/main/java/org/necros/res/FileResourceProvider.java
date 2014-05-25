package org.necros.res;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.necros.res.repo.FileRepositoryLocator;
import org.necros.util.StringUtils;

public class FileResourceProvider
implements ResourceProvider {
	public static final String SEPARATOR = ",";
	private static final String[] DEFAULT_ROOT_PATHS = {"/"};
	
	protected String[] rootPaths = DEFAULT_ROOT_PATHS;
	protected List<FileRepositoryLocator> locators;
	protected File[] basePaths;
	
	protected File[] ensuerBasePaths() {
		synchronized(this) {
			if (basePaths == null) {
				List<File> paths = new ArrayList<File>();
				for (String p: rootPaths) {
					File f = new File(p);
					if (f.exists() && f.isDirectory()) {
						if (locators == null || locators.isEmpty()) {
							paths.add(f);
						} else {
							for (FileRepositoryLocator l: locators) {
								File[] fs = l.findRepos(f.getAbsolutePath());
								for (File r: fs) {
									paths.add(r);
								}
							}
						}
					}
				}
				basePaths = paths.toArray(new File[]{});
			}
		}
		return basePaths;
	}

	@Override
	public InputStream read(String name) {
		File[] fs = ensuerBasePaths();
		for (File f: fs) {
			File res = new File(f, name);
			if (res.exists() && res.isFile()) {
				try {
					return new FileInputStream(res);
				} catch (FileNotFoundException e) {
					//This exception won't be thrown, so it won't be handled.
				}
			}
		}
		return null;
	}

	public void setRootPaths(String rootPaths) {
		if (StringUtils.isEmpty(rootPaths)) {
			this.rootPaths = DEFAULT_ROOT_PATHS;
		} else {
			this.rootPaths = rootPaths.split(SEPARATOR);
		}
	}

	public void setLocators(List<FileRepositoryLocator> locators) {
		this.locators = locators;
	}
}
