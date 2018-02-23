package gui;
public interface SelectorInterface {
	
	/**
	 * @param score : the score of a player
	 * 
	 * Set the score of a player
	 */
	public void setPlayerScore(int score);
	
	/**
	 * @param name : the player's name
	 * 
	 * Set the player's name
	 */
	public void setPlayerName(String name);
	
	/**
	 * @return int[][] : the selected pattern of a word
	 * 
	 * Return the selected pattern
	 */
	public int[][] getSelectedPattern();
	
	/**
	 * @return true : if GUI is timeout
	 * 		   false: otherwise
	 */
	public boolean isSelectorTimeout();
	
	/**
	 * @return true : if the selector has submitted the pattern
	 * 		   false: otherwise
	 */
	public boolean isSelectorSubmitted();
	
	/**
	 * @param word : the specific word to be shown on GUI
	 */
	public void setQuestionWord(String word);
	
	/**
	 * @param numOfBlocks : number of remaining blocks that can be chosen
	 */
	public void setRemainingNumBlocks(int numOfBlocks);
	
	public int getRemainingNumBlocks();
	
	public void showInfo(String info, boolean isWaiting);

}
