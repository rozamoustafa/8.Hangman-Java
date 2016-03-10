package hangman;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileReadWriter {
	private ObjectOutputStream output;
	private ObjectInputStream input;
	ArrayList<Players> myArray = new ArrayList<Players>();

	public void openFileToWrite() {
		try {
			// open file
			output = new ObjectOutputStream(new FileOutputStream("players.ser", true));
		} catch (IOException ioException) {
			System.err.println("Error opening file.");
		}
	}

	// add records to file
	public void addRecords(int scores, String name) {
		// object to be written to file
		Players players = new Players(name, scores); 

		try {
			// output values to file
			output.writeObject(players); // output players
		} catch (IOException ioException) {
			System.err.println("Error writing to file.");
			return;
		}
	}

	public void closeFileFromWriting() {
		try {
			// close file
			if (output != null) {
				output.close();
			}
		} catch (IOException ioException) {
			// show error
			System.err.println("Error closing file.");
			// exit
			System.exit(1);
		}
	}

	//open the file to get the records
	public void openFiletoRead() {
		try {
			if (true) {
				input = new ObjectInputStream(new FileInputStream("players.ser"));
			}
		} catch (IOException ioException) {
			System.err.println("Error opening file.");
		}
	}

	//shows array with records including player name and score
	public void readRecords() {
		Players records;

		try {
			// input the values from the file
			Object obj = null;

			while (!(obj = input.readObject()).equals(null) && obj instanceof Players) {				
					records = (Players) obj;
					myArray.add(records);
					System.out.printf("DEBUG: %-10d%-12s\n", records.getScores(), records.getName());			
			}			 
		} catch (EOFException endOfFileException) {
			return; // end of file was reached
		} catch (ClassNotFoundException classNotFoundException) {
			System.err.println("Unable to create object.");
		} catch (IOException ioException) {
			System.err.println("Error during reading from file.");
		}
	}

	//sort the score board, shows top score
	public void printAndSortScoreBoard() {
		Players temp;
		int n = myArray.size();

		for (int pass = 1; pass < n; pass++) {
			for (int i = 0; i < n - pass; i++) {
				if (myArray.get(i).getScores() > myArray.get(i + 1).getScores()) {
					temp = myArray.get(i);
					myArray.set(i, myArray.get(i + 1));
					myArray.set(i + 1, temp);
				}
			}
		}
		scoreboard();
	}
	
	//print out the score board to player
	public void scoreboard(){
		System.out.println("Scoreboard:");
		for (int i = 0; i < myArray.size(); i++) {
			System.out.printf("%d. %s ----> %d", i, myArray.get(i).getName(), myArray.get(i).getScores());
		}
	}

	//closing file
	public void closeFileFromReading() {
		try {
			if (input != null) {
				input.close();
			}
			System.exit(0);
		} catch (IOException ioException) {
			System.err.println("Error closing file.");
			System.exit(1);
		}
	}
}
