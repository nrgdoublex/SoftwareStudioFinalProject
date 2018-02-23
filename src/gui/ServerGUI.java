package gui;

import javax.swing.*;

public class ServerGUI extends JFrame{
	/*
	 * Message area for server itself and control units
	 */
	private JTextArea messageArea;
	
	public ServerGUI(){
		this.setTitle("Server");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.messageArea = new JTextArea(20,40);
		this.messageArea.setEnabled(true);
		JScrollPane scrollPane = new JScrollPane(this.messageArea);
		this.add(scrollPane);
		
		this.pack();
		this.setVisible(true);
		
		this.addLine("Server starts listening on port 8000...");
	}
	
	public void addLine(final String str){
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run(){
				ServerGUI.this.messageArea.append(str+"\n");
			}
		});
	}
}
