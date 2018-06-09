package Platformer;

import java.awt.image.*;
import java.io.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.util.*;

public class EnemyManager
{
    
    private ArrayList<Enemy> enemies;
    
    public EnemyManager(TileMap tileMap,Player player,String dataFile)
    {
        enemies=new ArrayList<Enemy>();
        try{
            BufferedReader br=new BufferedReader(new FileReader(dataFile));
            
            int numEnemies=Integer.parseInt(br.readLine());
            String delimeter="\\s+";
            for(int i=0;i<numEnemies;i++)
            {
                String[] attributes= br.readLine().split(delimeter);
                Enemy enemy=new Enemy(tileMap,player);
                int rowNum=Integer.parseInt(attributes[0]);
                int colNum=Integer.parseInt(attributes[1]);
                int leftBound=Integer.parseInt(attributes[2]);
                int rightBound=Integer.parseInt(attributes[3]);
                enemy.setBounds(rowNum,colNum,leftBound,rightBound);
                enemies.add(enemy);
            }
        }catch(Exception e){}
    }
    
    public void update()
    {
        for(Enemy enemy : enemies)
            enemy.update();
    }
    
    public void draw(java.awt.Graphics2D g)
    {
        for(Enemy enemy : enemies)
            enemy.draw(g);
    }
}
