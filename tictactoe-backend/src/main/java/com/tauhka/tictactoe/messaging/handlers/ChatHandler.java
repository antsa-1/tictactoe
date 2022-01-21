package com.tauhka.tictactoe.messaging.handlers;

import java.util.UUID;

import com.tauhka.tictactoe.game.Table;
import com.tauhka.tictactoe.messaging.Message;
import com.tauhka.tictactoe.messaging.MessageTitle;
import com.tauhka.tictactoe.web.TicTacToeEndpoint;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Default;

@Default
@Dependent
public class ChatHandler {
	public static final String COMMON = "COMMON";

	public Message handleChatMessage(Message message, TicTacToeEndpoint ticTacToeEndpoint) {
		if (message.getMessage() == null || message.getMessage().length() < 1 || message.getMessage().length() > 75) {
			throw new IllegalArgumentException("Chat message not accepted:" + message.getMessage());
		}
		if (COMMON.equals(message.getTo())) {
			Message chatMessage = new Message();
			chatMessage.setFrom(ticTacToeEndpoint.getUser().getName());
			chatMessage.setTo(COMMON);
			chatMessage.setMessage(message.getMessage());
			chatMessage.setTitle(MessageTitle.CHAT);
			return chatMessage;
		} else {
			// Message to chat table
			UUID tableID = UUID.fromString(message.getTo());
			Table table = TicTacToeEndpoint.TABLES.get(tableID);
			if (table == null) {
				throw new IllegalArgumentException("No table found for chat " + message.getTo());
			}

			Message chatMessage = new Message();
			chatMessage.setFrom(ticTacToeEndpoint.getUser().getName());
			chatMessage.setTo(tableID.toString());
			chatMessage.setMessage(message.getMessage());
			chatMessage.setTable(table);
			chatMessage.setTitle(MessageTitle.CHAT);
			return chatMessage;
		}
	}
}
