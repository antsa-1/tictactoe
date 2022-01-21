package com.tauhka.tictactoe.game;

public enum GameToken {
	X("X"), O("O");

	private String token;

	private GameToken(String text) {
		this.token = text;
	}

	public String getAsText() {
		return this.token;
	}

}
