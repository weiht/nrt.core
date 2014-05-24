package org.necros.scripting;

import java.io.IOException;
import java.util.Map;

import javax.script.ScriptException;

import org.necros.res.ResourceProvider;

public abstract class AbstractScriptRunner
implements ScriptRunner {
	protected ResourceProvider resourceProvider;

	@Override
	public Map<String, Object> runScript(String script,
			Map<String, Object> extraContext) throws ScriptException,
			IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setResourceProvider(ResourceProvider resourceProvider) {
		this.resourceProvider = resourceProvider;
	}
}
