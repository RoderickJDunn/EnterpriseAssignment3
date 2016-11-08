package bouncingsprites;
import sun.rmi.runtime.Log;
import utils.LogIt;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.awt.*;
import java.io.Serializable;
import java.util.Random;

/**
 * Backs one 'bouncing ball' in the User Interface.
 * This class is a modified version of a class of the same name provided by Stan Pieda
 */
@Entity
public class Sprite implements Runnable, Serializable {

    private int id;

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

    private Color color = Color.BLACK;

    /**
     * Store whether this Sprite was inside/outside last time this was checked.
     */
    private boolean wasInside = false;

    private transient SpriteSimulation simulation;

    /**
     * Synchronized buffer object that manages # of boax occupants
     */
    private transient Buffer occupantsBuffer;

    /**
     * Debugging variable to track which sprites are inside/outside
     */
    private static int idCount = 0;

    final static int SIZE = 10;
    final static int MAX_SPEED = 5;

    private transient final static Random random = new Random();


    /**
     * No-arg constructor for Hibernate
     */
    Sprite() {}

    /**
     * Randomly generate position and velocity of new Sprite
     * @param simulation
     * @param occupantsBuffer
     */
    public Sprite (SpriteSimulation simulation, Buffer occupantsBuffer, Color color)
    {
        LogIt.debug("(Sprite) Basic Constructor");
        this.simulation = simulation;
        x = random.nextInt(simulation.getPanelWidth());
        y = random.nextInt(simulation.getPanelHeight());
        dx = random.nextInt(2*MAX_SPEED) - MAX_SPEED;
        dy = random.nextInt(2*MAX_SPEED) - MAX_SPEED;
        this.occupantsBuffer = occupantsBuffer;
        this.color = color;
        id = idCount;
        idCount++;
    }

    /**
     * Pass in the positional coordinates and color of the new Sprite
     */
    public Sprite (SpriteSimulation simulation, Buffer occupantsBuffer, Color color, int x, int y)
    {
        LogIt.debug("(Sprite) Normal Constructor");
        this.simulation = simulation;
        this.x = x;
        this.y = y;
        this.color = color;
        dx = random.nextInt(2*MAX_SPEED) - MAX_SPEED;
        dy = random.nextInt(2*MAX_SPEED) - MAX_SPEED;
        this.occupantsBuffer = occupantsBuffer;
        id = idCount;
        idCount++;
    }

    /**
     * Pass in the positional coordinates, velocity components, and color of the new Sprite
     */
    public Sprite (SpriteSimulation simulation, Buffer occupantsBuffer, int x, int y, int dx, int dy, Color c)
    {
        LogIt.debug("(Sprite) Full Constructor");
        this.simulation = simulation;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.occupantsBuffer = occupantsBuffer;
        this.color = c;
        id = idCount;
        idCount++;
    }


    /**
     * Initializes a Sprite that has been reconstructed from the database (by hibernate).
     * @param simulation
     * @param occupantsBuffer
     */
    void initializePersistedSprite(SpriteSimulation simulation, Buffer occupantsBuffer) {

        // TODO: USE Executor instead of manual threading
        if (this.simulation != null || this.occupantsBuffer != null) {
            LogIt.error("Sprite has already been initialized!");
            return;
        }
        this.simulation = simulation;
        this.occupantsBuffer = occupantsBuffer;
    }

    @Id @GeneratedValue @Column(name = "id")
    public int getId() {
        return id;
    }
    public void setId(int id) { this.id = id; }

    @Column(nullable = false)
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }

    @Column(nullable = false)
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }

    @Column(nullable = false)
    public int getDx() {
        return dx;
    }
    public void setDx(int dx) {
        this.dx = dx;
    }

    @Column(nullable = false)
    public int getDy() {
        return dy;
    }
    public void setDy(int dy) {
        this.dy = dy;
    }

    @Column(nullable = false)
    public Color getColor() {
        return color;
    }
    public void setColor(Color c) {this.color = c;}

    /**
     * Determine if this sprite is currently inside the box.
     * @return true if inside, false if not
     */
    private boolean checkIfInside() {
        LogIt.verbose("%d, %d", x, y);
        if (x > simulation.getBoxX()-5 && x < simulation.getBoxX() + simulation.getBoxWidth() &&
                y > simulation.getBoxY()-10 && y < simulation.getBoxY()-10 + simulation.getBoxHeight()) {
            wasInside = true;
            LogIt.debug("INSIDE!");
            return true;
        }
        else {
            wasInside = false;
            return false;
        }
    }

    private boolean detectEntrance(boolean isInside, boolean wasInside) {
        if (isInside && !wasInside) {
            // Sprite attempting to enter (Sprite is inside, but was outside in last check)
            LogIt.debug("Sprite %d attempting to enter\n", id);
            return true;
        } else { return false; }
    }

    private boolean detectExit(boolean isInside, boolean wasInside) {
        if (!isInside && wasInside) {
            // Sprite attempting to leave (Sprite is outside, but was inside in last check)
            LogIt.debug("Sprite %d attempting to leave\n", id);
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
    private void move(){
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
            LogIt.debug("Sprite %d attempting to exit\n", id);
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
            LogIt.debug("Left Wall Collision! [%d, %d]\n", x, y);
            x = 0;
            dx = -dx;
        }
        if (y < 0 && dy < 0){
            //bounce off the top wall
//            System.out.printf("Top Wall Collision! [%d, %d]\n", x, y);
            y = 0;
            dy = -dy;
        }
        if (x > simulation.getPanelWidth() - SIZE && dx > 0){
            //bounce off the right wall
//            System.out.printf("Right Wall Collision! [%d, %d]\n", x, y);
            x = simulation.getPanelWidth() - SIZE;
        	dx = - dx;
        }
        if (y > simulation.getPanelHeight() - SIZE && dy > 0){
            //bounce off the bottom wall
//            System.out.printf("Bottom Wall Collision! [%d, %d]\n", x, y);
            y = simulation.getPanelHeight() - SIZE;
        	dy = -dy;
        }

        //make the ball move
        x += dx;
        y += dy;
    }

    @Override
    public void run() {
        LogIt.info("Running Sprite %d\n", id);
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
