package org.necros.auth;

public interface AuthenticationService {
	public abstract Login authenticate(String loginName,
			String loginPassword);
	public abstract Login changePassword(String loginName,
			String oldPassword, String newPassword);
}
