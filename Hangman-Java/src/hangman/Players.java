package hangman;

import java.io.Serializable;

public class Players implements Serializable {
	
	//player information
	private String name;
	private int scores;

	//constructor
	public Players(String name, int scores) {
		this.name = name;
		this.scores = scores;
	}

	//get player name
	public String getName() {
		return name;
	}

	//get player score
	public int getScores() {
		return scores;
	}

}
