package com.tauhka.tictactoe.messaging;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.tauhka.tictactoe.game.GameMode;
import com.tauhka.tictactoe.game.GameResult;
import com.tauhka.tictactoe.game.Table;
import com.tauhka.tictactoe.game.User;

import jakarta.json.bind.annotation.JsonbProperty;

//Base class for Messaging. Different fields are populated based on different situations. MessageTitle is indicator for what fields should/could be read.
public class Message implements Serializable {

	private static final long serialVersionUID = 1L;
	@JsonbProperty("title")
	private MessageTitle title;
	@JsonbProperty("message")
	private String message;
	@JsonbProperty("x")
	private int x;
	@JsonbProperty("y")
	private int y;
	@JsonbProperty("who")
	private User who;
	@JsonbProperty("to")
	private String to;
	@JsonbProperty("from")
	private String from;
	@JsonbProperty("token")
	private String token;
	@JsonbProperty("players")
	private List<User> users;
	@JsonbProperty("table")
	private Table table;
	@JsonbProperty("tables")
	private List<Table> tables = new ArrayList<Table>();
	@JsonbProperty("gameModes")
	private List<GameMode> gameModes;
	@JsonbProperty("win")
	private GameResult win;
	@JsonbProperty("computer")
	private Boolean computer;

	public MessageTitle getTitle() {
		return title;
	}

	public void addTable(Table table) {
		this.tables.add(table);
	}

	public void setTitle(MessageTitle title) {
		this.title = title;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public Boolean getComputer() {
		return computer;
	}

	public void setComputer(Boolean computer) {
		this.computer = computer;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public GameResult getWin() {
		return win;
	}

	public void setWin(GameResult win) {
		this.win = win;
	}

	public List<GameMode> getGameModes() {
		return gameModes;
	}

	public void setGameModes(List<GameMode> gameModes) {
		this.gameModes = gameModes;
	}

	public String getMessage() {
		return message;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public List<Table> getTables() {
		return tables;
	}

	public void setTables(List<Table> tables) {
		this.tables = tables;
	}

	public User getWho() {
		return who;
	}

	public void setWho(User who) {
		this.who = who;
	}

	@Override
	public String toString() {
		return "Message [title=" + title + ", message=" + message + ", who=" + who + ", to=" + to + "]";
	}

}
