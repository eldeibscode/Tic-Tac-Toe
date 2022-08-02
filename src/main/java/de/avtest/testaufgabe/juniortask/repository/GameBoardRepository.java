package de.avtest.testaufgabe.juniortask.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author moham
 *
 */
public class GameBoardRepository {
	private static String path = new File("").getAbsolutePath() + "/src/main/resources/states.csv";

	/**
	 * @return all stored gameBoard as strings.
	 */
	public static Map<String, String> readAllStates() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		String id, board, size, lastPlayer;
		try {
			BufferedReader file = new BufferedReader(new FileReader(path));
			String line;
			while ((line = file.readLine()) != null) {
				id = line.substring(0, line.indexOf(",")).trim();
				board = line.substring(line.indexOf(",") + 1).trim();
//				System.out.println("id:" + id);
//				System.out.println("board:" + board);
				map.put(id, board);
			}
			file.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * @param id
	 * @param state
	 * Saved or update the gameBoard-state as String to the file-system.
	 */
	public static void saveOrUpdateState(String id, String state) {
		Map<String, String> boards = readAllStates();
		if (boards.get(id) == null) {
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(path, true));
				bw.append(id + ", " + state);
				bw.newLine();
				bw.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			updateState(id, state);
		}
	}
	
	
	/**
	 * @param id
	 * @param state
	 * update the stored gameBoard-state.
	 */
	public static void updateState(String id, String state) {
		Map<String, String> allState = readAllStates();
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(path));

			for (Map.Entry<String, String> entry : allState.entrySet()) {
//				System.out.println(entry.getKey() + ", " + entry.getValue());
				if (entry.getKey().equals(id)) {
					bw.append(id + ", " + state);
				} else {
					bw.append(entry.getKey() + ", " + entry.getValue());
				}
				bw.newLine();
			}
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
