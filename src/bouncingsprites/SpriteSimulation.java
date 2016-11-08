package bouncingsprites;

import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dataaccess.DataAccess;
import utils.LogIt;

/**
 *	The Simulation of all Sprites moving within a container box
 */
public class SpriteSimulation implements Runnable, SpriteSimulationInterface {

	/**
	 * List of sprites (bouncing balls) currently in User Interface
	 */
	private ArrayList<Sprite> sprites;

	private Buffer occupantsBuffer;

	private ExecutorService execService;

	DataAccess dataAccess;

	/**
	 * Positional and size parameters for the UI rectangle (box)
	 */
	private int frameWidth = 400;
	private int frameHeight = 400;
	private int panelWidth = 392;
	private int panelHeight = 392;
	private int boxX = frameWidth/3;
	private int boxY = frameHeight/3;
	private int boxWidth = frameWidth/3;
	private int boxHeight = frameHeight/3;
	private HashMap<UUID, Color> allClients;

	public SpriteSimulation(){
		occupantsBuffer = new SynchronizedBuffer();
		execService = Executors.newCachedThreadPool();
		dataAccess =  DataAccess.getInstance();
		sprites = new ArrayList<>();
		allClients = new HashMap<>();

		Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownHandler()));

		// TODO: Check DB to see if Sprite table has any entires. Load if yes, otherwise create new arraylist.
		List persistedSprites;
		if ((persistedSprites = dataAccess.getPersistedSprites()) != null) {
			sprites.addAll(persistedSprites);
			for (int i = 0; i < sprites.size(); i++) {
				sprites.get(i).initializePersistedSprite(this, occupantsBuffer);
				try {
					execService.execute(sprites.get(i));
				} catch(IllegalThreadStateException e) {
					LogIt.error("Sprite thread is already running");
				}
			}
		}

		// TODO: Check DB to see if ClientInfo table has any entires. Load if yes, otherwise create new HashMap.
		// TODO: Do we even need to persist clientInfo... If color is persisted with sprite? I suppose if same client reconnects after server restarts...



	}

	/**
	 * Creates a new ball at the position contained in the MouseEvent provided
	 */
	private void newSprite (UUID uuid, Point point){
		// TODO: depending in what uuid is passed in, look up the appropriate color in the Database.
		// TEMPORARY METHOD OF STORING MAPPINGS -- should be persistent in DB
		Color c = allClients.get(uuid);
		sprites.add(new Sprite(this, occupantsBuffer, c, point.x, point.y));
		execService.execute(sprites.get(sprites.size()-1));  // spawn a new Thread for the newly created Sprite
		LogIt.info("New sprite created");
	}

	/**
	 * Used for Testing.
	 * Set positional and velocity parameters for the next sprite to be added to the user interface.
	 */
	public Sprite newTestSprite(int x, int y, int dx, int dy) {
		if (sprites.size()>0) sprites.get(sprites.size()-1).setColorAsOld();
		Sprite nextSprite = new Sprite(this, occupantsBuffer, x, y, dx, dy, Color.CYAN);
		sprites.add(nextSprite);
		new Thread(sprites.get(sprites.size()-1)).start(); // spawn a new Thread for the newly created Sprite
//		System.out.println("New test ball created");
		LogIt.info("New test sprite created");
		return nextSprite;
	}

	public void run(){
		LogIt.info("Running simulation");

		while (true){

	        //sleep while waiting to display the next frame of the animation
	        try {
	            Thread.sleep(10);  // wake up roughly 25 frames per second
	        }
	        catch ( InterruptedException exception ) {
	            exception.printStackTrace();
	        }
	    }
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

	public int getPanelWidth() {
		return panelWidth;
	}

	public void setFrameWidth(int frameWidth) {
		this.frameWidth = frameWidth;
	}

	public int getPanelHeight() {
		return panelHeight;
	}

	// Remote Interface Methods
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

	//TODO: This is useless: The idea was to eventually be able to support resizable panel, but since there are multiple
	//			clients, these UI parameters will be overridden by each new client. For now its better just to hard-code
	//			the panel and box dimensions server-side, and make the panel not resizable client-side.
	@Override
	public void updateUIParameters(Dimension pDimensions, Point boxXY) throws RemoteException {
		this.panelWidth = pDimensions.width;
		this.panelHeight = pDimensions.height;
		this.boxX = boxXY.x;
		this.boxY = boxXY.y;
	}

	@Override
	public ArrayList<Sprite> getSprites() throws RemoteException {
		// Send next frame (all sprites)
		return sprites;
	}

	@Override
	public void createSprite(UUID uuid, Point point) throws RemoteException {
		LogIt.debug("Client %s requested new Sprite", uuid.toString());
		newSprite(uuid, point);
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

	private class ShutdownHandler implements Runnable {

		@Override
		public void run() {
			LogIt.info("Saving sprites to database");
			LogIt.info("Shutting down...");
			dataAccess.saveSprites(sprites);
		}
	}
}

