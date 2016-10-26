package bouncingspritesclient;

/**
 * Created by Roderick on 2016-10-22.
 */


import bouncingsprites.SpriteSimulationInterface;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
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

    private ArrayList<Point> lastFrame;

    public LinkedList<ArrayList<Point>> frameQueue;

    private ExecutorService execService;

    private SpriteSimulationInterface simulation;
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
        lastFrame = new ArrayList<>();
        execService = Executors.newCachedThreadPool();
        frameQueue = new LinkedList<>();
    }

    public void setSimulation(SpriteSimulationInterface simulation) {
        this.simulation = simulation;
    }

    public void addFrame(ArrayList<Point> frame) {
        frameQueue.add(frame);
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss.SSS");
    public void animate(){
//        try {
//            Thread.sleep(4000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
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

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
//        System.out.println("Painting components");
        drawBox(g);
        if (!frameQueue.isEmpty()) {
            lastFrame = frameQueue.remove();
        }
        else {
            // TODO: Display "Loading" screen, wait for list of frames to buffer
            System.out.println("Frame queue is empty.. using last frame");
        }
        for (int i=0; i<lastFrame.size(); i++) {
            g.setColor(Color.CYAN);
            //            System.out.println(points.get(i).x);
            g.fillOval(lastFrame.get(i).x, lastFrame.get(i).y, 10, 10);;
        }
//        System.out.println("");
//        System.out.println(dateFormat.format(new Date()));
//        System.out.printf("%d  :  %d", lastFrame.get(0).x, lastFrame.get(0).y);
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
        return boxX = (int)(this.getHeight()*0.33);
    }

    public int getBoxY() {
        return boxY = (int)(this.getHeight()*0.33);
    }

    public int getBoxWidth() {
        return boxWidth;
    }

    public int getBoxHeight() {
        return boxHeight;
    }


    private class Mouse extends MouseAdapter {

        @Override
        public void mousePressed( final MouseEvent event ){
            try {
                simulation.createSprite();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            System.out.println(getHeight());
            System.out.println(getWidth());
        }
    }
}
