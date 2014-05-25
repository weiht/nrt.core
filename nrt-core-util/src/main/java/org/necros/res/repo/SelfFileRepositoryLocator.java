package org.necros.res.repo;

import java.io.File;

public class SelfFileRepositoryLocator
extends AbstractFileRepositoryLocator {
	@Override
	protected File[] doFindRepos(File f) {
		return new File[]{f};
	}
}
