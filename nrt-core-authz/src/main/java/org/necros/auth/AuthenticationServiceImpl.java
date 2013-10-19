package org.necros.auth;

import org.springframework.util.StringUtils;

public class AuthenticationServiceImpl
implements AuthenticationService {
	private LoginManager loginManager;
	private PasswordEncoder passwordEncoder;

	@Override
	public Login authenticate(String loginName, String loginPassword) {
		if (!StringUtils.hasText(loginName) || !StringUtils.hasText(loginName))
			return null;
		Login login = loginManager.getWithName(loginName);
		if (login == null) return null;
		String cipher = passwordEncoder == null
				? loginPassword
				: passwordEncoder.encode(loginPassword, loginName, login);
		if (cipher.equals(login.getPassword())) return login;
		return null;
	}

	@Override
	public Login changePassword(String loginName, String oldPassword,
			String newPassword) {
		Login login = loginManager.getWithName(loginName);
		if (login == null) return null;
		String cipher = passwordEncoder == null
				? oldPassword
				: passwordEncoder.encode(oldPassword, loginName, login);
		if (cipher.equals(login.getPassword())) {
			cipher = passwordEncoder == null
					? newPassword
					: passwordEncoder.encode(newPassword, loginName, login);
			login.setPassword(cipher);
			return login;
		}
		return null;
	}

	public void setLoginManager(LoginManager loginManager) {
		this.loginManager = loginManager;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
}
