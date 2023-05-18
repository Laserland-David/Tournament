package tournament;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GenerateJSON {
	
	private List<List<String>> splittedLists = new ArrayList<>();

	public GenerateJSON(List<List<String>> splittedLists) {
		this.splittedLists = splittedLists;
	}
	
	public GenerateJSON() {
		// TODO Auto-generated constructor stub
	}
	
	public List<List<String>> getSplittedLists() {
		return splittedLists;
	}


	public void setSplittedLists(List<List<String>> splittedLists) {
		this.splittedLists = splittedLists;
	}



	
	public void initTournament(){
		List<Player> playerList = null;
		List<Game> games = new ArrayList<>();
        for (int i = 0; i < splittedLists.size(); i++) {
        	List<String> splittedList = splittedLists.get(i);
        	int GameCount = i + 1;
        	
        	Game gameX = new Game();
        	gameX.setGameNumber(GameCount);
        	
        	playerList = new ArrayList<>();
        	for (String playerName : splittedList) {
        		Player player = new Player(playerName);
        		playerList.add(player);
			}
        	gameX.setPlayers(playerList);
        	games.add(gameX);
        }

		generateFile(games);
	}
	
	public void generateFile(List<Game> games) {

		GamesData gamesData = new GamesData();
	    gamesData.setGames(games);
		
		// Erstelle einen Gson-Builder
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setPrettyPrinting(); // Formatierung der JSON-Datei

		// Erzeuge ein Gson-Objekt
		Gson gson = gsonBuilder.create();

		// Konvertiere das Objekt in JSON-Format
		String json = gson.toJson(gamesData);

		// Definiere den Dateipfad und Dateinamen
		String filePath = "C:\\LL_Bautzen\\Programming\\Files\\Datei2.json";

		try (FileWriter fileWriter = new FileWriter(filePath)) {
			// Schreibe das JSON in die Datei
			fileWriter.write(json);
			System.out.println("JSON-Datei erfolgreich erstellt.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	  static class GamesData {
	        private List<Game> games;

	        // Getter und Setter

	        public List<Game> getGames() {
	            return games;
	        }

	        public void setGames(List<Game> games) {
	            this.games = games;
	        }
	    }
}
