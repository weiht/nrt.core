package org.necros.res.repo;

import java.io.File;

import org.necros.util.StringUtils;

public abstract class AbstractFileRepositoryLocator
implements FileRepositoryLocator {
	private static final File[] NO_REPOS_FOUND = {};

	@Override
	public File[] findRepos(String base) {
		if (StringUtils.isEmpty(base)) {
			return NO_REPOS_FOUND;
		}
		File f = new File(base);
		if (f.exists() && f.isDirectory()) {
			return doFindRepos(f);
		}
		return NO_REPOS_FOUND;
	}

	protected abstract File[] doFindRepos(File f);
}
