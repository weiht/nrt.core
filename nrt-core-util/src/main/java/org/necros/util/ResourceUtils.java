package org.necros.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class ResourceUtils {
	private static final Logger logger = LoggerFactory.getLogger(ResourceUtils.class);
    private static ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
	
	public static InputStream loadResource(String resourceName) {
		logger.debug("Finding resource: {}", resourceName);
		if (StringUtils.isEmpty(resourceName)) {
			logger.warn("Empty resource name.");
			return null;
		}

		Resource r = resolver.getResource(resourceName);
		if (r.exists()) {
			try {
				return r.getInputStream();
			} catch (IOException ex) {
				logger.warn("Error loading resource stream: {}.", resourceName, ex);
				return null;
			}
		} else {
			logger.warn("No resource found for name: {}", resourceName);
			return null;
		}
	}

	public static void closeStream(InputStream ins) {
		if (ins != null) {
			try {
				ins.close();
			} catch (Exception ex) {
				//
			}
		}
	}

	public static void closeStream(OutputStream outs) {
		if (outs != null) {
			try {
				outs.close();
			} catch (Exception ex) {
				//
			}
		}
	}
}
