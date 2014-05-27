package org.necros.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class MimeUtils {
	private Map<String, String> types = new HashMap<String, String>();

	public boolean isFileBinary(String fileName) {
		String t = getType(fileName);
		return isTypeBinary(t);
	}

	public boolean isTypeBinary(String t) {
		if (StringUtils.isEmpty(t)) return true;

		//TODO More properties to identify. Possibly use a list or map.
		if (t.indexOf("text") >= 0) return false;
		if (t.indexOf("js") >= 0) return false;
		if (t.indexOf("script") >= 0) return false;
		if (t.indexOf("css") >= 0) return false;

		return true;
	}

	public String getType(String fileName) {
		if (StringUtils.isEmpty(fileName)) return null;
		String ext = StringUtils.extractExtension(fileName);
		String t = types.get(ext);
		if (StringUtils.isEmpty(t)) {
			t = URLConnection.guessContentTypeFromName(fileName);
		}
		if (StringUtils.isEmpty(t)) {
			InputStream ins = ResourceUtils.loadResource(t);
			if (ins != null) {
				try {
					t = URLConnection.guessContentTypeFromStream(ins);
				} catch (IOException e) {
					//
				} finally {
					ResourceUtils.closeStream(ins);
				}
			}
		}
		return t;
	}

	public String getTypeDef(String fileName, String def) {
		String t = getType(fileName);
		return StringUtils.isEmpty(t) ? def : t;
	}

	public void addTypes(Map<String, String> types) {
		if (types != null && !types.isEmpty()) {
			this.types.putAll(types);
		}
	}
}
