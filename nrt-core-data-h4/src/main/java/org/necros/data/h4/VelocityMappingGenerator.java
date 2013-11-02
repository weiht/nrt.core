package org.necros.data.h4;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.necros.data.MetaClass;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

public class VelocityMappingGenerator
implements MappingGenerator, ApplicationContextAware {
	private String templateFile;
	private File tempFile;
	private String mappingPath;
	private File mappingDir;
	private ApplicationContext appCtx;
	private VelocityEngine engine;
	
	public VelocityMappingGenerator() {
		engine = new VelocityEngine();
		engine.init();
	}

	@Override
	public String generateMapping(MetaClass clazz) throws IOException {
		VelocityContext ctx = new VelocityContext();
		ctx.put("metaClass", clazz);
		Template tpl = engine.getTemplate(getTempFile());
		File mappingFile = makeMappingFileName(clazz);
		if (mappingFile.exists()) mappingFile.delete();
		FileWriter w = new FileWriter(mappingFile);
		try {
			tpl.merge(ctx, w);
		} finally {
			w.close();
		}
		return mappingFile.getAbsolutePath();
	}

	private String getTempFile() throws IOException {
		if (tempFile != null && tempFile.exists()) return tempFile.getAbsolutePath();
		File f = File.createTempFile("tmp", "vm");
		FileWriter w = new FileWriter(f);
		Resource res = appCtx.getResource(templateFile);
		try {
			InputStreamReader r = new InputStreamReader(res.getInputStream());
			try {
				char[] buff = new char[4096];
				int cnt = 0;
				while ((cnt = r.read(buff, 0, buff.length)) > 0) {
					w.write(buff, 0, cnt);
				}
			} finally {
				r.close();
			}
		} finally {
			w.close();
		}
		tempFile = f;
		return f.getAbsolutePath();
	}

	private File makeMappingFileName(MetaClass clazz) throws IOException {
		if (mappingDir == null) {
			mappingDir = new File(mappingPath).getAbsoluteFile();
		}
		String pkg = clazz.getMetaPackage();
		File dir = mappingDir;
		if (pkg != null) {
			String path = pkg.replace('.', File.separatorChar);
			String parent = mappingDir.getAbsolutePath();
			if (!parent.endsWith(File.separator)) parent += File.separator;
			dir = new File(parent + path);
			if (!dir.exists() && !dir.mkdirs() || !dir.isDirectory())
				throw new IOException("Invalid mapping file directory.");
		}
		String dirName = dir.getAbsolutePath();
		if (!dirName.endsWith(File.separator)) dirName += File.separator;
		File f = new File(dirName + clazz.getName());
		return f;
	}

	public void setMappingPath(String mappingPath) {
		this.mappingPath = mappingPath;
	}

	public void setTemplateFile(String templateFile) {
		this.templateFile = templateFile;
	}

	@Override
	public void setApplicationContext(ApplicationContext appicationContext)
			throws BeansException {
		this.appCtx = appicationContext;
	}
}
