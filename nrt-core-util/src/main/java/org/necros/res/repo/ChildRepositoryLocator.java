package org.necros.res.repo;

import java.io.File;
import java.io.FileFilter;

public class ChildRepositoryLocator
extends AbstractFileRepositoryLocator {
	private FileFilter filter;
	
	@Override
	protected File[] doFindRepos(File f) {
		return f.listFiles(filter);
	}

	public void setContains(String contains) {
		filter = new RepositoryFilterByChild(contains);
	}
}
