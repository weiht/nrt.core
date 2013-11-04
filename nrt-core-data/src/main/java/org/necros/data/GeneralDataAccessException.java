package org.necros.data;

@SuppressWarnings("serial")
public class GeneralDataAccessException extends Exception {
	public GeneralDataAccessException() {
		super();
	}

	public GeneralDataAccessException(String msg) {
		super(msg);
	}

	public GeneralDataAccessException(Throwable cause) {
		super(cause);
	}

	public GeneralDataAccessException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
