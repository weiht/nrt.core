package org.necros.res.repo;

import java.io.File;

public interface FileRepositoryLocator {
	/**
	 * Find repositories within the specified base path.
	 * @param base Base path
	 * @return Repositories found, or empty list if not found.
	 */
	public abstract File[] findRepos(String base);
}
