package Platformer;

import java.awt.image.*;
import java.io.*;
import java.awt.*;
import javax.imageio.ImageIO;
public class TileMap{

    // for scrolling the map
    private int x;    // x position of the first tile to be drawn 
    private int y;    // y postion of the first tile to be drawn
    // x and y position relative to the Gamepanel 
    private int tileSize;
    private int [][] map; 
    
    
    private int mapWidth;   //  how many columns of tiles
    private int mapHeight; //  how many rows of tiles
    
    private BufferedImage tileset;
    private Tile[][] tiles;
    
    // remove the black spaces from appearing on the camera
    private int minx;
    private int miny;
    
    private int maxx=0;
    private int maxy=0;
    
    //private double scale;
    private int sTileSize;
    
    private int maxRowTiles;
    private int maxColTiles;
    
    public TileMap(String s, int tileSize,int ratio){
        this.tileSize=tileSize;
        
        
        try{
            BufferedReader br=new BufferedReader(new FileReader(s));
            
            mapWidth=Integer.parseInt(br.readLine());
            mapHeight=Integer.parseInt(br.readLine());
            
            //scale = (double)GamePanel.HEIGHT / mapHeight;//*2/3.0);
            sTileSize=GamePanel.HEIGHT / ratio;//(mapHeight);//(int)(scale*tileSize);
            
            maxRowTiles=GamePanel.HEIGHT/sTileSize;
            maxColTiles=GamePanel.WIDTH/sTileSize;
        
            map=new int[mapHeight][mapWidth];
            
            minx=GamePanel.WIDTH - mapWidth * sTileSize;
            miny=GamePanel.HEIGHT - mapHeight *sTileSize;
            
            String delimiters="\\s+";   // any white space
            
            for(int row=0;row<mapHeight;row++){
                String line=br.readLine();
                String[] tokens = line.split(delimiters);
                for(int col=0;col<mapWidth;col++){
                    map[row][col]=Integer.parseInt(tokens[col]);
                }
            }
        
        }catch(Exception e){
        
            e.printStackTrace();
        }
        
    }
   
    
    public void loadTiles(String s){
    
        try{
            tileset=ImageIO.read(new File(s));
            int numTilesAcross=tileset.getWidth()/tileSize;
                                    // one pixle border taken into account
            tiles=new Tile[2][numTilesAcross];
            BufferedImage subimage;
            for(int col=0;col<numTilesAcross;col++){
                subimage=
                tileset.getSubimage(col*tileSize,0,tileSize,tileSize);
                
                tiles[0][col]=new Tile(subimage,false);
                
                subimage=
        tileset.getSubimage(col*tileSize,tileSize,tileSize,tileSize);
                
                tiles[1][col]=new Tile(subimage,true);
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    
    }
    
    public int getx(){return x;}
    public int gety(){return y;}
    
    
    public int getColTile(int x){
        return x/sTileSize;//tileSize;
    }
    public int getRowTile(int y){
        return y/sTileSize;//tileSize;
    }
    
    public boolean reachedEnd(){return x== minx;}
    
    //public int getTile(int row,int col){
    //  return map[row][col];        replaced with isBlocked
    //}
    
    public int getTileSize(){return sTileSize;}
    
    //public int getScaledTileSize(){return sTileSize;}
    
    public boolean isBlocked(int row, int col){
        int rc=map[row][col];
        
        int r=   rc / tiles[0].length;  
        int c= rc % tiles[0].length;
        return tiles[r][c].isBlocked();
    }
    
    public void setx(int i){ 
        x=i;
        if(x < minx){     // cap the x offset position of the tileMap
            x=minx;
        }if(x> maxx){
            x=maxx;
        }
    
    }
    public void sety(int i){ 
        y=i;
        if(y < miny){     // cap the y offset position of the tileMap
            y=miny;
        }if(y> maxy){
            y=maxy;
        }
        
    }
        
    public void update(){
    
    }
    
    public void draw(Graphics2D g){
        
        int rowOffSet=-y/sTileSize;
        int colOffSet=-x/sTileSize;
        
    //for(int row=0;row<mapHeight;row++){
      for(int row=rowOffSet;row<rowOffSet+maxRowTiles+2;row++){
          if(row == mapHeight) break;
        //for(int col=0;col<mapWidth;col++){
          for(int col=colOffSet;col<colOffSet+maxColTiles+2;col++){
            if(col == mapWidth) break;
            int rc=map[row][col];
            
            int r=rc / tiles[0].length;  
            int c=rc % tiles[0].length;
            
        g.drawImage(tiles[r][c].getImage(),x+col*sTileSize,y+row*sTileSize,
        sTileSize,sTileSize,null);
             
        //g.setColor(Color.WHITE);
        //g.drawString("Off Set : "+colOffSet,40,150);
        }
    }
    }

}
