package com.tauhka.portal.web.exeptions;

public class PasswordNotValidException extends RuntimeException {
	private String message;

	public PasswordNotValidException(String string) {
		this.message = string;
	}

	private static final long serialVersionUID = 1L;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "PasswordNotValidException [message=" + message + "]";
	}

}
