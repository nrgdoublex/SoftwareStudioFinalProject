package gui;
import model.*;
import processing.core.*;
import java.io.*;

public class SelectorGUI extends PApplet implements SelectorInterface {
	
	private static final long serialVersionUID = 1L;
	
	private static final File fontArialFile = new File(GameProtocol.FONT_ARIAL_PATH);
	private static final File fontKaiuFile = new File(GameProtocol.FONT_KAIU_PATH);
	private static final File fontJLSFile = new File(GameProtocol.FONT_JLS_PATH);
	private static final File fontJLS1File = new File(GameProtocol.FONT_JLS1_PATH);
	private static final File fontSMBFile = new File(GameProtocol.FONT_SMB_PATH);
	private static final File fontSMB1File = new File(GameProtocol.FONT_SMB2_PATH);
	
	private static final File imageClockFile = new File(GameProtocol.IMAGE_CLOCK_PATH);
	private static final File imageRefreshFile = new File(GameProtocol.IMAGE_REFRESH_PATH);
	
	
	public static final int PATTERN_SIZE = GameProtocol.DIMENSION_OF_PATTERN;
	public static final int HEIGHT = GameProtocol.SELECTOR_HEIGHT;
	public static final int WIDTH = GameProtocol.SELECTOR_WIDTH;
	public static final int TIME_LENGTH = 60*GameProtocol.TIME_LIMIT;
	private int back_r = 104;
	private int back_g = 31;
	private int back_b = 18;
	private int[][] pattern;
	private String word;
	private int score;
	private int time;
	private String playerName;
	private int remainingBlocks;
	private boolean isSelectorSubmitted;
	private boolean patternLock;
	private String information;
	private boolean isWaiting;
	
	private PFont[] font;
	private PImage clock, refresh;
	private int frame1, frame2, frame3, frame4;
	
	public void setup() {
		size(WIDTH, HEIGHT);
		background(back_r, back_g, back_b);
		
		this.pattern = new int[PATTERN_SIZE][PATTERN_SIZE];
		for (int i = 0; i < PATTERN_SIZE; i++) {
			for (int j = 0; j < PATTERN_SIZE; j++) 
				pattern[i][j] = 0;
		}
		this.word = new String();
		this.score = 0;
		this.time = TIME_LENGTH;
		this.playerName = new String();
		this.remainingBlocks = -1;
		this.isSelectorSubmitted = false;
		this.patternLock = false;
		this.information = new String();
		this.isWaiting = false;
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
		this.clock = loadImage(imageClockFile.getCanonicalPath());
		this.refresh = loadImage(imageRefreshFile.getCanonicalPath());
		this.frame1 = this.frame2 = this.frame3 = this.frame4 = 0;
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void draw() {
		/***************
		 * text labels *
		 ***************/
		//shadows
		fill(0, 0, 0);
		
		textFont(this.font[7]);
		textSize(40);
		text("<                   >", 10 +2, 50 +2);
		
		textFont(this.font[8]);
		textSize(40);
		text("  S E L E C T O R ", 10 +2, 50 +2);
		
		textFont(this.font[3]);
		textSize(30);
		text("SCORE : ", WIDTH-260 +2, 50 +2);
		
		textFont(this.font[3]);
		textSize(30);
		text("TARGET : ", WIDTH/10-60 +2, HEIGHT/3 +2);
		
		//shadows & background refreshing
		fill(this.back_r, this.back_g, this.back_b);
		noStroke();
		//stroke(0);
		rect(WIDTH-160, 5, 150, 60);
		fill(0, 0, 0);
		textFont(this.font[6]);
		textSize(35);
		text(this.score, WIDTH-150 +2, 50 +2);
		
		fill(this.back_r, this.back_g, this.back_b);
		noStroke();
		//stroke(0);
		rect(WIDTH/2-70, 10, 360, 50);
		fill(0, 0, 0);
		textFont(this.font[3]);
		textSize(40);
		text(this.playerName, WIDTH/2-60 +2, 50 +2);
		
		fill(this.back_r, this.back_g, this.back_b);
		noStroke();
		//stroke(0);
		rect(WIDTH/10-60, HEIGHT/3+20, 200, 200);
		fill(0, 0, 0);
		textFont(this.font[1]);
		textSize(170);
		text(this.word, WIDTH/10-60 +2, HEIGHT/3+170 +2);
		
		//texts
		fill(240, 240, 240);
		
		textFont(this.font[7]);
		textSize(40);
		text("<                   >", 10, 50);
		
		textFont(this.font[8]);
		textSize(40);
		text("  S E L E C T O R ", 10, 50);
		
		textFont(this.font[3]);
		textSize(40);
		text(this.playerName, WIDTH/2-60, 50);
		
		textFont(this.font[3]);
		textSize(30);
		text("SCORE : ", WIDTH-260, 50);
		
		textFont(this.font[6]);
		textSize(35);
		text(this.score, WIDTH-150, 50);
		
		textFont(this.font[3]);
		textSize(30);
		text("TARGET : ", WIDTH/10-60, HEIGHT/3);
		
		textFont(this.font[1]);
		textSize(170);
		text(this.word, WIDTH/10-60, HEIGHT/3+170);
		
		/************
		 * time bar *
		 ************/
		noStroke();
		fill(30, 30, 30);
		rect(20, HEIGHT-60, WIDTH-40, 40);
		
		if (!this.isSelectorSubmitted) {
			if (this.time > 0) {
				if (this.time <= 60*11 && this.time > 60*6) {
					fill(255, 219, 41);
					rect(20, HEIGHT-60, map(this.time, 0, TIME_LENGTH, 0, WIDTH-40) -1, 40 -1);
				}
				else if (this.time <= 60*6) {
					if (this.frame1 == 0)
						fill(255, 255, 255);
					else 
						fill(255, 0, 0);
					rect(20, HEIGHT-60, map(this.time, 0, TIME_LENGTH, 0, WIDTH-40) -1, 40 -1);
					this.frame1 = (this.frame1 + 1) % 4;
				}
				else {
					fill(156, 231, 205);
					rect(20, HEIGHT-60, map(this.time, 0, TIME_LENGTH, 0, WIDTH-40) -1, 40 -1);
				}
				this.time--;
			}
			else {
				fill(30, 30, 30);
				rect(20, HEIGHT-60, map(this.time, 0, TIME_LENGTH, 0, WIDTH-40) -1, 40 -1);
			}
		}
		else {
			fill(200, 200, 200);
			rect(20, HEIGHT-60, map(this.time, 0, TIME_LENGTH, 0, WIDTH-40) -1, 40 -1);
		}
		//show remaining time
		fill(back_r, back_g, back_b);
		rect(20, HEIGHT-160, 200, 90);
		
		image(this.clock, 20, HEIGHT-115, 45, 45);
		
		textFont(this.font[6]);
		textSize(45);
		
		fill(0, 0, 0);
		text(this.time/60 + "", 20 +50 +2, HEIGHT-75 +2);
		if (!this.isSelectorSubmitted)
			fill(240, 240, 240);
		else 
			fill(200, 200, 200);
		text(this.time/60 + "", 20 +50, HEIGHT-75);
	
		/*****************
		 * submit button *
		 *****************/
		float b_x = WIDTH-200-65;
		float b_y = HEIGHT/2;
		float b_width = 200;
		float b_height = 90;
		
		if (this.remainingBlocks == 0 && 
				!this.isSelectorTimeout() &&
				!this.isSelectorSubmitted) {
			if (this.frame3 < 25) {
				fill(255, 255, 0);
				rect(b_x -10, b_y -10, b_width +20, b_height +20, 13);
			}
			else {
				fill(back_r, back_g, back_b);
				rect(b_x -10, b_y -10, b_width +20, b_height +20);
			}
			this.frame3 = (this.frame3 + 1) % 50;
		}
		else {
			fill(back_r, back_g, back_b);
			rect(b_x -10, b_y -10, b_width +20, b_height +20);
		}
		stroke(0, 0, 0, 100);
		fill(197, 226, 226);
		rect(b_x, b_y, b_width, b_height, 6);
		
		textFont(this.font[3]);
		textSize(35);
		
		fill(90, 90, 90);
		text("SUBMIT", b_x+b_width/4-3 +2, b_y+b_height/2+10 +2);
		fill(0, 0, 0);
		text("SUBMIT", b_x+b_width/4-3, b_y+b_height/2+10);
		
		//mouse action
		if (mouseX >= b_x && mouseX <= b_x+b_width && 
				mouseY >= b_y && mouseY <= b_y+b_height &&
				!this.isSelectorSubmitted &&
				!this.isSelectorTimeout()) {
			fill(197+20, 226+20, 226+20);
			rect(b_x, b_y, b_width, b_height, 6);
			
			textFont(this.font[3]);
			textSize(35);
			
			fill(90, 90, 90);
			text("SUBMIT", b_x+b_width/4-3 +2, b_y+b_height/2+10 +2);
			fill(0, 0, 0);
			text("SUBMIT", b_x+b_width/4-3, b_y+b_height/2+10);
			
			if (mousePressed) {
				stroke(255, 255, 255);
				fill(197-20, 226-20, 226-20);
				rect(b_x, b_y, b_width, b_height, 6);
				
				textFont(this.font[3]);
				textSize(35);
				
				fill(90, 90, 90);
				text("SUBMIT", b_x+b_width/4-3 +2, b_y+b_height/2+10 +3);
				fill(0, 0, 0);
				text("SUBMIT", b_x+b_width/4-3, b_y+b_height/2+10 +3);
			}
		}
		
		if (this.isSelectorSubmitted || this.isSelectorTimeout()) {
			stroke(255, 255, 255);
			fill(197-10, 226-10, 226-10);
			rect(b_x, b_y, b_width, b_height, 6);
			
			textFont(this.font[3]);
			textSize(32);
			
			fill(20, 20, 20);
			text("SUBMITTED", b_x+b_width/4-3 -20, b_y+b_height/2+10 +3);
		}
		
		/*************
		 *  pattern  *
		 *************/
		noStroke();
		float p_x = WIDTH/4+10;
		float p_y = HEIGHT/6-35;
		float p_width = WIDTH/2-60;
		float p_height = HEIGHT/3*2+60;
		fill(0, 0, 0);
		rect(p_x-5, p_y-5, p_width+10, p_height+10);
		//blocks
		stroke(0, 0, 0, 100);
		float p_ww = p_width/PATTERN_SIZE-2;
		float p_hh = p_height/PATTERN_SIZE-2;
		for (int i = (int) p_x+1, ii = 0; i < p_x + p_width; i += p_ww+2, ii++) {
			for (int j = (int) p_y+1, jj = 0; j < p_y + p_height; j += p_hh+2, jj++) {
				//unselected
				if (this.pattern[ii][jj] == 0) {
					if (this.patternLock) {
						fill(255-100, 255-100, 255-100);
					}
					else
						fill(255, 255, 255);
						rect(i, j, p_ww, p_hh);
				}
				//selected
				else if (this.pattern[ii][jj] == 1) {
					if (this.patternLock) {
						fill(0-100, 70-100, 140-100);
					}
					else 
						fill(0, 70, 140);
						rect(i, j, p_ww, p_hh);
				}
				//mouse hovering
				if (!this.patternLock) {
					if (mouseX >= i && mouseX <= i+p_ww && 
							mouseY >= j && mouseY <= j+p_hh) {
						fill(0, 120, 240, 50);
						rect(i, j, p_ww, p_hh);
					}
				}
			}
		}
		if (this.isSelectorTimeout())
			this.patternLock = true;
		
		/***************************************
		 * remaining blocks limiting mechanism *
		 ***************************************/
		if (this.remainingBlocks >= 0) {
			//refresh number display
			fill(back_r, back_g, back_b);
			noStroke();
			//stroke(0);
			rect(b_x+40, b_y-80, 160, 60);
			//show text
			if (!this.isSelectorTimeout() &&
					!this.isSelectorSubmitted && 
					this.remainingBlocks >0) {
				if (this.frame2 > 3)
					fill(255, 0, 0);
				else 
					fill(255, 255, 0);
				this.frame2 = (this.frame2 + 1) % 11;
			}
			else 
			fill(150, 150, 150);
			textFont(this.font[3]);
			textSize(45);
			text("!! REMAINING !!\n   !! BLOCKS !!\n        " + 
					"< " + this.remainingBlocks + " >"
					, b_x-40, b_y-140);
			/*
			 * block limiting control is in 'mousePressed()' method
			 */
		}
		
		/********************
		 * show information *
		 ********************/
		//refreshing
		fill(back_r, back_g, back_b);
		noStroke();
		//stroke(0);
		rect(b_x-60, b_y+140, 330, 100);
		
		//showing information
		textFont(this.font[3]);
		textSize(25);
		fill(255, 255, 0);
		if (!this.information.equals("")) {
			//if the info is too long, it needs two rolls to show
			if (this.information.length() > 28) {
				//text("too long", b_x-55, b_y+170);
				String[] subs = this.information.split(" ");
				StringBuffer i1 = new StringBuffer();
				StringBuffer i2 = new StringBuffer();
				int temp = 0;
				for (int i = 0, l = 0; l < 20 && i < subs.length-1; i++, l = i1.length()) {
					i1.append(subs[i] + " ");
					temp = i;
				}
				for (int i = temp+1; i < subs.length; i++) {
					i2.append(subs[i] + " ");
				}
				i2.deleteCharAt(i2.length()-1);
				
				if (!this.isWaiting) {
					text(i1.toString(), b_x-55, b_y+170);
					text(i2.toString(), b_x-55, b_y+200);
				}
				else {
					if (this.frame4 < 20) {
						text(i1.toString(), b_x-55, b_y+170);
						text(i2.toString(), b_x-55, b_y+200);
					}
					else if (this.frame4 >= 20 && this.frame4 < 40) {
						text(i1.toString(), b_x-55, b_y+170);
						text(i2.toString() + ".", b_x-55, b_y+200);
					}
					else if (this.frame4 >= 40 && this.frame4 < 60) {
						text(i1.toString(), b_x-55, b_y+170);
						text(i2.toString() + "..", b_x-55, b_y+200);
					}
					else {
						text(i1.toString(), b_x-55, b_y+170);
						text(i2.toString() + "...", b_x-55, b_y+200);
					}
				}
			}
			//info that only takes one roll
			else {
				if (!this.isWaiting) {
					text(this.information, b_x-55, b_y+180);
				}
				else {
					if (this.frame4 < 20) {
						text(this.information, b_x-55, b_y+180);
					}
					else if (this.frame4 >= 20 && this.frame4 < 40) {
						text(this.information + ".", b_x-55, b_y+180);
					}
					else if (this.frame4 >= 40 && this.frame4 < 60) {
						text(this.information + "..", b_x-55, b_y+180);
					}
					else 
						text(this.information + "...", b_x-55, b_y+180);
				}
			}
			this.frame4 = (this.frame4 + 1) % 80;
		}
		
		/******************
		 * refresh button *
		 ******************/
		float rb_x = WIDTH/10+70;
		float rb_y = HEIGHT/10*7+10;
		float rb_width = 80;
		float rb_height = 70;
		
		stroke(0, 0, 0, 100);
		fill(197, 226, 226);
		rect(rb_x, rb_y, rb_width, rb_height, 6);
		
		//mouse action
		if (mouseX >= rb_x && mouseX <= rb_x+rb_width && 
				mouseY >= rb_y && mouseY <= rb_y+rb_height &&
				!this.isSelectorSubmitted &&
				!this.isSelectorTimeout()) {
			stroke(0, 0, 0, 100);
			fill(197+20, 226+20, 226+20);
			rect(rb_x, rb_y, rb_width, rb_height, 6);
			
			if (mousePressed) {
				stroke(255, 255, 255);
				fill(197-20, 226-20, 226-20);
				rect(rb_x, rb_y, rb_width, rb_height, 6);
			}
		}
		else if (this.isSelectorSubmitted || 
				this.isSelectorTimeout()) {
			stroke(255, 255, 255);
			fill(197-10, 226-10, 226-10);
			rect(rb_x, rb_y, rb_width, rb_height, 6);
		}
		
		//show image
		image(this.refresh, rb_x+11, rb_y+6, 60, 60);	
	}
	
	public void mouseDragged() {
		float p_x = WIDTH/4+10;
		float p_y = HEIGHT/6-35;
		float p_width = WIDTH/2-60;
		float p_height = HEIGHT/3*2+60;
		float p_ww = p_width/PATTERN_SIZE-2;
		float p_hh = p_height/PATTERN_SIZE-2;
		
		for (int i = (int) p_x+1, ii = 0; i < p_x + p_width; i += p_ww+2, ii++) {
			for (int j = (int) p_y+1, jj = 0; j < p_y + p_height; j += p_hh+2, jj++) {
				if (mouseX >= i && mouseX <= i+p_ww && 
						mouseY >= j && mouseY <= j+p_hh) {
					if (!this.patternLock) {
						
						if (this.pattern[ii][jj] == 0 &&
								this.remainingBlocks != 0) {
							this.pattern[ii][jj] = 1;
							
							this.remainingBlocks --;
						}
						//System.out.println(ii + " , " + jj + " : " + this.pattern[ii][jj]);
					}
				}
			}
		}
	}
	
	public void mousePressed() {
		float p_x = WIDTH/4+10;
		float p_y = HEIGHT/6-35;
		float p_width = WIDTH/2-60;
		float p_height = HEIGHT/3*2+60;
		float p_ww = p_width/PATTERN_SIZE-2;
		float p_hh = p_height/PATTERN_SIZE-2;
		
		for (int i = (int) p_x+1, ii = 0; i < p_x + p_width; i += p_ww+2, ii++) {
			for (int j = (int) p_y+1, jj = 0; j < p_y + p_height; j += p_hh+2, jj++) {
				if (mouseX >= i && mouseX <= i+p_ww && 
						mouseY >= j && mouseY <= j+p_hh) {
					if (!this.patternLock) {
						if (this.pattern[ii][jj] == 0 &&
								this.remainingBlocks != 0) {
							this.pattern[ii][jj] = 1;
							
							this.remainingBlocks --;
						}
						else if (this.pattern[ii][jj] == 1) {
							this.pattern[ii][jj] = 0;
							
							this.remainingBlocks ++;
						}
						//System.out.println(ii + " , " + jj + " : " + this.pattern[ii][jj]);
					}
				}
			}
		}
	}
	
	public void mouseClicked() {
		//submit button pressed
		float b_x = WIDTH-200-65;
		float b_y = HEIGHT/2;
		float b_width = 200;
		float b_height = 90;
		
		if (mouseX >= b_x && mouseX <= b_x+b_width && 
				mouseY >= b_y && mouseY <= b_y+b_height &&
				!this.isSelectorTimeout()) {
			this.isSelectorSubmitted = true;
			this.patternLock = true;
		}
		
		//refresh button pressed
		float rb_x = WIDTH/10+70;
		float rb_y = HEIGHT/10*7+10;
		float rb_width = 80;
		float rb_height = 70;
		
		if (mouseX >= rb_x && mouseX <= rb_x+rb_width && 
				mouseY >= rb_y && mouseY <= rb_y+rb_height &&
				!this.isSelectorTimeout() &&
				!this.isSelectorSubmitted && 
				!this.patternLock) {
			//if remaining blocks limited -> collect all selected blocks
			if (this.remainingBlocks >= 0) {
				int allSelectedBlocks = 0;
				for (int i = 0; i < PATTERN_SIZE; i++) {
					for (int j = 0; j < PATTERN_SIZE; j++) {
						if (this.pattern[i][j] == 1) {
							allSelectedBlocks++;
						}
					}
				}
				this.remainingBlocks += allSelectedBlocks;
			}
			//refreshing
			for (int i = 0; i < PATTERN_SIZE; i++) {
				for (int j = 0; j < PATTERN_SIZE; j++) {
					this.pattern[i][j] = 0;
				}
			}
		}
	}
	
	public void keyPressed() {
		
		/***********
		 * testing *
		 ***********/
/*		if (key == '1') {
			this.setRemainingNumBlocks(15);
		}
		if (key == '2') {
			fill(this.back_r, this.back_g, this.back_b);
			noStroke();
			rect(WIDTH-200-65-50, HEIGHT/2-200, 290, 170);
			this.setRemainingNumBlocks(-1);
		}
		if (key == '3') {
			this.setPlayerScore(score+50);
		}
		if (key == '4') {
			this.setPlayerScore(score+100);
		}
		if (key == '5') {
			this.setPlayerScore(score+1000);
		}
		if (key == '6') {
			this.setPlayerScore(0);
		}
		if (key == '7') {
			this.setPlayerScore(score-50);
		}
		if (key == '8') {
			this.setPlayerScore(score-100);
		}
		if (key == '9') {
			this.setPlayerScore(score-1000);
		}
		if (key =='q') {
			this.setQuestionWord("測");
		}
		if (key =='w') {
			this.setQuestionWord("試");
		}
		if (key =='e') {
			this.setQuestionWord("文");
		}
		if (key =='r') {
			this.setQuestionWord("字");
		}
		if (key == '`') {
			this.setPlayerScore(60);	
			this.setPlayerName("Mr. Good Good");
			this.setQuestionWord("我");
		}
		if (key == 'i') {
			this.showInfo("this can be a long sentence but still don't go too far", true);
		}
		if (key == 'k') {
			this.showInfo("a shorter sentence here", false);
		}
		if (key == 'o') {
			this.closeInfo();
		}
		if (key == 'z') {
			for(int i=0;i<PATTERN_SIZE;i++){
				for(int j=0;j<PATTERN_SIZE;j++){
					System.out.print(this.pattern[i][j]+",");
				}
				System.out.println("");
			}
		}
/*test*/
	}
	
	@Override
	public void setPlayerScore(int score) {
		// TODO Auto-generated method stub	
		synchronized(this) {
			this.score = score;
		}
	}

	@Override
	public void setPlayerName(String name) {
		// TODO Auto-generated method stub	
		synchronized(this) {
			this.playerName = name;
		}
	}

	@Override
	public int[][] getSelectedPattern() {
		// TODO Auto-generated method stub
		return this.pattern;
	}

	@Override
	public boolean isSelectorTimeout() {
		// TODO Auto-generated method stub
		if (this.time > 0)
			return false;
		else 
			return true;
	}

	@Override
	public boolean isSelectorSubmitted() {
		// TODO Auto-generated method stub
		return this.isSelectorSubmitted;
	}

	@Override
	public void setQuestionWord(String word) {
		// TODO Auto-generated method stub		
		synchronized(this) {
			this.word = word;
		}
	}

	@Override
	public void setRemainingNumBlocks(int numOfBlocks) {
		// TODO Auto-generated method stub
		this.remainingBlocks = numOfBlocks;
	}
	
	public void showInfo(String info, boolean isWaiting) {
		synchronized(this) {
			this.information = info;
			this.isWaiting = isWaiting;
		}
	}
	
	public void closeInfo() {
		this.information = "";
	}

	public int getRemainingNumBlocks() {
		return this.remainingBlocks;
	}

	
	public static void main(String args[]){
		SelectorGUI gui = new SelectorGUI();
		gui.init();
		gui.start();
	}
}
