package model;

public class Group {
	public static final int NO_SUBMIT 		= 0;
	public static final int FIRST_SUBMIT 	= 1;
	public static final int SECOND_SUBMIT 	= 2;
	
	private Player selector;
	private Player chooser;
	private int submitStatus;
	
	public Group(Player selector, Player chooser){
		this.selector = selector;
		this.chooser = chooser;
		this.submitStatus = NO_SUBMIT;
	}
	
	public int getSelectorID(){
		return this.selector.getPlayerID();
	}
	
	public int getChooserID(){
		return this.chooser.getPlayerID();
	}
	
	public int getSelectorScore(){
		return this.selector.getPlayerScore();
	}
	
	public int getChooserScore(){
		return this.chooser.getPlayerScore();
	}
	
	public void IncreaseSelectorScore(int score){
		this.selector.increasePlayerScoreBy(score);
	}
	
	public void IncreaseChooserScore(int score){
		this.chooser.increasePlayerScoreBy(score);
	}
}
