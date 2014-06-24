package org.necros.scripting;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptException;

import org.necros.res.ResourceProvider;
import org.necros.util.ResourceUtils;
import org.necros.util.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public abstract class AbstractScriptRunner
implements ScriptRunner, ApplicationContextAware {
	public static final String KEY_APPLICATION_CONTEXT = "applicationContext";
	
	protected ResourceProvider resourceProvider;
	protected ApplicationContext applicationContext;
	private Map<String, Object> rootBindings;

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
		if (extraContext == null) {
			extraContext = new HashMap<String, Object>(ensureRootBindings());
		} else {
			extraContext.putAll(ensureRootBindings());
		}
		try {
			return doRunScript(ins, extraContext);
		} finally {
			ResourceUtils.closeStream(ins);
		}
	}

	private Map<String, Object> ensureRootBindings() {
		if (rootBindings == null) {
			rootBindings = new HashMap<String, Object>();
			rootBindings.put(KEY_APPLICATION_CONTEXT, applicationContext);
			//TODO Do some extra initialization
		}
		return rootBindings;
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
	
	@Override
	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		this.applicationContext = ctx;
	}
}
