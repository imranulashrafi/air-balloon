

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


public class LandingArea {
    
   
    public int x;
    
    public int y;
    
    private BufferedImage landingAreaImg;
    
    public int landingAreaImgWidth;
    
    
    public LandingArea()
    {
        Initialize();
        LoadContent();
    }
    
    
    private void Initialize()
    {   
        
        x = (int)(Framework.frameWidth * 0.46);
        
        y = (int)(Framework.frameHeight * 0.88);
    }
    
    private void LoadContent()
    {
        try
        {
            URL landingAreaImgUrl = this.getClass().getResource("/images/landing_area.png");
            landingAreaImg = ImageIO.read(landingAreaImgUrl);
            landingAreaImgWidth = landingAreaImg.getWidth();
        }
        catch (IOException ex) {
            Logger.getLogger(LandingArea.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void Draw(Graphics2D g2d)
    {
        g2d.drawImage(landingAreaImg, x, y, null);
    }
    
}
