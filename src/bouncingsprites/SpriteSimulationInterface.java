package bouncingsprites;

import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by Roderick on 2016-10-22.
 */
public interface SpriteSimulationInterface extends Remote {

    String printSomething() throws java.rmi.RemoteException;

    Dimension getFrameDimensions() throws java.rmi.RemoteException;

    void updateUIParameters(Dimension pDimension, Point boxXY) throws java.rmi.RemoteException;

    Dimension getRectDimension() throws java.rmi.RemoteException;

    ArrayList<Point> getSpriteLocations() throws java.rmi.RemoteException;

    void createSprite() throws java.rmi.RemoteException;
}
