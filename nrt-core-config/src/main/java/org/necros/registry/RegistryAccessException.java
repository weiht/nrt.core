package org.necros.registry;

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
