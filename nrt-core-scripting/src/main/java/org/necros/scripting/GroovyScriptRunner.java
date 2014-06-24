package org.necros.scripting;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.script.ScriptException;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.necros.res.repo.RepositoryLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GroovyScriptRunner
extends AbstractScriptRunner {
	private static final Logger logger = LoggerFactory.getLogger(GroovyScriptRunner.class);

	private static final String GROOVY_FILE_EXTENSION = ".groovy";
	
	private RepositoryLocator repositoryLocator;
	private List<String> classpaths;

	@Override
	protected Map<String, Object> doRunScript(InputStream ins,
			Map<String, Object> extraContext) throws ScriptException,
			IOException {
		GroovyShell shell = getShell(extraContext);
		shell.evaluate(new InputStreamReader(ins));
		return getShellResult(shell);
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getShellResult(GroovyShell shell) {
		return shell.getContext().getVariables();
	}

	private GroovyShell getShell(Map<String, Object> extraContext) {
		Binding bindings = new Binding(extraContext);
		CompilerConfiguration cconf = new CompilerConfiguration();
		cconf.setClasspathList(ensureClasspaths());
		GroovyShell shell = new GroovyShell(bindings, cconf);
		bindings.setVariable("shell", shell);
		bindings.setVariable("resourceProvider", resourceProvider);
		return shell;
	}

	private List<String> ensureClasspaths() {
		if (classpaths == null) {
			classpaths = new ArrayList<String>();
			for (File f: repositoryLocator.getRepositories()) {
				classpaths.add(f.getAbsolutePath());
			}
			logger.trace("Groovy script classpaths: {}", classpaths);
		}
		return classpaths;
	}

	@Override
	public String getScriptExtensionName() {
		return GROOVY_FILE_EXTENSION;
	}

	public void setRepositoryLocator(RepositoryLocator repositoryLocator) {
		this.repositoryLocator = repositoryLocator;
	}
}
