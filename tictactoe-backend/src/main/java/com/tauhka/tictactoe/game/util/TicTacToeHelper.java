package com.tauhka.tictactoe.game.util;

import java.util.ArrayList;
import java.util.List;

import com.tauhka.tictactoe.game.Move;
import com.tauhka.tictactoe.game.Table;

public class TicTacToeHelper {
	//Get empty positions for board
	public static List<Move> getAvailableMoves(Table table) {
		List<Move> availableMoves = new ArrayList<Move>();
		for (int x = 0; x < table.getX(); x++) {
			for (int y = 0; y < table.getY(); y++) {
				if (table.getBoard()[x][y] == null) {
					availableMoves.add(new Move(x, y));
				}
			}
		}
		return availableMoves;
	}
}
