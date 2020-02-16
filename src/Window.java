

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Window extends JFrame{
        
    private Window()
    {
        
        
    	this.setTitle("Air Balloon Landing");
        
        
        if(false) 
        {
            
            this.setUndecorated(true);
            this.setExtendedState(this.MAXIMIZED_BOTH);
        }
        else
        {
           
            this.setSize(800,600);
            this.setLocationRelativeTo(null);
            this.setResizable(false);
        }
        
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.setContentPane(new Framework());
        
        this.setVisible(true);
    }

    public static void main(String[] args)
    {
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Window();
            }
        });
    }
}
