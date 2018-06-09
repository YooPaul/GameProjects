package Platformer;

import java.awt.image.*;
import java.io.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.util.*;
import java.awt.event.*;

public class Level1State extends GameState
{
    
    private TileMap tileMap;
    private Player player; 
    private EnemyManager enemyManager;
    private Background background;
    
    private BufferedImage screenShot;
    
    public Level1State(GameStateManager gsm)
    {
        super(gsm);
        init();
    }
    
    public void init()
    {
        screenShot=new BufferedImage(GamePanel.WIDTH,GamePanel.HEIGHT,BufferedImage.TYPE_INT_ARGB);
        background=new Background(5);
        
        tileMap=new TileMap("/Users/paulyoo/Desktop/level1-1.map",30,8);
        tileMap.loadTiles("/Users/paulyoo/Desktop/grasstileset.gif"); // load the tileset
    
        player=new Player(tileMap);
        player.setx(300);
        player.sety(300);
        
        enemyManager=new EnemyManager(tileMap,player,"/Users/paulyoo/Desktop/enemyData2.dat");
    }
    
    public void update()
    {
        background.update();
        tileMap.update();
        player.update();
        enemyManager.update();
        
    }
    
    public void draw(Graphics2D g)
    {
        background.draw(g);
        tileMap.draw(g);
        enemyManager.draw(g);
        player.draw(g);
        if(tileMap.reachedEnd())
        {
            g.drawString("Boss Stage",GamePanel.WIDTH/2-20,GamePanel.HEIGHT/2-15);
        }
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
        }   
        if(code==KeyEvent.VK_ESCAPE)
        {
            Graphics2D g= screenShot.createGraphics();
             background.draw(g);
             tileMap.draw(g);
             enemyManager.draw(g);
             player.draw(g);
             g.dispose();
             gsm.changeState(GameStateManager.PAUSESTATE);
             ((PauseState)gsm.getCurrentGameState()).setScreen(screenShot);
             ((PauseState)gsm.getCurrentGameState()).setResume(GameStateManager.LEVEL1STATE);
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
