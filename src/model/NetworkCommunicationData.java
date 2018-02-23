package model;

import json.JSONArray;
import json.JSONObject;

public class NetworkCommunicationData {
	
/*----------------------------------------names in JSON Message----------------------------------------*/
	public static String JSON_STATE								= "State";
	public static String JSON_CLIENT_IDENTITY					= "ClientIdentity";
	public static String JSON_WORD 								= "ChineseWord";
	public static String JSON_PLAYERNAME 						= "PlayerName";
	public static String JSON_PATTERN 							= "Pattern";
	public static String JSON_GOT_SCORE 						= "Got Score";
	public static String JSON_TOTAL_SCORE 						= "TOTAL Score";
	public static String JSON_CHOOSER_WRITE_TEXT_FLAG 			= "ChooserWriteTextFlag";
	public static String JSON_SELECTOR_EXTRA_BLOCK_FLAG 		= "SelectorExtraBlockFlag";
	public static String JSON_CHOOSER_ENABLE_SELECT_GROUP 		= "ChooserSelectedGroupFlag";
	public static String JSON_CHOOSER_SELECTED_GROUP 			= "ChooserSelectedGroup";
	public static String JSON_KEEP_GAME				 			= "KeepGaming";
	
	public static String JSON_PLAYER0_SCORE						= "Player0Score";
	public static String JSON_PLAYER1_SCORE						= "Player1Score";
	public static String JSON_PLAYER2_SCORE						= "Player2Score";
	public static String JSON_PLAYER3_SCORE						= "Player3Score";
	public static String JSON_PLAYER_ID							= "PlayerID";
/*----------------------------------------names in JSON Message----------------------------------------*/
	
/*------------------------------State Msg between Client and Server------------------------------*/
	public static final String MSG_SERVER_GAMESTART						= "GameStart";
	public static final String MSG_SERVER_GROUPING						= "Grouping";
	public static final String MSG_SERVER_RECEIVE_PATTERN_FROM_SELECTOR	= "Selector Submit Pattern ACK";
	public static final String MSG_SERVER_ENABLE_SELECTOR_EXTRA_BLOCK	= "Selector Enable Extra Block";
	public static final String MSG_SERVER_SEND_PATTERN_TO_CHOOSER		= "Send Pattern To Chooser";
	public static final String MSG_SERVER_ENABLE_CHOOSER_SELECT			= "Chooser Send Answer ACK";
	public static final String MSG_SERVER_RECEIVE_CHOOSER_SELECT		= "Chooser Send Selection ACK";
	public static final String MSG_SERVER_STAGEEND						= "Stage End";
	public static final String MSG_SERVER_NEXTSTAGE						= "Next Stage ACK";
	public static final String MSG_SERVER_GAMEOVER						= "Game Over";
	public static final String MSG_SERVER_RENEW_SCORE					= "Renew Score";
	public static final String MSG_SERVER_GAME_RESULT					= "Game Result";
	
	public static final String MSG_CLIENT_GAMESTART						= "GameStart ACK";
	public static final String MSG_CLIENT_GROUPING						= "Grouping ACK";
	public static final String MSG_CLIENT_SELECTOR_SUBMIT_PATTERN		= "Selector Submit Pattern";
	public static final String MSG_CLIENT_ENABLE_SELECTOR_EXTRA_BLOCK	= "Selector Enable Extra Block ACK";
	public static final String MSG_CLIENT_CHOOSER_RECEIVE_PATTERN		= "Send Pattern To Chooser ACK";
	public static final String MSG_CLIENT_CHOOSER_ANSWERED				= "Chooser Send Answer";
	public static final String MSG_CLIENT_CHOOSER_SELECTED				= "Chooser Send Selection";
	public static final String MSG_CLIENT_STAGEEND						= "Stage End";
	public static final String MSG_CLIENT_NEXTSTAGE						= "Next Stage";
	public static final String MSG_CLIENT_GAMEOVER						= "Game Over ACK";
	public static final String MSG_CLIENT_RENEW_SCORE					= "Renew Score ACK";
	public static final String MSG_CLIENT_GAME_RESULT					= "Game Result";
	
/*-------------------------------State Msg between Client and Server-------------------------------*/
	
/*------------------------------value of "ClientIndetity" object------------------------------*/
	public static String SELECTOR						= "Selector";
	public static String CHOOSER						= "Chooser";
/*------------------------------value of "ClientIndetity" object------------------------------*/
	
/*------------------------------value of "ChooserWriteTextFlag" object------------------------------*/	
	public static String ENABLE_CHOOSER_WRITE_TEXT		= "enable";
	public static String DISABLE_CHOOSER_WRITE_TEXT		= "disable";
/*------------------------------value of "ChooserWriteTextFlag" object------------------------------*/
	
/*------------------------------value of "SelectorExtraBlockFlag" object------------------------------*/	
	public static String ENABLE_SELECTOR_EXTRA_BLOCK	= "enable";
	public static String DISABLE_SELECTOR_EXTRA_BLOCK	= "disable";
/*------------------------------value of "SelectorExtraBlockFlag" object------------------------------*/

/*------------------------------value of "ChooserSelectedGroupFlag" object------------------------------*/
	public static String ENABLE_CHOOSER_SELECT_GROUP	= "enable";
	public static String DISABLE_CHOOSER_SELECT_GROUP	= "disable";
/*------------------------------value of "ChooserSelectedGroupFlag" object------------------------------*/

/*------------------------------value of "ChooserSelectedGroup" object------------------------------*/
	public static String CHOOSER_SELECT_FIRST_GROUP		= "1stGroup";
	public static String CHOOSER_SELECT_SECOND_GROUP	= "2ndGroup";
/*------------------------------value of "ChooserSelectedGroup" object------------------------------*/

/*------------------------------value of "KeepGaming" object------------------------------*/
	public static String KEEP_GAMING_YES				= "yes";
	public static String KEEP_GAMING_NO					= "no";
/*------------------------------value of "KeepGaming" object------------------------------*/
	
	public String state;
	public String clientIdentity;
	public String chineseWord;
	public int[] pattern;
	public int gotScore;
	public int totalScore;
	public boolean enableSelectorExtraBlock;
	public boolean enableChooserWriteText;
	public boolean enableChooserSelectGroup;
	public SelectedGroup selectedGroup;
	public boolean keepGaming;
	public String playerName;
	public int player0Score;
	public int player1Score;
	public int player2Score;
	public int player3Score;
	public int playerID;
	
	public NetworkCommunicationData(){
		this.resetData();
	}
	
	public void resetData(){
		this.state = null;
		this.clientIdentity = null;
		this.chineseWord = null;
		this.pattern = null;
		this.enableChooserWriteText = false;
		this.enableSelectorExtraBlock = false;
		this.enableChooserSelectGroup = false;
		this.selectedGroup = SelectedGroup.NOGROUP;
		this.keepGaming = true;
		this.gotScore = 0;
		this.totalScore = 0;
		this.playerName = null;
	}
	
	public JSONObject parseJSONString(String input){
		//String output = null;
		JSONObject json = new JSONObject(input);
		
		if(json.has(NetworkCommunicationData.JSON_STATE))
			this.state = json.getString(NetworkCommunicationData.JSON_STATE);
		
		if(json.has(NetworkCommunicationData.JSON_CLIENT_IDENTITY))
			this.clientIdentity = json.getString(NetworkCommunicationData.JSON_CLIENT_IDENTITY);
		
		if(json.has(NetworkCommunicationData.JSON_WORD))
			this.chineseWord = json.getString(NetworkCommunicationData.JSON_WORD);
		
		if(json.has(NetworkCommunicationData.JSON_PLAYERNAME))
			this.playerName = json.getString(NetworkCommunicationData.JSON_PLAYERNAME);
		
		if(json.has(NetworkCommunicationData.JSON_PATTERN)){
			JSONArray array = json.getJSONArray(NetworkCommunicationData.JSON_PATTERN);
			int length = array.length();
			this.pattern= new int[length];
			for(int i=0;i<length;i++){
				this.pattern[i]= array.getInt(i);
			}
		}
		
		if(json.has(NetworkCommunicationData.JSON_GOT_SCORE)){
			this.gotScore = json.getInt(NetworkCommunicationData.JSON_GOT_SCORE);
		}
		
		if(json.has(NetworkCommunicationData.JSON_TOTAL_SCORE)){
			this.totalScore = json.getInt(NetworkCommunicationData.JSON_TOTAL_SCORE);
		}
		
		if(json.has(NetworkCommunicationData.JSON_CHOOSER_WRITE_TEXT_FLAG)){
			if(json.getString(JSON_CHOOSER_WRITE_TEXT_FLAG).equals(ENABLE_CHOOSER_WRITE_TEXT))
				this.enableChooserWriteText = true;
			else
				this.enableChooserWriteText = false;
		}
		
		if(json.has(NetworkCommunicationData.JSON_SELECTOR_EXTRA_BLOCK_FLAG)){
			if(json.getString(JSON_SELECTOR_EXTRA_BLOCK_FLAG).equals(ENABLE_SELECTOR_EXTRA_BLOCK))
				this.enableSelectorExtraBlock = true;
			else
				this.enableSelectorExtraBlock = false;
		}
		
		if(json.has(NetworkCommunicationData.JSON_CHOOSER_ENABLE_SELECT_GROUP)){
			if(json.getString(JSON_CHOOSER_ENABLE_SELECT_GROUP).equals(ENABLE_CHOOSER_SELECT_GROUP))
				this.enableChooserSelectGroup = true;
			else
				this.enableChooserSelectGroup = false;
		}
		
		if(json.has(NetworkCommunicationData.JSON_CHOOSER_SELECTED_GROUP)){
			if(json.getString(JSON_CHOOSER_SELECTED_GROUP).equals(CHOOSER_SELECT_FIRST_GROUP))
				this.selectedGroup = SelectedGroup.FIRST_GROUP;
			else if(json.getString(JSON_CHOOSER_SELECTED_GROUP).equals(CHOOSER_SELECT_SECOND_GROUP))
				this.selectedGroup = SelectedGroup.SECOND_GROUP;
			else
				this.selectedGroup = SelectedGroup.NOGROUP;
		}
		
		if(json.has(NetworkCommunicationData.JSON_KEEP_GAME)){
			if(json.getString(JSON_KEEP_GAME).equals(KEEP_GAMING_YES))
				this.keepGaming = true;
			else
				this.keepGaming = false;
		}
		
		if(json.has(NetworkCommunicationData.JSON_PLAYER0_SCORE)){
			this.player0Score = json.getInt(NetworkCommunicationData.JSON_PLAYER0_SCORE);
		}
		if(json.has(NetworkCommunicationData.JSON_PLAYER1_SCORE)){
			this.player1Score = json.getInt(NetworkCommunicationData.JSON_PLAYER1_SCORE);
		}
		if(json.has(NetworkCommunicationData.JSON_PLAYER2_SCORE)){
			this.player2Score = json.getInt(NetworkCommunicationData.JSON_PLAYER2_SCORE);
		}
		if(json.has(NetworkCommunicationData.JSON_PLAYER3_SCORE)){
			this.player3Score = json.getInt(NetworkCommunicationData.JSON_PLAYER3_SCORE);
		}
		if(json.has(NetworkCommunicationData.JSON_PLAYER_ID)){
			this.playerID = json.getInt(NetworkCommunicationData.JSON_PLAYER_ID);
		}
		
		return json;
	}
}
