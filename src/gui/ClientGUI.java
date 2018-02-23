package gui;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ClientGUI extends JFrame implements ActionListener{
	/*
	 * The area outputing messages for the player
	 */
	private JTextArea messageArea;
	/*
	 * Fields of IP Addresses that the client can enter in 
	 */
	private JComboBox ip1;
	private JComboBox ip2;
	private JComboBox ip3;
	private JComboBox ip4;
	/*
	 * Fields of port number that the client can enter in 
	 */
	private JTextField port;
	/*
	 * Click it and try to connect to server
	 */
	private JButton connectButton;
	
	private boolean hasPlayerSentIPAddr;
	
	private String IPAddress;
	
	public ClientGUI(){
		//get the content panel
		Container mainPanel = this.getContentPane();
		//set the size of the client window, not game window
		this.setSize(400, 330);
		//set title of the client window
		this.setTitle("Client");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//we use flowlayout here
		mainPanel.setLayout(new FlowLayout());
		
		//instantiate the message area
		this.messageArea = new JTextArea(10,33);
		this.messageArea.setEnabled(true);
		JScrollPane scrollPane = new JScrollPane(this.messageArea);
		mainPanel.add(scrollPane);
		
		//instantiate the UI of IP fields 
		JPanel ipPanel = new JPanel();
		JLabel ipLabel = new JLabel("Server IP:");
		String[] ipFields= returnIPField();
		String dot = ".";
		this.ip1 = new JComboBox(ipFields);
		JLabel dot1 = new JLabel(dot);
		this.ip2 = new JComboBox(ipFields);
		JLabel dot2 = new JLabel(dot);
		this.ip3 = new JComboBox(ipFields);
		JLabel dot3 = new JLabel(dot);
		this.ip4 = new JComboBox(ipFields);
		ipPanel.add(ipLabel);
		ipPanel.add(this.ip1);
		ipPanel.add(dot1);
		ipPanel.add(this.ip2);
		ipPanel.add(dot2);
		ipPanel.add(this.ip3);
		ipPanel.add(dot3);
		ipPanel.add(this.ip4);
		mainPanel.add(ipPanel);
		
		//instantiate the UI of port number
		JPanel portPanel = new JPanel();
		JLabel portLabel = new JLabel("Port Number:");
		this.port = new JTextField(20);
		this.port.setText("8000");
		portPanel.add(portLabel);
		portPanel.add(this.port);
		mainPanel.add(portPanel);
		
		//instantiate connection button
		this.connectButton = new JButton("Log In");
		this.connectButton.addActionListener(this);
		mainPanel.add(this.connectButton);
		
		//set default value of IP address
		this.ip1.setSelectedItem("127");
		this.ip2.setSelectedItem("0");
		this.ip3.setSelectedItem("0");
		this.ip4.setSelectedItem("1");
		
		this.hasPlayerSentIPAddr = false;
		
		this.setVisible(true);
		
		this.addLine("Please enter IP Address to connect to server...");
	}
	
	/*
	 * Prepare possible values of each IP address field
	 * It is ranged from 0 to 255
	 */
	private String[] returnIPField(){
		String[] ipFields = new String[256];
		for(int i=0;i<256;i++){
			ipFields[i]=""+i;
		}
		return ipFields;
	}
	
	/*
	 * Append messages on message area and is shown on UI
	 */
	public void addLine(final String str){
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run(){
				ClientGUI.this.messageArea.append(str+"\n");
			}
		});
	}
	
	public void actionPerformed(ActionEvent e){
		/*
		 * If the button is clicked, try to connect to server.
		 * And if the connection is success, the UI would be disabled to make the player
		 * unable to connect again.
		 */
		if(e.getSource()==this.connectButton){
			int port=0;
			try{
				//try to parse the port number
				port = Integer.parseInt(this.port.getText());
				if(port<1||port>65535){
					JOptionPane.showMessageDialog(null, "Please enter correct port number(range is from 1 to 65535)",
												"Port number Incorrect!!", JOptionPane.INFORMATION_MESSAGE);
					this.addLine("Port number Incorrect!!");
					return;
				}
			}
			catch(NumberFormatException e1){
				JOptionPane.showMessageDialog(null, "Please enter correct port number(range is from 1 to 65535)",
						"Port number Incorrect", JOptionPane.INFORMATION_MESSAGE);
				this.addLine("Port number Incorrect!!");
				return;
			}
			catch(Exception e2){
				this.addLine("Port number Incorrect!!");
			}

			this.connectButton.setEnabled(false);
			this.ip1.setEnabled(false);
			this.ip2.setEnabled(false);
			this.ip3.setEnabled(false);
			this.ip4.setEnabled(false);
			this.port.setEnabled(false);
			this.IPAddress = new String((String)this.ip1.getSelectedItem()+"."+
					(String)this.ip2.getSelectedItem()+"."+
					(String)this.ip3.getSelectedItem()+"."+
					(String)this.ip4.getSelectedItem());
			this.hasPlayerSentIPAddr = true;
		}
	}
	
	public String getIPAddress(){
		try{
			while(true){
				if(this.hasPlayerSentIPAddr==true)
					break;
				Thread.sleep(100);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return this.IPAddress;
	}
}
