package org.necros.auth;

import java.util.Map;

public class MockLoginManager
implements LoginManager {
	private Map<String, Login> logins;
	private PasswordEncoder passwordEncoder;
	
	public void init() {
		if (logins != null) {
			for (Login l: logins.values()) {
				l.setPassword(encode(l));
			}
		}
	}

	@Override
	public Login get(String id) {
		throw new RuntimeException("Not implemented.");
	}

	@Override
	public Login getWithName(String loginName) {
		if (logins == null) return null;
		Login l = logins.get(loginName);
		return l;
	}

	private String encode(Login login) {
		return passwordEncoder.encode(
				login.getPassword(), login.getLoginName(), login
			);
	}

	@Override
	public Login add(Login login) {
		throw new RuntimeException("Not implemented.");
	}

	@Override
	public Login remove(String id) {
		throw new RuntimeException("Not implemented.");
	}

	@Override
	public Login enable(String id) {
		throw new RuntimeException("Not implemented.");
	}

	@Override
	public Login disable(String id) {
		throw new RuntimeException("Not implemented.");
	}

	@Override
	public String resetPassword(String loginName) {
		throw new RuntimeException("Not implemented.");
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public void setLogins(Map<String, Login> logins) {
		this.logins = logins;
	}
}
