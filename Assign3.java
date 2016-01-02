/*
Madhur Malhotra
MoWe 6:00 - 7:15 PM
10/29/15
*/
import java.util.*;
import java.io.*;
public class Assign3
{
	public static void main(String[] args)
	{
		new Assign3(); // Calls Assig3 which is the word search method that then calls the recursive method for each word in phrase
	}

	/* Method that outprints the grid, updates which words have been found, and continues to 
	call the recursive method while there are still words left to find in the phrase. */  
	public Assign3()
	{
		Scanner keyboard = new Scanner(System.in); // Scanner for user input
		Scanner input; // Scanner for file input
		File file;
		String fileName = "", currPhrase = "";
		char [][] grid; // game board 2D array
		String [] words, dimensions;
		String result;
		int rows, cols, count = 0;

		while (true) // Runs to make sure a valid file is input.
		{
			try
			{
				System.out.println("Please enter grid file name (with .txt): ");
				fileName = keyboard.nextLine();
				file = new File(fileName);
				input = new Scanner(file); // Initializes scanner for file input

				break;
			}
			catch (IOException e) // Catch for if file is not found
			{
				System.out.println("That is not a valid file.");
			}
		}

		dimensions = (input.nextLine()).split(" "); // Creates String array for dimensions of grid given in file
		rows = Integer.parseInt(dimensions[0]); // Uses first entry in dimensions array to set how many rows
		cols = Integer.parseInt(dimensions[1]); // Uses second entry in dimensions array to set number of columns

		grid = new char[rows][cols]; // Sets actual dimensions of grid 2D array

		for (int i = 0; i < rows; i++) // Nested for loop uses input from file to set each slot in 2D grid array
		{
			String gridRow = input.nextLine(); // Reads in full line of file
			for (int j = 0; j < gridRow.length(); j++)
			{
				grid[i][j] = Character.toLowerCase(gridRow.charAt(j)); // Sets grid slot as j position of full line
			}
		}

		for (int i = 0; i < rows; i++) // Nested for loop that outprints the grid based on how many rows and columns were specified
		{
			for (int j = 0; j < cols; j++)
			{
				System.out.print(grid[i][j] + " "); // Outprints letter and space to create visual grid
				grid[i][j] = Character.toLowerCase(grid[i][j]); // Sets all letters to lower case
			}
			System.out.println(); // Next row
		}

		System.out.println("Please enter the phrase to search for (words separated with single space): ");
		currPhrase = (keyboard.nextLine()).toLowerCase(); // Reads in full phrase user wants to search for
		while (!currPhrase.equals(""))
		{
			StringBuilder output = new StringBuilder(); // Used to keep track of what the output will be during the findWord method
			result = null; // Initially, result is null because word has not been found
			String [] splitWords = currPhrase.split(" "); // Splits words from specified array into String array where each index is a word
			words = new String[(splitWords.length + 1)];
			for (int index = 0; index < splitWords.length; index++)
			{
				words[index] = splitWords[index]; // Copies split array into the array of words to search for
			}
			words[splitWords.length] = null;

			System.out.println("Looking for: " + currPhrase);
			System.out.println("Containing " + (words.length - 1) + " words");

			for (int r = 0; (r < rows && (result == null)); r++) // Checks each slot in grid for the current word by calling recursive findWord
			{
				for (int c = 0; (c < cols && (result == null)); c++)
				{
					if (grid[r][c] == currPhrase.charAt(0)) // Calls recursive method if the current letter in grid matches the first letter of the current word
						result = findWord(r, c, words[0], 1, grid, 0, words, output);
				}
			}
			
			if (result != null) // If the word was found and a String was returned
			{
				System.out.println("The phrase: " + currPhrase);
				System.out.println("was found: ");
				System.out.println(result); // Outprints starting and ending coordinates of each word that was found

				for (int i = 0; i < rows; i++) // Nested for loop outprints the grid with the found words in capital letters
				{
					for (int j = 0; j < cols; j++)
					{
						System.out.print(grid[i][j] + " ");
						grid[i][j] = Character.toLowerCase(grid[i][j]);
					}
					System.out.println();
				}
			}
			else // If word was not found, the user is notified
			{
				System.out.println("The phrase: " + currPhrase);
				System.out.println("was not found.");
			}

			System.out.println(" ");
			System.out.println("Please enter the phrase to search for (words separated with single space): ");
			currPhrase = (keyboard.nextLine()).toLowerCase(); // Changes phrase to look for and program reruns
		}
		if (currPhrase == "") // Ends program if user does not enter any word or phrase
		{
			System.exit(0);
		}
	}

	/* findWord is the recursive method that uses both recursion and backtracking to find the current
	word specified by the Assig3 method. It returns a string containing the word, starting coordinates, 
	and ending coordinates of the current word if it was found. If the word was not found, it returns
	null. */
	public String findWord(int row, int col, String curWord, int location, char [][] board, int index, String [] phrase, StringBuilder answer)
	{
		String outcome = null; // Originally, outcome is null because word has not been found
		if (row >= board.length || row < 0 || col >= board[0].length || col < 0) // Runs if the row and/or col are out of the grid bounds
			return null; 
		else if (curWord == null) // Runs if there are no more words to search for -- base case
			return answer.toString();
		else // Runs if there is a word to look for and it is in the proper bounds
		{
			StringBuilder right = new StringBuilder();// Lines 145-148 create StringBuilders for the four directions a word can extend so that the current word can be compared to them and be found or not
			StringBuilder down = new StringBuilder();
			StringBuilder left = new StringBuilder();
			StringBuilder up = new StringBuilder();

			if (location == 1) // Runs if location is 1, i.e. is the recursive call from the Assig3 method
			{
				for (int i = 0; i < curWord.length(); i++) // Adds letters to the up "word" so it is the same length as the current word
				{
					if (row >= board.length || (row - i) < 0 || col >= board[0].length || col < 0) // If out of bounds stop adding letters
						break;
					else
						up.append(board[row-i][col]);
				}

				for (int j = 0; j < curWord.length(); j++) // Adds letters to the down "word" so it is the same length as the current word
				{
					if ((row + j) >= board.length || row < 0 || col >= board[0].length || col < 0) // If out of bounds stop adding letters
						break;
					else
						down.append(board[row+j][col]);
				}

				for (int k = 0; k < curWord.length(); k++) // Adds letters to the left "word" so it is the same length as the current word
				{
					if (row >= board.length || row < 0 || col >= board[0].length || (col - k) < 0) // If out of bounds stop adding letters
						break;
					else
						left.append(board[row][col-k]);
				}

				for (int l = 0; l < curWord.length(); l++) // Adds letters to the right "word" so it is the same length as the current word
				{
					if (row >= board.length || row < 0 || (col + l) >= board[0].length || col < 0) // If out of bounds stop adding letters
						break;
					else
						right.append(board[row][col+l]);
				}
			}
			else // Runs for all recursive calls after initial call
			{
				for (int i = 0; i < curWord.length(); i++) // Adds letters to the up "word" so it is the same length as the current word
				{
					int newRow = (row - i - 1);
					if (row >= board.length || newRow < 0 || col >= board[0].length || col < 0) // If out of bounds stop adding letters
						break;
					else
						up.append(board[newRow][col]);
				}

				for (int j = 0; j < curWord.length(); j++) // Adds letters to the down "word" so it is the same length as the current word
				{
					int newRow = (row + j + 1);
					if (newRow >= board.length || row < 0 || col >= board[0].length || col < 0) // If out of bounds stop adding letters
						break;
					else
						down.append(board[newRow][col]);
				}

				for (int k = 0; k < curWord.length(); k++) // Adds letters to the left "word" so it is the same length as the current word
				{
					int newCol = (col - k - 1);
					if (row >= board.length || row < 0 || col >= board[0].length || newCol < 0) // If out of bounds stop adding letters
						break;
					else
						left.append(board[row][newCol]);
				}

				for (int l = 0; l < curWord.length(); l++) // Adds letters to the right "word" so it is the same length as the current word
				{
					int newCol = (col + l + 1);
					if (row >= board.length || row < 0 || newCol >= board[0].length || col < 0) // If out of bounds stop adding letters
						break;
					else
						right.append(board[row][newCol]);
				}
			}

			if (right.toString().equals(curWord) && (outcome == null)) // Checks if the word is contained in the grid to the right of the starting letter first
			{
				if (location == 1) // If original call from Assig3
				{
					for (int i = 0; i < curWord.length(); i++) // Changes letters of found word to upper case
					{
						board[row][col+i] = Character.toUpperCase(board[row][col+i]);
					}

					answer.append(curWord + ": (" + row + "," + col + ") to (" + row + "," + (col + curWord.length() - 1) + ")" + "\n"); // Appends to return answer the word and coordinates of the found word
					outcome = findWord(row, col + curWord.length() - 1, phrase[index+1], 2, board, index + 1, phrase, answer); // Recursive call to find next word

					if (outcome == null) // If the next word was not found, backtracking code
					{
					 	for (int i = 0; i < curWord.length(); i++)
						{
							board[row][col+i] = Character.toLowerCase(board[row][col+i]); // Change letters of previous word back to lower case
						}
						
						if (row > 9 && col > 9 && (col + curWord.length() - 1) > 9) // Lines 242-249 delete previous word based on length of word and dimension of grid
							answer.delete((answer.length() - 21 - curWord.length()), answer.length());
						else if (row > 9 && (col + curWord.length() - 1) > 9)
							answer.delete((answer.length() - 20 - curWord.length()), answer.length());
						else if ((col + curWord.length() - 1) > 9)
							answer.delete((answer.length() - 18 - curWord.length()), answer.length());
						else
							answer.delete((answer.length() - 17 - curWord.length()), answer.length());
					}
				}
				else // If recursive call from findWord
				{
					for (int i = 0; i < curWord.length(); i++) // Changes letters of found word to upper case
					{
						int newCol = (col + i + 1);
						board[row][newCol] = Character.toUpperCase(board[row][newCol]);
					}

					answer.append(curWord + ": (" + row + "," + (col+1) + ") to (" + row + "," + (col + curWord.length()) + ")" + "\n"); // Appends to return answer the word and coordinates of the found word
					outcome = findWord(row, col + curWord.length(), phrase[index+1], 2, board, index + 1, phrase, answer); // Recursive call to find next word
				
					if (outcome == null) // Backtracking code if next word was not found
					{
					 	for (int i = 0; i < curWord.length(); i++) // Changes letters from previous word back to lower case
						{
							int newCol = (col + i + 1);
							board[row][newCol] = Character.toLowerCase(board[row][newCol]);
						}
						
						if (row > 9 && (col+1) > 9 && (col + curWord.length()) > 9) // Lines 271-278 delete previous word based on length of word and dimension of grid
							answer.delete((answer.length() - 21 - curWord.length()), answer.length());
						else if (row > 9 && (col + curWord.length()) > 9)
							answer.delete((answer.length() - 20 - curWord.length()), answer.length());
						else if ((col + curWord.length()) > 9)
							answer.delete((answer.length() - 18 - curWord.length()), answer.length());
						else
							answer.delete((answer.length() - 17 - curWord.length()), answer.length());
					}
				}
			}

			// Method description and comments are the same as comments for code to check for word to the right
			if (down.toString().equals(curWord) && (outcome == null)) // Second, checks for the word in the downward direction of the first letter
			{
				if (location == 1)
				{
					for (int i = 0; i < curWord.length(); i++)
					{
						board[row+i][col] = Character.toUpperCase(board[row+i][col]);
					}

					answer.append(curWord + ": (" + row + "," + col + ") to (" + (row + curWord.length() - 1) + "," + col + ")" + "\n");
					outcome = findWord(row + curWord.length() - 1, col, phrase[index+1], 2, board, index + 1, phrase, answer);
				
					if (outcome == null) // Backtracking code
					{
						for (int i = 0; i < curWord.length(); i++)
						{
							board[row+i][col] = Character.toLowerCase(board[row+i][col]);
						}
						
						if (row > 9 && col > 9 && (row + curWord.length() - 1) > 9)
							answer.delete((answer.length() - 21 - curWord.length()), answer.length());
						else if ((row + curWord.length() - 1) > 9 && col > 9)
							answer.delete((answer.length() - 20 - curWord.length()), answer.length());
						else if ((row + curWord.length() - 1) > 9)
							answer.delete((answer.length() - 18 - curWord.length()), answer.length());
						else
							answer.delete((answer.length() - 17 - curWord.length()), answer.length());
					}
				}
				else
				{
					for (int i = 0; i < curWord.length(); i++)
					{
						int newRow = (row + i + 1);
						board[newRow][col] = Character.toUpperCase(board[newRow][col]);
					}

					answer.append(curWord + ": (" + (row+1) + "," + col + ") to (" + (row + curWord.length()) + "," + col + ")" + "\n");
					outcome = findWord(row + curWord.length(), col, phrase[index+1], 2, board, index + 1, phrase, answer);
				
					if (outcome == null) // Backtracking code
					{
						for (int i = 0; i < curWord.length(); i++)
						{
							int newRow = (row + i + 1);
							board[newRow][col] = Character.toLowerCase(board[newRow][col]);
						}
						
						if ((row + 1) > 9 && col > 9 && (row + curWord.length()) > 9)
							answer.delete((answer.length() - 21 - curWord.length()), answer.length());
						else if ((row + curWord.length()) > 9 && col > 9)
							answer.delete((answer.length() - 20 - curWord.length()), answer.length());
						else if ((row + curWord.length()) > 9)
							answer.delete((answer.length() - 18 - curWord.length()), answer.length());
						else
							answer.delete((answer.length() - 17 - curWord.length()), answer.length());
					}
				}
			}

			// Method description and comments are the same as comments for code to check for word to the right
			if (left.toString().equals(curWord) && (outcome == null)) // Thirdly, checks for the word in the left direction of the first letter of the word
			{
				if (location == 1)
				{
					for (int i = 0; i < curWord.length(); i++)
					{
						board[row][col-i] = Character.toUpperCase(board[row][col-i]);
					}

					answer.append(curWord + ": (" + row + "," + col + ") to (" + row + "," + (col - curWord.length() + 1) + ")" + "\n");
					outcome = findWord(row, col - curWord.length() + 1, phrase[index+1], 2, board, index + 1, phrase, answer);
				
					if (outcome == null) // Backtracking code
					{
						for (int i = 0; i < curWord.length(); i++)
						{
							board[row][col-i] = Character.toLowerCase(board[row][col-i]);
						}
						
						if (row > 9 && col > 9 && (col - curWord.length() + 1) > 9)
							answer.delete((answer.length() - 21 - curWord.length()), answer.length());
						else if (row > 9 && col > 9)
							answer.delete((answer.length() - 20 - curWord.length()), answer.length());
						else if (col > 9)
							answer.delete((answer.length() - 18 - curWord.length()), answer.length());
						else
							answer.delete((answer.length() - 17 - curWord.length()), answer.length());
					}
				}
				else
				{
					for (int i = 0; i < curWord.length(); i++)
					{
						int newCol = (col - i - 1);
						board[row][newCol] = Character.toUpperCase(board[row][newCol]);
					}

					answer.append(curWord + ": (" + row + "," + (col-1) + ") to (" + row + "," + (col - curWord.length()) + ")" + "\n");
					outcome = findWord(row, col - curWord.length(), phrase[index+1], 2, board, index + 1, phrase, answer);
				
					if (outcome == null) // Backtracking code
					{
						for (int i = 0; i < curWord.length(); i++)
						{
							int newCol = (col - i - 1);
							board[row][newCol] = Character.toLowerCase(board[row][newCol]);
						}
						
						if (row > 9 && (col - 1) > 9 && (col - curWord.length()) > 9)
							answer.delete((answer.length() - 21 - curWord.length()), answer.length());
						else if (row > 9 && (col - 1) > 9)
							answer.delete((answer.length() - 20 - curWord.length()), answer.length());
						else if ((col - 1) > 9)
							answer.delete((answer.length() - 18 - curWord.length()), answer.length());
						else
							answer.delete((answer.length() - 17 - curWord.length()), answer.length());
					}
				}
			}

			// Method description and comments are the same as comments for code to check for word to the right
			if (up.toString().equals(curWord) && (outcome == null)) // Lastly, checks for the word upwards of the first letter of the word
			{
				if (location == 1)
				{
					for (int i = 0; i < curWord.length(); i++)
					{
						board[row-i][col] = Character.toUpperCase(board[row-i][col]);
					}

					answer.append(curWord + ": (" + row + "," + col + ") to (" + (row - curWord.length() + 1) + "," + col + ")" + "\n");
					outcome = findWord(row - curWord.length() + 1, col, phrase[index+1], 2, board, index + 1, phrase, answer);
				
					if (outcome == null) // Backtracking code
					{
						for (int i = 0; i < curWord.length(); i++)
						{
							board[row-i][col] = Character.toLowerCase(board[row-i][col]);
						}
						
						if (row > 9 && col > 9 && (row - curWord.length() + 1) > 9)
							answer.delete((answer.length() - 21 - curWord.length()), answer.length());
						else if (row > 9 && col > 9)
							answer.delete((answer.length() - 20 - curWord.length()), answer.length());
						else if (row > 9)
							answer.delete((answer.length() - 18 - curWord.length()), answer.length());
						else
							answer.delete((answer.length() - 17 - curWord.length()), answer.length());
					}
				}
				else
				{
					for (int i = 0; i < curWord.length(); i++)
					{
						int newRow = (row - i - 1);
						board[newRow][col] = Character.toUpperCase(board[newRow][col]);
					}

					answer.append(curWord + ": (" + (row-1) + "," + col + ") to (" + (row - curWord.length()) + "," + col + ")" + "\n");
					outcome = findWord(row - curWord.length(), col, phrase[index+1], 2, board, index + 1, phrase, answer);
				
					if (outcome == null) // Backtracking code
					{
						for (int i = 0; i < curWord.length(); i++)
						{
							int newRow = (row - i - 1);
							board[newRow][col] = Character.toLowerCase(board[newRow][col]);
						}
						
						if ((row - 1) > 9 && col > 9 && (row - curWord.length()) > 9) 
							answer.delete((answer.length() - 21 - curWord.length()), answer.length());
						else if ((row - 1) > 9 && col > 9)
							answer.delete((answer.length() - 20 - curWord.length()), answer.length());
						else if ((row - 1) > 9)
							answer.delete((answer.length() - 18 - curWord.length()), answer.length());
						else
							answer.delete((answer.length() - 17 - curWord.length()), answer.length());
					}
				}
			}
		}
		return outcome; // Returns the final string that is the collection of all the words found and all of their start/end coordinates
		// If one word was not found, whole phrase was not found and null is returned
	}
}