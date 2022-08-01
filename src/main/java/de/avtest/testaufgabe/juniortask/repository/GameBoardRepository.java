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

public class GameBoardRepository {
	private static String path = new File("").getAbsolutePath() + "/src/main/resources/states.csv";

	public static Map<String, String> readAllStates() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		String id, board, size, lastPlayer;
		try {
			BufferedReader file = new BufferedReader(new FileReader(path));
			String line;
			while ((line = file.readLine()) != null) {
				id = line.substring(0, line.indexOf(","));
				board = line.substring(line.indexOf(",") + 2);
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
	
//	public static String readState(String id) {
//		Map<String, String> allState = readAllStates();
//		return allState.get(id);
//	}

	public static void saveOrUpdateState(String id, String state) {
		Map<String, String> boards = readAllStates();
		if (boards.get(id) == null) {
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(path, true));
				bw.append(id + ", " + state);
				bw.newLine();
				bw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			updateState(id, state);
		}
	}



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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

//		saveState("8bedd5da-1194-11ed-861d-0242ac120ddd", "3, bot, XOXOX-X--");
//		System.out.println("read State: " + readState("8bedd5da-1194-11ed-861d-0242ac120002"));

//		Map<String, String> map = GameBoardRepository.readAllStates();
////		System.out.println("using keySet");
//		for (String key : map.keySet()) {
//			System.out.println(key + ":" + map.get(key));
//		}
		saveOrUpdateState("testId33", "Halsjdlkfjsflklksfk");
	}

}
