package com.tauhka.tictactoe.game.util;

import com.tauhka.tictactoe.game.GameToken;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.bind.adapter.JsonbAdapter;

//Converta board to JSON
public class TicTacToeBoardAdapter implements JsonbAdapter<GameToken[][], JsonArray> {
	private static JsonArrayBuilder arrBuilder = Json.createArrayBuilder(); // should be thread-safe!?
	private static JsonObjectBuilder objBuilder = Json.createObjectBuilder(); // should be thread-safe!?

	@Override
	public JsonArray adaptToJson(GameToken[][] board) throws Exception {

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j] != null) {
					JsonObject ob = objBuilder.add("coordinates", Integer.toString(i) + Integer.toString(j)).add("x", i)
							.add("y", j).add("token", board[i][j].getAsText()).build();
					arrBuilder.add(ob);
				}
			}
		}
		// builder.
		return arrBuilder.build();

	}

	@Override
	public GameToken[][] adaptFromJson(JsonArray obj) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
