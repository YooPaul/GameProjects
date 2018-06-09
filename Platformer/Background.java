package Platformer;
import java.awt.image.*;
import java.io.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.util.*;

public class Background
{
    
    private BufferedImage background;
    
    private int x;
    private int y;
    
    private int width;
    private int height;
    
    private int moveSpeed;
    public Background(int moveSpeed)
    {
        x=0;
        y=0;
        this.moveSpeed=moveSpeed;
        width=GamePanel.WIDTH;
        height=GamePanel.HEIGHT;
        try{
            background =ImageIO.read(new File("/Users/paulyoo/Desktop/Megaman/background.jpg"));
            
        }catch(Exception e){}
    }
    public void update()
    {
      //  x-=moveSpeed;
        //if(x<=-width)
          //  x=width;
    }
    
    public void draw(Graphics2D g)
    {
        g.drawImage(background,x,y,width,height,null);
      /*
        if(x<0)
            //g.drawImage(background,0,y,x+width,y+height,0,y,0,y+height,null);
            g.drawImage(background,x+width,y,width,height,null);
         else if(x>0)
            g.drawImage(background,x-width,y,width,height,null);
        */
    }
}
