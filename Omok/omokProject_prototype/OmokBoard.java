package omokProject_prototype;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
public class OmokBoard extends JFrame implements ActionListener {
	private final int line=18;
	private final int line_space=33;
	private int turn=1;//2 가 흰색 1 이 검은색
	private int[][] stone=new int[line][line];
	private int xpos;
	private int ypos;
	public static OmokBoard ob;
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
		Graphics g=getGraphics();
		addMouseListener(new MouseAdapter(){
			
			@Override
			public void mouseClicked(MouseEvent e) {
				int x=e.getX(),y=e.getY();
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
						turn=2;
					}
					else if(turn==2){
						g.setColor(Color.WHITE);
						stone[x/line_space-1][y/line_space-1]=2;
						turn=1;
					}
					g.fillOval(x-line_space/2, y-line_space/2, (int)(line_space*0.9), (int)(line_space*0.9));
					xpos=x/line_space-1; ypos=y/line_space-1;
					//System.out.println(xpos+" and "+ypos);
					if(checkFive()){
						if(turn==1){
							JOptionPane.showMessageDialog(ob, "흰색의 승리", "게임 끝", JOptionPane.INFORMATION_MESSAGE);
						}else
							JOptionPane.showMessageDialog(OmokBoard.ob, "검은색의 승리", "게임 끝", JOptionPane.INFORMATION_MESSAGE);
						System.exit(0);
					}
					}
							
						
				//g.setColor(Color.BLACK);
				//g.fillOval(x-line_space/2, y-line_space/2, line_space, line_space);
			}
		});
	}
	public static void main(String[] args){
		OmokBoard.ob=new OmokBoard();
		//OmokBoard ob=new OmokBoard();
	}
	public void actionPerformed(ActionEvent e){
		
	}
	
	public boolean checkFive(){
		//가로
		int dturn;
		if(turn==1) dturn=2;
		else dturn=1;
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
