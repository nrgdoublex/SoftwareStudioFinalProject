package gui;

import model.*;

import java.io.*;
import java.util.*;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

public class LeaderBoardGUI extends PApplet{

	private static final int PLAYER0_ID = 0;
	private static final int PLAYER1_ID = 1;
	private static final int PLAYER2_ID = 2;
	private static final int PLAYER3_ID = 3;
	public static final int HEIGHT = GameProtocol.LEADERBOARD_HEIGHT;
	public static final int WIDTH = GameProtocol.LEADERBOARD_WIDTH;
	
	private static final File fontArialFile = new File(GameProtocol.FONT_ARIAL_PATH);
	private static final File fontKaiuFile = new File(GameProtocol.FONT_KAIU_PATH);
	private static final File fontJLSFile = new File(GameProtocol.FONT_JLS_PATH);
	private static final File fontJLS1File = new File(GameProtocol.FONT_JLS1_PATH);
	private static final File fontSMBFile = new File(GameProtocol.FONT_SMB_PATH);
	private static final File fontSMB1File = new File(GameProtocol.FONT_SMB2_PATH);
	
	private static final File imageTrophyFile = new File(GameProtocol.IMAGE_TROPHY_PATH);
	
	private int player0Score;
	private int player1Score;
	private int player2Score;
	private int player3Score;
	
	private int back_r = 240;
	private int back_g = 128;
	private int back_b = 128;
	
	private int ID = 3;
	
	private PFont[] font;
	private PImage trophy;
	
	Player[] playerArray;
	
	public LeaderBoardGUI(int player0Score, int player1Score, int player2Score, int player3Score, int playerID){
		this.player0Score = player0Score;
		this.player1Score = player1Score;
		this.player2Score = player2Score;
		this.player3Score = player3Score;
		this.ID = playerID;
		
		playerArray = new Player[4];
		playerArray[0] = new Player(PLAYER0_ID, this.player0Score);
		playerArray[1] = new Player(PLAYER1_ID, this.player1Score);
		playerArray[2] = new Player(PLAYER2_ID, this.player2Score);
		playerArray[3] = new Player(PLAYER3_ID, this.player3Score);
		Arrays.sort(playerArray);
	}
	
	public void setup(){
		size(WIDTH, HEIGHT);
		
		this.font = new PFont[10];
		try{
		this.font[0] = 
				createFont(fontArialFile.getCanonicalPath(), 50);
		this.font[1] = 
				createFont(fontKaiuFile.getCanonicalPath(), 50);
		this.font[3] = 
				createFont(fontJLSFile.getCanonicalPath(), 50);
		this.font[6] = 
				createFont(fontJLS1File.getCanonicalPath(), 50);
		this.font[7] = 
				createFont(fontSMBFile.getCanonicalPath(), 50);
		this.font[8] = 
				createFont(fontSMB1File.getCanonicalPath(), 50);
		
		this.trophy = loadImage(imageTrophyFile.getCanonicalPath());
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public synchronized void draw(){
		
		//draw image and background
		background(this.back_r,this.back_g,this.back_b);
		tint(255, 200);
		image(this.trophy,350,100);
		
		//draw title
		fill(0, 0, 0);
		textAlign(LEFT);
		textFont(this.font[7]);
		textSize(40);
		text("<                            >", 300 +2, 70 +2);
		textFont(this.font[8]);
		textSize(40);
		text(" L E A D E R B O A R D ", 340 +2, 70 +2);
		
/*-------------------------------------------------------------------------*/
		//texts
		fill(0,100,0);
		textFont(this.font[7]);
		textSize(40);
		text("<                            >", 300, 70);
		textFont(this.font[8]);
		textSize(40);
		text(" L E A D E R B O A R D ", 340, 70);
		
		//Draw Player 0 Info
		this.showPlayerScore(PLAYER0_ID,this.player0Score,getHeightByRank(PLAYER0_ID));
			
		//Draw Player 1 Info		
		this.showPlayerScore(PLAYER1_ID,this.player1Score,getHeightByRank(PLAYER1_ID));
		
		//Draw Player 2 Info
		this.showPlayerScore(PLAYER2_ID,this.player2Score,getHeightByRank(PLAYER2_ID));
		
		//Draw Player 3 Info
		this.showPlayerScore(PLAYER3_ID,this.player3Score,getHeightByRank(PLAYER3_ID));
	}
	
	private int getHeightByRank(int playerID){
		int height=0;
		int rank=0;
		for(rank=0;rank<this.playerArray.length;rank++){
			if(this.playerArray[rank].getPlayerID()==playerID)
				break;
		}
		
		switch(rank){
		case 0:
			height=170;
			break;
		case 1:
			height=320;
			break;
		case 2:
			height=470;
			break;
		case 3:
			height=620;
			break;
		default:
			System.out.println("[LeaderBoard]:should not be here");
		}
		return height;
	}
	
	private void showPlayerScore(int playerID, int score, int YPosition){
		noStroke();
		if(this.ID == playerID)
			fill(148, 0,211, 127);
		else
			fill(30, 30, 30, 127);
		rect(20, YPosition-70, WIDTH-40, 100);
		textAlign(LEFT);
		textFont(this.font[3]);
		textSize(50);
		if(this.ID == playerID){
			fill(49, 79, 79, 127);
			text("ME :", WIDTH/10-60, YPosition);
		}
		else{
			fill(30, 30, 30, 127);
			text("PLAYER "+ playerID +" :", WIDTH/10-60, YPosition);
		}
		text("POINTS", WIDTH-200, YPosition);
		textAlign(RIGHT);
		fill(255,165,0);
		text(score, WIDTH-250, YPosition);
	}
	
	public synchronized void setPlayerID(int ID){
		this.ID = ID;
	}
	
	public synchronized int getPlayerID(){
		return this.ID;
	}
}
