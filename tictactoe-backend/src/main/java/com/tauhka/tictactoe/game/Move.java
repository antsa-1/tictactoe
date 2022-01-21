package com.tauhka.tictactoe.game;

import java.math.BigDecimal;
import java.util.Objects;

public class Move {

	private int x;
	private int y;
	private GameToken token;
	private BigDecimal value;//For checking win conditions.

	public Move(int x, int y) {
		super();
		this.x = x;
		this.y = y;

	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public GameToken getToken() {
		return token;
	}

	public void setToken(GameToken token) {
		this.token = token;
	}

	@Override
	public int hashCode() {
		return Objects.hash(token, x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Move other = (Move) obj;
		return token == other.token && x == other.x && y == other.y;
	}

	@Override
	public String toString() {
		return "Move [x=" + x + ", y=" + y + ", token=" + token + ", value=" + value + "]";
	}
}
