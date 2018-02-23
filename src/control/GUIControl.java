package control;
import gui.*;

public class GUIControl {
	//selector's GUI
	private SelectorGUI selector;
	//chooser's GUI
	private ChooserGUI chooser;
	//leader board
	private LeaderBoardGUI leaderBoard;
	//Main frame of GUI
	private GUIFrame gameFrame;
	
	/**
	 * Default constructor
	 */
	public GUIControl(){
		this.gameFrame = new GUIFrame();
	}
	
//------------------------------------selector--------------------------------	
	
	/**
	 * @return SelectorGUI : new GUI of the selector 
	 */
	public SelectorGUI showSelectorGUI(){
		resetGUI();
		this.selector = new SelectorGUI();
		this.chooser=null;
		this.selector.init();
		this.selector.start();
		this.gameFrame.showSelector(this.selector);
		
		
		return this.selector;
	}
	
	public void dispose(){
		if(this.selector!=null)
			this.selector.dispose();
		if(this.chooser!=null)
			this.chooser.dispose();
		if(this.gameFrame!=null){
			this.gameFrame.setVisible(false);
			this.gameFrame.dispose();
		}
	}
	
	private void resetGUI(){
		if(this.selector!=null){
			this.selector.dispose();
			this.selector=null;
		}
		if(this.chooser!=null){
			this.chooser.dispose();
			this.chooser=null;
		}
	}
	
	/**
	 * @param word : the referenced word for the selector 
	 */
	public void setWord(String word){
		System.out.println("[setword]"+word);
		this.selector.setQuestionWord(word);
	}
	
	/**
	 * @return true : if the selector has submitted its pattern
	 * 		   false: otherwise 
	 */
	public boolean isSelectorSubmitted(){
		return this.selector.isSelectorSubmitted();
	}
	
	/**
	 * @return true : if the selector is timeout
	 * 		   false: otherwise 
	 */
	public boolean isSelectorTimeout(){
		return this.selector.isSelectorTimeout();
	}
	
	/**
	 * @return int[][] : the matrix representing the pattern
	 */
	public int[][] getPatternFromSelector(){
		return this.selector.getSelectedPattern();
	}
	
	/**
	 * @param num : the number of extra blocks that the last submitted selector can select additionally.
	 */
	public void setExtraAvailableBlockNumToSelector(int num){
		this.selector.setRemainingNumBlocks(num);
	}
	
	public void renewSelectorScore(int score){
		this.selector.setPlayerScore(score);
	}
	
	public int getRemainingNumBlocks(){
		return this.selector.getRemainingNumBlocks();
	}
	
	public void setSelectorName(String name){
		this.selector.setPlayerName(name);
	}
	
//------------------------------------chooser--------------------------------	
	
	/**
	 * @return ChooserGUI : new GUI of the chooser 
	 */
	public ChooserGUI showChooserGUI(){
		resetGUI();
		this.chooser = new ChooserGUI();
		this.selector=null;
		this.chooser.init();
		this.chooser.start();
		this.gameFrame.showChooser(this.chooser);
		
		
		return this.chooser;
	}
	
	/**
	 * @param permission : if true, enable the chooser to fill in the word; otherwise disable it.
	 */
	public void setEnableChooserWriteText(boolean permission){
		this.chooser.EnableChooserFillAnswer(permission);
	}
	
	/**
	 * @param permission : if true, enable the chooser to choose; otherwise disable it.
	 */
	public void setEnableChooserToChoose(boolean permission){
		this.chooser.EnableChooserToChoose(permission);
	}
	
	/**
	 * @param pattern : the pattern of first submitted group
	 */
	public void setFirstGroupPatternToChooser(int[][] pattern){
		this.chooser.setFirstGroupPattern(pattern);
	}
	
	/**
	 * @param pattern : the pattern of last submitted group
	 */
	public void setSecondGroupPatternToChooser(int[][] pattern){
		this.chooser.setSecondGroupPattern(pattern);
	}
	
	/**
	 * @return true : if the chooser has submitted its answer of the word
	 * 		   false: otherwise 
	 */
	public boolean isChooserCompleteSelect(){
		return this.chooser.hasChooserAnswered();
	}
	
	/**
	 * @return String : the word the chooser fill in as the answer
	 */
	public String getSelectedAnswerFromChooser(){
		return this.chooser.getChooserAnswer();
	}
	
	/**
	 * @return true : if the chooser has chosen its selected group
	 * 		   false: otherwise 
	 */
	public boolean hasChooserSelectedGroup(){
		return this.chooser.hasChooserSelectedGroup();
	}
	
	/**
	 * @return 1: if the chooser chooses the first group
	 * 		   2: if the chooser chooses the last group
	 */
	public int getBetterChoiceID(){
		return this.chooser.getChooserSelectedGroup();
	}
	
	/**
	 * @param score : the score to be shown on GUI
	 */
	public void renewChooserScore(int score){
		this.chooser.setPlayerScore(score);
	}
	
	public void selectorShowInfo(String info, boolean enableDot){
		System.out.println("[selectorShowInfo]:" + info);
		this.selector.showInfo(info,enableDot);
	}
	
	public void chooserShowInfo(String info, boolean enableDot){
		System.out.println("[chooserShowInfo]:" + info);
		this.chooser.showInfo(info,enableDot);
	}
	
	public void closeChooserInfo(){
		this.chooser.closeInfo();
	}
	
	public void setChooserName(String name){
		this.chooser.setPlayerName(name);
	}
	
	public void informChooserAnswerCorrect(int answer){
		this.chooser.answerJudge(answer);
	}
	
//------------------------------------leader board--------------------------------	
	public void showLeaderBoard(int player0Score, int player1Score, int player2Score, int player3Score, int playerID){
		resetGUI();
		this.leaderBoard = new LeaderBoardGUI(player0Score,player1Score,player2Score,player3Score,playerID);
		this.selector=null;
		this.chooser=null;
		this.leaderBoard.init();
		this.leaderBoard.start();
		this.gameFrame.showLeaderBoard(this.leaderBoard);
	}
}
