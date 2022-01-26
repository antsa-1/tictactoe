package com.tauhka.tictactoe.messaging;

import java.util.logging.Logger;

import com.tauhka.tictactoe.game.util.TicTacToeBoardAdapter;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder;

public class MessageEncoder implements Encoder.Text<Message> {

	private static final Logger LOGGER = Logger.getLogger(MessageEncoder.class.getName());
	private final JsonbConfig ticTacToeConfig = new JsonbConfig().withAdapters(new TicTacToeBoardAdapter());

	@Override
	public void destroy() {
		LOGGER.fine("MessageEncoder destroy");
	}

	@Override
	public String encode(Message message) throws EncodeException {
		try {
			Jsonb jsonb = JsonbBuilder.create(ticTacToeConfig);
			String retVal = jsonb.toJson(message, Message.class);
			return retVal;
		} catch (Exception e) {
			LOGGER.severe("Encode error in MessageEncoder:" + e.getMessage());
		}
		return null;
	}

}