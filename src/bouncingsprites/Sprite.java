package bouncingsprites;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * Backs one 'bouncing ball' in the User Interface.
 * This class is a modified version of a class of the same name provided by Stan Pieda
 */
public class Sprite implements Runnable {

	public final static Random random = new Random();

    SpriteSimulation simulation;

    public LinkedList<Point> positionsHistory;

    /**
     * Synchronized buffer object that manages # of box occupants
     */
    public final Buffer occupantsBuffer;

    final static int SIZE = 10;
    final static int MAX_SPEED = 5;

    /**
     * Debugging variable to track which sprites are inside/outside
     */
    public static int idCount = 0;

    public int id = 0;

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
     * @param container
     * @param occupantsBuffer
     */
    public Sprite (SpriteSimulation container, Buffer occupantsBuffer)
    {
    	this.simulation = container;
        x = random.nextInt(container.getPanelWidth());
        y = random.nextInt(container.getPanelHeight());
        dx = random.nextInt(2*MAX_SPEED) - MAX_SPEED;
        dy = random.nextInt(2*MAX_SPEED) - MAX_SPEED;
        this.occupantsBuffer = occupantsBuffer;
        positionsHistory = new LinkedList<>();
        id = idCount;
        idCount++;
    }

    /**
     * Pass in the positional coordinates of the new Sprite
     */
    public Sprite (SpriteSimulation container, Buffer occupantsBuffer, int x, int y)
    {
        this.simulation = container;
        this.x = x;
        this.y = y;
        dx = random.nextInt(2*MAX_SPEED) - MAX_SPEED;
        dy = random.nextInt(2*MAX_SPEED) - MAX_SPEED;
        this.occupantsBuffer = occupantsBuffer;
        positionsHistory = new LinkedList<>();
        id = idCount;
        idCount++;
    }

    /**
     * Pass in the positional coordinates and the velocity components of the new Sprite
     */
    public Sprite (SpriteSimulation panel, Buffer occupantsBuffer, int x, int y, int dx, int dy)
    {
        this.simulation = panel;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.occupantsBuffer = occupantsBuffer;
        positionsHistory = new LinkedList<>();
        id = idCount;
        idCount++;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Point getNextPosition(){
        return positionsHistory.remove();
    }

    /**
     * Determine if this sprite is currently inside the box.
     * @return true if inside, false if not
     */
    public boolean checkIfInside() {
        if (x > simulation.getBoxX()-10 && x < simulation.getBoxX() + simulation.getBoxWidth() &&
                y > simulation.getBoxY()-10 && y < simulation.getBoxY() + simulation.getBoxHeight()) {
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
//      System.out.printf("Sprite %d is moving\n", id);
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
        if (x > simulation.getPanelWidth() - SIZE && dx > 0){
            //bounce off the right wall
        	x = simulation.getPanelWidth() - SIZE;
        	dx = - dx;
        }       
        if (y > simulation.getPanelHeight() - SIZE && dy > 0){
            //bounce off the bottom wall
        	y = simulation.getPanelHeight() - SIZE;
        	dy = -dy;
        }

        //make the ball move
        x += dx;
        y += dy;

        positionsHistory.add(new Point(x, y));
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
