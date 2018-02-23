package gui;

import javax.swing.*;

public class GUIFrame extends JFrame{

	
	public GUIFrame(){
		
	}
	
	public GUIFrame(SelectorGUI selector){
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("SELECTOR_TEST");
		
		//must add some offsets or the content will be incomplete
		this.setSize(SelectorGUI.WIDTH +20, SelectorGUI.HEIGHT +40); 
		
		this.setContentPane(selector);
		this.setVisible(true);
	}
	
	public GUIFrame(ChooserGUI chooser){
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("CHOOSER_TEST");
		
		//must add some offsets or the content will be incomplete
		this.setSize(ChooserGUI.WIDTH +20, ChooserGUI.HEIGHT +40); 
		
		this.setContentPane(chooser);
		this.setVisible(true);
	}
	
	public void showSelector(SelectorGUI selector){
		this.setVisible(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("SELECTOR");
		
		//must add some offsets or the content will be incomplete
		this.setSize(SelectorGUI.WIDTH +20, SelectorGUI.HEIGHT +40); 
		this.setContentPane(selector);
		this.setVisible(true);
	}
	
	public void showChooser(ChooserGUI chooser){
		this.setVisible(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("CHOOSER");
		
		//must add some offsets or the content will be incomplete
		this.setSize(ChooserGUI.WIDTH +20, ChooserGUI.HEIGHT +40); 
		
		this.setContentPane(chooser);
		this.setVisible(true);
	}
	
	public void showLeaderBoard(LeaderBoardGUI leaderBoard){
		this.setVisible(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("LEADERBOARD");
		
		//must add some offsets or the content will be incomplete
		this.setSize(LeaderBoardGUI.WIDTH +20, LeaderBoardGUI.HEIGHT +40); 
		
		this.setContentPane(leaderBoard);
		this.setVisible(true);
	}
}
