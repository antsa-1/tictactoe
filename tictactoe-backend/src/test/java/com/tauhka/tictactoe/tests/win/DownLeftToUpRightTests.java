package com.tauhka.tictactoe.tests.win;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

import com.tauhka.tictactoe.game.GameToken;
import com.tauhka.tictactoe.game.util.WinnerChecker;

public class DownLeftToUpRightTests {

	@Test
	public void fromBottomLeftToUpRight() {
		GameToken[][] board = new GameToken[20][20];
		board[0][19] = GameToken.O;
		board[1][18] = GameToken.O;
		board[2][17] = GameToken.O;
		board[3][16] = GameToken.O;
		board[4][15] = GameToken.O;

		assertSame(GameToken.O, WinnerChecker.checkWinner(board, 5).getToken());
	}

	@Test
	public void fromBottomLeftToUpRightOpponentInBetween() {
		GameToken[][] board = new GameToken[20][20];
		board[0][19] = GameToken.O;
		board[1][18] = GameToken.O;
		board[2][17] = GameToken.O;
		board[3][16] = GameToken.O;
		board[4][15] = GameToken.X;

		board[5][14] = GameToken.O;
		board[6][13] = GameToken.O;
		board[7][12] = GameToken.O;
		board[8][11] = GameToken.O;
		board[9][10] = GameToken.O;
		assertSame(GameToken.O, WinnerChecker.checkWinner(board, 5).getToken());
	}

	@Test
	public void fromDownLeftToUpRight() {
		GameToken[][] board = new GameToken[20][20];
		board[0][4] = GameToken.O;
		board[1][3] = GameToken.O;
		board[2][2] = GameToken.O;
		board[3][1] = GameToken.O;
		board[4][0] = GameToken.O;

		assertSame(GameToken.O, WinnerChecker.checkWinner(board, 5).getToken());
	}

}
