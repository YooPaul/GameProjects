import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import java.awt.geom.AffineTransform; 
import java.awt.event.*;

import java.net.URL;

public class StreetFigth extends JPanel implements Runnable,KeyListener
{
    
    private int width;
    private int height; 
    
    private Thread thread; 
    private boolean running; 
    
    private int FPS=30;
    private int targetTime=1000/FPS;
    
    private BufferedImage buf;
    private BufferedImage[] backgrounds;
    private int index=0;
    
    private long startTime; 
    private long elapsedTime; 
    private int delay;
    
    private Player player;
    private Enemy enemy;
    
    private BufferedImage name1,name2;
    private BufferedImage victory,win;
    
   // private Icon icon;
   // private JLabel label;
    
    public StreetFigth(){
        backgrounds=new BufferedImage[8];
        try{
            for(int i=0;i<8;i++)
                backgrounds[i]=ImageIO.read(new File("/Users/paulyoo/Desktop/gif/sf"+(i+1)+".png"));
            width=backgrounds[0].getWidth();
            height=backgrounds[0].getHeight();
            setPreferredSize(new Dimension(width,height));
           // icon=new ImageIcon("/Users/paulyoo/Desktop/sf1.gif");
           // label=new JLabel(icon);
           name1=ImageIO.read(new File("/Users/paulyoo/Desktop/Fonts/sf_paul.png"));
           name2=ImageIO.read(new File("/Users/paulyoo/Desktop/Fonts/sf_harris.png"));
           name1=Anime.scale(name1,BufferedImage.TYPE_INT_ARGB,name1.getWidth()/2,
           name1.getHeight()/2,0.5,0.5);
           name2=Anime.scale(name2,BufferedImage.TYPE_INT_ARGB,name2.getWidth()/2,
           name2.getHeight()/2,0.5,0.5);
           victory=ImageIO.read(new File("/Users/paulyoo/Desktop/Fonts/KO.png"));
           win=ImageIO.read(new File("/Users/paulyoo/Desktop/Fonts/win.png"));
       }catch(Exception e){e.printStackTrace();}
       buf=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
       setFocusable(true);
       requestFocus();
    }
    
    @Override
    public void addNotify(){
        super.addNotify();
        if(thread==null){
            thread=new Thread(this);
            thread.start();
        }
        addKeyListener(this);
    }
    
    @Override
    public void run(){
    
        init();
        long startTime;
        long elapsedTime;
        long waitTime; 
        
        while(running){
            startTime=System.nanoTime();
            
            render();
            draw();
            
            elapsedTime=System.nanoTime()-startTime; 
            waitTime=targetTime-elapsedTime/1000000;
            if(waitTime<=0) continue;
            
            try{
                Thread.sleep(waitTime);
            }catch(Exception e){e.printStackTrace();}
        
        }
    }
    
    public void init(){
        running=true;
        startTime=System.nanoTime();
        delay=200;
        player=new Player();
        player.setDelay(200);
        enemy=new Enemy();
        enemy.setDelay(200);
    }
    
    public void render(){
       // add(label);
       player.render();
       enemy.render();
       
       if(player.getRect().intersects(enemy.getRect())) {
           if(player.getHadouken()){
               enemy.decreaseHealth(4); enemy.setGotHit(true); player.setShot(false);//knockedDown(true);
          }
          if(player.getFootStomp()){
            enemy.decreaseHealth(3); enemy.setGotHit(true);
          }
        }
    }
    
    public void drawOnBuf(){
        Graphics g=buf.createGraphics();
        elapsedTime=System.nanoTime()-startTime;
        if(elapsedTime/1000000>delay) {
            index++;
            startTime=System.nanoTime();  
        }
        if(index>7) index=0;
        g.drawImage(backgrounds[index],0,0,null);
        enemy.draw(g);
        player.draw(g);
        g.dispose();
    
    }
    
    public void draw(){
        Graphics g=getGraphics();
        /*
        elapsedTime=System.nanoTime()-startTime;
        if(elapsedTime/1000000>delay) {
            index++;
            startTime=System.nanoTime();  
        }
        if(index>7) index=0;
        g.drawImage(backgrounds[index],0,0,null);
        player.draw(g);
        */
        drawOnBuf();
        g.drawImage(buf,0,0,null);
        g.dispose();
    }
    
    public static void main(){
        JFrame f=new JFrame("Street Fighter");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setContentPane(new StreetFigth());
        f.pack();
        f.setVisible(true);
    
    }
    
    private class Player{
        private int x; 
        private int y; 
        
        private int dx;
        private int dy;
        //private final int maxSpeed=10; 
        
        private final int fullHP=width/2-60;
        private int health=fullHP;
        
        private static final int LEFT=-1;
        private static final int IDLE=0;
        private static final int RIGHT=1;
        
        private int currentMotion;
        
        private BufferedImage[] currentAction; 
        private BufferedImage[] idle;
        private BufferedImage[] walking; 
        private BufferedImage[] forwardJump;
        private BufferedImage[] footStomp; 
        private BufferedImage[] hadouken;
        private BufferedImage[] jumping;
        
        private Blast blast;
        //private int index=0;
        
        //private long startTime; 
        //private long elapsedTime; 
        //private int delay;
        private Animation animation; 
        private boolean alreadyJumping;
        private int doubleJump;
        
        private boolean footStompAttack;
        private boolean hadoukenAttack;
        private boolean isJumping;
        
        private Rectangle rect;
        
        private class Animation{
        
            private BufferedImage[] motion; 
            private int delay; 
            
            private long startTime; 
            private long elapsedTime; 
            private int index; 
            
            
            public Animation(){
                motion=null; 
                delay=0;
                index=0;
                startTime=System.nanoTime();
            }
            
            public void setMotion(BufferedImage[] motion){
                if(motion!=this.motion){
                    this.motion=motion;
                    index=0;
                }
                
            }
            
            public void setDelay(int delay){this.delay=delay;}
            
            public void render(){
                elapsedTime=System.nanoTime()-startTime;
                if(elapsedTime/1000000>delay){
                    //index=index>=currentAction.length-1 ? 0:index+1; 
                    index++;
                    if(isJumping && index<=3){ dy=-(4-index)*20; }//System.out.println(index + " : "+y);}
                    else if(isJumping && index>3) {dy=(index-3)*20; doubleJump++;}//System.out.println(index + " : "+y);}
                    y+=dy;
                    if(index>currentAction.length-1) index=0;
                    startTime=System.nanoTime();
                }
                if(currentAction==footStomp && index==currentAction.length-1){ footStompAttack=false;
                delay=200;}
                if(currentAction==hadouken && index==currentAction.length-1) {hadoukenAttack=false;
                delay=200;}
                if((currentAction==jumping || currentAction==forwardJump) && index==currentAction.length-1) 
                {isJumping=false;                       // && operator executes first
                    alreadyJumping=false;
                    doubleJump=0;
                    delay=200;}
                if(currentAction==hadouken && index==3) blast.setXY(x+hadouken[3].getWidth()-10,y);
            }
            
            public BufferedImage getMotion(){return motion[index];}
            
            public BufferedImage[] getAction(){return motion;}
        
        }
        
        private class Blast{
            private int x;
            private int y;
            
            private int index;
            private BufferedImage[] blast; 
            //private boolean collided;
            private boolean shoot;
            private int tempX;
            
            public Blast(String p){
                blast=new BufferedImage[2];
                loadFromSprite(p,blast);
                index=0;
                shoot=false;
            }
            
            public void collision(){
            
            }
            
            public boolean isShot(){return shoot;}
            
            public void setShoot(boolean b){shoot=b;}
            
            public void setXY(int x,int y){
                this.x=x;
                this.y=y;
                shoot=true;
                //collided=false; 
                tempX=x;
            }
                        
            public void draw(Graphics g){
                //for(int i=tempX;i<=x;i+=10)
                    g.drawImage(blast[index],x+=10,y,null);//i,y,null);//x+=10,y,null);
                //x+=10;
                if(x>width-blast[index].getWidth()) shoot=false;
                index=1;
                rect.setBounds(x,y,blast[index].getWidth(),blast[index].getHeight());
            }
        
        }
        
        public Player(){
                idle=new BufferedImage[4];
                walking=new BufferedImage[5];
                forwardJump=new BufferedImage[6];
                footStomp=new BufferedImage[5];
                hadouken=new BufferedImage[5];
                jumping=new BufferedImage[7];
                loadFromSprite("/Users/paulyoo/Desktop/ryu/idel.gif",idle);
                loadFromSprite("/Users/paulyoo/Desktop/ryu/walking.gif",walking);
                loadFromDirectory("/Users/paulyoo/Desktop/ryu/forwardJump/",".gif",forwardJump);
                loadFromDirectory("/Users/paulyoo/Desktop/ryu/footStomp/",".gif",footStomp);
                loadFromDirectory("/Users/paulyoo/Desktop/ryu/hadouken/",".gif",hadouken);
                loadFromSprite("/Users/paulyoo/Desktop/ryu/jumping.gif",jumping);
                startTime=System.nanoTime();
                currentAction=idle;
                x=currentAction[0].getWidth();//width/2-currentAction[0].getWidth()/2;
                y=height-currentAction[0].getHeight()-10;
                animation=new Animation();
                animation.setDelay(200);
                blast=new Blast("/Users/paulyoo/Desktop/ryu/blast.gif");
                rect=new Rectangle();
        }
        
        private void loadFromDirectory(String path,String fileExtension,BufferedImage[] buf){
              try{
                  int width;
                  int height;
                  for(int i=0;i<buf.length;i++){
                    buf[i]=ImageIO.read(new File(path+(i+1)+fileExtension));
                    width=buf[i].getWidth();
                    height=buf[i].getHeight();
                    buf[i]=Anime.scale(buf[i],BufferedImage.TYPE_INT_ARGB,width*2,height*2,2,2);
                }
              }catch(Exception e){e.printStackTrace();}
        }
        
        private void loadFromSprite(String path,BufferedImage[] buf){
              try{
                  BufferedImage sprite=ImageIO.read(new File(path));
                  int width= sprite.getWidth()/buf.length;
                  for(int i=0;i<buf.length;i++){
                    buf[i]=sprite.getSubimage(i*width,0,width,sprite.getHeight());
                    buf[i]=Anime.scale(buf[i],BufferedImage.TYPE_INT_ARGB,
                    buf[i].getWidth()*2,buf[i].getHeight()*2,2.0,2.0);
                }
              }catch(Exception e){e.printStackTrace();}
        }
        
        public void render(){
            //currentAction=forwardJump;
            if(!isJumping){
            if(dx==0){ 
                    currentAction=idle; 
            }
            else{
                    currentAction=walking;
                    /*
                x+=dx; 
            //System.out.println(x);
                if(x>width-currentAction[0].getWidth()) x=width-currentAction[0].getWidth();
                else if(x<0) x=0;
                */
            }
            if(footStompAttack){ currentAction=footStomp; animation.setDelay(100); }
            //currentAction=idle; 
            if(hadoukenAttack) { currentAction=hadouken; animation.setDelay(100); }
            
        } else{//if(isJumping) {
                
                if(dx==0 && (!alreadyJumping || doubleJump<3) )//&& dy!=0)
                   { currentAction=jumping; alreadyJumping=true;}
                else if(dx!=0 && (!alreadyJumping || doubleJump<3))// && dy!=0)
                {currentAction=forwardJump; alreadyJumping=true;}
                //else if(dx==0 && currentAction!=forwardsJump)
                //currentAction=jumping;
                animation.setDelay(100); 
               // health-=1;
            }
            x+=dx; 
            if(x>width-currentAction[0].getWidth()) x=width-currentAction[0].getWidth();
            else if(x<0) x=0;
            if(!isJumping) 
                {y=height-currentAction[0].getHeight()-10;dy=0;}
            animation.setMotion(currentAction);
            animation.render();
            //currentAction=forwardJump;
        }
        
        public void setCurrentMotion(int c){currentMotion=c;}
        
        public int getCurrentMotion(){return currentMotion;}
        
        public void setDelay(int d){delay=d;}
        
        public void setDx(int d){dx=d;}
        
        public void setFootStomp(boolean b){footStompAttack=b;}
        
        public void setHadouken(boolean b){hadoukenAttack=b;}
        
        public boolean getFootStomp(){ return footStompAttack;}
        
        public boolean getHadouken(){return blast.isShot();}
        
        public void setJumping(boolean b){isJumping=b;}
        
        public void incrementDoubleJump(){doubleJump++;}
        
        public boolean isJumping(){return isJumping;}
        
        public Rectangle getRect(){ return rect;}
        
        public void decreaseHealth(int dHP){health -=dHP;}
        
        public void setShot(boolean b){blast.setShoot(b);};
        
        public void draw(Graphics g){
            /*
            elapsedTime=System.nanoTime()-startTime;
            if(elapsedTime/1000000>delay){
                //index=index>=currentAction.length-1 ? 0:index+1; 
                index++;
                startTime=System.nanoTime();
            }*/
            //if(currentAction==footStomp && index==currentAction.length-1){ footStompAttack=false;}
            //if(currentAction==hadouken && index==currentAction.length-1) hadoukenAttack=false;
            //if(index>currentAction.length-1) index=0;
            //g.drawImage(currentAction[index],x,y,null);
            
            g.drawImage(animation.getMotion(),x,y,null);
            rect.setBounds(x,y,animation.getMotion().getWidth(),animation.getMotion().getHeight());
            if(blast.isShot()) blast.draw(g);
            g.setColor(Color.BLACK);
            g.drawRect(30,20,fullHP,30);
            g.setColor(Color.YELLOW);
            g.fillRect(fullHP-health+30,20,health,30);
            g.drawImage(name1,width/2-30-name1.getWidth(),65,null);
        
        }
    
    }
    
    public void keyPressed(KeyEvent e){
      
        if(e.getKeyCode()==KeyEvent.VK_RIGHT){
            player.setDx(5);
            player.setCurrentMotion(Player.RIGHT);
            enemy.setDx(-5);
            enemy.setCurrentMotion(Player.RIGHT);
            if(player.isJumping()) player.incrementDoubleJump();
        }
        if(e.getKeyCode()==KeyEvent.VK_LEFT){ 
            player.setDx(-5);
            player.setCurrentMotion(Player.LEFT);
            enemy.setDx(5);
            enemy.setCurrentMotion(Player.LEFT);
            if(player.isJumping()) player.incrementDoubleJump();
        }
        if(e.getKeyCode()==KeyEvent.VK_A){ 
            player.setFootStomp(true);
            enemy.setKneeKick(true);
        }
        if(e.getKeyCode()==KeyEvent.VK_S){ 
            player.setHadouken(true);
            //enemy.setKneeKick(true);
        }
        if(e.getKeyCode()==KeyEvent.VK_Q){ 
            player.setJumping(true);
            player.incrementDoubleJump();
            enemy.setJumping(true);
        }
        
    }
    public void keyReleased(KeyEvent e){
        if(e.getKeyCode()==KeyEvent.VK_RIGHT){ 
            if(player.getCurrentMotion()!=Player.LEFT)
                player.setDx(0);
            if(enemy.getCurrentMotion()!=Player.LEFT)
                enemy.setDx(0);
        }
        if(e.getKeyCode()==KeyEvent.VK_LEFT){ 
            if(player.getCurrentMotion()!=Player.RIGHT)
                player.setDx(0);
            if(enemy.getCurrentMotion()!=Player.RIGHT)
                enemy.setDx(0);
        }
        
    }
    public void keyTyped(KeyEvent e){}

    private class Enemy{
        private int x; 
        private int y; 
        
        private int dx;
        private int dy;
        //private final int maxSpeed=10; 
        
        private final int fullHP=width/2-60;
        private int health=fullHP;
        
        private static final int LEFT=-1;
        private static final int IDLE=0;
        private static final int RIGHT=1;
        
        private int currentMotion;
        
        private BufferedImage[] currentAction; 
        private BufferedImage[] idle;
        private BufferedImage[] walking; 
        private BufferedImage[] kneeKick;
        private BufferedImage[] knockDown; 
        private BufferedImage[] KO;
        private BufferedImage[] jumping;
        private BufferedImage[] gotHit;
        
        //private int index=0;
        
        //private long startTime; 
        //private long elapsedTime; 
        //private int delay;
        private Animation animation; 
        private boolean alreadyJumping;
        private int doubleJump;
        
        private boolean kneeKickAttack;
        private boolean isJumping;
        private boolean didGetHit;
        private boolean gotKnockedDown;
        private boolean gotKO;
        
        private Rectangle rect; 
        
        private class Animation{
        
            private BufferedImage[] motion; 
            private int delay; 
            
            private long startTime; 
            private long elapsedTime; 
            private int index; 
            
            
            public Animation(){
                motion=null; 
                delay=0;
                index=0;
                startTime=System.nanoTime();
            }
            
            public void setMotion(BufferedImage[] motion){
                if(motion!=this.motion){
                    this.motion=motion;
                    index=0;
                }
                
            }
            
            public void setDelay(int delay){this.delay=delay;}
            
            public void render(){
                elapsedTime=System.nanoTime()-startTime;
                if(elapsedTime/1000000>delay){
                    //index=index>=currentAction.length-1 ? 0:index+1; 
                    if(!(currentAction==KO  && index==currentAction.length-1)) {
                    index++;
                    if(isJumping && index<=2){ dy=-(4-index)*20; }//System.out.println(index + " : "+y);}
                    else if(isJumping && index>2) {dy=(index-3)*20; doubleJump++;}//System.out.println(index + " : "+y);}
                   if(gotKnockedDown && index<=6){ y=110; }//System.out.println(index + " : "+y);}
                    else if(gotKnockedDown && index>6) {y=width-currentAction[0].getWidth()-10;}
                    y+=dy;
                    if(index>currentAction.length-1) index=0;
                    startTime=System.nanoTime();
                }
                }
                if(currentAction==kneeKick && index==currentAction.length-1){ kneeKickAttack=false;
                dx=0; delay=200;}
                //if(currentAction==hadouken && index==currentAction.length-1) {hadoukenAttack=false;
                //delay=200;}
                if(currentAction==jumping  && index==currentAction.length-1) 
                {isJumping=false;                       // && operator executes first
                    alreadyJumping=false;
                    doubleJump=0;
                    delay=200;}
                 if(currentAction==gotHit  && index==currentAction.length-1) 
                {didGetHit=false;}
                
                if(currentAction==knockDown  && index==currentAction.length-1) 
                {gotKnockedDown=false; delay=200;}
                
                if(currentAction==KO  && index==currentAction.length-1) 
                {index=currentAction.length-1; y=height-110;}
                
            }
            
            public BufferedImage getMotion(){return motion[index];}
            
            public BufferedImage[] getAction(){return motion;}
        
        }
        
        
        
        public Enemy(){
                idle=new BufferedImage[4];
                walking=new BufferedImage[6];
                kneeKick=new BufferedImage[6];
                KO=new BufferedImage[5];
                knockDown=new BufferedImage[8];
                gotHit=new BufferedImage[3];
               // forwardJump=new BufferedImage[6];
                //footStomp=new BufferedImage[5];
                //hadouken=new BufferedImage[5];
                jumping=new BufferedImage[5];
                loadFromSprite("/Users/paulyoo/Desktop/sagat/idel.gif",idle);
                loadFromDirectory("/Users/paulyoo/Desktop/sagat/walking/",".gif",walking);
                loadFromDirectory("/Users/paulyoo/Desktop/sagat/KO/",".gif",KO);
                loadFromDirectory("/Users/paulyoo/Desktop/sagat/gotHit/",".gif",gotHit);
                loadFromDirectory("/Users/paulyoo/Desktop/sagat/knockDown/",".gif",knockDown);
                loadFromDirectory("/Users/paulyoo/Desktop/sagat/kneeKick/",".gif",kneeKick);
                loadFromDirectory("/Users/paulyoo/Desktop/sagat/jumping/",".gif",jumping);
                startTime=System.nanoTime();
                currentAction=idle;
                x=width-currentAction[0].getWidth();
                y=height-currentAction[0].getHeight()-10;
                animation=new Animation();
                animation.setDelay(200);
                rect=new Rectangle();
                
        }
        
        private void loadFromDirectory(String path,String fileExtension,BufferedImage[] buf){
              try{
                  int width;
                  int height;
                  for(int i=0;i<buf.length;i++){
                    buf[i]=ImageIO.read(new File(path+(i+1)+fileExtension));
                    width=buf[i].getWidth();
                    height=buf[i].getHeight();
                    buf[i]=Anime.scale(buf[i],BufferedImage.TYPE_INT_ARGB,width*2,height*2,2,2);
                }
              }catch(Exception e){e.printStackTrace();}
        }
        
        private void loadFromSprite(String path,BufferedImage[] buf){
              try{
                  BufferedImage sprite=ImageIO.read(new File(path));
                  int width= sprite.getWidth()/buf.length;
                  for(int i=0;i<buf.length;i++){
                    buf[i]=sprite.getSubimage(i*width,0,width,sprite.getHeight());
                    buf[i]=Anime.scale(buf[i],BufferedImage.TYPE_INT_ARGB,
                    buf[i].getWidth()*2,buf[i].getHeight()*2,2.0,2.0);
                }
              }catch(Exception e){e.printStackTrace();}
        }
        
        public void render(){
            //currentAction=forwardJump;
            if(!gotKO){
            if(!isJumping){
            if(dx==0){ 
                    currentAction=idle; 
            }
            else{
                    currentAction=walking;
                    /*
                x+=dx; 
            //System.out.println(x);
                if(x>width-currentAction[0].getWidth()) x=width-currentAction[0].getWidth();
                else if(x<0) x=0;
                */
            }
           if(kneeKickAttack){ currentAction=kneeKick; dx=-0; animation.setDelay(100);}
            //currentAction=idle; 
            //if(hadoukenAttack) { currentAction=hadouken; animation.setDelay(100); }
            
        } else{//if(isJumping) {
                
                //if(dx==0 && (!alreadyJumping || doubleJump<3) )//&& dy!=0)
                  // { currentAction=jumping; alreadyJumping=true;}
                //else if(dx!=0 && (!alreadyJumping || doubleJump<3))// && dy!=0)
                //{currentAction=forwardJump; alreadyJumping=true;}
                //else if(dx==0 && currentAction!=forwardsJump)
                //currentAction=jumping;
                currentAction=jumping;
                animation.setDelay(100); 
                //health-=1;
            }
            x+=dx; 
            if(x>width-currentAction[0].getWidth()) x=width-currentAction[0].getWidth();
            else if(x<0) x=0;
            if(!isJumping) 
                {y=height-currentAction[0].getHeight()-10;dy=0;}
                
            if(didGetHit) currentAction=gotHit;
            else if(gotKnockedDown) {currentAction=knockDown; delay=100;}
            
        }  
            if(health<=0){ currentAction=KO; gotKO=true; }
            animation.setMotion(currentAction);
            animation.render();
            //currentAction=forwardJump;
        }
        
        public void setCurrentMotion(int c){currentMotion=c;}
        
        public int getCurrentMotion(){return currentMotion;}
        
        public void setDelay(int d){delay=d;}
        
        public void setDx(int d){dx=d;}
        
        public void setKneeKick(boolean b){kneeKickAttack=b;}
        
        public void setJumping(boolean b){isJumping=b;}
        
        public void incrementDoubleJump(){doubleJump++;}
        
        public boolean isJumping(){return isJumping;}
        
        public Rectangle getRect(){ return rect;}
        
        public void decreaseHealth(int dHP){health -=dHP;}
        
        public void setknockedDown(boolean b){gotKnockedDown=b;}
        
        public void setKO(boolean b){gotKO=b;}
        
        public void setGotHit(boolean b){didGetHit=b;}
        
        public void draw(Graphics g){
            /*
            elapsedTime=System.nanoTime()-startTime;
            if(elapsedTime/1000000>delay){
                //index=index>=currentAction.length-1 ? 0:index+1; 
                index++;
                startTime=System.nanoTime();
            }*/
            //if(currentAction==footStomp && index==currentAction.length-1){ footStompAttack=false;}
            //if(currentAction==hadouken && index==currentAction.length-1) hadoukenAttack=false;
            //if(index>currentAction.length-1) index=0;
            //g.drawImage(currentAction[index],x,y,null);
            
            g.drawImage(animation.getMotion(),x+animation.getMotion().getWidth(),y,
            -animation.getMotion().getWidth(),
            animation.getMotion().getHeight(),null);
            rect.setBounds(x,y,animation.getMotion().getWidth(),animation.getMotion().getHeight());
            
            g.setColor(Color.BLACK);
            g.drawRect(width-(30+fullHP),20,fullHP,30);
            g.setColor(Color.BLUE);
            g.fillRect(width-(30+fullHP),20,fullHP,30);
            g.setColor(Color.YELLOW);
            g.fillRect(width-(30+fullHP),20,health,30);
            g.drawImage(name2,width/2+30,65,null);
            
            if(health<=0  &&counter<=50){ g.drawImage(victory,width/2-victory.getWidth()/2,
            height/2-victory.getHeight()/2,null); counter++;}
            else if(health<=0 && counter>50){ g.drawImage(win,width/2-win.getWidth()/2,
            height/2-win.getHeight()/2,null);}
            
        
        }
        int counter=0;
    
    }
}
