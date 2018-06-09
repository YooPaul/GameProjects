package Platformer;

import java.awt.image.*;

public class Animation{

    private BufferedImage[] frames; 
    private int currentFrame;
    
    private long startTime;
    private long delay; // delay between each frame
    
    private boolean stopAtLastFrame;
    private boolean playedOnce;

    public Animation(){}   // empty default constructor
    
    public void setFrames(BufferedImage[] images){
        if(frames!=images)
        {
            frames=images;
            currentFrame=0;
            startTime=System.nanoTime();
            playedOnce=false;
            //if(currentFrame >= frames.length) currentFrame=0;
        }
        //if(currentFrame >= frames.length) currentFrame=0;
        stopAtLastFrame=false;
    }
    
    public void setDelay(long d){
        delay=d;
    }
    
    public void update(){
        if(delay ==-1) return;   // no animation 
        
        long elapsed =(System.nanoTime()-startTime)/ 1000000; 
                            //convert nanoseconds to milliseconds
        if(elapsed > delay){
            currentFrame++;
            startTime=System.nanoTime();
        }
        if(currentFrame== frames.length){
            if(stopAtLastFrame)
            currentFrame=frames.length-1;
            else
            {
                playedOnce=true;
                currentFrame=0;
        }
        }
    
    }
    
    public int getCurrentFrame(){return currentFrame;}
    
    public boolean isNull(){return frames==null;}
    
    public void setStopAtLastFrame(boolean b){ stopAtLastFrame=b;}
    
    public boolean hasPlayedOnce(){return playedOnce;}
    
    public BufferedImage getImage(){
        return frames[currentFrame];
    }
    
    
    
    

}
