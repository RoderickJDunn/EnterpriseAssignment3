package bouncingsprites;

import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import utils.LogIt;

/**
 *	The UI container for all Sprites and the rectangular box
 *  This class is a modified version of a class of the same name provided by Stan Pieda
 */
public class SpriteSimulation implements Runnable, SpriteSimulationInterface {

	/**
	 * List of sprites (bouncing balls) currently in User Interface
	 */
	private ArrayList<Sprite> sprites;

	private Buffer occupantsBuffer;

	private ExecutorService execService;

//	private ArrayList<Sprite> frame;
	/**
	 * Positional and size parameters for the UI rectangle (box)
	 */

	private int frameWidth = 400;
	private int frameHeight = 400;
	private int panelWidth = 400;
	private int panelHeight = 400;
	private int boxX = frameWidth/3;
	private int boxY = frameHeight/3;
	private int boxWidth = frameWidth/3;
	private int boxHeight = frameWidth/3;
	private HashMap<UUID, Color> allClients;

	public SpriteSimulation(){
		occupantsBuffer = new SynchronizedBuffer();
		sprites = new ArrayList<>();
		allClients = new HashMap<>();
//		frame = new ArrayList<>();
		execService = Executors.newCachedThreadPool();
	}

	/**
	 * Creates a new ball at the position contained in the MouseEvent provided
	 */
	private void newSprite (UUID uuid){
		// TODO: depending in what uuid is passed in, look up the appropriate color in the Database.
		// TEMPORARY METHOD OF STORING MAPPINGS
		Color c = allClients.get(uuid);
		//
		sprites.add(new Sprite(this, occupantsBuffer, c));
//		sprites.add(new Sprite(this, occupantsBuffer, new Color((int) (Math.random() * 0xffffffff))));
		execService.execute(sprites.get(sprites.size()-1));  // spawn a new Thread for the newly created Sprite
		LogIt.info("New ball created");
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
//		System.out.println("New test ball created");
		LogIt.info("New test ball created");
		return nextSprite;
	}

	public void run(){
		LogIt.info("Running simulation");

//		newSprite();
		while (true){

			//updateCoordinates();
	        //sleep while waiting to display the next frame of the animation
	        try {
	            Thread.sleep(10);  // wake up roughly 25 frames per second
	        }
	        catch ( InterruptedException exception ) {
	            exception.printStackTrace();
	        }
	    }
	}

	public void updateCoordinates() {
		ArrayList<Point> spritePoints = new ArrayList<>();
		for (int i=0; i<=sprites.size()-1; i++) {
			spritePoints.add(sprites.get(i).getPosition());
		}
//		System.out.println(sprites.size());
//		System.out.println(spritePoints.size());
//		if (spritePoints.size()>0) System.out.println(spritePoints.get(0));
//		frame = spritePoints;
	}

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

	public void setFrameWidth(int frameWidth) {
		this.frameWidth = frameWidth;
	}

	public int getPanelHeight() {
		return panelHeight;
	}

	@Override
	public String printSomething() throws RemoteException {
		String test = "IT WORKED!!!!";
		System.out.println(test);
//		newSprite();
		return test;
	}

	@Override
	public Dimension getFrameDimensions() throws RemoteException {
		return new Dimension(frameWidth, frameHeight);
	}

	@Override
	public Dimension getRectDimension() throws RemoteException {
		return new Dimension(getBoxWidth(), getBoxHeight());
	}

	@Override
	public void updateUIParameters(Dimension pDimensions, Point boxXY) throws RemoteException {
		this.panelWidth = pDimensions.width;
		this.panelHeight = pDimensions.height;
		this.boxX = boxXY.x;
		this.boxY = boxXY.y;
	}

	@Override
	public ArrayList<Sprite> getSprites() throws RemoteException {
		// Send next frame (all sprite locations)
//		System.out.println("Sending Frame: ");
//		System.out.println(positionsQueue.peekFirst());
//		System.out.println(frame);
//		System.out.println(sprites);
		return sprites;
	}

	@Override
	public void createSprite(UUID uuid) throws RemoteException {
		LogIt.info(uuid.toString());
		newSprite(uuid);
	}

	@Override
	public ClientInfo getClientInfo() throws RemoteException {
		UUID id = UUID.randomUUID();
		Color color = new Color((int) (Math.random() * 0xff000000));
		ClientInfo c = new ClientInfo(id, color); // TODO: Persist this object
		// TEMPORARY METHOD OF TRACKING CLIENT_INFO
		allClients.put(c.getId(), c.getColor());
		//
		return c;
	}
}

