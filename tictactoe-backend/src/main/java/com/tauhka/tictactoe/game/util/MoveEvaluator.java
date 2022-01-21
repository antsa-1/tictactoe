package com.tauhka.tictactoe.game.util;

import java.math.BigDecimal;
import java.util.List;

import com.tauhka.tictactoe.game.GameToken;
import com.tauhka.tictactoe.game.Move;
import com.tauhka.tictactoe.game.Table;

//Just some testing for calculating computer best move.
public class MoveEvaluator {
	private static final BigDecimal CONNECTING_VALUE_WEIGHT = new BigDecimal("1.0");
	private static final BigDecimal DISTANCE_FROM_CENTER_WEIGHT = new BigDecimal("0.05");
	private static final BigDecimal LEFT_BETTER_WEIGHT = new BigDecimal("0.07");
	private static final BigDecimal RIGHT_BETTER_WEIGHT = new BigDecimal("0.07");;
	private static final BigDecimal TOKEN_IN_MIDDLE_WEIGHT = new BigDecimal("0.5");
	private static final BigDecimal DEAD_END = new BigDecimal("-1000");

	public static Move calculateMove(Table table, List<Move> availableMoves) {
		GameToken board[][] = table.getBoard();
		int requiredConnections = table.getGameMode().getRequiredConnections();
		GameToken tokenToBePlayed = table.getPlayerInTurn().getGameToken();

		Move bestMove = null;
		Move centerPoint = new Move((int) Math.ceil(table.getBoard().length / 2),
				(int) Math.ceil(table.getBoard().length / 2));
		for (Move move : availableMoves) {
			board[move.getX()][move.getY()] = tokenToBePlayed;
			MoveEvaluator.checkHorizontals(board, move, requiredConnections, tokenToBePlayed, centerPoint);
			if (bestMove == null || move.getValue().compareTo(bestMove.getValue()) > 0) {
				// System.err.println("UUS:" + move);
				bestMove = move;
			}
			board[move.getX()][move.getY()] = null;
			MoveEvaluator.checkVerticals(board, move, requiredConnections, tokenToBePlayed, centerPoint);
			if (bestMove == null || move.getValue().compareTo(bestMove.getValue()) > 0) {
				bestMove = move;
				// System.err.println("UUS22:" + move);
			}
			board[move.getX()][move.getY()] = null;
		}
		// System.out.println("BEST MOVE:" + bestMove);
		return bestMove;
	}

	private static void checkHorizontals(GameToken[][] board, Move move, int requiredAmount, GameToken followedToken,
			Move centerPoint) {

		int startingPointX = move.getX();
		int startingPointY = move.getY();
		GameToken oppositeToken = followedToken == GameToken.X ? GameToken.O : GameToken.X;
		int totalDirectConnections = 0;
		int directConnectionsOnRightSide = 0;
		int directConnectionsOnLeftSide = 0;
		int blanksOnRightSide = 0;
		int blanksOnLeftSide = 0;
		int possibleConnectionsOnRightSide = 0;
		int possibleConnectionsOnLeftSide = 0;

		boolean directConnectionsCountContinues = true;
		// To right from point
		for (int x = move.getX() + 1; x < board.length; x++) {
			if (board[x][startingPointY] == followedToken) {
				if (directConnectionsCountContinues) {
					totalDirectConnections++;
					directConnectionsOnRightSide++;
				}
				possibleConnectionsOnRightSide++;
			} else if (board[x][startingPointY] == null) {
				blanksOnRightSide++;
				directConnectionsCountContinues = false;
				possibleConnectionsOnRightSide++;
			} else {
				break;
			}
		}
		directConnectionsCountContinues = true;
		// To left from point
		for (int x = move.getX() - 1; x >= 0; x--) {
			if (board[x][startingPointY] == followedToken) {
				if (directConnectionsCountContinues) {
					totalDirectConnections++;
					directConnectionsOnLeftSide++;
				}
				possibleConnectionsOnLeftSide++;
			} else if (board[x][startingPointY] == null) {
				blanksOnLeftSide++;
				directConnectionsCountContinues = false;
				possibleConnectionsOnLeftSide++;
			} else {
				break;
			}
		}
		if (totalDirectConnections + possibleConnectionsOnLeftSide + possibleConnectionsOnRightSide < requiredAmount) {
			move.setValue(DEAD_END);

			return;
		}

		double leftSideEval = (totalDirectConnections + directConnectionsOnLeftSide)
				* CONNECTING_VALUE_WEIGHT.doubleValue()
				- (Math.abs((startingPointX - centerPoint.getX()) * DISTANCE_FROM_CENTER_WEIGHT.doubleValue()))
				- (Math.abs(startingPointY - centerPoint.getY() * DISTANCE_FROM_CENTER_WEIGHT.doubleValue()));
		double rightSideEval = (totalDirectConnections + directConnectionsOnRightSide)
				* CONNECTING_VALUE_WEIGHT.doubleValue()
				- (Math.abs((startingPointX - centerPoint.getX()) * DISTANCE_FROM_CENTER_WEIGHT.doubleValue()))
				- (Math.abs((startingPointY - centerPoint.getY()) * DISTANCE_FROM_CENTER_WEIGHT.doubleValue()));
		if (directConnectionsOnLeftSide == directConnectionsOnRightSide && directConnectionsOnLeftSide > 0) {
			leftSideEval += TOKEN_IN_MIDDLE_WEIGHT.doubleValue();
			rightSideEval += TOKEN_IN_MIDDLE_WEIGHT.doubleValue();
		}
		if (directConnectionsOnLeftSide > directConnectionsOnRightSide) {
			leftSideEval += LEFT_BETTER_WEIGHT.doubleValue();
		} else {
			rightSideEval += RIGHT_BETTER_WEIGHT.doubleValue();
		}
		move.setValue(new BigDecimal(leftSideEval > rightSideEval ? leftSideEval : rightSideEval));

	}

	private static void checkVerticals(GameToken[][] board, Move move, int requiredAmount, GameToken followedToken,
			Move centerPoint) {
		int startingPointX = move.getX();
		int startingPointY = move.getY();
		GameToken oppositeToken = followedToken == GameToken.X ? GameToken.O : GameToken.X;
		int totalDirectConnections = 0;
		int directConnectionsDown = 0;
		int directConnectionsUp = 0;
		int blanksOnRightSide = 0;
		int blanksOnLeftSide = 0;
		int possbileConnectionDown = 0;
		int possibleConnectionsUp = 0;

		boolean directConnectionsCountContinues = true;
		// To right from point
		for (int y = move.getY() + 1; y < board.length; y++) {
			if (board[startingPointX][y] == followedToken) {
				if (directConnectionsCountContinues) {
					totalDirectConnections++;
					directConnectionsDown++;
				}
				possbileConnectionDown++;
			} else if (board[startingPointX][y] == null) {
				blanksOnRightSide++;
				directConnectionsCountContinues = false;
				possbileConnectionDown++;
			} else {
				break;
			}
		}
		directConnectionsCountContinues = true;
		// To left from point
		for (int y = move.getY() - 1; y >= 0; y--) {
			if (board[startingPointX][y] == followedToken) {
				if (directConnectionsCountContinues) {
					totalDirectConnections++;
					directConnectionsUp++;
				}
				possibleConnectionsUp++;
			} else if (board[startingPointX][y] == null) {
				blanksOnLeftSide++;
				directConnectionsCountContinues = false;
				possibleConnectionsUp++;
			} else {
				break;
			}
		}
		if (totalDirectConnections + possibleConnectionsUp + possbileConnectionDown < requiredAmount) {
			move.setValue(DEAD_END);

			return;
		}

		double upEval = (totalDirectConnections + directConnectionsUp) * CONNECTING_VALUE_WEIGHT.doubleValue()
				- (Math.abs((startingPointX - centerPoint.getX()) * DISTANCE_FROM_CENTER_WEIGHT.doubleValue()))
				- (Math.abs(startingPointY - centerPoint.getY() * DISTANCE_FROM_CENTER_WEIGHT.doubleValue()));
		double downEval = (totalDirectConnections + directConnectionsDown) * CONNECTING_VALUE_WEIGHT.doubleValue()
				- (Math.abs((startingPointX - centerPoint.getX()) * DISTANCE_FROM_CENTER_WEIGHT.doubleValue()))
				- (Math.abs((startingPointY - centerPoint.getY()) * DISTANCE_FROM_CENTER_WEIGHT.doubleValue()));
		if (directConnectionsUp == directConnectionsDown && directConnectionsUp > 0) {
			upEval += TOKEN_IN_MIDDLE_WEIGHT.doubleValue();
			downEval += TOKEN_IN_MIDDLE_WEIGHT.doubleValue();
		}
		if (directConnectionsUp > directConnectionsDown) {
			upEval += LEFT_BETTER_WEIGHT.doubleValue();
		} else {
			downEval += RIGHT_BETTER_WEIGHT.doubleValue();
		}
		move.setValue(new BigDecimal(upEval > downEval ? upEval : downEval));

	}

}
