package model;

public class ServerData {
	
	private int[] group1Pattern;
	private int[] group2Pattern;
	private GroupIdentity firstSubmitGroup;
	private GroupIdentity secondSubmitGroup;
	private String questionWord;
	private String group1Word;
	private String group2Word;
	private boolean isGroup1Answered;
	private boolean isGroup2Answered;
	private boolean isGroup1AnswerCorrect;
	private boolean isGroup2AnswerCorrect;
	private boolean isGroup1SelectedGroup;
	private boolean isGroup2SelectedGroup;
	private GroupIdentity group1SelectedGroup;
	private GroupIdentity group2SelectedGroup;
	private boolean isGroup1AnswerEnd;
	private boolean isGroup2AnswerEnd;
	
	private boolean hasGroup1SelectorSentKeepGaming;
	private boolean hasGroup1ChooserSentKeepGaming;
	private boolean hasGroup2SelectorSentKeepGaming;
	private boolean hasGroup2ChooserSentKeepGaming;
	
	private boolean isGroup1SelectorKeepGaming;
	private boolean isGroup1ChooserKeepGaming;
	private boolean isGroup2SelectorKeepGaming;
	private boolean isGroup2ChooserKeepGaming;
	
	private int scoreOfGroup1InStage;
	private int scoreOfGroup2InStage;

	
	public ServerData(){
		this.reset();
	}
	
	public void reset(){
		this.group1Pattern					= null;
		this.group2Pattern					= null;
		this.firstSubmitGroup				= GroupIdentity.NOGROUP;
		this.secondSubmitGroup				= GroupIdentity.NOGROUP;
		this.questionWord					= null;
		this.group1Word						= null;
		this.group2Word						= null;
		this.isGroup1Answered				= false;
		this.isGroup2Answered				= false;
		this.isGroup1AnswerCorrect			= false;
		this.isGroup2AnswerCorrect			= false;
		this.isGroup1SelectedGroup			= false;
		this.isGroup2SelectedGroup			= false;
		this.group1SelectedGroup			= GroupIdentity.NOGROUP;
		this.group2SelectedGroup			= GroupIdentity.NOGROUP;
		this.isGroup1AnswerEnd				= false;
		this.isGroup2AnswerEnd				= false;
		
		this.hasGroup1SelectorSentKeepGaming= false;
		this.hasGroup1ChooserSentKeepGaming	= false;
		this.hasGroup2SelectorSentKeepGaming= false;
		this.hasGroup2ChooserSentKeepGaming	= false;
		this.isGroup1SelectorKeepGaming		= false;
		this.isGroup1ChooserKeepGaming		= false;
		this.isGroup2SelectorKeepGaming		= false;
		this.isGroup2ChooserKeepGaming		= false;
		this.scoreOfGroup1InStage			= 0;
		this.scoreOfGroup2InStage			= 0;
	}
	
/*------------------------------handling pattern------------------------------*/
	public void storeGroup1Pattern(int[] pattern){
		this.group1Pattern = new int[pattern.length];
		System.arraycopy(pattern, 0, this.group1Pattern, 0, pattern.length);
	}
	
	public int[] getGroup1Pattern(){
		return this.group1Pattern;
	}
	
	public void storeGroup2Pattern(int[] pattern){
		this.group2Pattern = new int[pattern.length];
		System.arraycopy(pattern, 0, this.group2Pattern, 0, pattern.length);
	}
	
	public int[] getGroup2Pattern(){
		return this.group2Pattern;
	}
/*------------------------------handling pattern------------------------------*/
	
/*------------------------------selector submit flags------------------------------*/
	public void storeFirstSubmitGroup(GroupIdentity group){
		if(group.compareTo(GroupIdentity.GROUP2)>0 || group.compareTo(GroupIdentity.NOGROUP)<0)
			this.firstSubmitGroup = GroupIdentity.NOGROUP;
		else
			this.firstSubmitGroup = group;
	}
	
	public GroupIdentity getFirstSubmitGroup(){
		return this.firstSubmitGroup;
	}
	
	public void storeSecondSubmitGroup(GroupIdentity group){
		if(group.compareTo(GroupIdentity.GROUP2)>0 || group.compareTo(GroupIdentity.NOGROUP)<0)
			this.secondSubmitGroup = GroupIdentity.NOGROUP;
		else
			this.secondSubmitGroup = group;
	}
	
	public GroupIdentity getSecondSubmitGroup(){
		return this.secondSubmitGroup;
	}
/*------------------------------selector submit flags------------------------------*/
	
/*------------------------------handling Chinese words------------------------------*/
	public void storeQuestionWord(String word){
		this.questionWord = word.trim();
	}
	
	public String getQuestionWord(){
		return this.questionWord;
	}
	
	public void storeGroup1Word(String word){
		this.group1Word = word.trim();
	}
	
	public String getGroup1Word(){
		return this.group1Word;
	}
	
	public void storeGroup2Word(String word){
		this.group2Word = word.trim();
	}
	
	public String getGroup2Word(){
		return this.group2Word;
	}
/*------------------------------handling Chinese words------------------------------*/
	
/*------------------------------chooser answer flags------------------------------*/
	public void setGroup1Answered(){
		this.isGroup1Answered=true;
	}

	public boolean isGroup1Answered(){
		return this.isGroup1Answered;
	}
	
	public void setGroup2Answered(){
		this.isGroup2Answered=true;
	}

	public boolean isGroup2Answered(){
		return this.isGroup2Answered;
	}
	
	public void setGroup1AnswerCorrect(boolean correct){
		this.isGroup1AnswerCorrect = correct;
	}
	
	public boolean isGroup1AnswerCorrect(){
		return this.isGroup1AnswerCorrect;
	}
	
	public void setGroup2AnswerCorrect(boolean correct){
		this.isGroup2AnswerCorrect = correct;
	}
	
	public boolean isGroup2AnswerCorrect(){
		return this.isGroup2AnswerCorrect;
	}
/*------------------------------chooser answer flags------------------------------*/
	
/*------------------------------chooser selecting groups flags------------------------------*/
	public void setGroup1SelectedGroup(){
		this.isGroup1SelectedGroup=true;
	}

	public boolean isGroup1SelectedGroup(){
		return this.isGroup1SelectedGroup;
	}
	
	public void setGroup2SelectedGroup(){
		this.isGroup2SelectedGroup=true;
	}

	public boolean isGroup2SelectedGroup(){
		return this.isGroup2SelectedGroup;
	}
	
	public void storeGroup1SelectedGroup(GroupIdentity group){
		if(group.compareTo(GroupIdentity.GROUP2)>0 || group.compareTo(GroupIdentity.NOGROUP)<0)
			this.group1SelectedGroup = GroupIdentity.NOGROUP;
		else
			this.group1SelectedGroup = group;
	}
	
	public GroupIdentity getGroup1SelectedGroup(){
		return this.group1SelectedGroup;
	}
	
	public void storeGroup2SelectedGroup(GroupIdentity group){
		if(group.compareTo(GroupIdentity.GROUP2)>0 || group.compareTo(GroupIdentity.NOGROUP)<0)
			this.group2SelectedGroup = GroupIdentity.NOGROUP;
		else
			this.group2SelectedGroup = group;
	}
	
	public GroupIdentity getGroup2SelectedGroup(){
		return this.group2SelectedGroup;
	}
/*------------------------------chooser selecting groups flags------------------------------*/

/*------------------------------group complete flags------------------------------*/
	public void setGroup1AnswerEnd(){
		this.isGroup1AnswerEnd=true;
	}
	
	public boolean isGroup1AnswerEnd(){
		return this.isGroup1AnswerEnd;
	}
	
	public void setGroup2AnswerEnd(){
		this.isGroup2AnswerEnd=true;
	}
	
	public boolean isGroup2AnswerEnd(){
		return this.isGroup2AnswerEnd;
	}
/*------------------------------group complete flags------------------------------*/	

/*------------------------------keep gaming flags------------------------------*/	
	public void setGroup1SelectorSentKeepGaming(){
		this.hasGroup1SelectorSentKeepGaming = true;
	}
	
	public boolean isGroup1SelectorSentKeepGaming(){
		return this.hasGroup1SelectorSentKeepGaming;
	}
	
	public void setGroup1ChooserSentKeepGaming(){
		this.hasGroup1ChooserSentKeepGaming = true;
	}
	
	public boolean isGroup1ChooserSentKeepGaming(){
		return this.hasGroup1ChooserSentKeepGaming;
	}
	
	public void setGroup2SelectorSentKeepGaming(){
		this.hasGroup2SelectorSentKeepGaming = true;
	}
	
	public boolean isGroup2SelectorSentKeepGaming(){
		return this.hasGroup2SelectorSentKeepGaming;
	}
	
	public void setGroup2ChooserSentKeepGaming(){
		this.hasGroup2ChooserSentKeepGaming = true;
	}
	
	public boolean isGroup2ChooserSentKeepGaming(){
		return this.hasGroup2ChooserSentKeepGaming;
	}
	
	public void setGroup1SelectorKeepGaming(boolean keepGaming){
		this.isGroup1SelectorKeepGaming = keepGaming;
	}
	
	public boolean isGroup1SelectorKeepGaming(){
		return this.isGroup1SelectorKeepGaming;
	}
	
	public void setGroup1ChooserKeepGaming(boolean keepGaming){
		this.isGroup1ChooserKeepGaming = keepGaming;
	}
	
	public boolean isGroup1ChooserKeepGaming(){
		return this.isGroup1ChooserKeepGaming;
	}
	
	public void setGroup2SelectorKeepGaming(boolean keepGaming){
		this.isGroup2SelectorKeepGaming = keepGaming;
	}
	
	public boolean isGroup2SelectorKeepGaming(){
		return this.isGroup2SelectorKeepGaming;
	}
	
	public void setGroup2ChooserKeepGaming(boolean keepGaming){
		this.isGroup2ChooserKeepGaming = keepGaming;
	}
	
	public boolean isGroup2ChooserKeepGaming(){
		return this.isGroup2ChooserKeepGaming;
	}
/*------------------------------keep gaming flags------------------------------*/
	
/*------------------------------group score in this stage------------------------------*/
	public void increaseScoreOfGroup1InStage(int score){
		this.scoreOfGroup1InStage += score;
	}
	
	public int getScoreOfGroup1InStage(){
		return this.scoreOfGroup1InStage;
	}
	
	public void increaseScoreOfGroup2InStage(int score){
		this.scoreOfGroup2InStage += score;
	}
	
	public int getScoreOfGroup2InStage(){
		return this.scoreOfGroup2InStage;
	}
/*------------------------------group score in this stage------------------------------*/
}
