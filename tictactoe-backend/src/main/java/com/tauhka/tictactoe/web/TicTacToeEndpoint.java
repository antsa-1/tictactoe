package com.tauhka.tictactoe.web;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.tauhka.tictactoe.game.Table;
import com.tauhka.tictactoe.game.User;
import com.tauhka.tictactoe.messaging.Message;
import com.tauhka.tictactoe.messaging.MessageDecoder;
import com.tauhka.tictactoe.messaging.MessageEncoder;
import com.tauhka.tictactoe.messaging.MessageTitle;
import com.tauhka.tictactoe.messaging.handlers.GameHandler;
import com.tauhka.tictactoe.messaging.handlers.UserHandler;

import jakarta.inject.Inject;
import jakarta.websocket.CloseReason;
import jakarta.websocket.EncodeException;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/ws", decoders = { MessageDecoder.class }, encoders = {
		MessageEncoder.class }, configurator = TicTacToeEndpointConfiguration.class)
public class TicTacToeEndpoint {
	private static final Logger LOGGER = Logger.getLogger(TicTacToeEndpoint.class.getName());
	public static ConcurrentHashMap<User, TicTacToeEndpoint> ENDPOINTS = new ConcurrentHashMap<User, TicTacToeEndpoint>();
	public static ConcurrentHashMap<UUID, Table> TABLES = new ConcurrentHashMap<UUID, Table>();

	@Inject
	private UserHandler userHandler;

	@Inject
	private GameHandler gameHandler;

	private Session session;
	private User user;

	@OnOpen
	public void onOpen(Session session, EndpointConfig conf) {
		LOGGER.log(Level.FINE, "TicTactoeEndpoint onOpen");
		session.setMaxIdleTimeout(1000 * 60 * 30); // millis
		session.setMaxTextMessageBufferSize(1000);
		session.setMaxBinaryMessageBufferSize(0);
		this.session = session;
	}

	@OnMessage
	public void onMessage(Message message, Session session) {
		LOGGER.log(Level.INFO, "TicTactoeEndpoint onMessage" + message);
		try {
			Message gameMessage = null;
			if (message.getTitle() == MessageTitle.LOGIN && this.user == null) {
				gameMessage = userHandler.handleLogin(message, session, this);
				this.user = gameMessage.getWho();
				gameMessage.setTo(this.user.getName());
				gameMessage.setFrom("System");
				sendPrivateMessage(gameMessage);
				sendMessageToAllExcept(userHandler.createNewPlayerMessage(this.user), this.user);
			} else if (this.user == null || this.user.getName() == null) {
				throw new CloseWebSocketException("TicTacToeEndpoint not login command but no user in session");
			} else if (message.getTitle() == MessageTitle.CREATE_TABLE) {
				gameMessage = gameHandler.createTable(message, this);
				sendCommonMessage(gameMessage);
			} else if (message.getTitle() == MessageTitle.LEAVE_TABLE) { // Ei REMOVE_TABLE sisään
				gameMessage = gameHandler.leaveTable(message, this);
				if (gameMessage != null) {
					sendMessageToTable(gameMessage.getTable(), gameMessage);
					sendCommonMessage(gameHandler.createRemoveTableMessage(gameMessage.getTable(), this));
				}
			} else if (message.getTitle() == MessageTitle.JOIN_TABLE) { // Ei REMOVE_TABLE sisään
				gameMessage = gameHandler.joinTable(message, this);
				sendCommonMessage(gameMessage);
			} else if (message.getTitle() == MessageTitle.REMOVE_TABLE) {
				gameMessage = gameHandler.removeEndpointOwnTable(this);
				sendCommonMessage(gameMessage);
			} else if (message.getTitle() == MessageTitle.MOVE) {
				gameMessage = gameHandler.addTokenToGame(message, this.getUser());
				sendMessageToTable(gameMessage.getTable(), gameMessage);
				if (gameMessage.getTable().isArtificialPlayerInTurn()
						&& gameMessage.getTitle() != MessageTitle.GAME_END) {
					Message artMoveMessage = gameHandler.makeComputerMove(gameMessage.getTable());
					sendMessageToTable(gameMessage.getTable(), artMoveMessage);
				}
			} else if (message.getTitle() == MessageTitle.WATCH) {
				gameMessage = gameHandler.watch(message, this);
				sendPrivateMessage(gameMessage);
				gameMessage = gameHandler.getWatcherInfo(message, this);
				sendMessageToTable(gameMessage.getTable(), gameMessage);
			} else if (message.getTitle() == MessageTitle.RESIGN) {
				gameMessage = gameHandler.resign(this);
				sendMessageToTable(gameMessage.getTable(), gameMessage);
			} else if (message.getTitle() == MessageTitle.REMATCH) {
				gameMessage = gameHandler.rematch(this);
				if (gameMessage != null) {
					sendMessageToTable(gameMessage.getTable(), gameMessage);
					if (gameMessage.getTable().isArtificialPlayerInTurn()) {
						Message artMoveMessage = gameHandler.makeComputerMove(gameMessage.getTable());
						sendMessageToTable(gameMessage.getTable(), artMoveMessage);
					}
				}
			} else {
				throw new CloseWebSocketException("unknown command:" + message);
			}
		} catch (CloseWebSocketException c) {
			LOGGER.log(Level.SEVERE, "TicTactoeEndpoint onMessagessa virhe", c);
			this.onClose(session, null);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "TicTactoeEndpoint onMessagessa virhe", e);
		}
	}

	public void sendPrivateMessage(Message message) {
		try {
			if (this.session.isOpen()) {// && this.session.isSecure()) {
				this.session.getBasicRemote().sendObject(message);
			} else {
				this.session.close();
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "TicTactoeEndpoint sendPrivateMessage IO-error error", e);
		} catch (EncodeException e) {
			LOGGER.log(Level.SEVERE, "TicTactoeEndpoint sendPrivateMessage encoding error", e);
		}
	}

	public void sendMessageToAllExcept(Message message, User exceptUser) {
		ENDPOINTS.forEach((k, v) -> {
			if (v.getSession() != null && v.getSession().isOpen() && !v.getUser().equals(exceptUser)) {
				try {
					v.getSession().getBasicRemote().sendObject(message);
				} catch (IOException e) {
					LOGGER.log(Level.SEVERE, "TicTactoeEndpoint sendMessageToAllExcept IO-error error", e);
				} catch (EncodeException e) {
					LOGGER.log(Level.SEVERE, "TicTactoeEndpoint sendMessageToAllExcept encoding error", e);
				}
			}
		});
	}

	public void sendCommonMessage(Message message) {
		if (message == null) {
			return;
		}
		ENDPOINTS.forEach((k, v) -> {
			if (v.getSession() != null && v.getSession().isOpen()) {
				try {
					v.getSession().getBasicRemote().sendObject(message);
				} catch (IOException e) {
					LOGGER.log(Level.SEVERE, "TicTactoeEndpoint sendCommonMessage IO-error", e);
				} catch (EncodeException e) {
					LOGGER.log(Level.SEVERE, "TicTactoeEndpoint sendCommonMessage encoding error", e);
				}
			}
		});
	}

	public void sendMessageToTable(Table table, Message message) {
		if (message == null) {
			return;
		}
		for (User user : table.getUsers()) {
			TicTacToeEndpoint endpoint = ENDPOINTS.get(user);
			if (endpoint != null && endpoint.getSession() != null && endpoint.getSession().isOpen()) {
				try {
					endpoint.getSession().getBasicRemote().sendObject(message);
				} catch (IOException e) {
					LOGGER.log(Level.SEVERE, "TicTactoeEndpoint sendMessage to table io.error", e);
				} catch (EncodeException e) {
					LOGGER.log(Level.SEVERE, "TicTactoeEndpoint sendMessage to table encoding error", e);
				}
			}
		}
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		LOGGER.log(Level.FINE, "TicTactoeEndpoint onClose", closeReason);
		if (session != null) {
			try {
				TicTacToeEndpoint.ENDPOINTS.remove(this.user);
				session.close();
				UUID owningUser = null;
				for (Table table : TABLES.values()) {
					boolean isPlayer = table.removePlayerIfExist(this.user);
					if (isPlayer) {
						owningUser = table.getId();
						break;
					} else {
						boolean isWatcher = table.removeWatcherIfExist(this.user);
						if (isWatcher) {
							Message removeWatcher = new Message();
							removeWatcher.setTitle(MessageTitle.WATCHER_LEFT);
							removeWatcher.setWho(this.user);
							// removeWatcher.setMessage(table.getId());
							sendMessageToTable(table, removeWatcher);
							break;
						}
					}
				}
				Table table = null;
				if (owningUser != null) {
					table = TABLES.remove(owningUser); // Not always playerA, table will be null if playerB. It is ok
				}
				Message disconnectMessage = userHandler.getUserDisconnectedMessage(this, table);
				sendCommonMessage(disconnectMessage);
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "TicTactoeEndpoint onClose virhe", e);
			}
		}
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		LOGGER.log(Level.SEVERE, "TicTactoeEndpoint error", throwable);
		this.onClose(session, null);
	}
}
