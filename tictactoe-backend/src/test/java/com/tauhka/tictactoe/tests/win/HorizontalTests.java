package com.tauhka.tictactoe.tests.win;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.tauhka.tictactoe.game.GameToken;
import com.tauhka.tictactoe.game.util.WinnerChecker;

class HorizontalTests {

	@Test
	void fromTheBegginingAllImmediate() {

		GameToken[][] board = new GameToken[10][10];

		board[0][0] = GameToken.X;
		board[0][1] = GameToken.X;
		board[0][2] = GameToken.X;
		board[0][3] = GameToken.X;
		board[0][4] = GameToken.X;
		board[0][5] = GameToken.X;
		board[0][0] = GameToken.X;
		board[0][0] = GameToken.X;
		assertEquals(GameToken.X, WinnerChecker.checkWinner(board, 5).getToken());

	}

	@Test
	void startWithDifferentToken() {
		GameToken[][] board2 = new GameToken[10][10];
		board2[0][0] = GameToken.O;
		board2[0][1] = GameToken.X;
		board2[0][2] = GameToken.X;
		board2[0][3] = GameToken.X;
		board2[0][4] = GameToken.X;
		board2[0][5] = GameToken.X;
		assertEquals(GameToken.X, WinnerChecker.checkWinner(board2, 5).getToken());
	}

	@Test
	void startWithNullToken() {
		GameToken[][] board2 = new GameToken[10][10];
		board2[0][0] = null;
		board2[0][1] = GameToken.X;
		board2[0][2] = GameToken.X;
		board2[0][3] = GameToken.X;
		board2[0][4] = GameToken.X;
		board2[0][5] = GameToken.X;
		assertEquals(GameToken.X, WinnerChecker.checkWinner(board2, 5).getToken());
	}

	@Test
	void inBetweenNullAndOtherToken() {
		GameToken[][] board2 = new GameToken[10][10];
		board2[0][0] = GameToken.X;
		board2[0][1] = null;
		board2[0][2] = GameToken.O;
		board2[0][3] = GameToken.X;
		board2[0][4] = GameToken.X;
		board2[0][5] = GameToken.X;
		board2[0][6] = GameToken.X;
		board2[0][7] = GameToken.X;
		board2[0][8] = GameToken.O;
		assertEquals(GameToken.X, WinnerChecker.checkWinner(board2, 5).getToken());
	}

	@Test
	void last5InRow() {
		GameToken[][] board2 = new GameToken[10][10];
		board2[0][0] = GameToken.X;
		board2[0][1] = null;
		board2[0][2] = GameToken.O;
		board2[0][3] = null;
		board2[0][5] = GameToken.X;
		board2[0][6] = GameToken.X;
		board2[0][7] = GameToken.X;
		board2[0][8] = GameToken.X;
		board2[0][9] = GameToken.X;
		assertEquals(GameToken.X, WinnerChecker.checkWinner(board2, 5).getToken());
	}

	@Test
	void last5InLastRow() {
		GameToken[][] board2 = new GameToken[10][10];
		board2[3][0] = GameToken.X;
		board2[2][1] = null;
		board2[1][2] = GameToken.O;
		board2[0][3] = null;
		board2[9][5] = GameToken.O;
		board2[9][6] = GameToken.O;
		board2[9][7] = GameToken.O;
		board2[9][8] = GameToken.O;
		board2[9][9] = GameToken.O;
		assertEquals(GameToken.O, WinnerChecker.checkWinner(board2, 5).getToken());
	}
}
