package gui;
import model.*;

import java.io.*;

import processing.core.*;

public class ChooserGUI extends PApplet implements ChooserInterface {
	
	private static final long serialVersionUID = 1L;
	
	private static final File fontArialFile = new File(GameProtocol.FONT_ARIAL_PATH);
	private static final File fontKaiuFile = new File(GameProtocol.FONT_KAIU_PATH);
	private static final File fontJLSFile = new File(GameProtocol.FONT_JLS_PATH);
	private static final File fontJLS1File = new File(GameProtocol.FONT_JLS1_PATH);
	private static final File fontSMBFile = new File(GameProtocol.FONT_SMB_PATH);
	private static final File fontSMB1File = new File(GameProtocol.FONT_SMB2_PATH);
	
	public static final int PATTERN_SIZE = GameProtocol.DIMENSION_OF_PATTERN;
	public static final int HEIGHT = GameProtocol.CHOOSER_HEIGHT;
	public static final int WIDTH = GameProtocol.CHOOSER_WIDTH;
	private int back_r = 40;
	private int back_g = 100;
	private int back_b = 123;
	private int[][] pattern1, pattern2;
	private int score;
	private String playerName;
	private String chooserAnswer;
	private boolean hasChooserAnswered;
	private int chooserSelectedGroup;
	private boolean selectedGroupOK;
	private boolean choosingLock;
	private boolean answeringLock;
	private boolean pattern1Got;
	private boolean pattern2Got;
	private String information;
	private boolean isWaiting;
	private int answerJudge;
	
	private PFont[] font;
	private int frame, frame1, frame2;
	
	public void setup() {
		size(WIDTH, HEIGHT);
		background(back_r, back_g, back_b);
		
		this.pattern1 = new int[PATTERN_SIZE][PATTERN_SIZE];
		this.pattern2 = new int[PATTERN_SIZE][PATTERN_SIZE];
		for (int i = 0; i < PATTERN_SIZE; i++) {
			for (int j = 0; j < PATTERN_SIZE; j++) 
				pattern1[i][j] = 0;
		}
		for (int i = 0; i < PATTERN_SIZE; i++) {
			for (int j = 0; j < PATTERN_SIZE; j++) 
				pattern2[i][j] = 0;
		}
		this.score = 0;
		this.playerName = new String();
		this.chooserAnswer = new String();
		this.hasChooserAnswered = false;
		this.chooserSelectedGroup = 0;
		this.selectedGroupOK = false;
		this.choosingLock = true;
		this.answeringLock = true;
		this.pattern1Got = false;
		this.pattern2Got = false;
		this.information = new String();
		this.isWaiting = false;
		this.answerJudge = 0;
		this.frame = frame1 = frame2 = 0;
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
		text("<                 >", 10 +2, 50 +2);
		
		textFont(this.font[8]);
		textSize(40);
		text("  C H O O S E R  ", 10 +2, 50 +2);
		
		textFont(this.font[3]);
		textSize(30);
		text("SCORE : ", WIDTH-260 +2, 50 +2);
		
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
		
		//texts
		fill(240, 240, 240);
		
		textFont(this.font[7]);
		textSize(40);
		text("<                 >", 10, 50);
		
		textFont(this.font[8]);
		textSize(40);
		text("  C H O O S E R  ", 10, 50);
		
		textFont(this.font[3]);
		textSize(40);
		text(this.playerName, WIDTH/2-60, 50);
		
		textFont(this.font[3]);
		textSize(30);
		text("SCORE : ", WIDTH-260, 50);
		
		textFont(this.font[6]);
		textSize(35);
		text(this.score, WIDTH-150, 50);
		
		/**************
		 *  patterns  *
		 **************/
		float p_width = WIDTH/2-100;
		float p_height = HEIGHT/3*2+40;
		float p_ww = p_width/PATTERN_SIZE-2;
		float p_hh = p_height/PATTERN_SIZE-2;
		//first pattern
		float p1_x = 60;
		float p1_y = HEIGHT/6-25;
		if (this.pattern1Got) {
			if (this.chooserSelectedGroup == 1) {
				noStroke();
				fill(255, 255, 0);
				rect(p1_x-5, p1_y-5, p_width+10, p_height+10);
			}
			else {
				noStroke();
				fill(0, 0, 0);
				rect(p1_x-5, p1_y-5, p_width+10, p_height+10);
			}
			//draw first blocks
			stroke(0, 0, 0, 100);
			for (int i = (int) p1_x+1, ii = 0; i < p1_x + p_width; i += p_ww+2, ii++) {
				for (int j = (int) p1_y+1, jj = 0; j < p1_y + p_height; j += p_hh+2, jj++) {
					if (this.pattern1[ii][jj] == 0) {
						fill(255, 255, 255);
					}
					else if (this.pattern1[ii][jj] == 1) {
						fill(209, 59, 0);
					}
					rect(i, j, p_ww, p_hh);
				}
			}
		}
		//waiting from pattern1 input
		else if (!this.pattern1Got) {
			noStroke();
			for (int i = 0; i < 5; i++) {
				int r = 10;
				if (i % 2 == 0) {
					fill(50, 50, 50);
					rect(p1_x-5 +r*i, p1_y-5 +r*i, p_width+10 -r*2*i, p_height+10 -r*2*i);
				}
				else {
					fill(back_r, back_g, back_b);
					rect(p1_x-5 +r*i, p1_y-5 +r*i, p_width+10 -r*2*i, p_height+10 -r*2*i);
				}
			}
			{	//word animation
				int rate = 120;
				textFont(this.font[3]);
				textSize(30);
				fill(200, 200, 200);
				if (this.frame >= 0 && this.frame < rate/3) {
					text("waiting for\n  pattern.",p1_x+p_width/2-70, p1_y+p_height/2-10);
				}
				else if (this.frame >= rate/3 && this.frame < rate/3*2) {
					text("waiting for\n  pattern..",p1_x+p_width/2-70, p1_y+p_height/2-10);
				}
				else {
					text("waiting for\n  pattern...",p1_x+p_width/2-70, p1_y+p_height/2-10);
				}
				this.frame = (this.frame + 1) % rate;
			}
		}
		//second pattern
		float p2_x = WIDTH-p_width-60;
		float p2_y = HEIGHT/6-25;
		if (this.pattern2Got) {
			if (this.chooserSelectedGroup == 2) {
				noStroke();
				fill(255, 255, 0);
				rect(p2_x-5, p2_y-5, p_width+10, p_height+10);
			}
			else {
				noStroke();
				fill(0, 0, 0);
				rect(p2_x-5, p2_y-5, p_width+10, p_height+10);
			}
			//draw second blocks
			stroke(0, 0, 0, 100);
			for (int i = (int) p2_x+1, ii = 0; i < p2_x + p_width; i += p_ww+2, ii++) {
				for (int j = (int) p2_y+1, jj = 0; j < p2_y + p_height; j += p_hh+2, jj++) {
					if (this.pattern2[ii][jj] == 0) {
						fill(255, 255, 255);
					}
					else if (this.pattern2[ii][jj] == 1) {
						fill(209, 59, 0);
					}
					rect(i, j, p_ww, p_hh);
				}
			}
		}
		//waiting from pattern2 input
		else if (!this.pattern2Got) {
			noStroke();
			for (int i = 0; i < 5; i++) {
				int r = 10;
				if (i % 2 == 0) {
					fill(50, 50, 50);
					rect(p2_x-5 +r*i, p2_y-5 +r*i, p_width+10 -r*2*i, p_height+10 -r*2*i);
				}
				else {
					fill(back_r, back_g, back_b);
					rect(p2_x-5 +r*i, p2_y-5 +r*i, p_width+10 -r*2*i, p_height+10 -r*2*i);
				}
			}
			{	//word animation
				int rate = 120;
				textFont(this.font[3]);
				textSize(30);
				fill(200, 200, 200);
				if (this.frame1 >= 0 && this.frame1 < rate/3) {
					text("waiting for\n  pattern.",p2_x+p_width/2-70, p2_y+p_height/2-10);
				}
				else if (this.frame1 >= rate/3 && this.frame1 < rate/3*2) {
					text("waiting for\n  pattern..",p2_x+p_width/2-70, p2_y+p_height/2-10);
				}
				else {
					text("waiting for\n  pattern...",p2_x+p_width/2-70, p2_y+p_height/2-10);
				}
				this.frame1 = (this.frame1 + 1) % rate;
			}
		}
		
		/****************
		 * answer field *
		 ****************/
		float a_x = WIDTH/2-50;
		float a_y = HEIGHT/100*97;
		float a_width = 100;
		float a_height = 60;
		//when pattern showed -> enable answering
		if (!this.answeringLock) {
			fill(0, 0, 0);
			rect(a_x -150, a_y -5, a_width +155, a_height +10);
			
			fill(255, 255, 255);
			rect(a_x, a_y, a_width, a_height);
			
			if (!this.hasChooserAnswered) {
				textFont(this.font[3]);
				textSize(35);
				fill(255, 255, 0);
				text("ANSWER",a_x -135 ,a_y +43);
				
				//waiting for answer
				fill(255, 255, 255);
				rect(a_x, a_y, a_width, a_height);
				
				fill(100, 100, 100);
				textSize(15);
				text("click here \n  to type", a_x +21, a_y +26);

				//thinking animation
				int rate = 120;
				fill(100, 100, 100);
				if (this.frame2 >= 0 && this.frame2 < rate/3) {
					textFont(this.font[0]);
					textSize(25);
					text(".", a_x+a_width-23, a_y+a_height-5);
				}
				else if (this.frame2 >= rate/3 && this.frame2 < rate/3*2) {
					textFont(this.font[0]);
					textSize(25);
					text("..", a_x+a_width-23, a_y+a_height-5);
				}
				else {
					textFont(this.font[0]);
					textSize(25);
					text("...", a_x+a_width-23, a_y+a_height-5);
				}
				this.frame2 = (this.frame2 + 1) % rate;
/**/						
			}
			else if (this.hasChooserAnswered) {
				if (this.answerJudge == 1) {
					fill(0, 200, 0);
					textFont(this.font[3]);
					textSize(33);
					text("CORRECT",a_x -135, a_y +43);
				}
				else if (this.answerJudge == 2) {
					fill(255, 0, 0);
					textFont(this.font[3]);
					textSize(35);
					text("WRONG",a_x -125, a_y +43);
				}
				else {
					fill(255, 255, 255);
					textFont(this.font[3]);
					textSize(35);
					text("ANSWER",a_x -135 ,a_y +43);
				}
				
				//display chooser's answer
				fill(0, 0, 0);
				textFont(this.font[1]);
				textSize(40);
				text(this.chooserAnswer, a_x+30, a_y+40);
				
				//show the answering result 
				noStroke();
				fill(back_r, back_g, back_b);
				rect(WIDTH-500, HEIGHT-70, 450, 55);
				if (this.answerJudge == 2) {
					textFont(this.font[3]);
					textSize(30);
					fill(0, 0, 0);
					text("Sorry, you got a wrong answer", WIDTH-480 +2, HEIGHT-35 +2);
					fill(240, 240, 240);
					text("Sorry, you got a wrong answer", WIDTH-480, HEIGHT-35);
				}
				else {
					textFont(this.font[3]);
					textSize(30);
					fill(0, 0, 0);
					text("Answer submitted", WIDTH-400 +2, HEIGHT-35 +2);
					fill(240, 240, 240);
					text("Answer submitted", WIDTH-400, HEIGHT-35);
				}
			}
		}
		//waiting for patterns
		else {
			int grey = 50;
			noStroke();
			fill(0 +grey, 0 +grey, 0 +grey);
			rect(a_x -150, a_y -5, a_width +155, a_height +10);
			
			fill(255 -grey*2, 255 -grey*2, 255 -grey*2);
			rect(a_x, a_y, a_width, a_height);
			
			textFont(this.font[3]);
			textSize(35);
			text("ANSWER",a_x -135 ,a_y +43);
			
/*			fill(0 +grey, 0 +grey, 0 +grey);
			textSize(15);
			text("waiting for\n patterns", a_x +20, a_y +24);
/**/					
		}
		
		/************
		 * choosing *
		 ************/
		//when chooser answered -> enable choosing patterns
		if (!this.choosingLock && !this.selectedGroupOK) {
			//notice chooser to choose
			noStroke();
			fill(back_r, back_g, back_b);
			rect(WIDTH-500, HEIGHT-70, 450, 55);
			
			textFont(this.font[3]);
			textSize(30);
			fill(0, 0, 0);
			text("Please choose the better pattern", WIDTH-490 +2, HEIGHT-35 +2);
			fill(240, 240, 240);
			text("Please choose the better pattern", WIDTH-490, HEIGHT-35);
			//mouse hovering effect
			if (this.pattern1Got && 
					(this.chooserSelectedGroup == 0 || this.chooserSelectedGroup == 2) &&
					mouseX >= p1_x && mouseX <= p1_x+p_width && 
					mouseY >= p1_y && mouseY <= p1_y+p_height) {
				noStroke();
				fill(255, 255, 0);
				rect(p1_x-5, p1_y-5, p_width+10, p_height+10);
				fill(0, 0, 0);
				rect(p1_x, p1_y, p_width, p_height);
				//draw first blocks
				stroke(0, 0, 0, 100);
				for (int i = (int) p1_x+1, ii = 0; i < p1_x + p_width; i += p_ww+2, ii++) {
					for (int j = (int) p1_y+1, jj = 0; j < p1_y + p_height; j += p_hh+2, jj++) {
						if (this.pattern1[ii][jj] == 0) {
							fill(255, 255, 255);
						}
						else if (this.pattern1[ii][jj] == 1) {
							fill(209, 59, 0);
						}
						rect(i, j, p_ww, p_hh);
					}
				}
			}
			else if (this.pattern2Got && 
					(this.chooserSelectedGroup == 0 || this.chooserSelectedGroup == 1) &&
					mouseX >= p2_x && mouseX <= p2_x+p_width && 
					mouseY >= p2_y && mouseY <= p2_y+p_height) {
				noStroke();
				fill(255, 255, 0);
				rect(p2_x-5, p2_y-5, p_width+10, p_height+10);
				fill(0, 0, 0);
				rect(p2_x, p2_y, p_width, p_height);
				//draw second blocks
				stroke(0, 0, 0, 100);
				for (int i = (int) p2_x+1, ii = 0; i < p2_x + p_width; i += p_ww+2, ii++) {
					for (int j = (int) p2_y+1, jj = 0; j < p2_y + p_height; j += p_hh+2, jj++) {
						if (this.pattern2[ii][jj] == 0) {
							fill(255, 255, 255);
						}
						else if (this.pattern2[ii][jj] == 1) {
							fill(209, 59, 0);
						}
						rect(i, j, p_ww, p_hh);
					}
				}
			}
		}
		//OK button
		float ok_x = WIDTH-360;
		float ok_y = HEIGHT-73;
		float ok_width = 150;
		float ok_height = 60;
		
		float ok_t_x = ok_x+ok_width/2-22;
		float ok_t_y = ok_y+ok_height/2+12;
		
		if (this.chooserSelectedGroup != 0 && !this.selectedGroupOK) {
			noStroke();
			fill(back_r, back_g, back_b);
			rect(WIDTH-500, HEIGHT-70, 450, 55);
			
			stroke(20, 20, 20, 100);
			fill(255, 118, 20);
			rect(ok_x, ok_y, ok_width, ok_height, 6);
			
			if (mouseX >= ok_x && mouseX <= ok_x+ok_width &&
					mouseY >= ok_y && mouseY <= ok_y+ok_height) {
				fill(255, 169, 107);
				rect(ok_x, ok_y, ok_width, ok_height, 6);
			}
			
			textFont(this.font[3]);
			textSize(40);
			
			fill(200, 200, 200);
			text("OK", ok_t_x +2, ok_t_y +2);
			fill(0, 0, 0);
			text("OK", ok_t_x, ok_t_y);
		}
		else if (this.selectedGroupOK) {
			noStroke();
			fill(back_r, back_g, back_b);
			rect(WIDTH-500, HEIGHT-70, 450, 55);
			
			textFont(this.font[3]);
			textSize(30);
			fill(0, 0, 0);
			text("Chose pattern submitted", WIDTH-450 +2, HEIGHT-35 +2);
			fill(240, 240, 240);
			text("Chose pattern submitted", WIDTH-450, HEIGHT-35);
		}
		
		/********************
		 * show information *
		 ********************/
		//refreshing
		fill(back_r, back_g, back_b);
		noStroke();
		//stroke(0);
		rect(30, HEIGHT-80, 330, 70);
		
		//showing information
		textFont(this.font[3]);
		textSize(25);
		fill(0, 255, 0);
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
					text(i1.toString(), 50, HEIGHT-50);
					text(i2.toString(), 50, HEIGHT-20);
				}
				else {
					if (this.frame2 < 20) {
						text(i1.toString(), 50, HEIGHT-50);
						text(i2.toString(), 50, HEIGHT-20);
					}
					else if (this.frame2 >= 20 && this.frame2 < 40) {
						text(i1.toString(), 50, HEIGHT-50);
						text(i2.toString() + ".", 50, HEIGHT-20);
					}
					else if (this.frame2 >= 40 && this.frame2 < 60) {
						text(i1.toString(), 50, HEIGHT-50);
						text(i2.toString() + "..", 50, HEIGHT-20);
					}
					else {
						text(i1.toString(), 50, HEIGHT-50);
						text(i2.toString() + "...", 50, HEIGHT-20);
					}
				}
			}
			//info that only takes one roll
			else {
				if (!this.isWaiting) {
					text(this.information, 50, HEIGHT-35);
				}
				else {
					if (this.frame2 < 20) {
						text(this.information, 50, HEIGHT-50);
					}
					else if (this.frame2 >= 20 && this.frame2 < 40) {
						text(this.information + ".", 50, HEIGHT-50);
					}
					else if (this.frame2 >= 40 && this.frame2 < 60) {
						text(this.information + "..", 50, HEIGHT-50);
					}
					else 
						text(this.information + "...", 50, HEIGHT-50);
				}
			}
			this.frame2 = (this.frame2 + 1) % 80;
		}
	}
	
	public void mouseClicked() {
		/****************************
		 * getting chooser's answer *
		 ****************************/
		float a_x = WIDTH/2-50;
		float a_y = HEIGHT/100*97;
		float a_width = 100;
		float a_height = 60;
		//ask for answer input
		if (!this.answeringLock) {
			if (mouseX >= a_x && mouseY >= a_y && 
					mouseX <= a_x+a_width && mouseY <= a_y+a_height &&
					!this.hasChooserAnswered) {
				String input = 
						(String) javax.swing.JOptionPane.showInputDialog(null, 
						"The word is : ", "Please enter a Chinese word", 3, null, 
						null, null);
				if (input != null && input.length() > 0) {
					this.chooserAnswer = String.valueOf(input.toCharArray()[0]);
					this.hasChooserAnswered = true;	
				}
			}
		}
		
		/********************
		 * pattern choosing *
		 ********************/
		float p_width = WIDTH/2-100;
		float p_height = HEIGHT/3*2+40;
		//first 
		float p1_x = 60;
		float p1_y = HEIGHT/6-25;
		//second
		float p2_x = WIDTH-p_width-60;
		float p2_y = HEIGHT/6-25;
		
		if (!this.choosingLock && !this.selectedGroupOK) {
			if (mouseX >= p1_x && mouseX <= p1_x+p_width && 
					mouseY >= p1_y && mouseY <= p1_y+p_height) {
				this.chooserSelectedGroup = 1;
			}
			else if (mouseX >= p2_x && mouseX <= p2_x+p_width && 
					mouseY >= p2_y && mouseY <= p2_y+p_height) {
				this.chooserSelectedGroup = 2;
			}
		}
		
		/***************
		 * choosing OK *
		 ***************/
		float ok_x = WIDTH-360;
		float ok_y = HEIGHT-73;
		float ok_width = 150;
		float ok_height = 60;
		
		if (this.chooserSelectedGroup != 0 && !this.selectedGroupOK) {
			if (mouseX >= ok_x && mouseX <= ok_x+ok_width &&
					mouseY >= ok_y && mouseY <= ok_y+ok_height) {
				noStroke();
				fill(back_r, back_g, back_b);
				rect(ok_x -2, ok_y -2, ok_width +4, ok_height +4);
				this.selectedGroupOK = true;
			}
		}
	}
	
	public void keyPressed() {
		
		/***********
		 * testing *
		 ***********/
		if (key == '1') {
			this.pattern1Got = true;
		}
		if (key == '2') {
			
			this.pattern2Got = true;
		}
		if (key == '3') {
			this.answeringLock = false;
		}
		if (key == '4') {
			this.choosingLock = false;
		}
		if (key == '5') {
			this.setPlayerScore(score+50);
		}
		if (key == '6') {
			this.setPlayerScore(score-50);
		}
		if (key == '`') {
			this.setPlayerName("Mr. Good Good");
			this.setPlayerScore(900);
		}
		if (key == 'i') {
			this.showInfo("this can be a long sentence but still don't go too far", true);
		}
		if (key == 'k') {
			this.showInfo("shorter sentence it is", false);
		}
		if (key == 'o') {
			this.closeInfo();
		}
		if (key == '[') {
			this.answerJudge(1);
		}
		if (key == ']') {
			this.answerJudge(2);
		}
		if (key == '|') {
			this.answerJudge(0);
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
	public void setFirstGroupPattern(int[][] pattern) {
		// TODO Auto-generated method stub
		this.pattern1 = pattern;
		this.pattern1Got = true;
	}
	@Override
	public void setSecondGroupPattern(int[][] pattern) {
		// TODO Auto-generated method stub
		this.pattern2 = pattern;
		this.pattern2Got = true;
	}
	@Override
	public boolean hasChooserAnswered() {
		// TODO Auto-generated method stub
		return this.hasChooserAnswered;
	}
	@Override
	public String getChooserAnswer() {
		// TODO Auto-generated method stub
		return this.chooserAnswer;
	}
	@Override
	public boolean hasChooserSelectedGroup() {
		// TODO Auto-generated method stub
		return this.selectedGroupOK;
	}
	@Override
	public int getChooserSelectedGroup() {
		// TODO Auto-generated method stub
		return this.chooserSelectedGroup;
	}
	@Override
	public void EnableChooserFillAnswer(boolean permission) {
		// TODO Auto-generated method stub
		if (permission) 
			this.answeringLock = false;
		else 
			this.answeringLock = true;
	}
	
	public void EnableChooserToChoose(boolean permission) {
		if (permission) 
			this.choosingLock = false;
		else 
			this.choosingLock = true;
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
	
	public void answerJudge(int judge) {
		this.answerJudge = judge;
	}
}
