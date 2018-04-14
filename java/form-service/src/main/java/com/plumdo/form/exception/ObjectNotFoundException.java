package com.plumdo.form.exception;

public class ObjectNotFoundException extends BaseException {

	private static final long serialVersionUID = 1L;
	private Class<?> objectClass;

	public ObjectNotFoundException(String message) {
		super(message);
	}

	public ObjectNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ObjectNotFoundException(String message, Class<?> objectClass) {
		this(message, objectClass, null);
	}

	public ObjectNotFoundException(Class<?> objectClass) {
		this(null, objectClass, null);
	}

	public ObjectNotFoundException(String message, Class<?> objectClass, Throwable cause) {
		super(message, cause);
		this.objectClass = objectClass;
	}

	public Class<?> getObjectClass() {
		return objectClass;
	}
}
