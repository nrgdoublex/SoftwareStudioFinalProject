package gui;
public interface ChooserInterface {
	
	/**
	 * @param score : the score of a player
	 */
	public void setPlayerScore(int score);
	
	/**
	 * @param name : the player's name
	 */
	public void setPlayerName(String name);
	
	/**
	 * @param pattern : the pattern of the group first submitted
	 */
	public void setFirstGroupPattern(int[][] pattern);
	
	/**
	 * @param pattern : the pattern of the group last submitted
	 */
	public void setSecondGroupPattern(int[][] pattern);
	
	/**
	 * @return true : if the chooser has submitted his answer
	 * 		   false: otherwise
	 */
	public boolean hasChooserAnswered();
	
	/**
	 * @return String : the word the chooser answered
	 */
	public String getChooserAnswer();
	
	/**
	 * @return true : if the chooser has chosen which group has better pattern
	 * 		   false: otherwise
	 */
	public boolean hasChooserSelectedGroup();
	
	/**
	 * @return 0 : if the chooser chooses the group of first submitted
	 * 		   1 : if the chooser chooses the group of last submitted
	 */
	public int getChooserSelectedGroup();
	
	/**
	 * @param permission : if true, enable chooser to fill the word ;false otherwise 
	 */
	public void EnableChooserFillAnswer(boolean permission);
	
	public void EnableChooserToChoose(boolean permission);
	
	public void showInfo(String info, boolean isWaiting);
	
	public void answerJudge(int judge);
	
}
