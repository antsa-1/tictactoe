package com.tauhka.tictactoe.messaging;

public enum MessageTitle {

	LOGIN("LOGIN"), JOIN_TABLE("JOIN_TABLE"), CHAT("CHAT"), LOGOUT("LOGOUT"), START_GAME("START_GAME"), REMOVE_PLAYER("REMOVE_PLAYER"), WATCH("WATCH"), ADD_WATCHER("ADD_WATCHER"), WATCHER_LEFT("WATCHER_LEFT"),
	LEAVE_TABLE("LEAVE_TABLE"), NEW_PLAYER("NEW_PLAYER"), CREATE_TABLE("CREATE_TABLE"), REMOVE_TABLE("REMOVE_TABLE"), CHALLENGE("CHALLENGE"), MOVE("MOVE"), GAME_END("GAME_END"), RESIGN("RESIGN"), WINNER("WINNER"),
	REMATCH("REMATCH");

	private String text;

	private MessageTitle(String text) {
		this.text = text;
	}

	public String getAsText() {
		return this.text;
	}

	public static MessageTitle get(String text) {
		if (text == null || text.trim().equals("")) {
			throw new IllegalArgumentException("Not correct enumText" + text);
		}
		if (text.equals(LOGIN.getAsText())) {
			return LOGIN;
		} else if (text.equals(CHAT.getAsText())) {
			return CHAT;
		} else if (text.equals(LOGOUT.getAsText())) {
			return LOGOUT;
		} else if (text.equals(START_GAME.getAsText())) {
			return START_GAME;
		} else if (text.equals(NEW_PLAYER.getAsText())) {
			return NEW_PLAYER;
		} else if (text.equals(REMOVE_PLAYER.getAsText())) {
			return REMOVE_PLAYER;
		} else if (text.equals(WATCH.getAsText())) {
			return WATCH;
		} else if (text.equals(LEAVE_TABLE.getAsText())) {
			return LEAVE_TABLE;
		} else if (text.equals(CREATE_TABLE.getAsText())) {
			return CREATE_TABLE;
		} else if (text.equals(JOIN_TABLE.getAsText())) {
			return JOIN_TABLE;
		} else if (text.equals(REMOVE_TABLE.getAsText())) {
			return REMOVE_TABLE;
		} else if (text.equals(REMATCH.getAsText())) {
			return REMATCH;
		} else if (text.equals(MOVE.getAsText())) {
			return MOVE;
		} else if (text.equals(ADD_WATCHER.getAsText())) {
			return ADD_WATCHER;
		}
		throw new IllegalArgumentException("Not enumText" + text);
	}
}
