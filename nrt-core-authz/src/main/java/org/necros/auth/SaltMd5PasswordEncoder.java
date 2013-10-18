/**
 * 
 */
package org.necros.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.necros.util.StringUtils;

/**
 * @author weiht
 *
 */
public class SaltMd5PasswordEncoder implements PasswordEncoder {
	private MessageDigest getMd5() {
		try {
			return MessageDigest.getInstance("md5");
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
	
	public String encode(String passwd, String login, Object salt) {
		String saltStr = login;
		if (salt instanceof Login) {
			Login l = (Login)salt;
			saltStr = l.getId();
		}
		String toEncode = passwd + login + saltStr;
		MessageDigest md5 = getMd5();
		byte[] d = md5.digest(toEncode.getBytes());
		return StringUtils.bytesToString(d);
	}
}