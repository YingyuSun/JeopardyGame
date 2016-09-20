package yingyusu_CSCI201L_Assignment1;

public class Team {
	private String name;
	private int totalPoints = 0;
	private int bet = 0;
	private boolean finalQuestion = false;
	
	public Team(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setPoints(int points) {
		totalPoints = points;
	}
	
	public int getPoints() {
		return totalPoints;
	}
	
	public void setBet(int b) {
		bet = b;
	}
	
	public int getBet() {
		return bet;
	}
	
	public void setFinalQuestion(boolean b) {
		finalQuestion = b;
	}
	
	public boolean getFinalQuestion() {
		return finalQuestion;
	}
	
}
