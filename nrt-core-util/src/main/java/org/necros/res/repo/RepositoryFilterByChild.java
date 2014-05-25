package org.necros.res.repo;

import java.io.File;
import java.io.FileFilter;

import org.necros.util.StringUtils;

public class RepositoryFilterByChild implements FileFilter {
	private String[] containing;
	
	public RepositoryFilterByChild(String contains) {
		if (StringUtils.isEmpty(contains)) {
			containing = null;
		} else {
			containing = contains.split(",");
		}
	}

	@Override
	public boolean accept(File f) {
		if (!f.isDirectory()) return false;
		if (containing == null || containing.length == 0) return true;
		for (String con: containing) {
			File c = new File(f, con);
			if (c.exists() && c.isDirectory()) return true;
		}
		return false;
	}
}
