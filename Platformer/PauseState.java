package Platformer;

import java.awt.image.*;
import java.io.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.util.*;
import java.awt.event.*;

public class PauseState extends GameState
{
    private BufferedImage screenShot;
    
    private String[] options={"Resume","Option","Quit"};
    
    private int currentOption;
    
    private Font font;
    
    private int resume;
    
    public PauseState(GameStateManager gsm)
    {
        super(gsm);
        currentOption=0;
        font=new Font("Verdana",Font.BOLD,28);
    }
    
    public PauseState(GameStateManager gsm,BufferedImage screenShot)
    {
        super(gsm);
        this.screenShot=screenShot;
        currentOption=0;
        font=new Font("Verdana",Font.BOLD,28);
    }
    
    public void setResume(int r){resume=r;}
    
    public void setScreen(BufferedImage screen){screenShot=screen;}
    
    public void init(){}
    
    public void update(){}
    
    public void draw(Graphics2D g){
        g.setFont(font);   
        g.drawImage(screenShot,0,0,GamePanel.WIDTH,GamePanel.HEIGHT,null);
        
        for(int i=0;i<options.length;i++)
        {
            if(i==currentOption) g.setColor(Color.BLUE);
            else g.setColor(Color.WHITE);
            
            g.drawString(options[i],GamePanel.WIDTH/2-50,GamePanel.HEIGHT/3+i*50);
        }
    }
    
    public void keyPressed(int key){
        if(key==KeyEvent.VK_UP)
         {
             currentOption--;
             if(currentOption<0) currentOption=options.length-1;
         }
         else if(key==KeyEvent.VK_DOWN)
         {
            currentOption++;
            if(currentOption==options.length) currentOption=0;
         }
         else if(key==KeyEvent.VK_ENTER)
         {
             if(currentOption==0)
                gsm.changeState(resume);
             else if(currentOption==1);
             else if(currentOption==2) System.exit(0);
         }
    
    }
    
    public void keyReleased(int key){}
    
}
