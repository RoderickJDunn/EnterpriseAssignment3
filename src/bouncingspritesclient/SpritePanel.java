package bouncingspritesclient;

/**
 * Created by Roderick on 2016-10-22.
 */


import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
        import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JPanel;

/**
 *	The UI container for all Sprites and the rectangular box
 *  This class is a modified version of a class of the same name provided by Stan Pieda
 */
public class SpritePanel extends JPanel {

    /**
     * List of sprites (bouncing balls) currently in User Interface
     */
//    private List<Sprite> sprites;

//    private Buffer occupantsBuffer;

    private ArrayList<Point> lastFrame;

    public LinkedList<ArrayList<Point>> frameQueue;

    private ExecutorService execService;
    /**
     * Positional and size parameters for the UI rectangle (box)
     */
    private int BORDER_WIDTH = 5;
    private int boxX = 0;
    private int boxY = 0;
    private int boxWidth = 0;
    private int boxHeight = 0;

    public SpritePanel(){
        addMouseListener(new Mouse()); // add listener for mouse actions
//        occupantsBuffer = new SynchronizedBuffer();
//        sprites = new ArrayList<>();
        lastFrame = new ArrayList<>();
        execService = Executors.newCachedThreadPool();
        frameQueue = new LinkedList<>();

    }

    public void addFrame(ArrayList<Point> frame) {
        frameQueue.add(frame);
    }


    /**
     * Creates a new ball at the position contained in the MouseEvent provided
     * @param event
     */
    private void newSprite (MouseEvent event){
        // if sprites ArrayList is 1+, change the color of the last created sprite to indicate it is not newest anymore
//        if (sprites.size()>0) sprites.get(sprites.size()-1).setColorAsOld();
//        sprites.add(new Sprite(this, occupantsBuffer, event.getX(), event.getY()));
//        execService.execute(sprites.get(sprites.size()-1));
        //new Thread(sprites.get(sprites.size()-1)).start(); // spawn a new Thread for the newly created Sprite
        System.out.println("New ball created");
    }

    /**
     * Used for Testing.
     * Set positional and velocity parameters for the next sprite to be added to the user interface.
     */
//    public Sprite newTestSprite(int x, int y, int dx, int dy) {
//        if (sprites.size()>0) sprites.get(sprites.size()-1).setColorAsOld();
//        Sprite nextSprite = new Sprite(this, occupantsBuffer, x, y, dx, dy);
//        sprites.add(nextSprite);
//        //new Thread(sprites.get(sprites.size()-1)).start(); // spawn a new Thread for the newly created Sprite
//        System.out.println("New test ball created");
//        return nextSprite;
//    }
    SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss.SSS");
    public void animate(){
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (true){
//            System.out.println("Repainting panel");
            repaint();
            //sleep while waiting to display the next frame of the animation
            try {
                Thread.sleep(40);  // wake up roughly 25 frames per second
            }
            catch ( InterruptedException exception ) {
                exception.printStackTrace();
            }
        }
    }

    private class Mouse extends MouseAdapter {
        @Override
        public void mousePressed( final MouseEvent event ){
            newSprite(event);
        }
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
//        System.out.println("Painting components");
        drawBox(g);
        if (!frameQueue.isEmpty()) {
            lastFrame = frameQueue.remove();
        }
        else {
            System.out.println("Frame queue is empty.. using last frame");
        }
        for (int i = 0; i < lastFrame.size()-1; i++) {
            g.setColor(Color.CYAN);
            //            System.out.println(points.get(i).x);
            g.fillOval(lastFrame.get(i).x, lastFrame.get(i).y, 10, 10);;
        }
        System.out.println("");
        System.out.println(dateFormat.format(new Date()));
        System.out.printf("%d  :  %d", lastFrame.get(0).x, lastFrame.get(0).y);
    }

    /**
     * Draw the box in the center of the Frame
     * @param g
     */
    public void drawBox(Graphics g) {
        boxWidth = (int) (this.getWidth()*0.33);
        boxHeight = (int) (this.getHeight()*0.33);
        boxX = (int)(this.getWidth()*0.33);
        boxY = (int)(this.getHeight()*0.33);
        // outer rectangle
        g.fillRect(boxX, boxY, boxWidth, boxHeight);
        // inner rectangle
        g.setColor(Color.white);
        g.fillRect(boxX+BORDER_WIDTH, boxY+BORDER_WIDTH, (boxWidth-BORDER_WIDTH*2), (boxHeight-BORDER_WIDTH*2));
    }

    public int getBoxX() {
        return boxX;
    }

    public int getBoxY() {
        return boxY;
    }

    public int getBoxWidth() {
        return boxWidth;
    }

    public int getBoxHeight() {
        return boxHeight;
    }
}
