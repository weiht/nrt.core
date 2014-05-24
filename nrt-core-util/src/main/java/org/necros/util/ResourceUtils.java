package org.necros.util;

import java.io.InputStream;
import java.io.OutputStream;

public class ResourceUtils {

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
