package tournament;

import java.util.HashMap;
import java.util.Map;

public class Player {

	private String uniqueID;
	private String name;
	private int level;
	private int teamIndex;
	private int colorCode;
	private String battlesuiteName;
	private Integer absoluteScore;
	private int anzahlSpiele;
	private Integer durschnitt;

	private Map<String,Integer> score; //= new HashMap<String, Integer>();
	

	public Player(){
		//leer
	}
	

	public Player(String name){
		this.name = name;
	}
	
	public Integer getDurschnitt() {
		return durschnitt;
	}

	public void setDurschnitt(Integer durschnitt) {
		this.durschnitt = durschnitt;
	}
	
	public int getAnzahlSpiele() {
		return anzahlSpiele;
	}

	public void setAnzahlSpiele(int anzahlSpiele) {
		this.anzahlSpiele = anzahlSpiele;
	}
	
	public Map<String, Integer> getScore() {
		return score;
	}

	public Integer getAbsoluteScore() {
		return absoluteScore;
	}

	public void setAbsoluteScore(Integer absoluteScore) {
		this.absoluteScore = absoluteScore;
	}

	public void setScore(Map<String, Integer> score) {
		this.score = score;
	}
	
	public String getUniqueID() {
		return uniqueID;
	}
	public void setUniqueID(String uniqueID) {
		this.uniqueID = uniqueID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getTeamIndex() {
		return teamIndex;
	}
	public void setTeamIndex(int teamIndex) {
		this.teamIndex = teamIndex;
	}
	public int getColorCode() {
		return colorCode;
	}
	public void setColorCode(int colorCode) {
		this.colorCode = colorCode;
	}


	public String getBattlesuiteName() {
		return battlesuiteName;
	}


	public void setBattlesuiteName(String battlesuiteName) {
		this.battlesuiteName = battlesuiteName;
	}

}