package Platformer;
import java.awt.image.*;
import java.io.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.util.*;


public class Portal
{
    private BufferedImage[] portalSprites;
    private Animation animation;
    
    private int x;
    private int y;
    
    private int width;
    private int height;
    
    private TileMap tileMap;
    private int tx;
    private int ty;
    
    private Rectangle rect;
    
    public Portal(TileMap tileMap)
    {
        this.tileMap=tileMap;
        portalSprites=new BufferedImage[4];
        try{
            for(int i=1;i<=portalSprites.length;i++)
                portalSprites[i-1]=ImageIO.read(new File("/Users/paulyoo/Desktop/portal/spacePortal"+
                i+".png"));
            
        }catch(Exception e){}
        animation =new Animation();
        animation.setFrames(portalSprites);
        animation.setDelay(100);
        width=animation.getImage().getWidth();
        height=animation.getImage().getHeight();
        rect=new Rectangle();
    }
    
    public void update()
    {
        tx=tileMap.getx();
        ty=tileMap.gety();
        
        if(tx+x+width/2<0 || tx+x-width/2>GamePanel.WIDTH) return;
        else if(ty+y+height/2<0 || ty+y-height/2>GamePanel.HEIGHT) return;
        
        animation.update();
    }
    
    public void setLocation(int row, int col)
    {
        y=(row+1)*tileMap.getTileSize()-height/2;
        x=col*tileMap.getTileSize()+width/2;
    }
    
    public boolean intersects(Rectangle rect)
    {
       // System.out.println(rect);
       // System.out.println(this.rect);
        this.rect.setBounds(x-width/2,y-height/2,width,height);
        return this.rect.intersects(rect);
    }
    
    public void draw(Graphics2D g)
    {
        
        if(tx+x+width/2<0 || tx+x-width/2>GamePanel.WIDTH) return;
        else if(ty+y+height/2<0 || ty+y-height/2>GamePanel.HEIGHT) return;
        
        g.drawImage(animation.getImage(),tx+x-width/2,ty+y-height/2,width,height,null);
    
    }
}
