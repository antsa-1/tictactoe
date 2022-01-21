package com.tauhka.tictactoe.tests.win;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.tauhka.tictactoe.game.GameToken;
import com.tauhka.tictactoe.game.util.WinnerChecker;

public class VerticalTests {

	@Test
	public void fromTheBeginning() {
		GameToken[][] board = new GameToken[10][10];
		board[0][0] = GameToken.X;
		board[1][0] = GameToken.X;
		board[2][0] = GameToken.X;
		board[3][0] = GameToken.X;
		board[4][0] = GameToken.X;
		board[5][0] = GameToken.X;
		board[0][0] = GameToken.X;
		board[0][0] = GameToken.X;
		assertEquals(GameToken.X, WinnerChecker.checkWinner(board, 5).getToken());
	}

	@Test
	public void last() {
		GameToken[][] board = new GameToken[10][10];
		board[0][9] = GameToken.O;
		board[1][9] = GameToken.O;
		board[2][9] = GameToken.O;
		board[3][9] = GameToken.O;
		board[5][9] = GameToken.X;
		board[6][9] = GameToken.X;
		board[7][9] = GameToken.X;
		board[8][9] = GameToken.X;
		board[9][9] = GameToken.X;
		assertEquals(GameToken.X, WinnerChecker.checkWinner(board, 5).getToken());
	}

	@Test
	public void vertical3x3() {
		GameToken[][] board = new GameToken[3][3];
		board[0][2] = GameToken.X;
		board[1][1] = GameToken.O;
		board[2][0] = GameToken.X;
		board[2][1] = GameToken.O;
		board[0][1] = GameToken.X;

		board[0][0] = GameToken.O;
		board[2][2] = GameToken.X;
		board[1][0] = GameToken.O;

		assertNull(WinnerChecker.checkWinner(board, 3));
	}

}
