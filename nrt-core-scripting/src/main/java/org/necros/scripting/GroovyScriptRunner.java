package org.necros.scripting;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import javax.script.ScriptException;

public class GroovyScriptRunner
extends AbstractScriptRunner {

	private static final String GROOVY_FILE_EXTENSION = ".groovy";

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
		GroovyShell shell = new GroovyShell(bindings);
		bindings.setVariable("shell", shell);
		bindings.setVariable("resourceProvider", resourceProvider);
		return shell;
	}

	@Override
	public String getScriptExtensionName() {
		return GROOVY_FILE_EXTENSION;
	}
}
