package yingyusu_CSCI201L_Assignment1;

public class Question {
	private String question;
	private String answer;
	private boolean chosen;
	
	public Question (String ques, String ans) {
		this.question = ques;
		this.answer = ans;
	}
	
	public String getQuestion() {
		return question;
	}
	
	public String getAnswer() {
		return answer;
	}
	
	public void setChosen(boolean b) {
		chosen = b;
	}
	
	public boolean isChosen() {
		return chosen;
	}

}
