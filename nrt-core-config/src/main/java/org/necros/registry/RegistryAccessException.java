package org.necros.registry;

@SuppressWarnings("serial")
public class RegistryAccessException extends Exception {
	public RegistryAccessException() {
	}
	
	public RegistryAccessException(String msg) {
		super(msg);
	}
	
	public RegistryAccessException(Throwable cause) {
		super(cause);
	}
	
	public RegistryAccessException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
