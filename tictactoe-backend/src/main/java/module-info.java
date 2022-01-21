module tictactoe {
	exports com.tauhka.tictactoe.ejb;
	exports com.tauhka.tictactoe.messaging.handlers;
	exports com.tauhka.tictactoe.game;

	exports com.tauhka.tictactoe.messaging;
	exports com.tauhka.tictactoe.web;

	requires jakarta.annotation;
	requires jakarta.ejb.api;
	requires jakarta.inject;
	requires jakarta.json;
	requires jakarta.json.bind;
	requires jakarta.servlet;
	requires java.logging;
	requires java.sql;

	requires org.junit.jupiter.api;
	requires org.junit.platform.engine;
	requires jakarta.websocket;
	requires jakarta.cdi;
}