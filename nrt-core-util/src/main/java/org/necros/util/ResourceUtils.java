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
	private static final int DEFAULT_BUFFER_SIZE = 4096;
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
	
	public static int transfer(InputStream ins, OutputStream outs) throws IOException {
		return transfer(ins, outs, DEFAULT_BUFFER_SIZE);
	}
	
	public static int transfer(InputStream ins, OutputStream outs, int buffSize) throws IOException {
		int b = buffSize;
		if (b < DEFAULT_BUFFER_SIZE) b = DEFAULT_BUFFER_SIZE;
		byte[] buff = new byte[b];
		int total = 0, r;
		while ((r = ins.read(buff)) >= 0) {
			if (r > 0) {
				total += r;
				outs.write(buff, 0, r);
			}
		}
		outs.flush();
		return total;
	}
}
