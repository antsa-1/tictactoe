package com.tauhka.tictactoe.web;

import com.tauhka.tictactoe.game.util.Constants;

import jakarta.websocket.server.ServerEndpointConfig;

public class TicTacToeEndpointConfiguration extends ServerEndpointConfig.Configurator {
	private static final String ENVIRONMENT = System.getProperty("Server_Environment");

	// Checks that browser sends origin header and it corresponds what is expected to be received
	// Also checks that Server has been given Environment parameter for example. -DServer_Environment="Development"
	// OR -DServer_Environment="Production"
	@Override
	public boolean checkOrigin(String originHeaderValue) {
		if (ENVIRONMENT == null) {
			throw new IllegalArgumentException("Environment is missing");
		}
		if (originHeaderValue == null) {
			return false;
		}

		if (ENVIRONMENT.equalsIgnoreCase(Constants.ENVIRONMENT_PRODUCTION)) {

			return originHeaderValue.equals(Constants.WEBSOCKET_PRODUCTION_ORIGIN);

		}
		return originHeaderValue.equals(Constants.WEBSOCKET_LOCALHOST_ORIGIN);
	}
}
