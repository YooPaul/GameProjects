package Platformer;

import java.awt.image.*;
import java.io.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.util.*;

public class Enemy{
    
    private double x;    
    // x position of the player inside the tileMap including the tile border
    private double y;
    // y position of the player inside the tileMap including the tile border
    // x and y position relative to the tilemap
    private double dx;   // x direction velocity
    private double dy;   // y direction velocity
    
    private int width;      // player dimension in pixels 
    private int height; 
    
    // boolean values will be altered with the key listener
    private boolean left;     // moving left or not 
    private boolean right;    // moving right or not 
    private boolean jumping;  // jumping or not 
    private boolean falling;  // falling or not 
    
    private double moveSpeed;  // horizontal acceleration
    private double maxSpeed;   // capping the maximum velocity
    private double maxFallingSpeed;    // maximum vertical velocity
    private double stopSpeed;  // decelaration due to friction and resistance
    private double jumpStart;   // jumping velocity
    private double gravity;   // vertical gravity acceleration
    
    private boolean topLeft;        // checking collision with the tiles
    private boolean topRight;
    private boolean bottomLeft;
    private boolean bottomRight;
    
    
    private Animation animation;
    private BufferedImage[] idelSprites;
    private BufferedImage[] walkingSprites;
    
    private boolean facingLeft;
    
    
    private TileMap tileMap;     // player must know the tileMap
    
    private Player player;
    
    private int maxX, minX;
    
    public Enemy(TileMap tm,Player player){
        
        tileMap =tm;
        this.player=player;
        
        width=100;//29*3;     // width and height of the player sprite image
        height=153; 
        
        moveSpeed=2.6;
        maxSpeed=16.2;
        maxFallingSpeed=18;
        stopSpeed=1.10;
        jumpStart=-25.0;
        gravity=1.14;
        
        
        try{
            idelSprites =new BufferedImage[2];
            walkingSprites =new BufferedImage[8];
            
            //idelSprites[0]=ImageIO.read(new File("kirbyidle.gif"      ));
            //jumpingSprites[0]=ImageIO.read(new File("kirbyjump.gif"));
            //fallingSprites[0]=ImageIO.read(new File("kirbyfall.gif"));
            
           
            for(int i=0;i<idelSprites.length; i++){
            idelSprites[i]=ImageIO.read(new File("/Users/paulyoo/Desktop/Enemies/enemy1/idle/"+
            "idle"+(i+1)+".png"));
            }
            for(int i=0;i<walkingSprites.length; i++){
            walkingSprites[i]=ImageIO.read(new File("/Users/paulyoo/Desktop/Enemies/enemy1/walking/"+
            "walking"+(i+1)+".png"));
            }
           
            
        
        }catch(Exception e){
            e.printStackTrace();
        }
        
        animation =new Animation();
       
        facingLeft=false;
        
    }
    
    public void setLeft(boolean b){ left=b;}
    public void setRight(boolean b){ right=b;}
    public void setJumping(boolean b){
         if(!falling)
            jumping=true;     //if the player is not falling 
     }
     
    
    public void update(){
        
        if(Math.abs(player.getx()-x)>GamePanel.WIDTH || Math.abs(player.gety()-y)>GamePanel.HEIGHT ) 
            return;
        
        if(player.getx()+player.getWidth()<x-width)
        {
            left=true;
            facingLeft=true;
        }
        else if(player.getx()-player.getWidth()>x+width)
        {
            right=true;
            //left=false;
            facingLeft=false;
        }
        else 
        {
            left=false;
            right=false;
        }
        //determine next position 
        if(left){
            dx-=moveSpeed; 
            if(dx<-maxSpeed)
                dx=-maxSpeed;
        }else if(right){
            dx+=moveSpeed;
            if(dx>maxSpeed)
                dx=maxSpeed;
        }else{
            if(dx>0){
                dx-=stopSpeed;
                if(dx<0){
                    dx=0;
                }
            }else if(dx<0){
                dx+=stopSpeed;
                if(dx>0)
                    dx=0;
            }
        }
            
        if(jumping){
            dy=jumpStart;
            falling=true;
            jumping=false;
        }
        
        // gravity acts on the player immediately after jump 
        if(falling){
            dy+=gravity;
            if(dy>maxFallingSpeed)
                dy=maxFallingSpeed; 
        }else{
            dy=0;     // player is standing on the ground
        }
        
        //check collisions  
        
        int currCol= tileMap.getColTile((int)x);
        int currRow=tileMap.getRowTile((int)y);
    
        double tox=x+dx;        
        // the x and y positions the player should be after moving
        double toy=y+dy;
        
        double tempx=x;  
        // don't change the actual x and y before checking
        double tempy=y; 
        
        calculateCorners(x,toy); // x and destination y
        if(dy<0){
            if(topLeft || topRight){
                dy=0; 
                tempy=currRow*tileMap.getTileSize()+height/2; 
                 //player bumps head 
            }else{
                tempy+=dy;
            }
        }
        else if(dy>0){
            if(bottomLeft || bottomRight){
                dy=0; 
                falling=false;
                tempy=(currRow+1)*tileMap.getTileSize()-height/2;
            }else{
                tempy+=dy;
            }
        }
        
        calculateCorners(tox,y); // x destination and y
        if(dx<0){
            if(topLeft || bottomLeft){
                dx=0; 
                tempx=currCol*tileMap.getTileSize()+width/2;
            }else {
                 tempx+=dx;
            }   
            
        }
        else if(dx>0){
            if(topRight || bottomRight ){
                dx=0; 
                tempx=(currCol+1)*tileMap.getTileSize()-width/2;
            }else {
                tempx+=dx;
            }
        }
        
       if(!falling){
            calculateCorners(x,y+1); //one pixel below the player
            if(!bottomLeft && !bottomRight){
                falling=true;
            }
        }
        
        x=tempx;
        y=tempy;
        if(minX>0)
        {
            if(x<minX)x=minX;
            //System.out.println(minX+"  "+maxX);
        }if(maxX>0)
            if(x>maxX)x=maxX;
        // sprite animation
       
        
        if(left || right){
            animation.setFrames(walkingSprites);
            animation.setDelay(100);
        }else{
            animation.setFrames(idelSprites);   // standing still 
            animation.setDelay(100);   // no animation
        }
        
        animation.update();
        
        
        if(dx <0){
            facingLeft=true;
        }
        else if(dx>0) {
            facingLeft=false;
        }
        
        
       
    }
    
    private void calculateCorners(double x, double y){
    
        //first get the corresponding left,right,top,bottom tiles 
        // the player is in contact with at position x and y
        // from the map file
        
        int leftTile=tileMap.getColTile((int) (x-width/2));   
        int rightTile=tileMap.getColTile((int) (x+width/2));
        int topTile=tileMap.getRowTile((int) (y-120/2));
        int bottomTile=tileMap.getRowTile((int)(y+120/2));
        
        // find out if the tile is blocked or not
         
        topLeft=tileMap.isBlocked(topTile,leftTile);
        topRight=tileMap.isBlocked(topTile,rightTile);
        bottomLeft=tileMap.isBlocked(bottomTile,leftTile);
        bottomRight=tileMap.isBlocked(bottomTile,rightTile);
        // 0 is the tile that blocks  
        // no longer need to use the conditional equal operator 
        
    }
    
    public void setx(int i){x=i;}
    public void sety(int i){y=i;}
    
    public void setBounds(int row, int col, int leftBound,int rightBound )
    {
        y=(row+1)*tileMap.getTileSize()-height/2;
        x=col*tileMap.getTileSize()+width/2;
        minX = leftBound* tileMap.getTileSize();
        maxX = rightBound* tileMap.getTileSize();
    }
    
    public void draw(Graphics2D g){
    
        int tx=tileMap.getx();
        int ty=tileMap.gety();
        
        if(tx+x+width/2<0 || tx+x-width/2>GamePanel.WIDTH) return;
        else if(ty+y+height/2<0 || ty+y-height/2>GamePanel.HEIGHT) return;
       
        int width=animation.getImage().getWidth()*3;
        int height=animation.getImage().getHeight()*3;
        
        
        if(facingLeft){
            g.drawImage(animation.getImage(),
            (int) (tx+x-width/2), (int)(ty+y-height/2),width,height,
            null
            );
        }
        else{   // different drawImage method 
            g.drawImage(animation.getImage(),
            (int) (tx+x-width/2 + width), (int)(ty+y-height/2),
            -width,height,
            null
            );
        }
        //g.setColor(Color.RED);
       // g.fillRect((int) (tx+x-width/2), (int)(ty+y-height/2),width,height);
       

    }
}