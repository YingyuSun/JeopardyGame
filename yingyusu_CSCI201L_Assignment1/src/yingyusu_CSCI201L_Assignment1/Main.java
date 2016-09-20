package yingyusu_CSCI201L_Assignment1;

import java.io.File;
import java.util.Scanner;
import java.io.IOException;

public class Main {
	private static String[] categories = null;
	private static int[] points = null;
	public static Question[][] questions = new Question[5][5];
	public static FinalJeopardy finalQuestion = null;
	
	// reading file line by line
	// line 1: categories, separated by ::s, must show error if more or less than 5 categories
	// line 2: points, separated by ::s, must show error if more or less than 5 categories
	// next lines: questions (::category::points::question::answer)
	// final jeopardy question can be anywhere except first line
	private static void readFile(File file) {
		int numQuestions = 0;
		int numFJ = 0;
		
		Scanner input = null;
		try {
			input = new Scanner(file);
			int lineNum = 0;
			while (input.hasNextLine()) {
				String line = input.nextLine(); // unparsed line
				
				if (line.trim().isEmpty()) {
					System.out.println("File has blank line.");
					System.exit(1);
				}
				
				if (lineNum == 0) { //reading categories
					String[] temp = line.split("::");
					if(temp[0].equals("")) {
						System.out.println("First line must hold categories.");
						System.exit(1);
					}
					categories = temp;
					
					if(categories.length != 5) {
						System.out.println("File does not have five categories.");
						System.exit(1);
					}
					for (int i = 0; i < categories.length; i++) {
						for (int j = i+1; j < categories.length; j++) {
							if ((categories[i].toLowerCase()).equals(categories[j].toLowerCase())) {
								System.out.println("File contains duplicate category.");
								System.exit(1);
							}
						}
					}
				}
				if (lineNum == 1) { // reading points
					String[] temp = line.split("::");
					if(temp[0].equals("")) {
						System.out.println("Second line must hold point values.");
						System.exit(1);
					}
					int[] tempPoints = new int[temp.length];
					for (int i = 0; i < temp.length; i++) {
						try {
							int num = Integer.parseInt(temp[i]);
							if (num <= 0) {
								System.out.println("Point values must be greater than zero.");
								System.exit(1);
							}
							tempPoints[i] = num;
						}
						catch (NumberFormatException nfe) {
							System.out.println("Points must be numeric values.");
							System.exit(1);
						}
					}
					points = tempPoints;

					if(points.length != 5) {
						System.out.println("File does not have five point values.");
						System.exit(1);
					}
					for (int i = 0; i < points.length; i++) {
						for (int j = i+1; j < points.length; j++) {
							if (points[i] == points[j]) {
								System.out.println("File contains duplicate point values.");
								System.exit(1);
							}
						}
					}
				}
			else if (lineNum > 1) {
					// start reading questions and answers
					if (line.trim().isEmpty()) {
						System.out.println("File has blank line.");
						System.exit(1);
					}
					String[] temp = line.split("::");
					
					String category = temp[1];
					
					if (category.equals("FJ")) {
						if (temp.length > 4) {
							System.out.println("Extra information given in final jeopardy question.");
							System.exit(1);
						}
						else if (temp.length < 4) {
							System.out.println("Not enough information given in final jeopardy question.");
							System.exit(1);
						}
						else {
							String question = temp[2];
							String answer = temp[3];
							finalQuestion = new FinalJeopardy(question, answer);
							numFJ++;
						}
					}
					else {
						if (temp.length != 5) {
							System.out.println("Missing information in questions. Make sure each question has a category, point value, and answer.");
							System.exit(1);
						}
						
						boolean containsC = false;
						for (int i = 0; i < categories.length; i++) {
							if ((categories[i].toLowerCase()).equals(category.toLowerCase())) {
								containsC = true;
							}
						}
						
						if (!containsC) {
							System.out.println("Question's category doesn't exist.");
							System.exit(1);
						}
						
						int pointValue = Integer.parseInt(temp[2]);
						boolean containsP = false;
						for (int i = 0; i < points.length; i++) {
							if (points[i] == pointValue) {
								containsP = true;
							}
						}
						if(!containsP) {
							System.out.println("Question's point value doesn't exist.");
							System.exit(1);
						}
						
						String question = temp[3];
						String answer = temp[4];
						
						for (int cat = 0; cat < 5; cat++) {
							if ((categories[cat].toLowerCase()).equals(category.toLowerCase())) {
								for (int pts = 0; pts < 5; pts++) {
									if (points[pts] == pointValue) {
										if (questions[cat][pts]!=null) {
											System.out.println("More than one question has the same category and point value.");
											System.exit(1);
										}
										questions[cat][pts] = new Question(question, answer);
										questions[cat][pts].setChosen(false);
										numQuestions++;
									}
								}
							}
						}
					}
				}
				lineNum++;
			}	
		input.close();
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
			System.out.println("Invalid file.");
			System.exit(1);
		}
		
		if (numFJ < 1) {
			System.out.println("File does not have a final jeopardy question.");
			System.exit(1);
		}
		if (numFJ > 1) {
			System.out.println("File can only have one final jeopardy question.");
			System.exit(1);
		}
		if ((numQuestions + numFJ) != 26) {
			System.out.println("File does not contain 26 questions.");
			System.exit(1);
		}
	}
	
	public static void main(String[] args) {
		if(args.length < 1) {
			System.out.println("Please enter an input file argument.");
			System.exit(1);
		}
		String filename = args[0];
		File file = new File(filename);
		readFile(file);
		
		Game game = new Game(categories, points);
		game.setUp();
		game.play(questions, finalQuestion);

	}
}
