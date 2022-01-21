package com.tauhka.tictactoe.game;

import jakarta.json.bind.annotation.JsonbProperty;

public class GameResult {
	@JsonbProperty("fromX") // WinRow starts coordinates
	private int fromX;
	@JsonbProperty("fromY")
	private int fromY;
	@JsonbProperty("toX") // WinRow ends coordinates
	private int toX;
	@JsonbProperty("toY")
	private int toY;
	@JsonbProperty("token")
	private GameToken token;

	@JsonbProperty("player")
	private User player;

	@JsonbProperty("draw")
	private boolean draw;

	public int getFromX() {
		return fromX;
	}

	public int getFromY() {
		return fromY;
	}

	public int getToX() {
		return toX;
	}

	public int getToY() {
		return toY;
	}

	public GameToken getToken() {
		return token;
	}

	public void setFromX(int fromX) {
		this.fromX = fromX;
	}

	public void setFromY(int fromY) {
		this.fromY = fromY;
	}

	public User getPlayer() {
		return player;
	}

	public Boolean getDraw() {
		return draw;
	}

	public void setDraw(Boolean draw) {
		this.draw = draw;
	}

	public void setPlayer(User player) {
		this.player = player;
	}

	public void setToX(int toX) {
		this.toX = toX;
	}

	public void setToY(int toY) {
		this.toY = toY;
	}

	public void setToken(GameToken token) {
		this.token = token;
	}

}
