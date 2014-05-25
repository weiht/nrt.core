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
	protected List<FileRepositoryLocator> repositoryLocators;
	protected List<FileRepositoryLocator> resourceLocators;
	protected File[] basePaths;
	
	protected File[] ensuerBasePaths() {
		synchronized(this) {
			if (basePaths == null) {
				List<File> paths = new ArrayList<File>();
				for (String p: rootPaths) {
					File f = new File(p);
					if (f.exists() && f.isDirectory()) {
						fetchRepositoryPaths(paths, f);
					}
				}
				basePaths = paths.toArray(new File[]{});
			}
		}
		return basePaths;
	}

	private void fetchRepositoryPaths(List<File> paths, File f) {
		if (repositoryLocators == null || repositoryLocators.isEmpty()) {
			fetchResourcePathsInRepository(paths, f);
		} else {
			for (FileRepositoryLocator l: repositoryLocators) {
				File[] repos = l.findRepos(f.getAbsolutePath());
				for (File r: repos) {
					fetchResourcePathsInRepository(paths, r);
				}
			}
		}
	}

	private void fetchResourcePathsInRepository(List<File> paths, File repo) {
		if (resourceLocators == null || resourceLocators.isEmpty()) {
			paths.add(repo);
		} else {
			for (FileRepositoryLocator l: resourceLocators) {
				File[] resPaths = l.findRepos(repo.getAbsolutePath());
				for (File rp: resPaths) {
					paths.add(rp);
				}
			}
		}
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

	public void setRepositoryLocators(List<FileRepositoryLocator> repositoryLocators) {
		this.repositoryLocators = repositoryLocators;
	}

	public void setResourceLocators(List<FileRepositoryLocator> resourceLocators) {
		this.resourceLocators = resourceLocators;
	}
}
