package bouncingsprites;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

/**
 * Backs one 'bouncing ball' in the User Interface.
 * This class is a modified version of a class of the same name provided by Stan Pieda
 */
public class Sprite implements Runnable {

	public final static Random random = new Random();

    SpritePanel panel;

    /**
     * Synchronized buffer object that manages # of box occupants
     */
    public final Buffer occupantsBuffer;

    final static int SIZE = 10;
    final static int MAX_SPEED = 5;
    /**
     * Debugging variable to track which sprites are inside/outside
     */
    public static int id = 0;

    /**
     * Coordinates (position) of Sprite
     */
    private int x;
	private int y;

    /**
     * X & Y Velocity components of Sprite
     */
    private int dx;
	private int dy;
	private Color color = Color.CYAN;

    /**
     * Store whether this Sprite was inside/outside last time this was checked.
     */
    private boolean wasInside = false;

    /**
     * Randomly generate position and velocity of new Sprite
     * @param panel
     * @param occupantsBuffer
     */
    public Sprite (SpritePanel panel, Buffer occupantsBuffer)
    {
    	this.panel = panel;
        x = random.nextInt(panel.getWidth());
        y = random.nextInt(panel.getHeight());
        dx = random.nextInt(2*MAX_SPEED) - MAX_SPEED;
        dy = random.nextInt(2*MAX_SPEED) - MAX_SPEED;
        this.occupantsBuffer = occupantsBuffer;
        id++;
    }

    /**
     * Pass in the positional coordinates of the new Sprite
     */
    public Sprite (SpritePanel panel, Buffer occupantsBuffer, int x, int y)
    {
        this.panel = panel;
        this.x = x;
        this.y = y;
        dx = random.nextInt(2*MAX_SPEED) - MAX_SPEED;
        dy = random.nextInt(2*MAX_SPEED) - MAX_SPEED;
        this.occupantsBuffer = occupantsBuffer;
        id++;
    }

    /**
     * Pass in the positional coordinates and the velocity components of the new Sprite
     */
    public Sprite (SpritePanel panel, Buffer occupantsBuffer, int x, int y, int dx, int dy)
    {
        this.panel = panel;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.occupantsBuffer = occupantsBuffer;
        id++;
    }

    /**
     * Determine if this sprite is currently inside the box.
     * @return true if inside, false if not
     */
    public boolean checkIfInside() {
        if (x > panel.getBoxX()-10 && x < panel.getBoxX() + panel.getBoxWidth() &&
                y > panel.getBoxY()-10 && y < panel.getBoxY() + panel.getBoxHeight()) {
            wasInside = true;
            return true;
        }
        else {
            wasInside = false;
            return false;
        }
    }

    public boolean detectEntrance(boolean isInside, boolean wasInside) {
        if (isInside && !wasInside) {
            // Sprite attempting to enter (Sprite is inside, but was outside in last check)
            System.out.printf("Sprite %d attempting to enter\n", id);
            return true;
        } else { return false; }
    }

    public boolean detectExit(boolean isInside, boolean wasInside) {
        if (!isInside && wasInside) {
            // Sprite attempting to leave (Sprite is outside, but was inside in last check)
            return true;
        } else { return false; }
    }

    public void draw(Graphics g){
        g.setColor(color);
	    g.fillOval(x, y, SIZE, SIZE);
    }

    /**
     * Set the color of this Sprite to Blue, to indiciate that it is no longer the newest Sprite
     */
    public void setColorAsOld() {
        color = Color.BLUE;
    }

    /**
     * Called at every frame to move the sprite.
     */
    public void move(){
        boolean wasInside = this.wasInside;
        boolean isInside = checkIfInside();

        // detect boundary crossing
        if (detectEntrance(isInside, wasInside)) {
            try {
                // if already 2 inside (no vacancies), freeze
                occupantsBuffer.blockingAdd();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else if (detectExit(isInside, wasInside)) {
            // Sprite attempting to leave (Sprite is outside, but was inside in last check)
            System.out.printf("Sprite %d attempting to exit\n", id);
            try {
                // if vacancies <= 1, freeze  (sprite can't leave unless another sprite is inside)
                occupantsBuffer.blockingRemove();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // check for bounce and make the ball bounce if necessary
        if (x < 0 && dx < 0){
            //bounce off the left wall 
            x = 0;
            dx = -dx;
        }
        if (y < 0 && dy < 0){
            //bounce off the top wall
            y = 0;
            dy = -dy;
        }
        if (x > panel.getWidth() - SIZE && dx > 0){
            //bounce off the right wall
        	x = panel.getWidth() - SIZE;
        	dx = - dx;
        }       
        if (y > panel.getHeight() - SIZE && dy > 0){
            //bounce off the bottom wall
        	y = panel.getHeight() - SIZE;
        	dy = -dy;
        }

        //make the ball move
        x += dx;
        y += dy;
    }

    @Override
    public void run() {
        System.out.printf("Running Sprite %d\n", id);
        while (true){
            move();
            //sleep while waiting to display the next frame of the animation
            try {
                Thread.sleep(40);  // wake up roughly 25 frames per second
            }
            catch ( InterruptedException exception ) {
                exception.printStackTrace();
            }
        }
    }
}
