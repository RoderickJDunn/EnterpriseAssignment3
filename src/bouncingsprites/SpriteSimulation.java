package bouncingsprites;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JPanel;

/**
 *	The UI container for all Sprites and the rectangular box
 *  This class is a modified version of a class of the same name provided by Stan Pieda
 */
public class SpriteSimulation implements Runnable, SpriteSimulationInterface {

	/**
	 * List of sprites (bouncing balls) currently in User Interface
	 */
	private List<Sprite> sprites;

	private Buffer occupantsBuffer;

	private ExecutorService execService;


	/**
	 * Positional and size parameters for the UI rectangle (box)
	 */
	private int BORDER_WIDTH = 5;
	private int boxX = 0;
	private int boxY = 0;
	private int boxWidth = 0;
	private int boxHeight = 0;
	private int panelWidth = 400;
	private int panelHeight = 400;

//	private ArrayList<Point> spriteLocations;

	public SpriteSimulation(){
		occupantsBuffer = new SynchronizedBuffer();
		sprites = new ArrayList<>();
//		spriteLocations = new ArrayList<>();
		execService = Executors.newCachedThreadPool();

	}


	/**
	 * Creates a new ball at the position contained in the MouseEvent provided
	 */
	private void newSprite (){
		// if sprites ArrayList is 1+, change the color of the last created sprite to indicate it is not newest anymore
		if (sprites.size()>0) sprites.get(sprites.size()-1).setColorAsOld();
//		sprites.add(new Sprite(this, occupantsBuffer, event.getX(), event.getY()));
		sprites.add(new Sprite(this, occupantsBuffer));

		execService.execute(sprites.get(sprites.size()-1));
		//new Thread(sprites.get(sprites.size()-1)).start(); // spawn a new Thread for the newly created Sprite
		System.out.println("New ball created");
	}

	/**
	 * Used for Testing.
	 * Set positional and velocity parameters for the next sprite to be added to the user interface.
	 */
	public Sprite newTestSprite(int x, int y, int dx, int dy) {
		if (sprites.size()>0) sprites.get(sprites.size()-1).setColorAsOld();
		Sprite nextSprite = new Sprite(this, occupantsBuffer, x, y, dx, dy);
		sprites.add(nextSprite);
		new Thread(sprites.get(sprites.size()-1)).start(); // spawn a new Thread for the newly created Sprite
		System.out.println("New test ball created");
		return nextSprite;
	}

	public void run(){
		System.out.println("Running simulation");

		newSprite();
		newSprite();
		newSprite();
//		while (true){
//
////			updateCoordinates();
//	        //sleep while waiting to display the next frame of the animation
//	        try {
//	            Thread.sleep(10);  // wake up roughly 25 frames per second
//	        }
//	        catch ( InterruptedException exception ) {
//	            exception.printStackTrace();
//	        }
//	    }
	}

//	public void updateCoordinates() {
//		ArrayList<Point> spritePoints = new ArrayList<>();
//		for (int i=0; i<sprites.size()-1; i++) {
//			spritePoints.add(sprites.get(i).getXY());
//		}
//
//	}

//	private class Mouse extends MouseAdapter {
//		@Override
//	    public void mousePressed( final MouseEvent event ){
//	        newSprite(event);
//	    }
//	}

//	@Override
//	public void paintComponent(Graphics g){
//		super.paintComponent(g);
//		drawBox(g);
//		for (Sprite sprite : sprites) {
//			if (sprite != null){
//				sprite.draw(g);
//			}
//		}
//	}

	/**
	 * Draw the box in the center of the Frame
	 * @param
	 */
//	public void drawBox(Graphics g) {
//		boxWidth = (int) (this.getWidth()*0.33);
//		boxHeight = (int) (this.getHeight()*0.33);
//		boxX = (int)(this.getWidth()*0.33);
//		boxY = (int)(this.getHeight()*0.33);
//		// outer rectangle
//		g.fillRect(boxX, boxY, boxWidth, boxHeight);
//		// inner rectangle
//		g.setColor(Color.white);
//		g.fillRect(boxX+BORDER_WIDTH, boxY+BORDER_WIDTH, (boxWidth-BORDER_WIDTH*2), (boxHeight-BORDER_WIDTH*2));
//	}

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

	public int getPanelWidth() {
		return panelWidth;
	}

	public void setPanelWidth(int panelWidth) {
		this.panelWidth = panelWidth;
	}

	public int getPanelHeight() {
		return panelHeight;
	}

	public void setPanelHeight(int panelHeight) {
		this.panelHeight = panelHeight;
	}

	@Override
	public String printSomething() throws RemoteException {
		String test = "IT WORKED!!!!";
		System.out.println(test);
		newSprite();
		return test;
	}

	@Override
	public Dimension getPanelDimensions() throws RemoteException {
		return new Dimension(panelWidth, panelHeight);
	}

	@Override
	public Dimension getRectDimension() throws RemoteException {
		return new Dimension(getBoxWidth(), getBoxHeight());
	}

	@Override
	public ArrayList<Point> getSpriteLocations() throws RemoteException {
		// Send next frame (all sprite locations)
//		System.out.println("Sending Frame: ");
//		System.out.println(positionsQueue.peekFirst());
		// TODO: Create the next frame before asking
		// TODO: Issue - all clients must be synced, so the server side cannot
		// 			'wait' to send each frame. There should be a frame available
		//			for x milliseconds, and whoever requests it during that time
		//			gets it, otherwise, they will get the next frame.
		ArrayList<Point> spriteLocations = new ArrayList<>();
		for (int i=0; i<sprites.size()-1; i++) {
			spriteLocations.add(sprites.get(i).getNextPosition());
		}
		return spriteLocations;
	}
}

