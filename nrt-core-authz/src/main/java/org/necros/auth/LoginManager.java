package org.necros.auth;

public interface LoginManager {
	public abstract Login get(String id);
	public abstract Login getWithName(String loginName);
	public abstract Login add(Login login);
	public abstract Login remove(String id);
	public abstract Login enable(String id);
	public abstract Login disable(String id);
	public abstract String resetPassword(String loginName);
}
