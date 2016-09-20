package yingyusu_CSCI201L_Assignment1;

import java.util.Scanner;
import java.util.Random;

public class Game {
	private static Team[] teams;
	private int numTeams;
	private String[] categoriesSet;
	private int[] pointsSet;
	private String[] pronouns = {"who", "what"};
	private String[] verbs = {"are", "is"};
	Scanner in = new Scanner(System.in);
	
	public Game (String[] categories, int[] points) {
		this.categoriesSet = categories;
		this.pointsSet = points;
	}

	public void setUp() {
		System.out.println("Welcome to jeopardy!");
		System.out.println("Enter the number of teams playing this game: ");
		boolean number = false;
		while(!number) {
			try {
				numTeams = Integer.parseInt(in.nextLine());
				number = true;
			}
			catch (NumberFormatException nfe) {
				System.out.println("Please enter a numeric value. Try again: ");
			}
		}

		while (numTeams < 1 || numTeams > 4) {
			System.out.println("Sorry, only 1-4 teams can play! Please try again: ");
			numTeams = Integer.parseInt(in.nextLine());
		}
		
		Team[] temp = new Team[numTeams];
		for (int i = 0; i < numTeams; i++) {
			System.out.println("Team " + (i+1) + ", what would you like to name yourself?");
			String teamName = in.nextLine();
			temp[i] = new Team(teamName);
			temp[i].setPoints(0);
		}
		teams = temp;
		System.out.println("Thanks! Setting up game...");
	}
	
	public void play(Question[][] questionSet, FinalJeopardy finalQuestion) {
		System.out.println("Let's play!");
		Random rand = new Random();
		int randomNum = rand.nextInt((numTeams - 1 ) + 1) + 1;
		System.out.println("The first team to go is: " + teams[randomNum-1].getName());
		
		int currentTeam = randomNum;
		int questionsLeft = 3;
		while(questionsLeft > 0) {
			String category;
			int points = 0;
			int categoryIndex = 0;
			int pointsIndex = 0;
			boolean chosen = true;
			Question current = null;
			
			while (chosen) {
				System.out.println(teams[currentTeam-1].getName() + ", please choose a category: ");
				
				// check if category exists
				boolean exists = false;
				while(!exists) {
					
					category = in.nextLine();
					if ((category.toLowerCase()).equals("replay")) {
						replay(questionSet, finalQuestion);
					}
					if ((category.toLowerCase()).equals("exit")) {
						System.out.println("Thanks for playing! Exiting game...");
						System.exit(0);
					}
					
					for (int i = 0; i < categoriesSet.length; i++) {
						if ((categoriesSet[i].toLowerCase()).equals(category.toLowerCase())) {
							categoryIndex = i;
							exists = true;
						}
					}
					if (!exists) {
						System.out.println("Invalid category. Please try again: ");
					}
				}
				
				System.out.println("Please choose a point value: ");
				
				exists = false;
				while(!exists) {
					String temp = in.nextLine();
					
					if ((temp.toLowerCase()).equals("replay")) {
						replay(questionSet, finalQuestion);
					}
					if ((temp.toLowerCase()).equals("exit")) {
						System.out.println("Thanks for playing! Exiting game...");
						System.exit(0);
					}
					
					points = Integer.parseInt(temp);
					
					for (int i = 0; i < pointsSet.length; i++) {
						if (pointsSet[i] == points) {
							pointsIndex = i;
							exists = true;
						}
					}
					if (!exists) {
						System.out.println("Invalid point value. Please try again: ");
					}
				}
				
				current = questionSet[categoryIndex][pointsIndex];
				if (current.isChosen() == false) { // reaches an unchosen question
					chosen = false;
					current.setChosen(true);
				}
				else {
					System.out.println("That question has already been chosen. Please try again: ");
				}
			}
		
			System.out.println(current.getQuestion());
			
			boolean correct = false;
			String[] answer = in.nextLine().split(" "); 
			
			if ((answer[0].toLowerCase()).equals("replay")) {
				replay(questionSet, finalQuestion);
			}
			if ((answer[0].toLowerCase()).equals("exit")) {
				System.out.println("Thanks for playing! Exiting game...");
				System.exit(0);
			}
			
			int tries = 1;
			while (tries < 2) {
				if (answer.length > 1) {
					for(int i = 0; i < 2; i++) { // check answer format
						if ((answer[0].toLowerCase()).equals(pronouns[i])) {
							for (int j = 0; j < 2; j++) {
								if((answer[1].toLowerCase()).equals(verbs[j])) {
									correct = true;
								}
							}
						}
					}
				}
				if(correct == false) {
					System.out.println("Please enter your answer in the form of a question. You have one more try: ");
					answer = in.nextLine().split(" ");
					if ((answer[0].toLowerCase()).equals("replay")) {
						replay(questionSet, finalQuestion);
					}
					if ((answer[0].toLowerCase()).equals("exit")) {
						System.out.println("Thanks for playing! Exiting game...");
						System.exit(0);
					}
				}
				tries++;
			}

			for(int i = 0; i < 2; i++) { // check answer format for second try
				if ((answer[0].toLowerCase()).equals(pronouns[i])) {
					for (int j = 0; j < 2; j++) {
						if((answer[1].toLowerCase()).equals(verbs[j])) {
							correct = true;
						}
					}
				}
			}
			
			if (correct) {
				String ans = "";
				for(int i = 2; i < answer.length; i++) {
					if ( i == answer.length - 1) {
						ans = ans + answer[i];
					}
					else {
						ans = ans + answer[i] + " ";
					}
				}

				if (((current.getAnswer().toLowerCase())).equals(ans.toLowerCase())) {
					correct = true;
				}
				else {
					correct = false;
				}
				
				int currentPoints = teams[currentTeam-1].getPoints();
				if (correct) {
					teams[currentTeam-1].setPoints(currentPoints + points);
					System.out.println("Congrats! Your answer is correct. You get " + points + " points added to your total.");
				}
				else {
					teams[currentTeam-1].setPoints(currentPoints - points);
					System.out.println("Sorry, the correct answer is: " + current.getAnswer() + ". You get " + points + " points deducted from your total.");
				}
			}
			else {
				int currentPoints = teams[currentTeam-1].getPoints();
				teams[currentTeam-1].setPoints(currentPoints - points);
				System.out.println("Sorry, you didn't answer in the form of a question. You get " + points + " points deducted from your total.");
			}
			
			
			System.out.println("Here are the updated team scores: ");
			
			for (int i = 0; i < teams.length; i++) {
				System.out.println(teams[i].getName()+ ": " + teams[i].getPoints());
			}
			
			currentTeam++;
			if (currentTeam > teams.length) {
				currentTeam = 1;
			}
			questionsLeft--;
		} // finished asking all questions
		
		System.out.println("We're done with all the questions! Now it's time for Final Jeopardy!");
		int bet = 0;
		
		int numFinalTeams = 0;
		Team[] finalTeams = new Team[teams.length];
		for (int i = 0; i < teams.length; i++) {
			if (teams[i].getPoints() > 0) {
				finalTeams[i] = teams[i];
				numFinalTeams++;
			}
		}
		
		if (numFinalTeams == 0) {
			System.out.println("No teams have enough points for final jeopardy!");
		}
		else {
			for (int i = 0; i < finalTeams.length; i++) {
				if (finalTeams[i] != null) {
					System.out.println(finalTeams[i].getName() + ", how much would you like to bet?");
					String temp = in.nextLine();
					if ((temp.toLowerCase()).equals("replay")) {
						replay(questionSet, finalQuestion);
					}
					if ((temp.toLowerCase()).equals("exit")) {
						System.out.println("Thanks for playing! Exiting game...");
						System.exit(0);
					}

					bet = Integer.parseInt(temp);
					
					while (bet > finalTeams[i].getPoints() || bet < 0) {
						System.out.println("You can't bet that amount.");
						temp = in.nextLine();
						if ((temp.toLowerCase()).equals("replay")) {
							replay(questionSet, finalQuestion);
						}
						if ((temp.toLowerCase()).equals("exit")) {
							System.out.println("Thanks for playing! Exiting game...");
							System.exit(0);
						}
						bet = Integer.parseInt(temp);
					}
					finalTeams[i].setBet(bet);
				}
				
			}
			
			System.out.println("The final jeopardy question is: ");
			System.out.println(finalQuestion.getQuestion());
			
			boolean correct = false;
		
			for (int i = 0; i < finalTeams.length; i++) {
				if (finalTeams[i] != null) {
					System.out.println(finalTeams[i].getName() + ", please enter your answer: ");
					String[] finalAnswer = in.nextLine().split(" "); 
					int triesFinal = 1;
					while (triesFinal < 2) {
						if (finalAnswer.length > 1) {
							for(int j = 0; j < 2; j++) { // check answer format
								if ((finalAnswer[0].toLowerCase()).equals(pronouns[j])) {
									for (int k = 0; k < 2; k++) {
										if((finalAnswer[1].toLowerCase()).equals(verbs[k])) {
											correct = true;
										}
									}
								}
							}
						}
						if(correct == false) {
							System.out.println("Please enter your answer in the form of a question. You have one more try: ");
							finalAnswer = in.nextLine().split(" ");
							if ((finalAnswer[0].toLowerCase()).equals("replay")) {
								replay(questionSet, finalQuestion);
							}
							if ((finalAnswer[0].toLowerCase()).equals("exit")) {
								System.out.println("Thanks for playing! Exiting game...");
								System.exit(0);
							}
						}
						triesFinal++;
					}
					if (finalAnswer.length > 1) {
						for(int j = 0; j < 2; j++) { // check answer format
							if ((finalAnswer[0].toLowerCase()).equals(pronouns[j])) {
								for (int k = 0; k < 2; k++) {
									if((finalAnswer[1].toLowerCase()).equals(verbs[k])) {
										correct = true;
									}
								}
							}
						}
					}
			
					finalTeams[i].setFinalQuestion(false);
					if (correct) {
						String temp = "";
						for(int index = 2; index < finalAnswer.length; index++) {
							if ( index == finalAnswer.length - 1) {
								temp = temp + finalAnswer[index];
							}
							else {
								temp = temp + finalAnswer[index] + " ";
							}
						}
						
						if ((temp.toLowerCase()).equals("replay")) {
							replay(questionSet, finalQuestion);
						}
						if ((temp.toLowerCase()).equals("exit")) {
							System.out.println("Thanks for playing! Exiting game...");
							System.exit(0);
						}
						if (((finalQuestion.getAnswer().toLowerCase())).equals(temp.toLowerCase())) {
							teams[i].setFinalQuestion(true);
						}
					}
					correct = false;
					
				}
				
			}
			
			for (int i = 0; i < finalTeams.length; i++) {
				if (finalTeams[i] != null) {
					if (finalTeams[i].getFinalQuestion() == true) {
						System.out.println(finalTeams[i].getName() + ", your answer is correct! You get " + finalTeams[i].getBet() + " points added to your total.");
						int currentPoints = finalTeams[i].getPoints();
						finalTeams[i].setPoints(currentPoints + bet);
					}
					else {
						System.out.println("Sorry, " + finalTeams[i].getName() + ", your answer is incorrect. You get " + finalTeams[i].getBet() + " points deducted from your total.");
						int currentPoints = finalTeams[i].getPoints();
						finalTeams[i].setPoints(currentPoints - finalTeams[i].getBet());
					}
				}
				
			}
			
		}
		
		for (int i = 0; i < teams.length; i++) {
			if (finalTeams[i] != null) {
				if (finalTeams[i].getName().equals(teams[i].getName())) {
					teams[i].setPoints(finalTeams[i].getPoints());
				}
			}
		}
		// arrange teams by lowest to highest points
		for (int i = 0; i < teams.length; ++i) {
	        for (int j = i + 1; j < teams.length; ++j)
	        {
	            if (teams[i].getPoints() > teams[j].getPoints()) {
	                Team temp = teams[i];
	                teams[i] = teams[j];
	                teams[j] = temp;
	            }
	        }
		}
		
		// check for ties
		int numTies = 0;
		for (int i = 0; i < teams.length-1; i++) {
			if(teams[i].getPoints() == teams[numTeams-1].getPoints()) {
				numTies++;
			}
		}
		Team[] ties = new Team[numTies];
		for (int i = 0; i < numTies; i++) {
			for (int j = 0; j < teams.length-1; j++) {
				if(teams[j].getPoints() == teams[numTeams-1].getPoints()) {
					ties[i] = teams[j];
				}
			}
		}
		if (numTies > 0) {
			String temp = "";
			for (int i = 0; i < ties.length; i++) {
				temp = temp + ", " + ties[i].getName();
			}
			System.out.print("The winners are " + teams[numTeams-1].getName() + temp);
		}
		else {
			System.out.println("The winner is " + teams[numTeams-1].getName());
		}
		in.close();
		System.exit(0);
	}
	
	public void replay(Question[][] questionSet, FinalJeopardy finalQuestion) {
		for (int i = 0; i < categoriesSet.length; i++) { // reset chosen questions
			for (int j = 0; j < pointsSet.length; j++) {
				questionSet[i][j].setChosen(false);
			}
		}
		for (int j = 0; j < numTeams; j++) { // reset team points
			teams[j].setPoints(0);
		}
		play(questionSet, finalQuestion);
	}
}
