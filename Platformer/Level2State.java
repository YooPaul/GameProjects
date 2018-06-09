package Platformer;

import java.awt.image.*;
import java.io.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.util.*;
import java.awt.event.*;

public class Level2State extends GameState
{
    
    private TileMap tileMap;
    private Player player; 
    private Background background;
    private EnemyManager enemyManager;
    private Portal portal;
    
    private BufferedImage screenShot;
    
    private boolean transition;
    private int transitionCount;
    
    public Level2State(GameStateManager gsm)
    {
        super(gsm);
        init();
    }
    
    public void init()
    {
        transition=false;
        transitionCount=0;
        screenShot=new BufferedImage(GamePanel.WIDTH,GamePanel.HEIGHT,BufferedImage.TYPE_INT_ARGB);
        background=new Background(5);
        
        tileMap=new TileMap("/Users/paulyoo/Desktop/tilemap.map",32,10);
        tileMap.loadTiles("/Users/paulyoo/Desktop/tileset.gif"); // load the tileset
    
        player=new Player(tileMap);
        player.setx(300);
        player.sety(1000);
        
        portal=new Portal(tileMap);
        portal.setLocation(5,27);
        
        enemyManager=new EnemyManager(tileMap,player,"/Users/paulyoo/Desktop/enemyData.dat");
    }
    
    public void update()
    {
        if(transition) return;
        background.update();
        tileMap.update();
        player.update();
        portal.update();
        enemyManager.update();
    }
    
    public void draw(Graphics2D g)
    {
        if(transition)
        {
            g.setColor(Color.BLACK);
            g.fillRect(0,0,transitionCount,GamePanel.HEIGHT);
            g.fillRect(GamePanel.WIDTH-transitionCount,0,transitionCount,GamePanel.HEIGHT);
            transitionCount+=20;
            if(transitionCount >GamePanel.WIDTH*3/4)
            {
                gsm.changeState(GameStateManager.LEVEL1STATE);
            }
            return;
        }
        
        background.draw(g);
        tileMap.draw(g);
        enemyManager.draw(g);
        portal.draw(g);
        player.draw(g);
    }
    
    public void keyPressed(int key)
    {
        int code=key;//e.getKeyCode();
        
        if(code==KeyEvent.VK_LEFT){
            player.setLeft(true);
        }
        else if(code==KeyEvent.VK_RIGHT){
            player.setRight(true);
        }
        else if(code==KeyEvent.VK_W){
            player.setJumping(true);
        }
        else if(code==KeyEvent.VK_SPACE){
            player.setAction(Player.CHARGING);
        }else if(code==KeyEvent.VK_UP)
        {
            if(portal.intersects(player.getRect())){
                transition=true;
            }
        }
        if(code==KeyEvent.VK_ESCAPE)
        {
            Graphics2D g= screenShot.createGraphics();
             background.draw(g);
             tileMap.draw(g);
             enemyManager.draw(g);
             portal.draw(g);
             player.draw(g);
             g.dispose();
             gsm.changeState(GameStateManager.PAUSESTATE);
             ((PauseState)gsm.getCurrentGameState()).setScreen(screenShot);
             ((PauseState)gsm.getCurrentGameState()).setResume(GameStateManager.LEVEL2STATE);
        }
    }
    
    public void keyReleased(int key)
    {
        int code=key;//e.getKeyCode();
    
        if(code==KeyEvent.VK_LEFT){
            player.setLeft(false);
            }
        if(code==KeyEvent.VK_RIGHT){
            player.setRight(false);
            }
        if(code==KeyEvent.VK_SPACE){
            player.setAction(Player.SHOOTING);
        } 
    }
}
