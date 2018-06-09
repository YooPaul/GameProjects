package netOmok;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.*;
import java.io.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
public class OmokGamePage_client extends JFrame implements ActionListener {
	private final int line=18;
	private final int line_space=33;
	private int turn=1;//2 �� ��� 1 �� ������
	private int[][] stone=new int[line][line];
	private int xpos;
	private int ypos;
	OmokDTO dto=new OmokDTO();
	JPanel pan=new JPanel();
	Socket socket;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	Graphics g;
	int player;
	OmokMouseListener om=new OmokMouseListener();
	public void paint(Graphics g){
		g.setColor(Color.ORANGE);
		g.fillRect(0, 0, (line+1)*line_space, (line+1)*line_space);
		g.setColor(Color.BLACK);
		for(int i=1;i<line+1;i++){
			g.drawLine(i*line_space, 1*line_space,i*line_space ,line*line_space);
			g.drawLine(1*line_space,i*line_space ,line*line_space, i*line_space);
			
		}
		
	
	}
	public OmokGamePage_client(){
		super("���� ������ Ŭ���̾�Ʈ ����");
		//setSize((int)((((line+1)*line_space)*2.5)),(int)(((line+1)*line_space)*1.5));
		setSize((line+1)*line_space, (line+1)*line_space);
		setLocation(100, 40);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		setLayout(null);
		add(pan);
		//dto.setPlayer(2);
		pan.setBounds(0, 0, (line+1)*line_space, (line+1)*line_space);
		g=pan.getGraphics();
		update(g);
		socketBegin();
		if(player==1){
			pan.addMouseListener(om);
		}
		Thread t=new Thread(new Accept());
		t.start();
	}
	public void socketBegin(){
		try {
			socket=new Socket("localhost",20016);	
			System.out.println("���� ���� ����");
			oos=new ObjectOutputStream(socket.getOutputStream());
			ois=new ObjectInputStream(socket.getInputStream());
			player=(Integer)ois.readObject();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		new OmokGamePage_client();
	}
	public void actionPerformed(ActionEvent e){
		
	}
	public class OmokMouseListener extends MouseAdapter {
		
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
				//if(turn==1){
					g.setColor(Color.WHITE);
					stone[x/line_space-1][y/line_space-1]=1;
					dto.setXpos(x/line_space-1);
					dto.setYpos(y/line_space-1);
					try {
						oos.writeObject(dto);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					turn=1;
				//}
				g.fillOval(x-line_space/2, (y-line_space/2)+10, (int)(line_space*0.9), (int)(line_space*0.9));
				xpos=x/line_space-1; ypos=y/line_space-1;
				//System.out.println(xpos+" and "+ypos);
				if(checkFive(player)){
						JOptionPane.showMessageDialog(new OmokGamePage_client(), "����� �¸�", "���� ��", JOptionPane.INFORMATION_MESSAGE);
						System.exit(0);
				}
					pan.removeMouseListener(om);
					//Thread t=new Accept();
					//t.start();
					
				}
						
					
			//g.setColor(Color.BLACK);
			//g.fillOval(x-line_space/2, y-line_space/2, line_space, line_space);
		}
	}
	public class Accept extends Thread{
	public void run(){
		while(true){
			try {
				OmokDTO dto=(OmokDTO)ois.readObject();
				System.out.println("�о��");
				g.setColor(Color.BLACK);
				stone[dto.getXpos()][dto.getYpos()]=2;
				g.fillOval((dto.getXpos()+1)*line_space-line_space/2, ((dto.getYpos()+1)*line_space-line_space/2)+14,
						(int)(line_space*0.9), (int)(line_space*0.9));
				if(checkFive(dto.getPlayer())){
					JOptionPane.showMessageDialog(new OmokGamePage_client(), "�������� �¸�", "���� ��", JOptionPane.INFORMATION_MESSAGE);
					System.exit(0);
				}
				pan.addMouseListener(om);
			//	break;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//pan.addMouseListener(om);
	}
	}
	public boolean checkFive(int player){
		//����
		
		if(stoneCount(player,stone,xpos,ypos,1,0)+stoneCount(player,stone,xpos,ypos,-1,0)>5){
			dto.setWinnerIs(player);	return true; }
		//����
		else if(stoneCount(player,stone,xpos,ypos,0,1)+stoneCount(player,stone,xpos,ypos,0,-1)>5){
			dto.setWinnerIs(player);return true;}
		//negative �밢��
		else if(stoneCount(player,stone,xpos,ypos,1,1)+stoneCount(player,stone,xpos,ypos,-1,-1)>5){
			dto.setWinnerIs(player);	return true;}
		//positive �밢��
		else if(stoneCount(player,stone,xpos,ypos,1,-1)+stoneCount(player,stone,xpos,ypos,-1,1)>5){
			dto.setWinnerIs(player);	return true;}
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
