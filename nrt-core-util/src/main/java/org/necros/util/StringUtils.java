/**
 * 
 */
package org.necros.util;

/**
 * @author weiht
 *
 */
public class StringUtils {
	private static final String HEX_STRING = "0123456789ABCDEF";
	private static final char[] HEX_CHARS = HEX_STRING.toCharArray();
	private static final String EMPTY_STR = "";
	private static final String EXT_SEPARATOR = ".";
	
	public static String bytesToString(byte[] src) {
		StringBuilder buff = new StringBuilder();
		for (byte b: src) {
			buff.append(HEX_CHARS[(byte)(b >> 4) & 0x0f]);
			buff.append(HEX_CHARS[b & 0x0f]);
		}
		return buff.toString();
	}
	
	public static byte[] stringToBytes(String src) {
		String str = src.length() % 2 == 0 ? src : ("0" + src);
		byte[] bytes = new byte[str.length() / 2];
		for (int i = 0; i < bytes.length; i ++) {
			byte b;
			int ix;
			ix = HEX_STRING.indexOf(str.charAt(i * 2));
			if (ix < 0) throw new NumberFormatException(src);
			b = (byte) (ix << 4);
			ix = HEX_STRING.indexOf(str.charAt(i * 2 + 1));
			if (ix < 0) throw new NumberFormatException(src);
			b |= ix;
			bytes[i] = b;
		}
		return bytes;
	}

	public static boolean isEmpty(String str) {
		if (str == null) return true;
		if (str.trim().isEmpty()) return true;
		return false;
	}

	/**
	 * Change a file path's extension to the given one.
	 * <br/>
	 * Note that the separator in the file path is expected to slash(/), not depending on the OS.
	 * 
	 * @param name File name.
	 * @param ext New file extension. No dot is added by this method.
	 * @return
	 */
	public static String changeFileExtension(String name, String ext) {
		if (isEmpty(name)) return name;
		if (isEmpty(ext)) return name;
		int ix = name.lastIndexOf('.', name.lastIndexOf('/'));
		if (ix < 1) return name;
		return name.substring(0, ix) + ext;
	}

	public static String extractExtension(String fn) {
		if (isEmpty(fn)) return EMPTY_STR;
		String fname = fn.trim();
		int ix = fname.lastIndexOf(EXT_SEPARATOR);
		if (ix >= 0) return fname.substring(ix);
		return fname;
	}
}