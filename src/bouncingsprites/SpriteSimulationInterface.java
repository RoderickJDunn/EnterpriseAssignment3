package bouncingsprites;

import java.awt.*;
import java.rmi.Remote;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Roderick on 2016-10-22.
 */
public interface SpriteSimulationInterface extends Remote {

    String printSomething() throws java.rmi.RemoteException;

    Dimension getFrameDimensions() throws java.rmi.RemoteException;

    void updateUIParameters(Dimension pDimension, Point boxXY) throws java.rmi.RemoteException;

    Dimension getRectDimension() throws java.rmi.RemoteException;

    ArrayList<Sprite> getSprites() throws java.rmi.RemoteException;

    void createSprite(UUID uuid, Point point) throws java.rmi.RemoteException;

    ClientInfo getClientInfo()throws java.rmi.RemoteException;
}
