package hangman;

import java.util.Random;
import java.util.Scanner;

public class Game {

	private String guessWord;
	private StringBuffer dashedWord;
	private FileReadWriter fileReadWriter;
	boolean isHelpUsed = false;
	private static final String[] wordForGuessing = { "computer", "programmer", "software", "debugger", "compiler",
			"developer", "algorithm", "array", "method", "variable" };

	//start the game and display menu
	public Game(boolean autoStart) {
		guessWord = getRandWord();
		dashedWord = getWord(guessWord);
		fileReadWriter = new FileReadWriter();
		if (autoStart) {
			displayMenu();
		}
	}

	//output the words in a random order
	private String getRandWord() {
		Random rand = new Random();
		String randWord = wordForGuessing[rand.nextInt(9)];
		return randWord;
	}

	//A description of the start menu
	public void displayMenu() {
		System.out.println("Welcome to Hangman game. Please, try to guess my secret word.\n"
				+ "Write 'top' to view the top scoreboard, 'restart' to start a new game,"
				+ "'help' to cheat and 'EXIT' to quit the game.");
		findLetterAndPrintIt();
	}
	
	//Set the letter in the right place on dashed line
	public void setLetter(StringBuffer wordLetter) {
		isHelpUsed = true;
		int i = 0, j = 0;
		while (j < 1) {
			if (wordLetter.charAt(i) == '_') {
				wordLetter.setCharAt(i, guessWord.charAt(i));
				++j;
			}
			++i;
		}	
	}

	//Returns the number of places where the guessed letter is right
	public int countRightLetter(String letterInput, StringBuffer wordLetter){
		int counter = 0;
		for (int i = 0; i < guessWord.length(); i++) {
			String currentLetter = Character.toString(guessWord.charAt(i));
			if (letterInput.equals(currentLetter)) {
				counter++;
				wordLetter.setCharAt(i, letterInput.charAt(0));
			}
		}
		return counter;
	}
	
	//Game execution 
	private void findLetterAndPrintIt() {
		String letter = "";
		StringBuffer dashBuff = new StringBuffer(dashedWord);
		int mistakes = 0;
		
		//Writes out if the letter right or wrong
		do {
			System.out.println("The secret word is: " + printDashes(dashBuff));
			//If the input is valid, writes out the dashed line
			do {
				System.out.println("Enter your guess(1 letter alowed): ");
				letter = new Scanner(System.in).next();

				if (letter.equals(Command.help.toString())) {
					setLetter(dashBuff);
					System.out.println("The secret word is: " + printDashes(dashBuff));
				}
				menu(letter);
			}while (!letter.matches("[a-z]")); //checks if the input is valid

			if (countRightLetter(letter, dashBuff) == 0) {
				++mistakes;
				System.out.printf("Sorry! There are no unrevealed letters \'%s\'. \n", letter);
			} else {
				System.out.printf("Good job! You revealed %d letter(s).\n", countRightLetter(letter, dashBuff));
			}
		} while (!dashBuff.toString().equals(guessWord));
		
		finishedWord(isHelpUsed, mistakes, dashBuff);
		// restart the game
		new Game(true);
	}// end method

	//Tells how the game went when the word is completed 
	public void finishedWord(boolean cheated, int numberOfMistakes, StringBuffer wordLetter ) {
		if (cheated == false) {
			System.out.println("You won with " + numberOfMistakes + " mistake(s).");
			System.out.println("The secret word is: " + printDashes(wordLetter));
			System.out.println("Please enter your name for the top scoreboard:");
			String playerName = new Scanner(System.in).next();
			
			//keeping track of the records
			fileReadWriter.openFileToWrite();
			fileReadWriter.addRecords(numberOfMistakes, playerName);
			fileReadWriter.closeFileFromWriting();
			fileReadWriter.openFiletoRead();
			fileReadWriter.readRecords();
			fileReadWriter.closeFileFromReading();
			fileReadWriter.printAndSortScoreBoard();
		} else {
			System.out.println("You won with " + numberOfMistakes
					+ " mistake(s). but you have cheated. You are not allowed to enter into the scoreboard.");
			System.out.println("The secret word is: " + printDashes(wordLetter));
		}
	}
	
	//makes the game possible to restart, show top score and exit
	private void menu(String letter) {
		if (letter.equals(Command.restart.toString())) {
			new Game(true);
		} else if (letter.equals(Command.top.toString())) {
				//show records
				fileReadWriter.openFiletoRead();
				fileReadWriter.readRecords();
				fileReadWriter.closeFileFromReading();
				fileReadWriter.printAndSortScoreBoard();
				new Game(true);
		} else if (letter.equals(Command.exit.toString())) {
				System.exit(1); 
		}
	}
		
	//Writes out the dashes 
	private StringBuffer getWord(String word) {
		StringBuffer dashes = new StringBuffer("");
		for (int i = 0; i < word.length(); i++) {
			dashes.append("_");
		}
		return dashes;
	}

	//Writes out word on the dashes
	private String printDashes(StringBuffer word) {
		String toDashes = "";
		for (int i = 0; i < word.length(); i++) {
			toDashes += (" " + word.charAt(i));
		}
		return toDashes;
	}
}
