package org.necros.res.repo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.necros.util.StringUtils;

public class ChildResourceLocator
extends AbstractFileRepositoryLocator {
	private static final String SEPARATOR = ",";
	
	private String[] resourcePaths;
	
	@Override
	protected File[] doFindRepos(File f) {
		if (resourcePaths == null || resourcePaths.length == 0) return new File[]{f};
		List<File> lst = new ArrayList<File>();
		for (String path: resourcePaths) {
			File p = new File(f, path);
			if (p.exists() && p.isDirectory()) {
				lst.add(p);
			}
		}
		return lst.toArray(new File[]{});
	}

	public void setResourcePaths(String paths) {
		if (StringUtils.isEmpty(paths)) {
			resourcePaths = null;
		} else {
			resourcePaths = paths.split(SEPARATOR);
		}
	}
}
