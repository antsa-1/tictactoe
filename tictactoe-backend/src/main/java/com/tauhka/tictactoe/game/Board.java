package com.tauhka.tictactoe.game;

import java.io.Serializable;

public class Board implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum SIGN {
		X, O
	};

	private SIGN[][] board;

	public Board(int x, int y) {
		super();
		this.board = new SIGN[x][y];
	}

	public void add(int x, int y, SIGN sign) {
		this.board[x][y] = sign;
	}

}
