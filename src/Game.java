

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Game {
	
	private Random random;
	
	private Robot robot;

    private Balloon balloon;
   
    private LandingArea landingArea;
    
    private BufferedImage backgroundImg;
    
    private BufferedImage redBorderImg;
    
    private ArrayList<Birds> enemyBirdsList = new ArrayList<Birds>();
    
    private int runAwayEnemies;
    

    public Game()
    {
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;
        
        Thread threadForInitGame = new Thread() {
            @Override
            public void run(){
                Initialize();
                LoadContent();
                
                Framework.gameState = Framework.GameState.PLAYING;
            }
        };
        threadForInitGame.start();
    }
    
    private void Initialize()
    {
    	
    	random = new Random();
    	try {
            robot = new Robot();
        } catch (AWTException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    	enemyBirdsList= new ArrayList<Birds>();
        balloon = new Balloon();
        landingArea  = new LandingArea();
    }
   
    private void LoadContent()
    {
        try
        {
            URL backgroundImgUrl = this.getClass().getResource("/images/background.png");
            backgroundImg = ImageIO.read(backgroundImgUrl);
            
            URL redBorderImgUrl = this.getClass().getResource("/images/red_border.png");
            redBorderImg = ImageIO.read(redBorderImgUrl);
            
            URL birdImgUrl = this.getClass().getResource("/images/bird.png");
            Birds.birdImg = ImageIO.read(birdImgUrl);
            
      
        }
        catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void RestartGame()
    {
        balloon.ResetPlayer();
        Birds.restartEnemy();
        enemyBirdsList.clear();
    }
    
    public void UpdateGame(long gameTime, Point mousePosition)
    {
        
        balloon.Update();
        
        if(balloon.y + balloon.balloonImgHeight - 10 > landingArea.y)
        {
            if((balloon.x > landingArea.x) && (balloon.x < landingArea.x + landingArea.landingAreaImgWidth - balloon.balloonImgWidth))
            {
                if(balloon.speedY <= balloon.topLandingSpeed)
                    balloon.landed = true;
                else
                    balloon.crashed = true;
                balloon.y =(int)(Framework.frameHeight * 0.88);
            }
            else
                balloon.crashed = true;
            balloon.y =(int)(Framework.frameHeight * 0.88);
                
            Framework.gameState = Framework.GameState.GAMEOVER;
        }
        
        createEnemyBirds(gameTime);
        updateEnemies();
    }
    
    private void createEnemyBirds(long gameTime)
    {
        if(gameTime - Birds.timeOfLastCreatedEnemy >= Birds.timeBetweenNewEnemies)
        {
            Birds eh = new Birds();
            int xCoordinate = Framework.frameWidth;
            int yCoordinate = random.nextInt(Framework.frameHeight - Birds.birdImg.getHeight());
            eh.Initialize(xCoordinate, yCoordinate);
            enemyBirdsList.add(eh);
          
            Birds.speedUp();
           
            Birds.timeOfLastCreatedEnemy = gameTime;
        }
    }
    
    private void updateEnemies()
    {
        for(int i = 0; i < enemyBirdsList.size(); i++)
        {
            Birds eh = enemyBirdsList.get(i);
            
            eh.Update();
            
            Rectangle balloonRectangel = new Rectangle(balloon.x, balloon.y, balloon.balloonImg.getWidth(), balloon.balloonImg.getHeight());
            Rectangle enemyRectangel = new Rectangle(eh.xCoordinate, eh.yCoordinate, Birds.birdImg.getWidth(), Birds.birdImg.getHeight());
            if(balloonRectangel.intersects(enemyRectangel)){
            	balloon.crashed = true;
            	
            	balloon.y =(int)(Framework.frameHeight * 0.88);
            	
            	Framework.gameState = Framework.GameState.GAMEOVER;
  
                enemyBirdsList.remove(i);
               
                break;
            }
            
            if(eh.isLeftScreen())
            {
                enemyBirdsList.remove(i);
                runAwayEnemies++;
            }
        }
    }
   
    public void Draw(Graphics2D g2d, Point mousePosition)
    {
        g2d.drawImage(backgroundImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        landingArea.Draw(g2d);
        balloon.Draw(g2d);
        for(int i = 0; i < enemyBirdsList.size(); i++)
        {
            enemyBirdsList.get(i).Draw(g2d);
        }
    }
    
 
    public void DrawGameOver(Graphics2D g2d, Point mousePosition, long gameTime)
    {
        Draw(g2d, mousePosition);
        for(int i = 0; i < enemyBirdsList.size(); i++)
        {
            enemyBirdsList.get(i).Draw(g2d);
        }
        
        g2d.drawString("Press space or enter to restart.", Framework.frameWidth / 2 - 60, Framework.frameHeight / 3 + 70);
        
        if(balloon.landed)
        {
            g2d.drawString("You have successfully landed!", Framework.frameWidth / 2 - 60, Framework.frameHeight / 3);
            g2d.drawString("You have landed in " + gameTime / Framework.secInNanosec + " seconds.", Framework.frameWidth / 2 - 60, Framework.frameHeight / 3 + 20);
        }
        else
        {
            g2d.setColor(Color.red);
            g2d.drawString("You have crashed the balloon!", Framework.frameWidth / 2 - 60, Framework.frameHeight / 3);
            g2d.drawImage(redBorderImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        }
    }
}
