package control;
import gui.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import json.JSONObject;
import model.*;
public class Server {
	// clientThreadPool is to keep listening on client's input
	private List<ListenerThread> clientThreadPool;
	// toClientLists is the interface to write string to clients
	private List<PrintWriter> toClientLists;
	private Model model;
	//socket listening to clients
	private ServerSocket server;
	//currently accepted client
	private Socket currentLoginClient;
	//game data
	private ServerData gameData;
	//GUI
	private ServerGUI serverGUI;
	
	public Server(){
		//create socket
		try {
			this.server = new ServerSocket(GameProtocol.PORT_NUMBER);
		}  catch (IOException e) {
			this.serverGUI.addLine("Socket Fail");
			System.exit(0);
		}
		this.model = new Model(GameProtocol.SCORE_CORRECT_ANSWER,
								GameProtocol.EXTRA_SCORE_FOR_FIRST_SUMMIT_GROUP,
								GameProtocol.EXTRA_SCORE_FOR_BETTER_PATTERN);
		this.model.setDimensionOfPattern(GameProtocol.DIMENSION_OF_PATTERN);
		this.clientThreadPool = new ArrayList<ListenerThread>();
		this.toClientLists = new ArrayList<PrintWriter>();
		this.serverGUI = new ServerGUI();
		this.serverGUI.addLine("Socket Builded, Port " + server.getLocalPort());
	}
	
	private void acceptConnection(){
		int currentClientCount = 0;
		try{
			//collect players
			while(currentClientCount < GameProtocol.NUM_OF_PLAYERS) {
				this.currentLoginClient = server.accept();
				currentClientCount++;
				// handle output, enable auto flush
				this.toClientLists.add(new PrintWriter(new OutputStreamWriter(currentLoginClient.getOutputStream(),"UTF-8"),true));
				// handle input, thread implemented
				this.clientThreadPool.add(new ListenerThread(currentLoginClient, currentClientCount-1,this));
				this.serverGUI.addLine("Client #" + currentClientCount + " Login...");
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void sendGroupingInfo(){
		JSONObject group1selector = new JSONObject();
		JSONObject group1chooser = new JSONObject();
		JSONObject group2selector = new JSONObject();
		JSONObject group2chooser = new JSONObject();
		group1selector.put(NetworkCommunicationData.JSON_STATE, NetworkCommunicationData.MSG_SERVER_GROUPING);
		group1chooser.put(NetworkCommunicationData.JSON_STATE, NetworkCommunicationData.MSG_SERVER_GROUPING);
		group2selector.put(NetworkCommunicationData.JSON_STATE, NetworkCommunicationData.MSG_SERVER_GROUPING);
		group2chooser.put(NetworkCommunicationData.JSON_STATE, NetworkCommunicationData.MSG_SERVER_GROUPING);
		
		group1selector.put(NetworkCommunicationData.JSON_CLIENT_IDENTITY, NetworkCommunicationData.SELECTOR);
		group1chooser.put(NetworkCommunicationData.JSON_CLIENT_IDENTITY, NetworkCommunicationData.CHOOSER);
		group2selector.put(NetworkCommunicationData.JSON_CLIENT_IDENTITY, NetworkCommunicationData.SELECTOR);
		group2chooser.put(NetworkCommunicationData.JSON_CLIENT_IDENTITY, NetworkCommunicationData.CHOOSER);
		
		group1selector.put(NetworkCommunicationData.JSON_TOTAL_SCORE, this.model.getGroup1().getSelectorScore());
		group1chooser.put(NetworkCommunicationData.JSON_TOTAL_SCORE, this.model.getGroup1().getChooserScore());
		group2selector.put(NetworkCommunicationData.JSON_TOTAL_SCORE, this.model.getGroup2().getSelectorScore());
		group2chooser.put(NetworkCommunicationData.JSON_TOTAL_SCORE, this.model.getGroup2().getChooserScore());
		
		group1selector.put(NetworkCommunicationData.JSON_PLAYERNAME, "Player" + this.model.getGroup1().getSelectorID());
		group1chooser.put(NetworkCommunicationData.JSON_PLAYERNAME, "Player" + this.model.getGroup1().getChooserID());
		group2selector.put(NetworkCommunicationData.JSON_PLAYERNAME, "Player" + this.model.getGroup2().getSelectorID());
		group2chooser.put(NetworkCommunicationData.JSON_PLAYERNAME, "Player" + this.model.getGroup2().getChooserID());
		
		group1selector.put(NetworkCommunicationData.JSON_WORD, this.gameData.getQuestionWord());
		group2selector.put(NetworkCommunicationData.JSON_WORD, this.gameData.getQuestionWord());

		toClientLists.get(this.model.getGroup1().getSelectorID()).println(group1selector.toString());
		toClientLists.get(this.model.getGroup1().getChooserID()).println(group1chooser.toString());
		toClientLists.get(this.model.getGroup2().getSelectorID()).println(group2selector.toString());
		toClientLists.get(this.model.getGroup2().getChooserID()).println(group2chooser.toString());
	}
	
	private void sendFirstSubmitPattern(int[] pattern, GroupIdentity lastSubmit){
		JSONObject group1chooser = new JSONObject();
		JSONObject group2chooser = new JSONObject();
		JSONObject lastSubmitSelector = new JSONObject();
		JSONObject firstSubmitSelector = new JSONObject();
		group1chooser.put(NetworkCommunicationData.JSON_STATE, NetworkCommunicationData.MSG_SERVER_SEND_PATTERN_TO_CHOOSER);
		group1chooser.put(NetworkCommunicationData.JSON_PATTERN, pattern);
		group1chooser.put(NetworkCommunicationData.JSON_CHOOSER_WRITE_TEXT_FLAG, NetworkCommunicationData.ENABLE_CHOOSER_WRITE_TEXT);
		
		group2chooser.put(NetworkCommunicationData.JSON_STATE, NetworkCommunicationData.MSG_SERVER_SEND_PATTERN_TO_CHOOSER);
		group2chooser.put(NetworkCommunicationData.JSON_PATTERN, pattern);
		group2chooser.put(NetworkCommunicationData.JSON_CHOOSER_WRITE_TEXT_FLAG, NetworkCommunicationData.ENABLE_CHOOSER_WRITE_TEXT);
		
		lastSubmitSelector.put(NetworkCommunicationData.JSON_STATE, NetworkCommunicationData.MSG_SERVER_ENABLE_SELECTOR_EXTRA_BLOCK);
		lastSubmitSelector.put(NetworkCommunicationData.JSON_SELECTOR_EXTRA_BLOCK_FLAG, NetworkCommunicationData.ENABLE_SELECTOR_EXTRA_BLOCK);
		
		if(lastSubmit.equals(GroupIdentity.GROUP1)){
			toClientLists.get(this.model.getGroup1().getSelectorID()).println(lastSubmitSelector.toString());
			//we save score got to game data, store it to model later
			this.model.IncGroupScoreByFirstSumbit(this.model.getGroup2());
			this.gameData.increaseScoreOfGroup2InStage(GameProtocol.EXTRA_SCORE_FOR_FIRST_SUMMIT_GROUP);
		}
		else{
			toClientLists.get(this.model.getGroup2().getSelectorID()).println(lastSubmitSelector.toString());
			//we save score got to game data, store it to model later
			this.model.IncGroupScoreByFirstSumbit(this.model.getGroup1());
			this.gameData.increaseScoreOfGroup1InStage(GameProtocol.EXTRA_SCORE_FOR_FIRST_SUMMIT_GROUP);
		}
		toClientLists.get(this.model.getGroup1().getChooserID()).println(group1chooser.toString());
		toClientLists.get(this.model.getGroup2().getChooserID()).println(group2chooser.toString());
		
		//if first submit group is group1, renew scores
		/*if(lastSubmit==ServerData.GROUP1){
			group2chooser = new JSONObject();
			
			firstSubmitSelector.put(NetworkCommunicationData.JSON_STATE, NetworkCommunicationData.MSG_SERVER_RENEW_SCORE);
			firstSubmitSelector.put(NetworkCommunicationData.JSON_TOTAL_SCORE, this.model.getGroup2().getSelectorScore());
			group2chooser.put(NetworkCommunicationData.JSON_STATE, NetworkCommunicationData.MSG_SERVER_RENEW_SCORE);
			group2chooser.put(NetworkCommunicationData.JSON_TOTAL_SCORE, this.model.getGroup2().getChooserScore());
			toClientLists.get(this.model.getGroup2().getSelectorID()).println(firstSubmitSelector);
			toClientLists.get(this.model.getGroup2().getChooserID()).println(group2chooser.toString());
		}
		//if first submit group is group1, renew scores
		else{
			group1chooser = new JSONObject();
			
			firstSubmitSelector.put(NetworkCommunicationData.JSON_STATE, NetworkCommunicationData.MSG_SERVER_RENEW_SCORE);
			firstSubmitSelector.put(NetworkCommunicationData.JSON_TOTAL_SCORE, this.model.getGroup1().getSelectorScore());
			group1chooser.put(NetworkCommunicationData.JSON_STATE, NetworkCommunicationData.MSG_SERVER_RENEW_SCORE);
			group1chooser.put(NetworkCommunicationData.JSON_TOTAL_SCORE, this.model.getGroup1().getChooserScore());
			toClientLists.get(this.model.getGroup1().getSelectorID()).println(firstSubmitSelector);
			toClientLists.get(this.model.getGroup1().getChooserID()).println(group1chooser.toString());
		}*/
	}
	
	private void sendSecondSubmitPattern(int[] pattern){
		JSONObject group1chooser = new JSONObject();
		JSONObject group2chooser = new JSONObject();
		group1chooser.put(NetworkCommunicationData.JSON_STATE, NetworkCommunicationData.MSG_SERVER_SEND_PATTERN_TO_CHOOSER);
		group1chooser.put(NetworkCommunicationData.JSON_PATTERN, pattern);
		
		group2chooser.put(NetworkCommunicationData.JSON_STATE, NetworkCommunicationData.MSG_SERVER_SEND_PATTERN_TO_CHOOSER);
		group2chooser.put(NetworkCommunicationData.JSON_PATTERN, pattern);
		
		toClientLists.get(this.model.getGroup1().getChooserID()).println(group1chooser.toString());
		toClientLists.get(this.model.getGroup2().getChooserID()).println(group2chooser.toString());
	}
	
	private void informChooserAnswerCorrect(GroupIdentity group){
		JSONObject groupChooser = new JSONObject();
		groupChooser.put(NetworkCommunicationData.JSON_STATE, NetworkCommunicationData.MSG_SERVER_ENABLE_CHOOSER_SELECT);
		groupChooser.put(NetworkCommunicationData.JSON_CHOOSER_ENABLE_SELECT_GROUP, NetworkCommunicationData.ENABLE_CHOOSER_SELECT_GROUP);
		if(group.equals(GroupIdentity.GROUP1)){
			toClientLists.get(this.model.getGroup1().getChooserID()).println(groupChooser.toString());
			//we save score got to game data, store it to model later
			this.model.IncGroupScoreByMatching(this.model.getGroup1());
			this.gameData.increaseScoreOfGroup1InStage(GameProtocol.SCORE_CORRECT_ANSWER);
		}
		else if(group.equals(GroupIdentity.GROUP2)){
			toClientLists.get(this.model.getGroup2().getChooserID()).println(groupChooser.toString());
			//we save score got to game data, store it to model later
			this.model.IncGroupScoreByMatching(this.model.getGroup2());
			this.gameData.increaseScoreOfGroup2InStage(GameProtocol.SCORE_CORRECT_ANSWER);
		}
		
		//renew scores
		/*JSONObject groupSelector = new JSONObject();
		groupChooser = new JSONObject();
		groupSelector.put(NetworkCommunicationData.JSON_STATE, NetworkCommunicationData.MSG_SERVER_RENEW_SCORE);
		groupChooser.put(NetworkCommunicationData.JSON_STATE, NetworkCommunicationData.MSG_SERVER_RENEW_SCORE);
		if(group==ServerData.GROUP1){
			groupSelector.put(NetworkCommunicationData.JSON_TOTAL_SCORE, this.model.getGroup1().getSelectorScore());
			groupChooser.put(NetworkCommunicationData.JSON_TOTAL_SCORE, this.model.getGroup1().getChooserScore());
			toClientLists.get(this.model.getGroup1().getSelectorID()).println(groupSelector.toString());
			toClientLists.get(this.model.getGroup1().getChooserID()).println(groupChooser.toString());
		}
		else{
			groupSelector.put(NetworkCommunicationData.JSON_TOTAL_SCORE, this.model.getGroup2().getSelectorScore());
			groupChooser.put(NetworkCommunicationData.JSON_TOTAL_SCORE, this.model.getGroup2().getChooserScore());
			toClientLists.get(this.model.getGroup2().getSelectorID()).println(groupSelector.toString());
			toClientLists.get(this.model.getGroup2().getChooserID()).println(groupChooser.toString());
		}*/

	}
	
	private void informChooserAnswerIncorrect(GroupIdentity group){
		JSONObject groupchooser = new JSONObject();
		groupchooser.put(NetworkCommunicationData.JSON_STATE, NetworkCommunicationData.MSG_SERVER_ENABLE_CHOOSER_SELECT);
		groupchooser.put(NetworkCommunicationData.JSON_CHOOSER_ENABLE_SELECT_GROUP, NetworkCommunicationData.DISABLE_CHOOSER_SELECT_GROUP);
		if(group.equals(GroupIdentity.GROUP1)){
			toClientLists.get(this.model.getGroup1().getChooserID()).println(groupchooser.toString());
		}
		else{
			toClientLists.get(this.model.getGroup2().getChooserID()).println(groupchooser.toString());
		}
	}
	
	private void renewScoreAtStageEnd(){
		JSONObject group1selector = new JSONObject();
		JSONObject group1chooser = new JSONObject();
		JSONObject group2selector = new JSONObject();
		JSONObject group2chooser = new JSONObject();
		this.serverGUI.addLine("Renew scores to clients...");
		group1selector.put(NetworkCommunicationData.JSON_STATE, NetworkCommunicationData.MSG_SERVER_RENEW_SCORE);
		group1chooser.put(NetworkCommunicationData.JSON_STATE, NetworkCommunicationData.MSG_SERVER_RENEW_SCORE);
		group2selector.put(NetworkCommunicationData.JSON_STATE, NetworkCommunicationData.MSG_SERVER_RENEW_SCORE);
		group2chooser.put(NetworkCommunicationData.JSON_STATE, NetworkCommunicationData.MSG_SERVER_RENEW_SCORE);
		
		group1selector.put(NetworkCommunicationData.JSON_TOTAL_SCORE, this.model.getGroup1().getSelectorScore());
		group1chooser.put(NetworkCommunicationData.JSON_TOTAL_SCORE, this.model.getGroup1().getChooserScore());
		group2selector.put(NetworkCommunicationData.JSON_TOTAL_SCORE, this.model.getGroup2().getSelectorScore());
		group2chooser.put(NetworkCommunicationData.JSON_TOTAL_SCORE, this.model.getGroup2().getChooserScore());
		
		toClientLists.get(this.model.getGroup1().getSelectorID()).println(group1selector.toString());
		toClientLists.get(this.model.getGroup1().getChooserID()).println(group1chooser.toString());
		toClientLists.get(this.model.getGroup2().getSelectorID()).println(group2selector.toString());
		toClientLists.get(this.model.getGroup2().getChooserID()).println(group2chooser.toString());
	}
	
	private void sendStageEndToClient(){
		JSONObject group1selector = new JSONObject();
		JSONObject group1chooser = new JSONObject();
		JSONObject group2selector = new JSONObject();
		JSONObject group2chooser = new JSONObject();
		this.serverGUI.addLine("One stage has ended...");
		group1selector.put(NetworkCommunicationData.JSON_STATE, NetworkCommunicationData.MSG_SERVER_STAGEEND);
		group1chooser.put(NetworkCommunicationData.JSON_STATE, NetworkCommunicationData.MSG_SERVER_STAGEEND);
		group2selector.put(NetworkCommunicationData.JSON_STATE, NetworkCommunicationData.MSG_SERVER_STAGEEND);
		group2chooser.put(NetworkCommunicationData.JSON_STATE, NetworkCommunicationData.MSG_SERVER_STAGEEND);
		
		group1selector.put(NetworkCommunicationData.JSON_GOT_SCORE, this.gameData.getScoreOfGroup1InStage());
		group1chooser.put(NetworkCommunicationData.JSON_GOT_SCORE, this.gameData.getScoreOfGroup1InStage());
		group2selector.put(NetworkCommunicationData.JSON_GOT_SCORE, this.gameData.getScoreOfGroup2InStage());
		group2chooser.put(NetworkCommunicationData.JSON_GOT_SCORE, this.gameData.getScoreOfGroup2InStage());
		
		toClientLists.get(this.model.getGroup1().getSelectorID()).println(group1selector.toString());
		toClientLists.get(this.model.getGroup1().getChooserID()).println(group1chooser.toString());
		toClientLists.get(this.model.getGroup2().getSelectorID()).println(group2selector.toString());
		toClientLists.get(this.model.getGroup2().getChooserID()).println(group2chooser.toString());
	}
	
	private void sendGameResultToClient(){
		JSONObject group1selector = new JSONObject();
		JSONObject group1chooser = new JSONObject();
		JSONObject group2selector = new JSONObject();
		JSONObject group2chooser = new JSONObject();
		this.serverGUI.addLine("Send game results to clients...");
		group1selector.put(NetworkCommunicationData.JSON_STATE, NetworkCommunicationData.MSG_SERVER_GAME_RESULT);
		group1chooser.put(NetworkCommunicationData.JSON_STATE, NetworkCommunicationData.MSG_SERVER_GAME_RESULT);
		group2selector.put(NetworkCommunicationData.JSON_STATE, NetworkCommunicationData.MSG_SERVER_GAME_RESULT);
		group2chooser.put(NetworkCommunicationData.JSON_STATE, NetworkCommunicationData.MSG_SERVER_GAME_RESULT);
		
		group1selector.put(NetworkCommunicationData.JSON_PLAYER0_SCORE, this.model.getPlayerScoreById(GameProtocol.PLAYER0_ID));
		group1chooser.put(NetworkCommunicationData.JSON_PLAYER0_SCORE, this.model.getPlayerScoreById(GameProtocol.PLAYER0_ID));
		group2selector.put(NetworkCommunicationData.JSON_PLAYER0_SCORE, this.model.getPlayerScoreById(GameProtocol.PLAYER0_ID));
		group2chooser.put(NetworkCommunicationData.JSON_PLAYER0_SCORE, this.model.getPlayerScoreById(GameProtocol.PLAYER0_ID));
		group1selector.put(NetworkCommunicationData.JSON_PLAYER1_SCORE, this.model.getPlayerScoreById(GameProtocol.PLAYER1_ID));
		group1chooser.put(NetworkCommunicationData.JSON_PLAYER1_SCORE, this.model.getPlayerScoreById(GameProtocol.PLAYER1_ID));
		group2selector.put(NetworkCommunicationData.JSON_PLAYER1_SCORE, this.model.getPlayerScoreById(GameProtocol.PLAYER1_ID));
		group2chooser.put(NetworkCommunicationData.JSON_PLAYER1_SCORE, this.model.getPlayerScoreById(GameProtocol.PLAYER1_ID));
		group1selector.put(NetworkCommunicationData.JSON_PLAYER2_SCORE, this.model.getPlayerScoreById(GameProtocol.PLAYER2_ID));
		group1chooser.put(NetworkCommunicationData.JSON_PLAYER2_SCORE, this.model.getPlayerScoreById(GameProtocol.PLAYER2_ID));
		group2selector.put(NetworkCommunicationData.JSON_PLAYER2_SCORE, this.model.getPlayerScoreById(GameProtocol.PLAYER2_ID));
		group2chooser.put(NetworkCommunicationData.JSON_PLAYER2_SCORE, this.model.getPlayerScoreById(GameProtocol.PLAYER2_ID));
		group1selector.put(NetworkCommunicationData.JSON_PLAYER3_SCORE, this.model.getPlayerScoreById(GameProtocol.PLAYER3_ID));
		group1chooser.put(NetworkCommunicationData.JSON_PLAYER3_SCORE, this.model.getPlayerScoreById(GameProtocol.PLAYER3_ID));
		group2selector.put(NetworkCommunicationData.JSON_PLAYER3_SCORE, this.model.getPlayerScoreById(GameProtocol.PLAYER3_ID));
		group2chooser.put(NetworkCommunicationData.JSON_PLAYER3_SCORE, this.model.getPlayerScoreById(GameProtocol.PLAYER3_ID));
		
		group1selector.put(NetworkCommunicationData.JSON_PLAYER_ID, this.model.getGroup1().getSelectorID());
		group1chooser.put(NetworkCommunicationData.JSON_PLAYER_ID, this.model.getGroup1().getChooserID());
		group2selector.put(NetworkCommunicationData.JSON_PLAYER_ID, this.model.getGroup2().getSelectorID());
		group2chooser.put(NetworkCommunicationData.JSON_PLAYER_ID, this.model.getGroup2().getChooserID());
		
		toClientLists.get(this.model.getGroup1().getSelectorID()).println(group1selector.toString());
		toClientLists.get(this.model.getGroup1().getChooserID()).println(group1chooser.toString());
		toClientLists.get(this.model.getGroup2().getSelectorID()).println(group2selector.toString());
		toClientLists.get(this.model.getGroup2().getChooserID()).println(group2chooser.toString());
	}
	
	private void stageMainThread(){
		//new a game data
		this.gameData = new ServerData();
		//store Chinese word retrieved from model
		this.gameData.storeQuestionWord(model.getRandomWord());
		//divide players into 2 groups
		this.model.setGroup();
		
		//send grouping info and word
		this.sendGroupingInfo();
		
		this.serverGUI.addLine("Stage start...");
		try{
			while(true){
				
				//receive first submit pattern, send it to choosers
				if(this.gameData.getGroup1Pattern()!=null && this.gameData.getFirstSubmitGroup().equals(GroupIdentity.NOGROUP)){
					this.gameData.storeFirstSubmitGroup(GroupIdentity.GROUP1);
					this.sendFirstSubmitPattern(this.gameData.getGroup1Pattern(),GroupIdentity.GROUP2);
				}
				else if(this.gameData.getGroup2Pattern()!=null && this.gameData.getFirstSubmitGroup().equals(GroupIdentity.NOGROUP)){
					this.gameData.storeFirstSubmitGroup(GroupIdentity.GROUP2);
					this.sendFirstSubmitPattern(this.gameData.getGroup2Pattern(),GroupIdentity.GROUP1);
				}
				
				//receive last submit pattern, send it to choosers
				if(this.gameData.getGroup1Pattern()!=null 
						&& this.gameData.getFirstSubmitGroup().equals(GroupIdentity.GROUP2)
						&& this.gameData.getSecondSubmitGroup().equals(GroupIdentity.NOGROUP)){
					this.gameData.storeSecondSubmitGroup(GroupIdentity.GROUP1);
					this.sendSecondSubmitPattern(this.gameData.getGroup1Pattern());
				}
				else if(this.gameData.getGroup2Pattern()!=null 
						&& this.gameData.getFirstSubmitGroup().equals(GroupIdentity.GROUP1)
						&& this.gameData.getSecondSubmitGroup().equals(GroupIdentity.NOGROUP)){
					this.gameData.storeSecondSubmitGroup(GroupIdentity.GROUP2);
					this.sendSecondSubmitPattern(this.gameData.getGroup2Pattern());
				}
				
				//receive answer of group1 chooser
				if(this.gameData.isGroup1AnswerEnd()!=true){
					//if chooser send word to server
					if(this.gameData.getGroup1Word()!= null && this.gameData.isGroup1Answered()==false){
						this.gameData.setGroup1Answered();
						if(this.gameData.getGroup1Word().equals(this.gameData.getQuestionWord())==true){
							this.gameData.setGroup1AnswerCorrect(true);
							this.informChooserAnswerCorrect(GroupIdentity.GROUP1);
						}
						else{
							this.gameData.setGroup1AnswerCorrect(false);
							this.gameData.setGroup1AnswerEnd();
							this.informChooserAnswerIncorrect(GroupIdentity.GROUP1);
						}
					}
					
					//if chooser send selection to server
					if(this.gameData.isGroup1AnswerCorrect()==true 
							&& this.gameData.getGroup1SelectedGroup().equals(GroupIdentity.NOGROUP)==false
							&& this.gameData.isGroup1SelectedGroup()==false){
						this.gameData.setGroup1SelectedGroup();
						this.gameData.setGroup1AnswerEnd();
					}
				}
	
				//receive answer of group2 chooser
				if(this.gameData.isGroup2AnswerEnd()!=true){
					//if chooser send word to server
					if(this.gameData.getGroup2Word()!= null && this.gameData.isGroup2Answered()==false){
						this.gameData.setGroup2Answered();
						if(this.gameData.getGroup2Word().equals(this.gameData.getQuestionWord())==true){
							this.gameData.setGroup2AnswerCorrect(true);
							this.informChooserAnswerCorrect(GroupIdentity.GROUP2);
						}
						else{
							this.gameData.setGroup2AnswerCorrect(false);
							this.gameData.setGroup2AnswerEnd();
							this.informChooserAnswerIncorrect(GroupIdentity.GROUP2);
						}
					}
					
					//if chooser send selection to server
					if(this.gameData.isGroup2AnswerCorrect()==true 
							&& this.gameData.getGroup2SelectedGroup().equals(GroupIdentity.NOGROUP)==false
							&& this.gameData.isGroup2SelectedGroup()==false){
						this.gameData.setGroup2SelectedGroup();
						this.gameData.setGroup2AnswerEnd();
					}
				}
				
				//stage end
				if(this.gameData.isGroup1AnswerEnd()==true && this.gameData.isGroup2AnswerEnd()==true){
					//if two groups can choose pattern and they chooser the same pattern
					if(this.gameData.getGroup1SelectedGroup().equals(this.gameData.getGroup2SelectedGroup())
							&& this.gameData.getGroup1SelectedGroup().equals(GroupIdentity.NOGROUP)==false){
						if(this.gameData.getGroup1SelectedGroup().equals(this.gameData.getFirstSubmitGroup())){
							if(this.gameData.getFirstSubmitGroup().equals(GroupIdentity.GROUP1)){
								this.model.IncGroupScoreByBetterLook(this.model.getGroup1());
								this.gameData.increaseScoreOfGroup1InStage(GameProtocol.EXTRA_SCORE_FOR_BETTER_PATTERN);
								this.model.patternHandle(this.gameData.getQuestionWord(), this.gameData.getGroup1Pattern());
							}
							else{
								this.model.IncGroupScoreByBetterLook(this.model.getGroup2());
								this.gameData.increaseScoreOfGroup2InStage(GameProtocol.EXTRA_SCORE_FOR_BETTER_PATTERN);
								this.model.patternHandle(this.gameData.getQuestionWord(), this.gameData.getGroup2Pattern());
							}
						}
						else{
							if(this.gameData.getSecondSubmitGroup().equals(GroupIdentity.GROUP1)){
								this.model.IncGroupScoreByBetterLook(this.model.getGroup1());
								this.gameData.increaseScoreOfGroup1InStage(GameProtocol.EXTRA_SCORE_FOR_BETTER_PATTERN);
								this.model.patternHandle(this.gameData.getQuestionWord(), this.gameData.getGroup1Pattern());
							}
							else{
								this.model.IncGroupScoreByBetterLook(this.model.getGroup2());
								this.gameData.increaseScoreOfGroup2InStage(GameProtocol.EXTRA_SCORE_FOR_BETTER_PATTERN);
								this.model.patternHandle(this.gameData.getQuestionWord(), this.gameData.getGroup2Pattern());
							}
						}
					}
					
					//save total scores got in this stage into model
					this.renewScoreAtStageEnd();
					this.sendStageEndToClient();
					this.sendGameResultToClient();
					break;
				}
				
				Thread.sleep(100);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private boolean whetherKeepGaming(){
		boolean result;
		
		try{
			while(true){
				if(this.gameData.isGroup1SelectorSentKeepGaming()==true 
				&& this.gameData.isGroup1ChooserSentKeepGaming()==true
				&& this.gameData.isGroup2SelectorSentKeepGaming()==true
				&& this.gameData.isGroup2ChooserSentKeepGaming()==true){
					break;
				}
				
				Thread.sleep(100);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		if(this.gameData.isGroup1SelectorKeepGaming()==true 
		&& this.gameData.isGroup1ChooserKeepGaming()==true
		&& this.gameData.isGroup2SelectorKeepGaming()==true
		&& this.gameData.isGroup2ChooserKeepGaming()==true){
			result = true;
		}
		else
			result = false;
		
		return result;
	}
	
	public void gameStart(){
		for(int i=0;i<GameProtocol.NUM_OF_PLAYERS;i++){
			this.clientThreadPool.get(i).start();
		}
		
		//broadcast all clients that game would start soon
		JSONObject json = new JSONObject();
		json.put(NetworkCommunicationData.JSON_STATE, NetworkCommunicationData.MSG_SERVER_GAMESTART);
		this.broadcast(json.toString());
		
		
		while(true){
			this.serverGUI.addLine("Start game...");
			this.stageMainThread();
			if(this.whetherKeepGaming()==false){
				json = new JSONObject();
				json.put(NetworkCommunicationData.JSON_STATE, NetworkCommunicationData.MSG_SERVER_GAMEOVER);
				this.broadcast(json.toString());
				
				this.sendGameResultToClient();
				break;
			}
			else{
				json = new JSONObject();
				json.put(NetworkCommunicationData.JSON_STATE, NetworkCommunicationData.MSG_SERVER_NEXTSTAGE);
				this.broadcast(json.toString());
			}
		}
	}
	
	private void broadcast(String s) {
		for(int i = 0; i < GameProtocol.NUM_OF_PLAYERS; i++) {
			try{
				toClientLists.get(i).println(s);
			}
			catch(Exception e){
				
			}
		}
	}
	
	class ListenerThread extends Thread {
		private Server parent;
		private BufferedReader fromClient;
		private int ID;
		
		public ListenerThread(Socket client, int ID, Server server){
			this.ID = ID;
			this.parent = server;
			try{
				this.fromClient = new BufferedReader(new InputStreamReader(client.getInputStream(),"UTF-8"));
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
		
		@Override
		public void run() {
			String input;
			JSONObject json;
			NetworkCommunicationData inputData = new NetworkCommunicationData();
			try {
				while(true) {
					input = this.fromClient.readLine();
					json = inputData.parseJSONString(input);
					//this.parent.serverGUI.addLine(json.toString());
					if(this.parent.model.isSelector(this.ID)){
						this.handleSelectorMsg(json,inputData);
					}
					else if(this.parent.model.isChooser(this.ID)){
						this.handleChooserMsg(json,inputData);
					}
				}
			}  catch (Exception ex) {
				this.parent.serverGUI.addLine("Some clients has logged out");
			}
		}		
		
		private void handleSelectorMsg(JSONObject json, NetworkCommunicationData data){
			//if selector send pattern to server
			if(data.state.equals(NetworkCommunicationData.MSG_CLIENT_SELECTOR_SUBMIT_PATTERN)){
				if(this.parent.model.groupOf(this.ID).equals(this.parent.model.getGroup1()))
					this.parent.gameData.storeGroup1Pattern(data.pattern);
				else if(this.parent.model.groupOf(this.ID).equals(this.parent.model.getGroup2()))
					this.parent.gameData.storeGroup2Pattern(data.pattern);
			}
			
			//if client decide to keep playing
			if(data.state.equals(NetworkCommunicationData.MSG_CLIENT_NEXTSTAGE)){
				if(this.parent.model.groupOf(this.ID).equals(this.parent.model.getGroup1())){
					this.parent.gameData.setGroup1SelectorSentKeepGaming();
					this.parent.gameData.setGroup1SelectorKeepGaming(data.keepGaming);
				}
				else if(this.parent.model.groupOf(this.ID).equals(this.parent.model.getGroup2())){
					this.parent.gameData.setGroup2SelectorSentKeepGaming();
					this.parent.gameData.setGroup2SelectorKeepGaming(data.keepGaming);
				}
			}
		}
		
		private void handleChooserMsg(JSONObject json, NetworkCommunicationData data){
			//if chooser answer the word
			if(data.state.equals(NetworkCommunicationData.MSG_CLIENT_CHOOSER_ANSWERED)){
				if(this.parent.model.groupOf(this.ID).equals(this.parent.model.getGroup1()))
					this.parent.gameData.storeGroup1Word(data.chineseWord);
				else if(this.parent.model.groupOf(this.ID).equals(this.parent.model.getGroup2()))
					this.parent.gameData.storeGroup2Word(data.chineseWord);
			}
			
			//if chooser choose one of groups as better
			if(data.state.equals(NetworkCommunicationData.MSG_CLIENT_CHOOSER_SELECTED)){
				if(this.parent.model.groupOf(this.ID).equals(this.parent.model.getGroup1())){
					if(data.selectedGroup.equals(SelectedGroup.FIRST_GROUP))
						this.parent.gameData.storeGroup1SelectedGroup(this.parent.gameData.getFirstSubmitGroup());
					else if(data.selectedGroup.equals(SelectedGroup.SECOND_GROUP))
						this.parent.gameData.storeGroup1SelectedGroup(this.parent.gameData.getSecondSubmitGroup());
				}
				else if(this.parent.model.groupOf(this.ID).equals(this.parent.model.getGroup2())){
					if(data.selectedGroup.equals(SelectedGroup.FIRST_GROUP))
						this.parent.gameData.storeGroup2SelectedGroup(this.parent.gameData.getFirstSubmitGroup());
					else if(data.selectedGroup.equals(SelectedGroup.SECOND_GROUP))
						this.parent.gameData.storeGroup2SelectedGroup(this.parent.gameData.getSecondSubmitGroup());
				}
			}
			
			//if client decide to keep playing
			if(data.state.equals(NetworkCommunicationData.MSG_CLIENT_NEXTSTAGE)){
				if(this.parent.model.groupOf(this.ID).equals(this.parent.model.getGroup1())){
					this.parent.gameData.setGroup1ChooserSentKeepGaming();
					this.parent.gameData.setGroup1ChooserKeepGaming(data.keepGaming);
				}
				else if(this.parent.model.groupOf(this.ID).equals(this.parent.model.getGroup2())){
					this.parent.gameData.setGroup2ChooserSentKeepGaming();
					this.parent.gameData.setGroup2ChooserKeepGaming(data.keepGaming);
				}
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			Server server = new Server();
			server.acceptConnection();
			server.gameStart();
			server.serverGUI.dispose();
		}  catch (Exception ex) {
			ex.printStackTrace();
			System.exit(0);
		}
	}
}
