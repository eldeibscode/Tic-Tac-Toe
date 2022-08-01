package de.avtest.testaufgabe.juniortask.rest;

import de.avtest.testaufgabe.juniortask.data.GameBoard;
import de.avtest.testaufgabe.juniortask.data.GameBoardSlice;
import de.avtest.testaufgabe.juniortask.data.enums.GameMark;
import de.avtest.testaufgabe.juniortask.data.enums.GamePlayer;
import de.avtest.testaufgabe.juniortask.service.GameBoardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("api/game")
public class GameController {

	private final Map<String, GameBoard> storedGames = GameBoardService.getStoredGames();
//	private final Map<String, GameBoard> storedGames = new LinkedHashMap<>();
	private final Random random = new Random();
	@Autowired
	private CopyrightController copyrightController;

	/**
	 *
	 * @param gameBoard
	 * @return
	 */
	protected ResponseEntity<String> statusOutput(GameBoard gameBoard) {
		var winner = this.whoHasWon(gameBoard);
		var finalOutput = "";
		if (this.someoneHasWon(gameBoard) && winner == null) {
			finalOutput = System.lineSeparator() + "Someone won the game";
		} else if (this.someoneHasWon(gameBoard) && winner.equals(GamePlayer.HUMAN)) {
			finalOutput = System.lineSeparator() + "You won the game! Congratulations!";
		} else if (this.someoneHasWon(gameBoard) && winner.equals(GamePlayer.HUMAN)) {
			finalOutput = System.lineSeparator() + "The won the game...";
		} else if (!gameBoard.spaceIsLeft()) {
			finalOutput = System.lineSeparator() + "It's a draw";
		} else {
			finalOutput = "";
		}

		return ResponseEntity.ok(copyrightController.getCopyright() + System.lineSeparator() + System.lineSeparator()
				+ gameBoard.draw() + finalOutput);
	}

	/**
	 *
	 * @param gameBoard
	 * @return
	 */
	protected boolean someoneHasWon(GameBoard gameBoard) {

		// ##### TASK 7 - Make this check more efficient
		// ###############################################################
		// =============================================================================================================
		// This function checks if the game has already won. It does this by checking
		// for every possible winning
		// condition. For example, the first block below checks if the first row
		// contains identical marks that are not
		// GameMark.NONE.
		// As you can see, this function is exorbitantly long and highly redundant. Your
		// task is to find a way to
		// shorten this function without compromising its functionality. Note that by
		// "shorten", we don't mean to just
		// remove spaces and line breaks ;)
		// =============================================================================================================
		return whoHasWon(gameBoard) != null;
	}

	/**
	 *
	 * @param gameBoard
	 * @return
	 */
	protected GamePlayer whoHasWon(GameBoard gameBoard) {
		// ##### TASK 8 - Check who has won
		// ############################################################################
		// =============================================================================================================
		// Here, you need to code a way to find out who has won the game.
		// This function needs to return null if nobody has won yet - you can use
		// someoneHasWon( $game ) for this.
		// If someone has won, it needs to return either GamePlayer::Human or
		// GamePlayer::Robot.
		// =============================================================================================================
		// test row
		// r is the index of row
		// c is the index of column
		// resRow for testing the rows
		// resCol for testing the columns
		// resDiag for testing the diagonal
		// resAntiDiag for testing the Anti-diagonal
		int boardSize = gameBoard.getSize();
		int resRow, resCol, resDiag, resAntiDiag;
		for (int r = 0; r < boardSize; r++) {
			resRow = resCol = resDiag = resAntiDiag = 0;
			for (int c = 0; c < boardSize; c++) {

				if (gameBoard.getSpace(r, c) == GameMark.CROSS)
					resRow++;
				else if (gameBoard.getSpace(r, c) == GameMark.CIRCLE)
					resRow--;

				if (gameBoard.getSpace(c, r) == GameMark.CROSS)
					resCol++;
				else if (gameBoard.getSpace(c, r) == GameMark.CIRCLE)
					resCol--;

				if (r == 0 && gameBoard.getSpace(c, c) == GameMark.CROSS)
					resDiag++;
				else if (r == 0 && gameBoard.getSpace(c, c) == GameMark.CIRCLE)
					resDiag--;

				if (r == 0 && gameBoard.getSpace(c, boardSize - 1 - c) == GameMark.CROSS)
					resAntiDiag++;
				else if (r == 0 && gameBoard.getSpace(c, boardSize - 1 - c) == GameMark.CIRCLE)
					resAntiDiag--;
			}
			if (resRow == boardSize || resCol == boardSize || resDiag == boardSize || resAntiDiag == boardSize) {
				return GamePlayer.ROBOT;
			} else if (resRow == -boardSize || resCol == -boardSize || resDiag == -boardSize
					|| resAntiDiag == -boardSize) {
				return GamePlayer.HUMAN;
			}
		}
		return null;
	}

	/**
	 * Is the given player allowed to take the next turn?
	 * 
	 * @param gameBoard
	 * @param player
	 * @return
	 */
	protected boolean isAllowedToPlay(GameBoard gameBoard, GamePlayer player) {
		// ##### TASK 6 - No cheating!
		// #################################################################################
		// =============================================================================================================
		// We don't want the player to be able to cheat. They should only be able to
		// make a move if it is their turn.
		// Neither the player nor the bot are allowed to make a move twice in a row. So,
		// you need to check which player
		// made the *last* move to find out if the player is allowed to act.
		// =============================================================================================================

		// The method gameBoard.getLastPlayer() will return either GamePlayer.ROBOT (the
		// last move was made by the bot),
		// GamePlayer.HUMAN (the last move was made by the player) or null (this is the
		// first move).
		// Inside `player` you have the player which wants to play now.
		// If he is allowed to play, you have to return true, otherwise you have to
		// return false.

		return gameBoard.getLastPlayer() != player;
	}

	/**
	 *
	 * @param gameId The ID of the game
	 * @param x      The x position entered by the player
	 * @param y      The y position entered by the player
	 * @return
	 */
	@GetMapping(value = "play", produces = "text/plain")
	public ResponseEntity<String> play(@RequestParam String gameId, @RequestParam int x, @RequestParam int y) {
		// Loading the game board
		var gameBoard = storedGames.get(gameId);

		// Check if the given position is actually valid; can't have the player draw a
		// cross on the table next to the
		// game board ;)
		if (x < 0 || y < 0 || x >= gameBoard.getSize() || y >= gameBoard.getSize()) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Position outside of the game board");
		}

		// Prevent the player from playing if the game has already ended
		if (this.someoneHasWon(gameBoard) || !gameBoard.spaceIsLeft()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("You are not allowed to play. The game has already ended.");
		}

		// Prevent the player from playing if it is not his turn
		if (!this.isAllowedToPlay(gameBoard, GamePlayer.HUMAN)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("You are not allowed to play. It is the bots turn!");
		}

		// ##### TASK 4 - Let the player make their move
		// ###############################################################
		// =============================================================================================================
		// Here, you need to code the logic that allows a player to make a move.
		// You can make use of the methods offered by the gameBoard object.
		// =============================================================================================================

		// We've previously ensured that the player is allowed to play and the game has
		// not ended yet.
		// The method gameBoard.getSpace( x, y ) will return the content of a space -
		// either GameMark.NONE (free),
		// GameMark.CROSS (belongs to the bot) or GameMark.CIRCLE (belongs to the
		// player).

		// You can compare two values with
		// a == b gets true if a is equals b
		// a != b gets true if a is not equals b
		//
		// Once all the checks have passed, you can finally update the game board by
		// calling
		// gameBoard.setSpace( x, y, GameMark.CIRCLE ).
		// [ The code to check if the space is free goes here ]

		// If the space is not free, run the code in the line below by removing the //
		if (gameBoard.getSpace(x, y) != GameMark.NONE) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("This space has already been claimed!");
		}
		
		gameBoard.setSpace(x, y, GameMark.CIRCLE);
		// [ The code to update the game board goes here ]

		// Saving the game board and output it to the player
		
//		save the gameBoard after play
		GameBoardService.saveState(gameId, gameBoard);
		
		return this.statusOutput(gameBoard);
	}

	@GetMapping(value = "playBot", produces = "text/plain")
	public ResponseEntity<String> playBot(@RequestParam String gameId) {
		// Loading the game board
		var gameBoard = storedGames.get(gameId);

		// ##### TASK 5 - Understand the bot
		// ###########################################################################
		// =============================================================================================================
		// This first step to beat your enemy is to thoroughly understand them.
		// Luckily, as a developer, you can literally look into its head. So, check out
		// the bot logic and try to
		// understand what it does.
		// =============================================================================================================

		// Prevent the player from playing if the game has already ended
		if (this.someoneHasWon(gameBoard) || !gameBoard.spaceIsLeft()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("You are not allowed to play. The game has already ended.");
		}

		// Prevent the player from playing if it is not his turn
		if (!this.isAllowedToPlay(gameBoard, GamePlayer.ROBOT)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("You are not allowed to play. It is the player turn!");
		}

		var freeSpaces = new LinkedList<Map<String, Integer>>();
		// get all rows of our game board
		for (int y = 0; y < gameBoard.getSize(); y++) {
			var row = gameBoard.getRow(y);
			// get all spaces inside the row
			for (int x = 0; x < gameBoard.getSize(); x++) {
				// check whether the space is still free
				var space = row.getSpace(x);
				if (space.isFree()) {
					// save the free space to our free spaces list
					freeSpaces.add(Map.of("x", x, "y", y));
				}
			}
		}

		// get random free space from our list
		var randomFreeSpace = freeSpaces.stream().skip(random.nextInt(freeSpaces.size())).findFirst()
				.orElseGet(() -> freeSpaces.get(0));

		gameBoard.setSpace(randomFreeSpace.get("x"), randomFreeSpace.get("y"), GameMark.CROSS);

//		save the gameBoard after playBot
		GameBoardService.saveState(gameId, gameBoard);
		
		return this.statusOutput(gameBoard);
	}

	@GetMapping(value = "display", produces = "text/plain")
	public ResponseEntity<String> display(@RequestParam String gameId) {
		// Loading the game board
		var gameBoard = storedGames.get(gameId);
		return this.statusOutput(gameBoard);
	}

	@GetMapping(value = "create", produces = "text/plain")
	public ResponseEntity<String> create() {
		// Loading the game board
		var uuid = UUID.randomUUID().toString();
		storedGames.put(uuid, new GameBoard());
		
//		save the gameBoard after Creating game
		GameBoardService.saveState(uuid, storedGames.get(uuid));
		
		return ResponseEntity.ok(uuid);
	}
}
