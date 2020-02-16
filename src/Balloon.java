

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Balloon {
    
   
    private Random random;
 
    public int x;
    
    public int y;
    
    public boolean landed;
   
    public boolean crashed;
        
    private int speedAccelerating;
    
    private int speedStopping;
   
    public int topLandingSpeed;
   
    private int speedX;
    
    public int speedY;
    
    public BufferedImage balloonImg;
    
    public BufferedImage balloonLandedImg;
    
    public BufferedImage balloonCrashedImg;
    
    private BufferedImage balloonFireImg;
    
    public int balloonImgWidth;
    
    public int balloonImgHeight;
    
    
    public Balloon()
    {
        Initialize();
        LoadContent();
        
        x = random.nextInt(1);
    }
    
    
    private void Initialize()
    {
        random = new Random();
        
        ResetPlayer();
        
        speedAccelerating = 2;
        speedStopping = 1;
        
        topLandingSpeed = 7;
    }
    
    private void LoadContent()
    {
        try
        {
            URL balloonImgUrl = this.getClass().getResource("/images/balloon.png");
            balloonImg = ImageIO.read(balloonImgUrl);
            balloonImgWidth = balloonImg.getWidth();
            balloonImgHeight = balloonImg.getHeight();
            
            URL balloonLandedImgUrl = this.getClass().getResource("/images/balloon_landed.png");
            balloonLandedImg = ImageIO.read(balloonLandedImgUrl);
            
            URL balloonCrashedImgUrl = this.getClass().getResource("/images/balloon_crashed.png");
            balloonCrashedImg = ImageIO.read(balloonCrashedImgUrl);
            
            URL balloonFireImgUrl = this.getClass().getResource("/images/balloon_fire.png");
            balloonFireImg = ImageIO.read(balloonFireImgUrl);
        }
        catch (IOException ex) {
            Logger.getLogger(Balloon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    public void ResetPlayer()
    {
        landed = false;
        crashed = false;
        
        x = random.nextInt(Framework.frameWidth - balloonImgWidth);
        y = 10;
        
        speedX = 0;
        speedY = 0;
    }
    
    
    public void Update()
    {
       
        if(Canvas.keyboardKeyState(KeyEvent.VK_UP))
            speedY -= speedAccelerating;
        else
            speedY += speedStopping;
        
        
        if(Canvas.keyboardKeyState(KeyEvent.VK_LEFT))
            speedX -= speedAccelerating;
        else if(speedX < 0)
            speedX += speedStopping;
        
        
        if(Canvas.keyboardKeyState(KeyEvent.VK_RIGHT))
            speedX += speedAccelerating;
        else if(speedX > 0)
            speedX -= speedStopping;
        
        x += speedX;
        y += speedY;
    }
    
    public void Draw(Graphics2D g2d)
    {
        g2d.setColor(Color.white);
        g2d.drawString("Balloon Coordinates: " + x + " : " + y, 5, 15);
        
        if(landed)
        {
            g2d.drawImage(balloonLandedImg, x,y - balloonImgHeight, null);
        }
        
        else if(crashed)
        {
            g2d.drawImage(balloonCrashedImg, x, y - balloonImgHeight, null);
        }
        else
        {
            if(Canvas.keyboardKeyState(KeyEvent.VK_UP))
                g2d.drawImage(balloonFireImg, x + 22, y + 66, null);
            g2d.drawImage(balloonImg, x, y, null);
        }
    }
    
}
