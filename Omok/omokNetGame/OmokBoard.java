package omokNetGame;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.net.*;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
public class OmokBoard extends JPanel  {
	private final int line=18;
	private final int line_space=33;
	private int turn=1;//2 가 흰색 1 이 검은색
	private int[][] stone=new int[line][line];
	private int xpos;
	private int ypos;
	public static OmokBoard ob;
	private Socket socket;
	private static Socket chatSocket;
	private static DataOutputStream chatdos;
	private static DataInputStream chatdis;
	private ObjectOutputStream dos;
	private ObjectInputStream dis;
	private Point p=new Point();
	private Graphics g;
	//private JFrame f=new JFrame("Omok");
	private MouseAdapter ma=new MouseAdapter(){
		
		@Override
		public void mouseClicked(MouseEvent e) {
			int x=e.getX(),y=e.getY();
			if(!putandCheck(x,y)) return;
			p.setLocation(x, y);
			try {
				dos.writeObject(p);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			removeMouseListener(ma);
		}
	};
	public boolean putandCheck(int x,int y){
		while(g==null){
			g=getGraphics();
			System.out.println(g);
		}
		int minx=Math.abs(x-(1*line_space));
		int miny=Math.abs(y-(1*line_space));
		int linex=1;
		int liney=1;
		for(int i=2;i<line+1;i++){
			if(Math.abs(x-(i*line_space))<minx)
			{
				minx=Math.abs(x-(i*line_space));
				linex=i;
			}
			if(Math.abs(y-(i*line_space))<miny)
			{
				miny=Math.abs(y-(i*line_space));
				liney=i;
			}
		}
				x=linex*line_space;
				y=liney*line_space;
			if(stone[x/line_space-1][y/line_space-1]==0){
			if(turn==1){
				g.setColor(Color.BLACK);
				stone[x/line_space-1][y/line_space-1]=1;
				
			}
			else if(turn==2){
				g.setColor(Color.WHITE);
				stone[x/line_space-1][y/line_space-1]=2;
				
			}
			g.fillOval(x-line_space/2, y-line_space/2, (int)(line_space*0.9), (int)(line_space*0.9));
			
			xpos=x/line_space-1; ypos=y/line_space-1;
			//System.out.println(xpos+" and "+ypos);
			
			if(checkFive()){
				if(turn==1){
					JOptionPane.showMessageDialog(ob, "Black Win", "Game Over", JOptionPane.INFORMATION_MESSAGE);
				}else
					JOptionPane.showMessageDialog(OmokBoard.ob, "White Win", "Game Over", JOptionPane.INFORMATION_MESSAGE);
				//System.exit(0);
			}
			return true;
			}else
				return false;
					
				
		//g.setColor(Color.BLACK);
		//g.fillOval(x-line_space/2, y-line_space/2, line_space, line_space);
	}
	
	
	public void paint(Graphics g){
		g.setColor(Color.ORANGE);
		g.fillRect(0, 0, (line+1)*line_space, (line+1)*line_space);
		g.setColor(Color.BLACK);
		for(int i=1;i<line+1;i++){
			g.drawLine(i*line_space, 1*line_space,i*line_space ,line*line_space);
			g.drawLine(1*line_space,i*line_space ,line*line_space, i*line_space);
			
		}
		//this.g=g;
		g=getGraphics();
	}
	public OmokBoard(){
		setLocation(0,0);
		//super("오목 보드판");
		setSize((line+1)*line_space,(line+1)*line_space);
		//setLocation(300, 200);
	//	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setResizable(false);
		//getContentPane().setLayout(null);
		
		//setVisible(true);
		g=getGraphics();
		/*
		f.setLocation(300, 200);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(true);
		f.setSize(938,679);
		f.setVisible(true);
		f.getContentPane().setLayout(null);
		f.getContentPane().add(this);
		f.getContentPane().repaint();
		*/
		try {
			socket=new Socket("localhost",9698);
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
		addMouseListener(ma);
		new DataParse().start();
	}
	private class DataParse extends Thread{
		
		public DataParse(){
			try {
				dos=new ObjectOutputStream(socket.getOutputStream());
				dis=new ObjectInputStream(socket.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public void run(){
			int orgTurn;
			try {
				turn=Integer.parseInt((String)dis.readObject());
				orgTurn=turn;
				if(turn==2){
					System.out.println("You are the White Stone. Wait for your turn.");
					removeMouseListener(ma);
				}else{
					System.out.println("You are the Black Stone. Start First.");
				}
				while(dis!=null){
					p=(Point)dis.readObject();
					System.out.println("Opponent Stone Read");
					turn=turn==1 ? 2:1;
					putandCheck(p.x,p.y);
					turn=orgTurn;
					addMouseListener(ma);
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args){
		JFrame f=new JFrame();
		f.setLocation(300, 200);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
		f.setSize(938,679);
		
		f.setResizable(false);
		OmokBoard.ob=new OmokBoard();
		f.setLayout(null);
		JPanel pan=new JPanel();
		JTextArea ta=new JTextArea();
		ta.setEditable(false);
		ta.setFont(new Font("Arial", Font.BOLD, 14));
		ta.setText("---Gomoku Chat Panel---\n");
		ta.setBounds(0,0,270,600);
		ta.setBackground(new Color(66, 197, 244));
		JTextArea ta2=new JTextArea();
		ta2.setBounds(0,600,270,70);
		ta2.setFont(new Font("Arial", Font.BOLD, 14));
		setAction(ta,ta2);
		pan.setBounds(660, 0, 270, 660);
		pan.setLayout(null);
	//	pan.add(new JLabel("Chat Panel"));
		pan.add(ta);
		pan.add(ta2);
		//pan.setBackground(Color.BLUE);
		f.add(ob);
		f.add(pan);
	//	f.add(ta);
		f.setVisible(true);
		f.repaint();
	
		//OmokBoard ob=new OmokBoard();
	}
	
	private static class ReceiveMessage extends Thread{
		private DataInputStream dis;
		private JTextArea ta;
		public ReceiveMessage(DataInputStream dis, JTextArea ta){
			this.dis=dis;
			this.ta=ta;
		}
		public void run(){
			String msg="";
			while(dis!=null){
				try {
					msg=dis.readUTF();
					ta.append("[Opponent] : "+msg+"\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;
				}
			}
		}
	}
	
	private static void setAction(JTextArea ta,JTextArea ta2){
		
		try {
			chatSocket = new Socket("localhost",9699);
			chatdos=new DataOutputStream(chatSocket.getOutputStream());
			chatdis=new DataInputStream(chatSocket.getInputStream());
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
		//socketConnect(socket,dos,dis);
		ta2.addKeyListener(new KeyListener(){

			private FontMetrics fm=ta2.getFontMetrics(ta2.getFont());
			private String[] str;
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				String msg="";
				if(arg0.getKeyCode()==KeyEvent.VK_ENTER){
					msg=ta2.getText();
					try {
						chatdos.writeUTF(msg);
						ta2.setText("");
						ta2.setCaretPosition(0);
						ta.append("[Me] : "+msg+"\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				if(arg0.getKeyCode()==KeyEvent.VK_ENTER){
					ta2.setText(null);
				}
				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				//System.out.println(ta2.getText().replaceAll("\n", "newline"));
				str=ta2.getText().split("\n");
				if(str.length>0){
				int width=fm.stringWidth(str[str.length-1]);
				if(!(width<270))
					ta2.append("\n");
				}
				
			}
			
		});
		new ReceiveMessage(chatdis,ta).start();
	}
	
	private static void socketConnect(Socket s,DataOutputStream dos,DataInputStream dis){
		try {
			s=new Socket("localhost",9699);
			dos=new DataOutputStream(s.getOutputStream());
			dis=new DataInputStream(s.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public boolean checkFive(){
		//가로
		int dturn=turn;
		//if(turn==1) dturn=2;
		//else dturn=1;
		if(stoneCount(dturn,stone,xpos,ypos,1,0)+stoneCount(dturn,stone,xpos,ypos,-1,0)>5)
			return true;
		//세로
		else if(stoneCount(dturn,stone,xpos,ypos,0,1)+stoneCount(dturn,stone,xpos,ypos,0,-1)>5)
			return true;
		//negative 대각선
		else if(stoneCount(dturn,stone,xpos,ypos,1,1)+stoneCount(dturn,stone,xpos,ypos,-1,-1)>5)
			return true;
		//positive 대각선
		else if(stoneCount(dturn,stone,xpos,ypos,1,-1)+stoneCount(dturn,stone,xpos,ypos,-1,1)>5)
			return true;
		else
			return false;
	}
	public int stoneCount(int turn,int[][] stone,int x,int y,int dx,int dy){
		int ct=0;
		for(;stone[x][y]==turn;ct++){
			x+=dx;
			y+=dy;
			if(x<1||x>=line||y<1||y>=line){
				ct++;
				break;
			}
		}
		return ct;
	}
}
