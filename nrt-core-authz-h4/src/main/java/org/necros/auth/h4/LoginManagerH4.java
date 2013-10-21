package org.necros.auth.h4;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.necros.auth.AuthException;
import org.necros.auth.Login;
import org.necros.auth.LoginManager;
import org.necros.auth.PasswordGenerator;
import org.necros.auth.PasswordEncoder;
import org.necros.data.IdGenerator;
import org.necros.data.UsableStatuses;
import org.necros.data.h4.SessionFactoryHelper;
import org.springframework.util.StringUtils;

public class LoginManagerH4 implements LoginManager {
	private static final Class<Login> CLAZZ = Login.class;
	private SessionFactory sessionFactory;
	private SessionFactoryHelper helper;
	private IdGenerator idGenerator;
	private PasswordGenerator passwordGenerator;
	private PasswordEncoder passwordEncoder;

	@Override
	public Login get(String id) {
		return (Login)helper.getSession().get(CLAZZ, id);
	}

	@Override
	public Login getWithName(String loginName) {
		return (Login)helper.createCriteria(CLAZZ)
				.add(Restrictions.eq("loginName", loginName))
				.uniqueResult();
	}

	@Override
	public Login add(Login login) throws AuthException {
		Login orig = getWithName(login.getLoginName());
		if (orig != null) throw new AuthException("Duplicated loginName.");
		login.setId((String) idGenerator.generate());
		String pwd = login.getPassword();
		if (StringUtils.hasText(pwd) && passwordEncoder != null) {
			login.setPassword(passwordEncoder.encode(pwd, login.getLoginName(), login));
		}
		helper.getSession().save(login);
		return login;
	}

	@Override
	public Login remove(String id) {
		Login orig = (Login) helper.getSession().get(CLAZZ, id);
		if (orig == null) return null;
		
		helper.getSession().delete(orig);
		return orig;
	}

	@Override
	public Login enable(String id) {
		Login orig = get(id);
		if (orig == null) return null;
		
		orig.setStatus(UsableStatuses.DISABLED);
		helper.getSession().update(orig);
		return orig;
	}

	@Override
	public Login disable(String id) {
		Login orig = get(id);
		if (orig == null) return null;
		
		orig.setStatus(null);
		helper.getSession().update(orig);
		return orig;
	}

	@Override
	public String resetPassword(String loginName) {
		Login orig = getWithName(loginName);
		if (orig == null) return null;
		
		String passwd = passwordGenerator.generate();
		if (passwordEncoder != null) {
			passwd = passwordEncoder.encode(passwd, loginName, orig);
		}
		orig.setPassword(passwd);
		helper.getSession().update(orig);
		return passwd;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.helper = SessionFactoryHelper.getInstance(sessionFactory);
	}

	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	public void setPasswordGenerator(PasswordGenerator passwordGenerator) {
		this.passwordGenerator = passwordGenerator;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
}
