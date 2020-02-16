import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class Birds {
	
	private static final long timeBetweenNewEnemiesInit = Framework.secInNanosec+100;
    public static long timeBetweenNewEnemies = timeBetweenNewEnemiesInit;
    public static long timeOfLastCreatedEnemy = -5;
    
    public int xCoordinate;
    public int yCoordinate;
    
    private static final double movingXspeedInit = -10;
    private static double movingXspeed = movingXspeedInit;
    
    public static BufferedImage birdImg;
    
    public void Initialize(int xCoordinate, int yCoordinate)
    {
        
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        Birds.movingXspeed = -10;
    }
    
    public static void restartEnemy(){
        Birds.timeBetweenNewEnemies = timeBetweenNewEnemiesInit;
        Birds.timeOfLastCreatedEnemy = 0;
        Birds.movingXspeed = movingXspeedInit;
    }
    
    public static void speedUp(){
        if(Birds.timeBetweenNewEnemies > Framework.secInNanosec)
            Birds.timeBetweenNewEnemies -= Framework.secInNanosec / 100;
        
       Birds.movingXspeed -= 0.25;
    }
    
    public boolean isLeftScreen()
    {
        if(xCoordinate < 0 - birdImg.getWidth(null))
            return true;
        else
            return false;
    }
    
    public void Update()
    {
        xCoordinate += movingXspeed;
    }
    
    public void Draw(Graphics2D g2d)
    { 
        g2d.drawImage(birdImg, xCoordinate, yCoordinate, null);
    }

}
