package com.tauhka.tictactoe.tests.win;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.tauhka.tictactoe.game.GameToken;
import com.tauhka.tictactoe.game.util.WinnerChecker;

public class DownRightToUpLeftTests {

	@Test

	public void upLeftDownRightFromTheBeginning() {
		GameToken[][] board = new GameToken[20][20];
		board[19][4] = GameToken.X;
		board[18][3] = GameToken.X;
		board[17][2] = GameToken.X;
		board[16][1] = GameToken.X;
		board[15][0] = GameToken.X;
		// board[14][5] = GameToken.X;

		assertSame(GameToken.X, WinnerChecker.checkWinner(board, 5).getToken());
	}

	@Test
	public void downLeftlast() {
		GameToken[][] board = new GameToken[20][20];
		board[19][19] = GameToken.O;
		board[18][18] = GameToken.O;
		board[17][17] = GameToken.O;
		board[16][16] = GameToken.O;
		board[15][15] = GameToken.O;

		assertSame(GameToken.O, WinnerChecker.checkWinner(board, 5).getToken());
	}

	@Test
	public void last() {
		GameToken[][] board = new GameToken[20][20];
		board[19][19] = GameToken.O;
		board[18][18] = GameToken.O;
		board[17][17] = GameToken.O;
		board[16][16] = GameToken.O;
		board[15][15] = GameToken.O;

		assertSame(GameToken.O, WinnerChecker.checkWinner(board, 5).getToken());
	}

	@Test
	@Disabled
	public void opponentInBetween() {
		GameToken[][] board = new GameToken[20][20];
		board[19][19] = GameToken.O;
		board[18][18] = GameToken.O;
		board[17][17] = GameToken.O;
		board[16][16] = GameToken.O;
		board[15][15] = GameToken.X;
		board[14][14] = GameToken.O;
		board[13][13] = GameToken.O;
		board[12][12] = GameToken.O;
		board[11][11] = GameToken.O;
		board[10][10] = GameToken.O;
		assertSame(GameToken.O, WinnerChecker.checkWinner(board, 5).getToken());
	}

}
