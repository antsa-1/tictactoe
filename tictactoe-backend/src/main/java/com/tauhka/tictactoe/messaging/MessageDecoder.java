package com.tauhka.tictactoe.messaging;

import java.util.logging.Logger;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.websocket.DecodeException;
import jakarta.websocket.Decoder;

public class MessageDecoder implements Decoder.Text<Message> {

	private static final Logger LOGGER = Logger.getLogger(Decoder.class.getName());

	@Override
	public void destroy() {
		LOGGER.fine("MessageDecoder destroy");
	}

	@Override
	public Message decode(String jsonMessage) throws DecodeException {
		Jsonb jsonb = JsonbBuilder.create();
		Message message = jsonb.fromJson(jsonMessage, Message.class);

		return message;

	}

	@Override
	public boolean willDecode(String arg0) {
		// TODO Auto-generated method stub
		return arg0 != null && arg0.contains("title");
	}

}