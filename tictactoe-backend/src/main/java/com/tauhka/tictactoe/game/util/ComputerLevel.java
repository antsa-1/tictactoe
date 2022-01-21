package com.tauhka.tictactoe.game.util;

import java.util.ArrayList;
import java.util.List;

public final class ComputerLevel {
	private static final List<ComputerLevel> COMPUTER_LEVELS;
	private final int id;
	private final String defaultText;

	static {
		COMPUTER_LEVELS = new ArrayList<ComputerLevel>();
		ComputerLevel gm = new ComputerLevel(1, "Super easy");
		ComputerLevel gm2 = new ComputerLevel(2, "Easy");
		COMPUTER_LEVELS.add(gm);
		COMPUTER_LEVELS.add(gm2);
	}

	public int getId() {
		return id;
	}

	public String getDefaultText() {
		return defaultText;
	}

	public ComputerLevel(int id, String defaultText) {
		this.id = id;
		this.defaultText = defaultText;
	}

	public static ComputerLevel getComputerLevel(Integer id) {
		if (id == null || id == 0) {
			return null;// no computer chosen
		}
		if (id < 1 || id > COMPUTER_LEVELS.size()) {
			throw new IllegalArgumentException("Wrong computerLevel:" + id);
		}
		return COMPUTER_LEVELS.get(id - 1);
	}
}
