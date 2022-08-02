package de.avtest.testaufgabe.juniortask.mapping;

import java.util.LinkedHashMap;
import java.util.Map;

import de.avtest.testaufgabe.juniortask.data.GameBoard;
import de.avtest.testaufgabe.juniortask.data.enums.GameMark;
import de.avtest.testaufgabe.juniortask.data.enums.GamePlayer;
import de.avtest.testaufgabe.juniortask.repository.GameBoardRepository;

/**
 * @author moham
 *	Mapped the GameBoard Object to String  saving it as Sting in the file-system
 */

public class GameBoardMapper {

	
	/**
	 * @return
	 * return all stored gameBoards
	 */
	public static Map<String, GameBoard> getAllGames() {
		Map<String, String> allStates = GameBoardRepository.readAllStates();
		Map<String, GameBoard> allGames = new LinkedHashMap<>();

		String id, lastPlayer, states;
		String[] values;
		int size = 0;
		for (Map.Entry<String, String> entry : allStates.entrySet()) {

			GameBoard gameBoard = new GameBoard();
			id = entry.getKey();
			values = entry.getValue().split(",");

			try {
				size = Integer.parseInt(values[0].trim());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			gameBoard.setSize(size);

			states = values[2].trim();
			int index = 0;
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					if (states.charAt(index) == 'X')
						gameBoard.setSpace(i, j, GameMark.CROSS);
					else if (states.charAt(index) == 'O')
						gameBoard.setSpace(i, j, GameMark.CIRCLE);
					else
						gameBoard.setSpace(i, j, GameMark.NONE);
					index++;
				}
			}

			lastPlayer = values[1].trim();
			if (lastPlayer.equals("player")) {
				gameBoard.setLastPlayer(GamePlayer.HUMAN);
			} else if (lastPlayer.equals("bot")) {
				gameBoard.setLastPlayer(GamePlayer.ROBOT);
			} else {
				gameBoard.setLastPlayer(null);
			}

			allGames.put(id, gameBoard);
		}
		return allGames;
	}

	/**
	 * @param id
	 * @param board
	 * saved the gameBoard if it not saved, or update the state of the board, it are saved before.
	 */
	public static void saveOrUpdateState(String id, GameBoard board) {
		StringBuilder boardState = new StringBuilder();
		for (int i = 0; i < board.getSize(); i++) {
			for (int j = 0; j < board.getSize(); j++) {
				if (board.getSpace(i, j) == GameMark.CIRCLE)
					boardState.append("O");
				else if (board.getSpace(i, j) == GameMark.CROSS)
					boardState.append("X");
				else
					boardState.append("-");
			}
		}

		String lastPlayer = null;
		if (board.getLastPlayer() == GamePlayer.HUMAN) {
			lastPlayer = "player";
		} else if (board.getLastPlayer() == GamePlayer.ROBOT) {
			lastPlayer = "bot";
		}

		String state = String.valueOf(board.getSize()) + ", " + lastPlayer + ", " + boardState.toString();
		GameBoardRepository.saveOrUpdateState(id, state);
	}

}
