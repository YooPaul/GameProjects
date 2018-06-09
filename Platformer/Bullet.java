package Platformer;

import java.awt.image.*;
import java.io.*;
import java.awt.*;
import javax.imageio.ImageIO;

public class Bullet
{
    private BufferedImage bullet;
    private int x;
    private int y;
    private int moveSpeed;
    
    private int width;
    private int height;
    
    private boolean movingLeft;
    
    public Bullet(BufferedImage b,int x, int y, int moveSpeed)
    {
        bullet=b;
        this.x=x;
        this.y=y;
        this.moveSpeed=moveSpeed;
        width=bullet.getWidth()*3;
        height=bullet.getHeight()*3;
    }
    
    public void setMovingLeft(boolean b){movingLeft=b;}
    
    public void update()
    {
        if(movingLeft) x-=moveSpeed;
        else x+=moveSpeed;
    }
    
    public void draw(Graphics2D g,int tx,int ty)
    {
        g.drawImage(bullet, tx+x-width/2,ty+y-height/2,width,height,null);
    }
    
}
