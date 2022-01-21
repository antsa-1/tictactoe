package com.tauhka.tictactoe.web;

public class CloseWebSocketException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String message;

	public CloseWebSocketException(String message) {
		super();
		this.message = message;
	}

	@Override
	public String toString() {
		return "CloseWebSocketExcpetion [message=" + message + "]";
	}

}
