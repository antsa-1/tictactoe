package com.tauhka.tictactoe.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

import com.tauhka.tictactoe.ai.ArtificialUser;
import com.tauhka.tictactoe.game.util.TicTacToeBoardAdapter;
import com.tauhka.tictactoe.game.util.WinnerChecker;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.json.bind.annotation.JsonbTypeAdapter;

//Table where tictactoe is played. Contains two players, board and spectators. Plus some logic.
public class Table {
	private static final Logger LOGGER = Logger.getLogger(Table.class.getName());
	@JsonbProperty("id")
	private UUID id;
	@JsonbProperty("playerA")
	private User playerA;
	@JsonbProperty("playerB")
	private User playerB;
	@JsonbProperty("playerInTurn")
	private User playerInTurn;
	@JsonbTransient()
	private User rematchPlayer;
	@JsonbTransient()
	private User startingPlayer;
	@JsonbProperty("timer")
	private int timer = 180;
	@JsonbProperty("watchers")
	private List<User> watchers = new ArrayList<User>();
	@JsonbTypeAdapter(TicTacToeBoardAdapter.class)
	private GameToken board[][];
	@JsonbProperty("x")
	private int x = 0;
	@JsonbProperty("y")
	private int y = 0;
	@JsonbProperty("draws")
	private int draws = 0;
	@JsonbTransient
	private boolean randomizeStarter;
	@JsonbProperty("gameMode")
	private GameMode gameMode;
	@JsonbTransient
	private int addedTokens;

	public Table(User playerA, GameMode gameMode, boolean randomizeStarter) {
		super();
		this.id = UUID.randomUUID();
		this.playerA = playerA;
		this.startingPlayer = playerA;
		this.playerA.setWins(0);
		playerA.setGameToken(GameToken.X);
		this.randomizeStarter = randomizeStarter;
		this.playerInTurn = this.playerA;
		board = new GameToken[gameMode.getX()][gameMode.getY()];
		this.gameMode = gameMode;
		this.x = this.gameMode.getX();
		this.y = this.gameMode.getY();
		this.addedTokens = 0;
	}

	@JsonbTransient
	public boolean isArtificialPlayerInTurn() {
		return this.playerInTurn != null && this.playerInTurn instanceof ArtificialUser;
	}

	private Table startRematch() {
		board = new GameToken[gameMode.getX()][gameMode.getY()];
		this.rematchPlayer = null;
		if (startingPlayer.equals(playerA)) {
			startingPlayer = playerB;
		} else {
			startingPlayer = playerA;
		}
		if (startingPlayer.equals(playerA)) {
			playerA.setGameToken(GameToken.X);
			playerB.setGameToken(GameToken.O);
		} else {
			playerA.setGameToken(GameToken.O);
			playerB.setGameToken(GameToken.X);
		}
		this.playerInTurn = startingPlayer;
		this.addedTokens = 0;
		return this;
	}

	public boolean isWaitingOpponent() {
		return this.playerA != null && this.playerB == null;
	}

	public boolean isPlayerInTurn(User u) {
		if (this.playerInTurn == null) {
			return false;
		}
		return this.playerInTurn.equals(u);
	}

	public synchronized User resign(User player) {
		if (this.isPlayer(player) && this.playerInTurn != null) {
			this.playerInTurn = null;
			User opponent = getOpponent(player);
			opponent.addWin();
			return opponent;
		}
		return null;
	}

	public int getDraws() {
		return draws;
	}

	public GameMode getGameMode() {
		return gameMode;
	}

	public void setDraws(int draws) {
		this.draws = draws;
	}

	public User getOpponent(User player) {
		if (playerA.equals(player)) {
			return playerB;
		}
		return playerA;
	}

	public boolean isPlayer(User user) {
		if (user == null) {
			return false;
		}
		return user.equals(playerA) || user.equals(playerB);
	}

	public boolean isWatcher(User user) {
		return this.watchers.contains(user);
	}

	public int getX() {
		return this.gameMode.getX();
	}

	public boolean addWatcher(User watcher) {
		if (this.playerA == null || this.playerB == null) {
			return false;
		}
		if (!this.watchers.contains(watcher)) {
			this.watchers.add(watcher);
			watcher.setWins(0);
			return true;
		}
		return false;
	}

	public int getY() {
		return this.gameMode.getY();
	}

	// Tuplaklikit jne. -> synchronized plus
	public synchronized String addGameToken(User user, int x, int y) {
		if (!user.equals(this.playerInTurn)) {
			throw new IllegalArgumentException("Player is not in turn in board:" + this);
			// throw new CloseWebSocketExcepetion("Player is not in turn in board:" + this);
		}
		GameToken token = this.board[x][y];
		if (token != null) {
			throw new IllegalArgumentException("Board already has token x:" + x + "+ y:" + y + " _" + this);
		}
		token = this.playerInTurn.getGameToken();
		this.board[x][y] = token;
		this.changePlayerInTurn();
		this.addedTokens++;
		return token.toString();
	}

	public GameResult checkWinAndDraw() {
		int requiredTokenConnects = this.gameMode.getRequiredConnections();
		GameResult result = WinnerChecker.checkWinner(this.board, requiredTokenConnects);
		if (result == null) {
			// No winner, is it a draw?
			if (addedTokens == gameMode.getX() * gameMode.getY()) {
				result = new GameResult();
				result.setDraw(true);
				this.playerA.addDraw();
				this.playerB.addDraw();
				// this.playerInTurn = null;
				initRematchForArtificialPlayer();
			}
			return result;
		}
		initRematchForArtificialPlayer();
		if (playerA.getGameToken() == result.getToken()) {
			result.setPlayer(playerA);
			playerA.addWin();
			return result;
		}
		result.setPlayer(playerB);
		playerB.addWin();
		return result;
	}

	public void initRematchForArtificialPlayer() {

		// PlayerA can't be Artificial player since it does not create tables
		if (this.playerB != null && this.playerB instanceof ArtificialUser) {
			this.suggestRematch(this.playerB);
		}
	}

	public int getTimer() {
		return timer;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	@JsonbTransient
	public List<User> getUsers() {
		List<User> users = new ArrayList<User>();
		if (playerA != null) {
			users.add(playerA);
		}
		if (playerB != null) {
			users.add(playerB);
		}

		users.addAll(watchers);
		return users;
	}

	public boolean removePlayerIfExist(User user) {
		if (playerA != null && playerA.equals(user)) {
			playerA = null;
			this.playerInTurn = null;
			return true;
		} else if (playerB != null && playerB.equals(user)) {
			playerB = null;
			this.playerInTurn = null;
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Table other = (Table) obj;
		return Objects.equals(id, other.id);
	}

	public boolean removeWatcherIfExist(User user) {
		return watchers.remove(user);
	}

	public User getPlayerA() {
		return playerA;
	}

	public UUID getId() {
		return id;
	}

	public User getPlayerB() {
		return playerB;
	}

	public boolean isRandomizeStarter() {
		return randomizeStarter;
	}

	public void setRandomizeStarter(boolean randomizeStarter) {
		this.randomizeStarter = randomizeStarter;
	}

	public User getPlayerInTurn() {
		return playerInTurn;
	}

	public void changePlayerInTurn() {
		if (this.playerInTurn.equals(playerA)) {
			this.playerInTurn = playerB;
			return;
		}
		this.playerInTurn = playerA;
	}

	public void setPlayerB(User playerB) {
		this.playerB = playerB;
		playerB.setGameToken(GameToken.O);
		playerB.setWins(0);
	}

	public List<User> getWatchers() {
		return watchers;
	}

	@Override
	public String toString() {
		return "TicTacToeTable [id=" + id + ", playerA=" + playerA + ", playerB=" + playerB + "]";
	}

	public GameToken[][] getBoard() {
		return board;
	}

	public void setBoard(GameToken[][] board) {
		this.board = board;
	}

	public synchronized boolean suggestRematch(User user) {
		if (this.rematchPlayer == null) {
			this.rematchPlayer = user;
			return false;
		}
		if (this.rematchPlayer.equals(user)) {
			return false;
		}
		this.startRematch();
		return true;
	}

}
