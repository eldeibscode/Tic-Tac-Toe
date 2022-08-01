package de.avtest.testaufgabe.juniortask.mapping;

import java.util.Iterator;

import de.avtest.testaufgabe.juniortask.data.GameBoard;
import de.avtest.testaufgabe.juniortask.data.enums.GameMark;
import de.avtest.testaufgabe.juniortask.data.enums.GamePlayer;
import de.avtest.testaufgabe.juniortask.repository.GameBoardRepository;

public class GameBoardMapper {
	public static GameBoard stringToGameBoard(String str) {
		GameBoard gb = new GameBoard();
		int size = 0;
		try {
			size = Integer.parseInt(str.substring(0, str.indexOf(":")));
		} catch (NumberFormatException  e) {
			e.printStackTrace();
		}
		System.out.println("size: " + size);
		
		String states = str.substring(str.indexOf(":[") + 2, str.indexOf("]:"));
		System.out.println("states: " + states);
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if(states.charAt(i + j) == 'X') gb.setSpace(i, j, GameMark.CROSS);
				else if(states.charAt(i+j) =='O') gb.setSpace(i, j, GameMark.CIRCLE);
				else gb.setSpace(i, j, GameMark.NONE);
			}
		}
		
		String lastPlayer = str.substring(str.indexOf("]:")+2);
		System.out.println("last Player: " + lastPlayer);
		
		if(lastPlayer.equals("player")) {
			gb.setLastPlayer(GamePlayer.HUMAN);
		}else if(lastPlayer.equals("bot")) {
			gb.setLastPlayer(GamePlayer.ROBOT);
		}else {
			gb.setLastPlayer(null);
		}
		
		return gb;
	}
	public static String gameBoardToString(GameBoard board) {
		StringBuilder boardState = new StringBuilder("[");
		for (int i = 0; i < board.getSize(); i++) {
			for (int j = 0; j < board.getSize(); j++) {
				if(board.getSpace(i, j) == GameMark.CIRCLE) boardState.append("O,");
				else if (board.getSpace(i, j) == GameMark.CROSS) boardState.append("X,");
				else boardState.append("-,");
			}
		}
		boardState.setLength(boardState.length() - 1);
		boardState.append("]");
		
		String lastPlayer = board.getLastPlayer() == GamePlayer.HUMAN ? "player" : "bot";	
		
		return String.valueOf(board.getSize()) +":"+ boardState.toString() +":" + lastPlayer;
	}
	
	
	public static void main(String[] args) {
//		System.out.println(gameBoardToString(new GameBoard()));
//		System.out.println(stringToGameBoard("3:[---------]:bot"));
		GameBoard gameBoard =  stringToGameBoard("3:[XOX---XOX]:bot");
		GameBoardRepository.saveOrUpdateState("id1", gameBoardToString(gameBoard));
		
	}
}
