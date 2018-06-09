package Platformer;

import java.util.*;

public class GameStateManager implements Runnable
{
    
    private ArrayList<GameState> gameStates;
    private int currentState;
    
    public static final int MENUSTATE=0;
    public static final int LEVEL1STATE=1;
    public static final int PAUSESTATE=2;
    public static final int LEVEL2STATE=3;
    
    public GameStateManager()
    {
        gameStates=new ArrayList<GameState>();
        currentState=MENUSTATE;
        gameStates.add(new MenuState(this));
        new Thread(this).start();
    }
    
    public void run(){
        GameState gs=new Level1State(this);
        gameStates.add(gs);
        GameState gs2=new PauseState(this);
        gameStates.add(gs2);
        
        gameStates.add(new Level2State(this));
    }
    
    public GameState getCurrentGameState(){return gameStates.get(currentState);}
    
    public int getCurrentState(){return currentState;}
    
    public void changeState(int state){currentState=state; }
    
    public void init()
    {
        gameStates.get(currentState).init();
    }
    
    public void update()
    {
        gameStates.get(currentState).update();
    }
    
    public void draw(java.awt.Graphics2D g)
    {
        gameStates.get(currentState).draw(g);
    }
    
    public void keyPressed(int key)
    {
        gameStates.get(currentState).keyPressed(key);
    }
    
    public void keyReleased(int key)
    {
        gameStates.get(currentState).keyReleased(key);
    }
}
