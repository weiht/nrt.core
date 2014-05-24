package org.necros.scripting;

import java.io.IOException;
import java.util.Map;

import javax.script.ScriptException;

public interface ScriptRunner {
	public Map<String, Object> runScript(String script, Map<String, Object> extraContext)
			throws ScriptException, IOException;
}
