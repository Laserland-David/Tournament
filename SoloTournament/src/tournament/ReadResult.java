package tournament;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ReadResult {

	
	public ReadResult() {
		// TODO Auto-generated constructor stub
	}
	
	public static void read(File file) {
		BufferedReader reader = null;
		String gameID = null;
		Map<String,Player> mapPlayer = new HashMap<String, Player>();


		//Nur Laserforce TDF Files berücksichtigen
		if(!file.getAbsolutePath().endsWith("tdf")) {
			return;
		}

		System.out.println(file.getAbsolutePath());
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_16LE));
			String line;
			boolean gameEnd = false;
			while((line = reader.readLine()) != null ) {
				if(line != null && !line.equals("")) {
					String start = line.trim();


					if(start.startsWith("1")){
						gameID = defineGameID(line);
					}

					if(start.startsWith("3")){
						definePlayers(mapPlayer, line);
					}


					if(start.startsWith("6")){
						String[] splitted = line.split("\t");
						String score = splitted[4].trim();
						String playerCode = splitted[2].trim();

						Player player = mapPlayer.get(playerCode);
						if(player != null) {
							//				System.out.println(player.getName() + " - " + score);
							Map<String, Integer> playerScoreMap = player.getScore();
							if(playerScoreMap == null) {
								playerScoreMap = new HashMap<String, Integer>();
							}

							if(!playerScoreMap.containsKey(gameID)) {
								playerScoreMap.put(gameID, Integer.parseInt(score));
								player.setScore(playerScoreMap);
							}
							mapPlayer.put(playerCode, player);

						}

					}


				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			// Stelle sicher, dass der BufferedReader geschlossen wird
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		try {
			sortAndPrintAndWriteByNormalScore(mapPlayer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	    


		private static String defineGameID(String curLine) {
			String[] splitted = curLine.split("\t");
			return splitted[3].trim();
		}
		
		private static void definePlayers(Map<String, Player> mapPlayer,String curLine) {
			String[] splitted = curLine.split("\t");
			String a1 = splitted[0].trim();
			String split3 = splitted[2].trim(); //Player Game ID
			String ObjectID = splitted[3].trim(); //player,target etc
			String playerName = splitted[4].trim(); //Player name
			String split6 = splitted[5].trim(); //Team Index
			String level = splitted[6].trim(); //lvl
			String category = splitted[7].trim(); //category
			String battlesuiteName = splitted[8].trim(); //Battlesuite Name
			
			Integer teamIndex = Integer.valueOf(split6);
			
			if("player".equals(ObjectID)) {
				Player player = null;
				if(!mapPlayer.containsKey(split3)) {
					 player = new Player();
					 player.setAnzahlSpiele(1);
				}
				else {
					player = mapPlayer.get(split3);
					player.setAnzahlSpiele(player.getAnzahlSpiele()+1);
				}
				
				player.setUniqueID(split3);
				player.setTeamIndex(teamIndex);
				player.setName(playerName);
				player.setBattlesuiteName(battlesuiteName);
				player.setLevel(Integer.valueOf(level) + 1); //0 basiert --> +1
				mapPlayer.put(split3, player);
			}
			
		}
		
		private static void sortAndPrintAndWriteByNormalScore(Map<String, Player> mapPlayer) throws IOException {
			System.out.println("------------ SCORE NORMAL GAMES ------------");
		
			
			List<Player> listPlayer = new ArrayList<>();
			
			for (var entry : mapPlayer.entrySet()) {
				//   System.out.println(entry.getKey() + "/" + entry.getValue());
				Player player = entry.getValue();
				//System.out.println(player.getScore().size());
				int absoluteScore = 0;
				int countGames = 1;
				for (var entryScore : player.getScore().entrySet()) {
					String fixGameID = entryScore.getKey();
					Integer fixGameScore = entryScore.getValue();
					absoluteScore = absoluteScore + fixGameScore;
					player.setAbsoluteScore(absoluteScore);
					player.setAnzahlSpiele(countGames++);

					//				    	if(player.getName().equals("[Toxic] Fussel"))
					//				    	System.out.println("Name: " + player.getName() + " / SpielID: " + fixGameID + " / Score: " + fixGameScore + " / ScoreGesamt:" +  absoluteScore);
				}

				//System.out.println("Name: " + player.getName() + " | Anzahl Spiele: " + player.getAnzahlSpiele() + " | Score: " + absoluteScore);
				
				if(player.getAnzahlSpiele() > 0) {
					player.setDurschnitt(absoluteScore / player.getAnzahlSpiele());
				}
				
				listPlayer.add(player);
			}

			
			
			Comparator<Player> byName = (Player obj1, Player obj2) -> obj2.getDurschnitt().compareTo(obj1.getDurschnitt());

			Map<Object, Object> sortedMap = mapPlayer.entrySet().stream()
					.sorted(Map.Entry.<String, Player>comparingByValue(byName))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

			Set set = sortedMap.entrySet();
			Iterator iterator = set.iterator();
			
			while (iterator.hasNext()) {
				Map.Entry me2 = (Map.Entry) iterator.next();
				if(mapPlayer.get(me2.getKey()).getUniqueID().startsWith("#")) {
					if(mapPlayer.get(me2.getKey()).getDurschnitt() == 0 && mapPlayer.get(me2.getKey()).getAnzahlSpiele() > 0 ) {
						continue;
					}
					
					String name = "Name: " + mapPlayer.get(me2.getKey()).getName();
					String games = "Anzahl Spiele: " + mapPlayer.get(me2.getKey()).getAnzahlSpiele();
					String score = "Absoluter Score " + mapPlayer.get(me2.getKey()).getAbsoluteScore();
					String average = "Durschnitt: " + mapPlayer.get(me2.getKey()).getDurschnitt();
					String cutter = "---------------------------------------------------";
					
					System.out.println(games);
					System.out.println(name);
					System.out.println(score);
					System.out.println(average);
					System.out.println(cutter);
					
					
				}

				// System.out.println("Anzal"mapPlayer.get(me2.getKey()).getAbsoluteScore() + "| Durschnitt: " + (mapPlayer.get(me2.getKey()).getDurschnitt()));
			}
			
		}
	}
	
	

