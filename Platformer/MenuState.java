package Platformer;

import java.awt.image.*;
import java.io.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.util.*;
import java.awt.event.*;

public class MenuState extends GameState
{
    
    private BufferedImage background;
    private String[] options={"Play","Option","Quit"};
    private int currentOption;
    
    private Font font;
    
    
    public MenuState(GameStateManager gsm)
    {
        super(gsm);
        font=new Font("Verdana",Font.BOLD,28);
        currentOption=0;
        
    }
    
    @Override
    public void init()
    {
        try{
            background=ImageIO.read(new File("/Users/paulyoo/Desktop/Megaman/background.png"));
        }catch(Exception e)
        {
            
        }
    }
    
    public void update()
    {
        
    }
    
    public void draw(Graphics2D g)
    {
        g.setFont(font);   
        g.drawImage(background,0,0,GamePanel.WIDTH,GamePanel.HEIGHT,null);
        
        for(int i=0;i<options.length;i++)
        {
            if(i==currentOption) g.setColor(Color.RED);
            else g.setColor(Color.BLACK);
            
            g.drawString(options[i],GamePanel.WIDTH/2-50,GamePanel.HEIGHT/3+i*50);
        }
    }
    
    public void keyPressed(int key)
    {
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
                gsm.changeState(GameStateManager.LEVEL2STATE);
             else if(currentOption==1);
             else if(currentOption==2) System.exit(0);
         }
    }
    
    public void keyReleased(int key)
    {
    
    }
}
