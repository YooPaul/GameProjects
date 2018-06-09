package Platformer;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.image.*;

public class GamePanel extends JPanel implements Runnable,KeyListener
{

    public static final int WIDTH=1024;
    public static final int HEIGHT=768;
    
    private Thread thread;
    private boolean running;
    
    private BufferedImage image; 
    private Graphics2D g;
    
    private int FPS=30;
    private int targetTime=1000/FPS;
    
    private GameStateManager gsm;
    
    public GamePanel(){
        super();
        setPreferredSize(new Dimension(WIDTH,HEIGHT));
        setFocusable(true);
        requestFocus();
    }
    
    public void addNotify(){
        super.addNotify();
        if(thread==null){
            thread= new Thread(this);
            thread.start();
        }
        
        addKeyListener(this);
    }
    
    public void run(){
        init();
        
        long startTime;
        long urdTime;
        long waitTime;
        
        while(running){
            startTime=System.nanoTime();
            
            update();
            render();
            draw();
            
            urdTime=(System.nanoTime()-startTime) /1000000;
            waitTime= targetTime-urdTime;
            
            try{
                Thread.sleep(waitTime);
            }catch(Exception e){
                //e.printStackTrace(); 
            }
        
        
        }
        
    }
    
    private void init(){
        running=true;
        
        image=new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
        g=(Graphics2D) image.getGraphics();
        
        gsm=new GameStateManager();
        gsm.init();
        
    }
    
    private void update(){
        gsm.update();
    }
    
    private void render(){ // draw map and player to the bufferedImage
    
        //g.setColor(Color.BLACK);
        //g.fillRect(0,0,WIDTH,HEIGHT);
        
        gsm.draw(g);
    }
    
    private void draw(){   // draw the bufferedImage to the actual Gamepanel
        Graphics g2=getGraphics();
        g2.drawImage(image,0,0,null);
        g2.dispose();
    
    }
    
    public void keyTyped(KeyEvent e){}
    public void keyPressed(KeyEvent e){
        gsm.keyPressed(e.getKeyCode());
        
    }
    public void keyReleased(KeyEvent e){
        gsm.keyReleased(e.getKeyCode());
    }   

}

