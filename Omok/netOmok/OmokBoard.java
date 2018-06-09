package netOmok;
import java.awt.Color;
import java.net.*;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
public class OmokBoard extends JFrame  {
	private final int line=18;
	private final int line_space=33;
	private int turn=1;//2 가 흰색 1 이 검은색
	private int[][] stone=new int[line][line];
	private int xpos;
	private int ypos;
	public static OmokBoard ob;
	private Socket socket;
	private ObjectOutputStream dos;
	private ObjectInputStream dis;
	private Point p=new Point();
	private Graphics g;
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
		
	
	}
	public OmokBoard(){
		super("오목 보드판");
		setSize((line+1)*line_space,(line+1)*line_space);
		setLocation(300, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		g=getGraphics();
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
		OmokBoard.ob=new OmokBoard();
		//OmokBoard ob=new OmokBoard();
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
