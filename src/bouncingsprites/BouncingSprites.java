package bouncingsprites;
//%W%	%G%
/*
 This app bounces a blue ball inside a JPanel.  The ball is created and begins
 moving with a mousePressed event.  When the ball hits the edge of
 the JPanel, it bounces off the edge and continues in the opposite
 direction.  
*/

public class BouncingSprites {
	
    public BouncingSprites() {}

    // Used for testing
//    public SpriteSimulation getSimulation() {
//        return simulation;
//    }

    public static void main(String[] args) {
        // run the sprites simulation

        // start server... pass in PORT if argument supplied, otherwise use default constructor
        if (args.length > 0) {
            (new BouncingSpritesServer(Integer.parseInt(args[0]))).runServer();
        } else {
            (new BouncingSpritesServer()).runServer();
        }

    }
}
