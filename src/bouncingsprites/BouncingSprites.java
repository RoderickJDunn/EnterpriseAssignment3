package bouncingsprites;
//%W%	%G%
/*
 This app bounces a blue ball inside a JPanel.  The ball is created and begins
 moving with a mousePressed event.  When the ball hits the edge of
 the JPanel, it bounces off the edge and continues in the opposite
 direction.  
*/
import javax.swing.JFrame;

public class BouncingSprites {
	
    private JFrame frame;
    private SpritePanel panel = new SpritePanel();
    public static int frameWidth = 400;
    public static int frameHeight = 400;

    public BouncingSprites() {
        frame = new JFrame("Bouncing Sprite");
        frame.setSize(frameWidth, frameHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setVisible(true);
    }
    public void startAnimation(){
    	 panel.animate();  // never returns due to infinite loop in animate method
    }

    // Used for testing
    public SpritePanel getPanel() {
        return panel;
    }

    public static void main(String[] args) {
        new BouncingSprites().startAnimation();
    }
}
