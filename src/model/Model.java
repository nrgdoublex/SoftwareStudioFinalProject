package model;
import java.util.*;
public class Model {
	//the object handling patterns
	private PatternHandler patternHandler;
	//the object handling words
	private WordHandler wordHandler;
	//the dimension of the pattern matrix
	private int dimensionOfPattern;
	
	//the player objects
	private Player player0;
	private Player player1;
	private Player player2;
	private Player player3;
	//the list of players
	private HashMap<Integer,Player> playerList;
	
	//the groups in the game, they would be renewed every round of the game
	private Group group1;
	private Group group2;
	
	//the score of correct answers
	private int scoreOfCorrectAnswer;
	//the score of first submit
	private int scoreOfFirstSubmitGroup=10;
	//the score of good patterns
	private int scoreOfBetterPattern=10;
	
	/**
	 * Default constructor
	 */
	public Model(int scoreOfCorrectAnswer, int scoreOfFirstSubmitGroup, int scoreOfBetterPattern){
		this.patternHandler = new PatternHandler();
		this.wordHandler = new WordHandler();
		player0 = new Player(GameProtocol.PLAYER0_ID);
		player1 = new Player(GameProtocol.PLAYER1_ID);
		player2 = new Player(GameProtocol.PLAYER2_ID);
		player3 = new Player(GameProtocol.PLAYER3_ID);
		this.playerList = new HashMap<Integer,Player>();
		this.playerList.put(GameProtocol.PLAYER0_ID, player0);
		this.playerList.put(GameProtocol.PLAYER1_ID, player1);
		this.playerList.put(GameProtocol.PLAYER2_ID, player2);
		this.playerList.put(GameProtocol.PLAYER3_ID, player3);
		this.scoreOfCorrectAnswer = scoreOfCorrectAnswer;
		this.scoreOfFirstSubmitGroup = scoreOfFirstSubmitGroup;
		this.scoreOfBetterPattern = scoreOfBetterPattern;
	}
	
	/**
	 * @return : the random word
	 */
	public String getRandomWord(){
		return this.wordHandler.getChineseWord();
	}
	
	/**
	 * @return : the dimension of the matrix representing a pattern
	 */
	public int getDimensionOfPattern(){
		return this.dimensionOfPattern;
	}
	
	/**
	 * @param dimension : the dimension of the matrix representing the pattern
	 */
	public void setDimensionOfPattern(int dimension){
		this.dimensionOfPattern = dimension;
	}
	
	/**
	 * @param array
	 * 
	 * It is used to shuffle the array of players' IDs
	 */
	private void shuffleArray(int[] array){
		Random rnd = new Random();
	    for (int i = array.length - 1; i > 0; i--){
	      int index = rnd.nextInt(i+1);
	      // Simple swap
	      int a = array[index];
	      array[index] = array[i];
	      array[i] = a;
	    }
	}
	
	/**
	 * This set the groups of each player randomly
	 * Other users can access the group and the players stored in this class by IDs
	 */
	public void setGroup(){
		int[] playerList = {0,1,2,3};
		shuffleArray(playerList);
		this.group1 = new Group(this.playerList.get(playerList[0]),this.playerList.get(playerList[2]));
		this.group2 = new Group(this.playerList.get(playerList[1]),this.playerList.get(playerList[3]));
	}
	
	/**
	 * @param playerID : the ID of the player
	 * @return group : the group of the player with a specific ID
	 * 
	 * 
	 */
	public Group groupOf(int playerID){
		if(this.group1.getSelectorID()==playerID||this.group1.getChooserID()==playerID)
			return this.group1;
		else
			return this.group2;
	}
	
	/**
	 * 
	 * @param playerID : the player ID
	 * @return true  : if it is selector
	 * 		   false : id it is chooser
	 */
	public boolean isSelector(int playerID){
		if(this.group1.getSelectorID()==playerID || this.group2.getSelectorID()==playerID)
			return true;
		else
			return false;
	}
	
	/**
	 * 
	 * @param playerID : the player ID
	 * @return true  : if it is chooser
	 * 		   false : id it is selector
	 */
	public boolean isChooser(int playerID){
		if(this.group1.getChooserID()==playerID || this.group2.getChooserID()==playerID)
			return true;
		else
			return false;
	}
	
	/**
	 * @return Group1
	 */
	public Group getGroup1(){
		return this.group1;
	}
	
	/**
	 * @return Group2
	 */
	public Group getGroup2(){
		return this.group2;
	}
	
	public int getPlayerScoreById(int ID){
		return this.playerList.get(ID).getPlayerScore();
	}
	
	/**
	 * @param group
	 * 
	 * Increase scores of a group if they submit the pattern first
	 */
	public void IncGroupScoreByFirstSumbit(Group group){
		group.IncreaseSelectorScore(scoreOfFirstSubmitGroup);
		group.IncreaseChooserScore(scoreOfFirstSubmitGroup);
	}
	
	/**
	 * @param group
	 * 
	 * Increase scores of a group if their chooser has the right pattern for its pattern
	 */
	public void IncGroupScoreByMatching(Group group){
		group.IncreaseSelectorScore(scoreOfCorrectAnswer);
		group.IncreaseChooserScore(scoreOfCorrectAnswer);
	}
	
	/**
	 * @param group
	 * 
	 * Increase scores of a group if the group is credited for having better pattern
	 */
	public void IncGroupScoreByBetterLook(Group group){
		group.IncreaseSelectorScore(scoreOfCorrectAnswer);
		group.IncreaseChooserScore(scoreOfBetterPattern);
	}
	
	public void IncGroupScoreInThisStage(Group group, int score){
		group.IncreaseSelectorScore(scoreOfCorrectAnswer);
		group.IncreaseChooserScore(scoreOfBetterPattern);
	}
	
	/**
	 * @param word : the specific word
	 * @param pattern : the pattern of the specific word
	 * 
	 * Save a specific pattern of a word
	 */
	public void patternHandle(String word, int[][] pattern){
		int[] patternToSave = new int[this.dimensionOfPattern*this.dimensionOfPattern];
		for(int i=0;i<this.dimensionOfPattern;i++){
			for(int j=0;j<this.dimensionOfPattern;j++){
				patternToSave[i*this.dimensionOfPattern+j] = pattern[i][j];
			}
		}
		this.patternHandler.savePattern(patternToSave, word);
	}
	
	public void patternHandle(String word, int[] pattern){
		int[] patternToSave = new int[this.dimensionOfPattern*this.dimensionOfPattern];
		for(int i=0;i<this.dimensionOfPattern*this.dimensionOfPattern;i++){
			patternToSave[i] = pattern[i];
		}
		this.patternHandler.savePattern(patternToSave, word);
	}
}
