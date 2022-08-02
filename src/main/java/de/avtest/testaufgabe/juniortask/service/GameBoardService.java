package de.avtest.testaufgabe.juniortask.service;

import java.util.HashMap;
import java.util.Map;

import de.avtest.testaufgabe.juniortask.data.GameBoard;
import de.avtest.testaufgabe.juniortask.mapping.GameBoardMapper;

public class GameBoardService {

	/**
	 * @return all stored game-board with id from mapper.
	 */
	public static Map<String, GameBoard> getStoredGames() {
		return GameBoardMapper.getAllGames();
	}

	/**
	 * @param id
	 * @param board
	 * save the current State of the GameBoard with given id
	 */
	public static void saveState(String id, GameBoard board) {
		GameBoardMapper.saveOrUpdateState(id, board);
	}
}
