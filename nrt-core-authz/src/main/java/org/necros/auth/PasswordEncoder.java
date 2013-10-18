/**
 * 
 */
package org.necros.auth;

/**
 * @author weiht
 *
 */
public interface PasswordEncoder {
	public abstract String encode(String passwd, String login, Object salt);
}