package tournament;

import java.util.List;

class Game {
	
	private int gameNumber;
    private List<Player> players;
   
    
    // Getter und Setter

    public int getGameNumber() {
		return gameNumber;
	}

	public void setGameNumber(int gameNumber) {
		this.gameNumber = gameNumber;
	}

	public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}