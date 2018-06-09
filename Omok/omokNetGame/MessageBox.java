package omokNetGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class MessageBox extends JLabel {

	private String msg;
	private FontMetrics fm;
	private int height, width;
	private final int hpad=15,vpad=15;
	private int playerNum; // me=0  opponent=1
	private String[] str;
	
	private int netWidth,netHeight;
	
	public static void main(String[] args) {
		JFrame f=new JFrame("Message Panel");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(400,300);
		f.setLayout(new BoxLayout(f.getContentPane(),BoxLayout.Y_AXIS));
		JPanel pan=new JPanel();
		JScrollPane sp=new JScrollPane(pan);
		JPanel p2=new JPanel();
		p2.setBackground(Color.black);
		p2.setPreferredSize(new Dimension(400,100));
		JTextField tf=new JTextField(30);
		p2.add(tf);
		JScrollBar sb=sp.getVerticalScrollBar();
		tf.addActionListener(new ActionListener(){

			
			int a=400;
			@Override
			public void actionPerformed(ActionEvent arg0) {
				pan.add(new MessageBox(tf.getText(),0));
				tf.setText("");
				pan.add(Box.createRigidArea(new Dimension(10,15)));
				pan.setPreferredSize(new Dimension(0,a));
				a+=25;
				sb.setVisible(false);
				sb.setValue(sb.getMaximum());
				pan.revalidate();
				pan.repaint();
				
			}
			
		});
		pan.setBackground(Color.RED);
		pan.setPreferredSize(new Dimension(400,200));
		pan.setMinimumSize(new Dimension(400,200));
		pan.setLayout(new BoxLayout(pan,BoxLayout.Y_AXIS));
		//pan.setLayout(new FlowLayout());
		
		//pan.add(new MessageBox("Hello",0));
		JLabel lab=new JLabel();
		//lab.setMaximumSize(new Dimension(0,200));
		//lab.setAlignmentX(0.5f);
		pan.add(lab);
		pan.add(new JLabel("Hey"));
		pan.add(new JLabel("Hey"));
		pan.add(new JLabel("Hey"));
		//pan.add(new MessageBox("Hello",0));
		
		
		f.getContentPane().add(sp);//add(pan);
		f.getContentPane().add(p2);
		//f.pack();
		f.setVisible(true);

	}
	
	public MessageBox(String msg,int player){
		super(); // call up parent's constructor to initialize parent member fields
		this.msg=msg;
		str=msg.split("\n");
		playerNum=player;
		fm=new Canvas().getFontMetrics(new Font("Arial", Font.BOLD, 14));
		height=fm.getHeight()-vpad;
		width=fm.stringWidth(msg);
		if(str.length<=1){
			netWidth=width+2*hpad;
			netHeight=height+(int)(1.3*vpad);
		}else{
			netWidth=fm.stringWidth(str[0])+2*hpad;
			netHeight=8*str.length*height+(int)(1.3*vpad);
		}
		setPreferredSize(new Dimension(width+2*hpad, height+(int)(1.3*vpad)));
		//setSize(new Dimension(width+2*hpad, height+2*vpad));
	}
	public int getWidth(){
		return netWidth;
	}
	
	public int getHeight(){
		return netHeight;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setFont(new Font("Arial", Font.BOLD, 14));
		//fm=g.getFontMetrics(); too late
		//height=fm.getHeight();
		//width=fm.stringWidth(msg);
		g.setColor(Color.BLACK);
		if(str.length<=1){
		g.drawRoundRect(0, 0, width+2*hpad, height+(int)(1.3*vpad), 5, 5);
		if(playerNum==0)
			g.setColor(Color.YELLOW);
		else
			g.setColor(Color.WHITE);
		g.fillRoundRect(0, 0, width+2*hpad, height+(int)(1.3*vpad), 5, 5);
		g.setColor(Color.BLACK);
		g.drawString(msg, hpad, vpad);
		}else{
			System.out.println(str.length);
			System.out.println(height);
			g.drawRoundRect(0, 0, width+2*hpad, 8*str.length*height+(int)(1.3*vpad), 10, 10);
			if(playerNum==0)
				g.setColor(Color.YELLOW);
			else
				g.setColor(Color.WHITE);
			g.fillRoundRect(0, 0, width+2*hpad, 8*str.length*height+(int)(1.3*vpad), 10, 10);
			g.setColor(Color.BLACK);
			for(int num=0;num<str.length;num++)
				g.drawString(str[num], hpad, vpad*(num+1));
		}
		
	}

}
