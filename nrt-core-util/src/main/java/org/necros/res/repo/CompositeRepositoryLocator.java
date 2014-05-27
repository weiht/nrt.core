package org.necros.res.repo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.necros.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompositeRepositoryLocator
implements RepositoryLocator {
	private static final Logger logger = LoggerFactory.getLogger(CompositeRepositoryLocator.class);
	
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
				if (logger.isTraceEnabled()) {
					logger.trace("Resource search paths: {}", paths);
				}
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
	public File[] getRepositories() {
		return ensuerBasePaths();
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
