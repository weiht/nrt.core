package org.necros.data;

@SuppressWarnings("serial")
public class MetaDataAccessException extends Exception {
	public MetaDataAccessException() {
		super();
	}

	public MetaDataAccessException(String msg) {
		super(msg);
	}

	public MetaDataAccessException(Throwable cause) {
		super(cause);
	}

	public MetaDataAccessException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
