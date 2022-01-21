package com.tauhka.portal.util;

import java.util.ArrayList;
import java.util.List;

//Same as tictactoe project's Constants.java. Should be in 1 place and distributed to both projects.
public final class Constants {
	public static final int USER_NAME_MIN_LENGTH = 1;
	public static final int USER_NAME_MAX_LENGTH = 15;
	public static final int PASSWORD_MIN_LENGTH = 8;
	public static final int PASSWORD_MAX_LENGTH = 150;
	public static final int CHAT_MESSAGE_MAX_LENGTH = 75;
	public static final int CHAT_MESSAGE_MIN_LENGTH = 1;
	public static final String ANONYM_LOGIN_NAME_START = "Anonym_";
	public static final String ANONYM_LOGIN_TOKEN_START = "Anom:";
	public static final String ENVIRONMENT_PRODUCTION = "PRODUCTION";
	public static final String NULL = "null";
	public static final int FEEDBACK_MAX_LENGTH = 400;
	public static final int FEEDBACK_MIN_LENGTH = 3;
	public static final String LOG_PREFIX = "TICTACTOE_";
	public static final String LOG_PREFIX_PORTAL = LOG_PREFIX + "PORTAL";
	public static final String LOG_PREFIX_TICTACTOE = LOG_PREFIX + "TICTACTOE ";

	public static final List<String> FORBIDDEN_WORD_PARTS = new ArrayList<String>();
	static {
		FORBIDDEN_WORD_PARTS.add("http");
		FORBIDDEN_WORD_PARTS.add("@");
		FORBIDDEN_WORD_PARTS.add("https");
		FORBIDDEN_WORD_PARTS.add("www");
		FORBIDDEN_WORD_PARTS.add("system");
		FORBIDDEN_WORD_PARTS.add("Anonym");
		FORBIDDEN_WORD_PARTS.add("null");
		FORBIDDEN_WORD_PARTS.add(".com");
		FORBIDDEN_WORD_PARTS.add("/");
		FORBIDDEN_WORD_PARTS.add("//");
		FORBIDDEN_WORD_PARTS.add("///");
		FORBIDDEN_WORD_PARTS.add("\\");
		FORBIDDEN_WORD_PARTS.add(":");
		FORBIDDEN_WORD_PARTS.add("<");
		FORBIDDEN_WORD_PARTS.add("&lt;");
	}

}