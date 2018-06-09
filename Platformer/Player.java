package Platformer;

import java.awt.image.*;
import java.io.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.util.*;

public class Player{
    
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
    private BufferedImage[] jumpingSprites;
    private BufferedImage[] fallingSprites;
    private BufferedImage[] shootingSprites;
    private BufferedImage[] chargingSprites;
    private boolean facingLeft;
    
    public static final int NOACTION=0;
    public static final int CHARGING=1;
    public static final int SHOOTING=2;
    
    private int currentAction;
    
    private ArrayList<Bullet> bullets;
    private BufferedImage[] bulletImages;
    
    private TileMap tileMap;     // player must know the tileMap
    
    private Rectangle rect;
    
    public Player(TileMap tm){
        
        tileMap =tm;
        
        width=100;     // width and height of the player sprite image
        height=100; 
        
        moveSpeed=2.6;
        maxSpeed=16.2;
        maxFallingSpeed=18;
        stopSpeed=1.10;
        jumpStart=-25.0;
        gravity=1.14;
        
        bullets=new ArrayList<Bullet>();
        
        rect=new Rectangle();
        
        try{
            idelSprites =new BufferedImage[4];
            jumpingSprites =new BufferedImage[3];
            fallingSprites =new BufferedImage[2];
            walkingSprites =new BufferedImage[11];
            shootingSprites=new BufferedImage[2];
            chargingSprites=new BufferedImage[5];
            bulletImages=new BufferedImage[5];
            
            //idelSprites[0]=ImageIO.read(new File("kirbyidle.gif"      ));
            //jumpingSprites[0]=ImageIO.read(new File("kirbyjump.gif"));
            //fallingSprites[0]=ImageIO.read(new File("kirbyfall.gif"));
            
            BufferedImage image=ImageIO.read(new File("/Users/paulyoo/Desktop//Megaman/idle.png"));
            for(int i=0;i<idelSprites.length; i++){
            idelSprites[i]=image.getSubimage(i*34,5,34,34);
            }
            for(int i=0;i<walkingSprites.length; i++){
            walkingSprites[i]=ImageIO.read(new File("/Users/paulyoo/Desktop//Megaman/running/"+
            "running "+(i+1)+".png"));
            }
            for(int i=0;i<jumpingSprites.length; i++){
            jumpingSprites[i]=ImageIO.read(new File("/Users/paulyoo/Desktop//Megaman/jump/"+
            "jump_0"+(i+1)+".png"));
            }for(int i=0;i<fallingSprites.length; i++){
            fallingSprites[i]=ImageIO.read(new File("/Users/paulyoo/Desktop//Megaman/fall/"+
            "fall"+i+".png"));
            }for(int i=0;i<shootingSprites.length;i++)
            {
                shootingSprites[i]=ImageIO.read(new File("/Users/paulyoo/Desktop/Megaman/shoot/"+
                "shoot"+i+".png"));
            }
            for(int i=0;i<chargingSprites.length;i++)
            {
                chargingSprites[i]=ImageIO.read(new File("/Users/paulyoo/Desktop/Megaman/charge/"+
                "charge"+i+".png"));
            }for(int i=0;i<bulletImages.length;i++)
            {
                bulletImages[i]=ImageIO.read(new File("/Users/paulyoo/Desktop/Megaman/bullet/"+
                "bullet"+i+".png"));
            }
            
        
        }catch(Exception e){
            e.printStackTrace();
        }
        
        animation =new Animation();
        
        currentAction=NOACTION;
        facingLeft=false;
        
    }
    
    public void setLeft(boolean b){ left=b;}
    public void setRight(boolean b){ right=b;}
    public void setJumping(boolean b){
         if(!falling)
            jumping=true;     //if the player is not falling 
     }
     public void setAction(int action){
         if(currentAction==NOACTION)
         {  
             currentAction=action;
             
             
         }else if(currentAction==CHARGING && action==SHOOTING )
         {
            currentAction=action;
             
             if(currentAction==SHOOTING)
             {
                 int tempX;
                 int tempY=(int)(y);//-height/10);
                 if(facingLeft)
                 {
                     tempX=(int)(x-width/2);
                 }
                 else{
                    tempX=(int)(x+width/2);
                 }
                 Bullet bullet=new Bullet(bulletImages[animation.getCurrentFrame()],tempX,tempY,(int)maxSpeed);
                 bullet.setMovingLeft(facingLeft);
                 bullets.add(bullet);
             }
         }
     }
    
    public void update(){
        
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
        if(dy>0){
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
            }else{
                tempx+=dx;
            }
        }
        if(dx>0){
            if(topRight || bottomRight){
                dx=0; 
                tempx=(currCol+1)*tileMap.getTileSize()-width/2;
            }else{
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
        
        // move the map   scrolling function 
        
        // keeps the player in the center of the screen
        tileMap.setx((int) (GamePanel.WIDTH/2 -x));  
        tileMap.sety((int) (GamePanel.HEIGHT/2-y));
        // when substituted in the g.fillRect method below 
        // the remainng value results in GamePanel.WIDTH/2 -Player.width/2
        
        
        
        
        // sprite animation
        if(currentAction==CHARGING)
        {
            animation.setFrames(chargingSprites);
            animation.setStopAtLastFrame(true);
            animation.setDelay(200);
        }
        else if(currentAction==SHOOTING)
        {
            animation.setFrames(shootingSprites);
            animation.setDelay(100);
        }
        else if(dy<0){
            animation.setFrames(jumpingSprites);
            animation.setStopAtLastFrame(true);
            animation.setDelay(100);
        }
        else if(dy>0){
            animation.setFrames(fallingSprites);
            animation.setStopAtLastFrame(true);
            animation.setDelay(100);
        }
        else if(left || right){
            animation.setFrames(walkingSprites);
            animation.setDelay(100);
        }else{
            animation.setFrames(idelSprites);   // standing still 
            animation.setDelay(100);   // no animation
        }
        
        animation.update();
        if(currentAction!=NOACTION)
        {
            if(animation.hasPlayedOnce())
            {
                currentAction=NOACTION;
            }
        }
        
        if(dx <0){
            facingLeft=true;
        }
        if(dx>0) {
            facingLeft=false;
        }
        
        for(int i=0;i<bullets.size();i++)
        bullets.get(i).update();
        
        
       
    }
    
    private void calculateCorners(double x, double y){
    
        //first get the corresponding left,right,top,bottom tiles 
        // the player is in contact with at position x and y
        // from the map file
        
        int leftTile=tileMap.getColTile((int) (x-width/2));   
        int rightTile=tileMap.getColTile((int) (x+width/2)-1);
        int topTile=tileMap.getRowTile((int) (y-height/2));
        int bottomTile=tileMap.getRowTile((int)(y+height/2) -1);
        
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
    public int getx(){return (int)x;}
    public int gety(){return (int)y;}
    public int getWidth(){return width;}
    public int getHeight(){return height;}
    
    public Rectangle getRect()
    {
        rect.setBounds((int)x-width/2,(int)y-height/2,width,height);
        return rect;
    }
    
    public void draw(Graphics2D g){
    
        int tx=tileMap.getx();
        int ty=tileMap.gety();
       
        int width=animation.getImage().getWidth()*3;
        int height=animation.getImage().getHeight()*3;
        
        
        if(!facingLeft){
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
       for(int i=0;i<bullets.size();i++)
        bullets.get(i).draw(g,tx,ty); 
       
        //g.setColor(Color.WHITE);
        //g.drawString("tx : "+tx+" ty : "+ty,20,20);
        
        //g.setColor(Color.WHITE);
        //g.drawString("x : "+x+" y : "+y,20,50);
        
        // need to add the tile x and y offset so the player does not get       // out of the screen 

    }
}
