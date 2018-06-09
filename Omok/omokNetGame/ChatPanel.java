package omokNetGame;

import java.awt.Color;

import javax.swing.*;
public class ChatPanel {

	public static void main(String[] args) {
		JFrame f=new JFrame("TextArea");
		f.setBounds(200, 200, 800, 750);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLayout(null);
		JTextArea ta=new JTextArea(50,50);
	//	ta.setBounds(0, 0, 600, 700);
		//ta.setBackground(Color.RED);
		//ta.setBounds(0,0,700,650);
		JPanel p=new JPanel();
		p.setBounds(0,0,600,700);
		//p.setBackground(Color.BLACK);
		p.add(ta);
		f.add(p);
	//	f.setResizable(false);
		
		f.setVisible(true);
		
	}

}
