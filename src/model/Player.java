package model;
public class Player implements Comparable<Player>{
	private int ID;
	private int score;
	
	public Player(int id){
		this.ID = id;
		this.score=0;
	}
	
	public Player(int id, int score){
		this.ID = id;
		this.score=score;
	}
	
	public int getPlayerID(){
		return this.ID;
	}
	
	public int getPlayerScore(){
		return this.score;
	}
	
	public void setPlayerScore(int score){
		this.score=score;
	}
	
	public void increasePlayerScoreBy(int increase){
		this.score+=increase;
	}
	
	public int compareTo(Player player) {
		return (this.score>player.score)?-1:(this.score<player.score?1:0);
	}
}
