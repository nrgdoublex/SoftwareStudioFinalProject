package control;
import gui.*;
import model.*;
import json.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

import javax.swing.*;

public class Client {
	
	//private BufferedReader fromServer;
	private GUIControl guiControl;
	private ClientGUI clientGUI;
	private ListenerThread listenerThread;
	private PrintWriter writerToServer;
	private BufferedReader readerFromServer;
	private Socket clientSocket;
	private boolean isStageEnded;
	private ClientIdentity clientIdentity;
	
	private static final boolean DebugOn=false;  
	
	public Client(){
		if(DebugOn){
			this.connectToServer(GameProtocol.IP_ADDRESS);
		}
		else{
			//enable GUI of client
			this.clientGUI = new ClientGUI();
			String IPAddress = this.clientGUI.getIPAddress();
			this.connectToServer(IPAddress);
			this.clientGUI.addLine("Waiting other players...");
		}
	}
	
	private void connectToServer(String IPAddress){
		try{
			//create listening socket
			this.clientSocket = new Socket(IPAddress, GameProtocol.PORT_NUMBER);
			//create writer and reader
			this.writerToServer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(),"UTF-8"),true);
			this.readerFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(),"UTF-8"));
			this.clientIdentity = ClientIdentity.None; 
			this.isStageEnded = false;
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void waitingGameStart(){
		String input;
		JSONObject json;
		NetworkCommunicationData inputData = new NetworkCommunicationData();
		try{
			while(true) {
				System.out.println("[waiting GameStart]");
				input = this.readerFromServer.readLine();
				json = inputData.parseJSONString(input);
				if(inputData.state.equals(NetworkCommunicationData.MSG_SERVER_GAMESTART))
					return;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void waitingGrouping(GUIControl guiControl){
		String input;
		JSONObject json;
		NetworkCommunicationData inputData = new NetworkCommunicationData();
		try{
			while(true) {
				System.out.println("[waitingGrouping]");
				input = this.readerFromServer.readLine();
				json = inputData.parseJSONString(input);
				if(inputData.state.equals(NetworkCommunicationData.MSG_SERVER_GROUPING)){
					if(!DebugOn)
						this.clientGUI.setVisible(false);
					
					//decide whether the client is selector or chooser
					if(inputData.clientIdentity.equals(NetworkCommunicationData.SELECTOR))
						this.clientIdentity = ClientIdentity.Selector;
					else if(inputData.clientIdentity.equals(NetworkCommunicationData.CHOOSER))
						this.clientIdentity = ClientIdentity.Chooser;
					else
						this.clientIdentity = ClientIdentity.None;
					
					//initialize selector here
					if(this.clientIdentity.equals(ClientIdentity.Selector)){
						guiControl.showSelectorGUI();
						//add delay to make selector UI complete initialization
						Thread.sleep(500);
						guiControl.setSelectorName(inputData.playerName);
						guiControl.setWord(inputData.chineseWord);
						guiControl.renewSelectorScore(inputData.totalScore);
						System.out.println(inputData.totalScore);
					}
					//initialize chooser here
					else if(this.clientIdentity.equals(ClientIdentity.Chooser)){
						guiControl.showChooserGUI();
						//add delay to make selector UI complete initialization
						Thread.sleep(500);
						guiControl.setChooserName(inputData.playerName);
						guiControl.renewChooserScore(inputData.totalScore);
						System.out.println(inputData.totalScore);
						guiControl.setEnableChooserWriteText(false);
					}
					else
						this.printmsg("[waitingGrouping]: Neither selector or chooser");
	
					return;
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private boolean keepGaming(){
		boolean result;
		JSONObject json = new JSONObject();
		if(JOptionPane.showConfirmDialog(null,"Want another game?","",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
			result=true;
			json.put(NetworkCommunicationData.JSON_KEEP_GAME, NetworkCommunicationData.KEEP_GAMING_YES);
		}
		else{
			result=false;
			json.put(NetworkCommunicationData.JSON_KEEP_GAME, NetworkCommunicationData.KEEP_GAMING_NO);
		}
		json.put(NetworkCommunicationData.JSON_STATE, NetworkCommunicationData.MSG_CLIENT_NEXTSTAGE);
		this.writerToServer.println(json.toString());
		
		return result;
	}
	
	private boolean waitingNextStage(){
		String input;
		JSONObject json;
		NetworkCommunicationData inputData = new NetworkCommunicationData();
		try{
			while(true) {
				input = this.readerFromServer.readLine();
				json = inputData.parseJSONString(input);
				
				if(inputData.state.equals(NetworkCommunicationData.MSG_SERVER_GAMEOVER)){
					return false;
				}
				else if(inputData.state.equals(NetworkCommunicationData.MSG_SERVER_NEXTSTAGE)){
					return true;
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	private void showLeaderBoard(){
		String input;
		JSONObject json;
		NetworkCommunicationData inputData = new NetworkCommunicationData();
		System.out.println("[showLeaderBoard]");
		try{
			while(true) {
				input = this.readerFromServer.readLine();
				json = inputData.parseJSONString(input);
				
				if(inputData.state.equals(NetworkCommunicationData.MSG_SERVER_GAME_RESULT)){
					guiControl.showLeaderBoard(inputData.player0Score,inputData.player1Score,
							inputData.player2Score,inputData.player3Score,inputData.playerID);
					break;
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void mainThread(){
		//start the game
		try{
			//wait server for starting game
			waitingGameStart();
			//every turn of game we renew gui
			this.guiControl = new GUIControl();
			while(true){
				//wait server to decide whether I am selector or chooser
				waitingGrouping(this.guiControl);
				
				//starting monitoring thread to listen message from server
				if(this.listenerThread==null || this.listenerThread.isAlive()==false)
					this.listenerThread = new ListenerThread(this);

				this.listenerThread.start();
				
				System.out.println("[mainThread]");
				//this is the main procedure of selector
				if(this.clientIdentity.equals(ClientIdentity.Selector)){
					this.selectorMainThread(this.guiControl);
				}
				//this is the main procedure of chooser
				else if(this.clientIdentity.equals(ClientIdentity.Chooser)){
					this.chooserMainThread(this.guiControl);
				}
				else{
					this.printmsg("[mainThread]:Neither selector or chooser");
					break;
				}
				
				//clear listenerThread
				this.listenerThread.join();
				//TODO:when stage ended
				if(this.keepGaming()==false)
					break;
				else{
					if(waitingNextStage()==true)
						continue;
					else{
						JOptionPane.showMessageDialog(null, "Other players has logged out.\nGame is Over.",
								"Game Over", JOptionPane.INFORMATION_MESSAGE);
						break;
					}
				}
			}
			this.showLeaderBoard();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("[gameover]");
	}
	
	
	
	private void selectorMainThread(GUIControl gui){
		JSONObject json;
		boolean hasSelectorSubmitted = false;
		try{
			System.out.println("[selectorMainThread]");
			while(true) {
				Thread.sleep(100);  // buffer time
				if((gui.isSelectorSubmitted() || gui.isSelectorTimeout()) && hasSelectorSubmitted==false) {
					hasSelectorSubmitted=true;
					json = new JSONObject();
					json.put(NetworkCommunicationData.JSON_STATE, NetworkCommunicationData.MSG_CLIENT_SELECTOR_SUBMIT_PATTERN);
					json.put(NetworkCommunicationData.JSON_PATTERN,
							this.makePattern(gui.getPatternFromSelector(),GameProtocol.DIMENSION_OF_PATTERN));
					System.out.println(Arrays.toString(this.makePattern(gui.getPatternFromSelector(),GameProtocol.DIMENSION_OF_PATTERN)));
					this.writerToServer.println(json.toString());
					gui.selectorShowInfo("Pattern submitted, please wait for your partner",true);
					System.out.println("[Remaining]:" + gui.getRemainingNumBlocks());
				}
				
				//TODO:when stage ended
				if(this.isStageEnded==true){
					this.isStageEnded=false;
					return;
				}
				Thread.sleep(100);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}

	}
	
	private void chooserMainThread(GUIControl gui){
		JSONObject json;
		boolean hasChooserAnswered = false;
		boolean hasChooserSelectedGroup = false;
		gui.chooserShowInfo("Please wait for incoming patterns",true);
		try{
			System.out.println("[chooserMainThread]");
			while(true) {
				//when chooser has answer, return it to server
				if(hasChooserAnswered==false && gui.isChooserCompleteSelect()){
					hasChooserAnswered=true;
					json = new JSONObject();
					json.put(NetworkCommunicationData.JSON_STATE, NetworkCommunicationData.MSG_CLIENT_CHOOSER_ANSWERED);
					json.put(NetworkCommunicationData.JSON_WORD, gui.getSelectedAnswerFromChooser());
					this.writerToServer.println(json.toString());
				}
				
				//when chooser has chosen a group as a best group, return it to server
				if(hasChooserSelectedGroup==false && gui.hasChooserSelectedGroup()){
					hasChooserSelectedGroup=true;
					json = new JSONObject();
					json.put(NetworkCommunicationData.JSON_STATE, NetworkCommunicationData.MSG_CLIENT_CHOOSER_SELECTED);
					//if chooser select group1
					if(gui.getBetterChoiceID()==SelectedGroup.FIRST_GROUP.ordinal())
						json.put(NetworkCommunicationData.JSON_CHOOSER_SELECTED_GROUP,NetworkCommunicationData.CHOOSER_SELECT_FIRST_GROUP);
					//if chooser select group1
					else if(gui.getBetterChoiceID()==SelectedGroup.SECOND_GROUP.ordinal())
						json.put(NetworkCommunicationData.JSON_CHOOSER_SELECTED_GROUP, NetworkCommunicationData.CHOOSER_SELECT_SECOND_GROUP);
					this.writerToServer.println(json.toString());
					
					gui.chooserShowInfo("Wait for game ending",true);
				}
				
				
				//TODO:when stage ended
				if(this.isStageEnded==true){
					this.isStageEnded=false;
					return;
				}
				
				Thread.sleep(100);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public int[] makePattern (int[][] arr, int dimension) { 
		int[] result = new int[dimension*dimension];
		int count = 0;
		for(int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				result[count++]= arr[j][i];
			}
		}
		return result;
	}
	
	class ListenerThread extends Thread{
		Client parent;
		private BufferedReader readerFromServer;
		
		public ListenerThread(Client parent) throws Exception {
			this.parent = parent;
			this.readerFromServer = this.parent.readerFromServer;
		}
		
		@Override
		public void run() {
			System.out.println("[listenerThread]");
			//this is main listening thread of selector
			if(this.parent.clientIdentity.equals(ClientIdentity.Selector))
				this.selectorListenThread();
			//this is main listening thread of chooser
			else if(this.parent.clientIdentity.equals(ClientIdentity.Chooser))
				this.chooserListenThread();
			else
				this.parent.printmsg("[ListenerThread]: Neither selector or chooser");
		}	
		
		private void selectorListenThread(){
			String input;
			JSONObject json;
			NetworkCommunicationData inputData = new NetworkCommunicationData();
			
			//reset enable-selector-extra-block flag
			inputData.enableSelectorExtraBlock = false;
			try {
				while(true) {
					input = this.readerFromServer.readLine();
					json = inputData.parseJSONString(input);
					
					//server ask selector to enable extra blocks
					if(inputData.state.equals(NetworkCommunicationData.MSG_SERVER_ENABLE_SELECTOR_EXTRA_BLOCK)){
						if(inputData.enableSelectorExtraBlock==true){
							this.parent.guiControl.setExtraAvailableBlockNumToSelector(GameProtocol.EXTRA_BLOCK_NUM);
						}
					}
					
					//renew points of player if he got points
					if(inputData.state.equals(NetworkCommunicationData.MSG_SERVER_RENEW_SCORE)){
						this.parent.guiControl.renewSelectorScore(inputData.totalScore);
					}
					
					//TODO:when stage ended
					if(inputData.state.equals(NetworkCommunicationData.MSG_SERVER_STAGEEND)){
						//show player the score he got
						String msg = "You got " + inputData.gotScore + " points in this stage"; 
						JOptionPane.showMessageDialog(null, msg, "Score", JOptionPane.INFORMATION_MESSAGE);
						this.parent.isStageEnded = true;
						return;
					}
				}
			}  catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		private void chooserListenThread(){
			String input;
			JSONObject json;
			NetworkCommunicationData inputData = new NetworkCommunicationData();
			
			boolean hasSetFirstGroupPattern=false;
			try {
				while(true) {
					input = this.readerFromServer.readLine();
					json = inputData.parseJSONString(input);
					
					//chooser receive pattern from server
					if(inputData.state.equals(NetworkCommunicationData.MSG_SERVER_SEND_PATTERN_TO_CHOOSER)){
						if(hasSetFirstGroupPattern==false){
							this.parent.guiControl.setFirstGroupPatternToChooser(this.decodePattern(inputData.pattern,
																				GameProtocol.DIMENSION_OF_PATTERN));
							hasSetFirstGroupPattern=true;
						}
						else{
							this.parent.guiControl.setSecondGroupPatternToChooser(this.decodePattern(inputData.pattern,
																				GameProtocol.DIMENSION_OF_PATTERN));
							this.parent.guiControl.setEnableChooserWriteText(inputData.enableChooserWriteText);
							this.parent.guiControl.closeChooserInfo();;
						}
						
					}
					
					//chooser get answer from server whether it can select better group
					if(inputData.state.equals(NetworkCommunicationData.MSG_SERVER_ENABLE_CHOOSER_SELECT)){
						this.parent.guiControl.setEnableChooserToChoose(inputData.enableChooserSelectGroup);
						
						if(inputData.enableChooserSelectGroup==false)
							this.parent.guiControl.chooserShowInfo("Wait for game ending",true);
						
						if(inputData.enableChooserSelectGroup==true)
							this.parent.guiControl.informChooserAnswerCorrect(1);
						else
							this.parent.guiControl.informChooserAnswerCorrect(2);
					}
					
					//renew points of player if he got points
					if(inputData.state.equals(NetworkCommunicationData.MSG_SERVER_RENEW_SCORE)){
						this.parent.guiControl.renewChooserScore(inputData.totalScore);
					}
					
					//TODO:when stage ended
					if(inputData.state.equals(NetworkCommunicationData.MSG_SERVER_STAGEEND)){
						//show player the score he got
						String msg = "You got " + inputData.gotScore + " points in this stage"; 
						JOptionPane.showMessageDialog(null, msg, "Score", JOptionPane.INFORMATION_MESSAGE);
						this.parent.isStageEnded = true;
						return;
					}
				}
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		public int[][] decodePattern(int[] array, int dimension) {
			int[][] arr = new int[dimension][dimension];			
			for(int i = 0; i < dimension; i++) {
				for(int j = 0; j < dimension; j++) {
					arr[i][j] = array[j*dimension+i];
				}
			}
			
			return arr;
		}
	}
	
	public static void main(String[] args) {
		try {
			Client client = new Client();
			client.mainThread();
			//client.guiControl.dispose();
		}  catch (Exception ex) {
			ex.printStackTrace();
			System.exit(0);
		}
	}
	
	private void printmsg(final String str){
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run(){
				System.out.println(str);
			}
		});
	}
}
