package org.necros.settings;

public class PreferenceException extends Exception {
	public PreferenceException() {
		super();
	}

	public PreferenceException(String msg) {
		super(msg);
	}

	public PreferenceException(Throwable cause) {
		super(cause);
	}

	public PreferenceException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
