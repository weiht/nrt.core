package org.necros.scripting;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.script.ScriptException;

import org.necros.res.ResourceProvider;
import org.necros.util.ResourceUtils;
import org.necros.util.StringUtils;

public abstract class AbstractScriptRunner
implements ScriptRunner {
	protected ResourceProvider resourceProvider;

	@Override
	public Map<String, Object> runScript(String script,
			Map<String, Object> extraContext) throws ScriptException,
			IOException {
		if (StringUtils.isEmpty(script)) {
			throw new IOException("No script specified.");
		}
		if (resourceProvider == null) return null;
		String name = scriptName(script);
		InputStream ins = resourceProvider.read(name);
		if (ins == null) return extraContext;
		try {
			return doRunScript(ins, extraContext);
		} finally {
			ResourceUtils.closeStream(ins);
		}
	}

	private String scriptName(String script) {
		String ext = getScriptExtensionName();
		if (StringUtils.isEmpty(ext)) {
			return script;
		}
		return StringUtils.changeFileExtension(script, ext);
	}

	protected abstract Map<String, Object> doRunScript(InputStream ins,
			Map<String, Object> extraContext)
					throws ScriptException, IOException;
	
	public abstract String getScriptExtensionName();

	public void setResourceProvider(ResourceProvider resourceProvider) {
		this.resourceProvider = resourceProvider;
	}
}
