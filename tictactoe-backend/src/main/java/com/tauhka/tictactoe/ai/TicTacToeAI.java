package com.tauhka.tictactoe.ai;

import java.util.List;

import com.tauhka.tictactoe.game.GameResult;
import com.tauhka.tictactoe.game.GameToken;
import com.tauhka.tictactoe.game.Move;
import com.tauhka.tictactoe.game.Table;
import com.tauhka.tictactoe.game.util.MoveEvaluator;
import com.tauhka.tictactoe.game.util.TicTacToeHelper;
import com.tauhka.tictactoe.game.util.WinnerChecker;

public interface TicTacToeAI {

	public default Move calculateBestMove(Table table) {
		Move move = null;
		List<Move> availableMoves = TicTacToeHelper.getAvailableMoves(table);
		move = checkOwnWinningCondition(table, availableMoves);
		if (move != null) {
			return move;
		}
		move = checkOpponentWinningCondition(table, availableMoves);
		if (move != null) {
			return move;
		}

		return MoveEvaluator.calculateMove(table, availableMoves);
	}

	private Move checkOpponentWinningCondition(Table table, List<Move> availableMoves) {
		GameToken tokenToBePlayed = table.getPlayerInTurn().getGameToken();
		// Check opponent token, since opponent is not in turn
		if (tokenToBePlayed == GameToken.O) {
			tokenToBePlayed = GameToken.X;
		} else {
			tokenToBePlayed = GameToken.O;
		}
		for (Move move : availableMoves) {
			GameToken[][] deepCloned = deepCopy(table.getBoard());
			deepCloned[move.getX()][move.getY()] = tokenToBePlayed;
			GameResult result = WinnerChecker.checkWinner(deepCloned, table.getGameMode().getRequiredConnections());
			if (result != null) {
				return move;
			}
		}
		return null;
	}

	private Move checkOwnWinningCondition(Table table, List<Move> availableMoves) {
		GameToken tokenToBePlayed = table.getPlayerInTurn().getGameToken();
		for (Move move : availableMoves) {
			GameToken[][] deepCloned = deepCopy(table.getBoard());
			deepCloned[move.getX()][move.getY()] = tokenToBePlayed;
			GameResult result = WinnerChecker.checkWinner(deepCloned, table.getGameMode().getRequiredConnections());
			if (result != null) {
				return move;
			}
		}
		return null;
	}

	private <T> GameToken[][] deepCopy(GameToken[][] matrix) {
		return java.util.Arrays.stream(matrix).map(el -> el.clone()).toArray(c -> matrix.clone());
	}
}
