package org.necros.dict;

@SuppressWarnings("serial")
public class DictionaryException extends Exception {
	public DictionaryException() {
		super();
	}

	public DictionaryException(String msg) {
		super(msg);
	}

	public DictionaryException(Throwable cause) {
		super(cause);
	}

	public DictionaryException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
