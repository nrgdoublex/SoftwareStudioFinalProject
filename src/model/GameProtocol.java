package model;

import java.io.File;

public final class GameProtocol{
	
/*------------------------------Network Constants Start------------------------------*/
	public static final String IP_ADDRESS 	= "127.0.0.1";
	public static final int PORT_NUMBER 	= 8000;
/*-------------------------------Network Constants End-------------------------------*/
	
/*------------------------------Game Setting Start------------------------------*/
	public static final int NUM_OF_PLAYERS 			= 4;
	public static final int DIMENSION_OF_PATTERN 	= 20;
	public static final int EXTRA_BLOCK_NUM 		= 10;
	public static final int TIME_LIMIT		 		= 60;
/*-------------------------------Game Setting End-------------------------------*/
	
/*------------------------------Player ID------------------------------*/
	public static final int PLAYER0_ID=0;
	public static final int PLAYER1_ID=1;
	public static final int PLAYER2_ID=2;
	public static final int PLAYER3_ID=3;
/*------------------------------Player ID------------------------------*/

/*------------------------------Scores------------------------------*/
	//the score of correct answers
	public static final int SCORE_CORRECT_ANSWER=10;
	//the score of first submit
	public static final int EXTRA_SCORE_FOR_FIRST_SUMMIT_GROUP=10;
	//the score of good patterns
	public static final int EXTRA_SCORE_FOR_BETTER_PATTERN=10;
/*------------------------------Scores------------------------------*/
	
/*------------------------------Resource Paths------------------------------*/
	public static final String RESOURCE_ROOT_DIR = "./res/";
	public static final String FONT_DIR = RESOURCE_ROOT_DIR + "font/";
	public static final String IMAGE_DIR = RESOURCE_ROOT_DIR + "image/";
	public static final String CHINESE_WORD_DIR = RESOURCE_ROOT_DIR + "word/";
	public static final String PATTERN_DIR = RESOURCE_ROOT_DIR + "pattern/";
	
	public static final String FONT_ARIAL_PATH = FONT_DIR + "arial.ttf";
	public static final String FONT_KAIU_PATH = FONT_DIR + "kaiu.ttf";
	public static final String FONT_JLS_PATH = FONT_DIR + "jls.otf";
	public static final String FONT_JLS1_PATH = FONT_DIR + "jls1.otf";
	public static final String FONT_SMB_PATH = FONT_DIR + "smb.ttf";
	public static final String FONT_SMB2_PATH = FONT_DIR + "smb2.ttf";
	
	public static final String IMAGE_CLOCK_PATH = IMAGE_DIR + "clock.png";
	public static final String IMAGE_REFRESH_PATH = IMAGE_DIR + "refresh.png";
	public static final String IMAGE_TROPHY_PATH = IMAGE_DIR + "trophy.png";
	
	public static final String CHINESE_WORD_PATH = CHINESE_WORD_DIR + "ChineseWord.txt";
/*------------------------------Resource Paths------------------------------*/
	
/*------------------------------Game GUI Dimension------------------------------*/
	public static final int SELECTOR_WIDTH = 1200;
	public static final int SELECTOR_HEIGHT = 660;
	public static final int CHOOSER_WIDTH = 1200;
	public static final int CHOOSER_HEIGHT = 660;
	public static final int LEADERBOARD_WIDTH = 1200;
	public static final int LEADERBOARD_HEIGHT = 660;
/*------------------------------Game GUI Dimension------------------------------*/
}
