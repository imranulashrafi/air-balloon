

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


public class Framework extends Canvas {
    
  
    public static int frameWidth;
   
    public static int frameHeight;

    public static final long secInNanosec = 1000000000L;
    
    public static final long milisecInNanosec = 1000000L;
    
    private final int GAME_FPS = 25;
   
    private final long GAME_UPDATE_PERIOD = secInNanosec / GAME_FPS;
    
    public static enum GameState{STARTING, VISUALIZING, GAME_CONTENT_LOADING, MAIN_MENU, OPTIONS, PLAYING, GAMEOVER, DESTROYED}
 
    public static GameState gameState;
   
    private long gameTime;
   
    private long lastTime;
    
    private Game game;
    
    
    private BufferedImage balloonMenuImg;
    
    
    public Framework ()
    {
        super();
        
        gameState = GameState.VISUALIZING;
        
        Thread gameThread = new Thread() {
            @Override
            public void run(){
                GameLoop();
            }
        };
        gameThread.start();
    }
    
    
    private void Initialize()
    {
        
    }
    
    private void LoadContent()
    {
        try
        {
            URL balloonMenuImgUrl = this.getClass().getResource("/images/menu.png");
            balloonMenuImg = ImageIO.read(balloonMenuImgUrl);
        }
        catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void GameLoop()
    {
        long visualizingTime = 0, lastVisualizingTime = System.nanoTime();
        long beginTime, timeTaken, timeLeft;
        
        while(true)
        {
            beginTime = System.nanoTime();
            
            switch (gameState)
            {
                case PLAYING:
                    gameTime += System.nanoTime() - lastTime;
                    
                    game.UpdateGame(gameTime, mousePosition());
                    
                    lastTime = System.nanoTime();
                break;
                case GAMEOVER:
                    //...
                break;
                case MAIN_MENU:
                    //...
                break;
                case OPTIONS:
                    //...
                break;
                case GAME_CONTENT_LOADING:
                    //...
                break;
                case STARTING:
                    
                    Initialize();
                   
                    LoadContent();

                    gameState = GameState.MAIN_MENU;
                break;
                case VISUALIZING:
                    if(this.getWidth() > 1 && visualizingTime > secInNanosec)
                    {
                        frameWidth = this.getWidth();
                        frameHeight = this.getHeight();

                        
                        gameState = GameState.STARTING;
                    }
                    else
                    {
                        visualizingTime += System.nanoTime() - lastVisualizingTime;
                        lastVisualizingTime = System.nanoTime();
                    }
                break;
            }
            
            
            repaint();
            
           
            timeTaken = System.nanoTime() - beginTime;
            timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / milisecInNanosec; 
            
            if (timeLeft < 10) 
                timeLeft = 10; 
            try {
                 
                 Thread.sleep(timeLeft);
            } catch (InterruptedException ex) { }
        }
    }
    
    @Override
    public void Draw(Graphics2D g2d)
    {
        switch (gameState)
        {
            case PLAYING:
                game.Draw(g2d, mousePosition());
            break;
            case GAMEOVER:
                game.DrawGameOver(g2d, mousePosition(), gameTime);
            break;
            case MAIN_MENU:
                g2d.drawImage(balloonMenuImg, 0, 0, frameWidth, frameHeight, null);
                g2d.setColor(Color.white);
                g2d.drawString("Use Up,Right,Left arrows to control the balloon.", frameWidth / 2 - 100, frameHeight / 2);
                g2d.drawString("Press any key to start the game.", frameWidth / 2 - 65, frameHeight / 2 + 30);
            break;
            case OPTIONS:
                //...
            break;
            case GAME_CONTENT_LOADING:
                g2d.setColor(Color.white);
                g2d.drawString("GAME is LOADING", frameWidth / 2 - 50, frameHeight / 2);
            break;
        }
    }
    
    private void newGame()
    {
        gameTime = 0;
        lastTime = System.nanoTime();
        
        game = new Game();
    }
  
    private void restartGame()
    {
       
        gameTime = 0;
        lastTime = System.nanoTime();
        game.RestartGame();
        gameState = GameState.PLAYING;
    }
   
    private Point mousePosition()
    {
        try
        {
            Point mp = this.getMousePosition();
            
            if(mp != null)
                return this.getMousePosition();
            else
                return new Point(0, 0);
        }
        catch (Exception e)
        {
            return new Point(0, 0);
        }
    }
    
    @Override
    public void keyReleasedFramework(KeyEvent e)
    {
        switch (gameState)
        {
            case MAIN_MENU:
                newGame();
            break;
            case GAMEOVER:
                if(e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER)
                    restartGame();
            break;
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e)
    {
        
    }
}
