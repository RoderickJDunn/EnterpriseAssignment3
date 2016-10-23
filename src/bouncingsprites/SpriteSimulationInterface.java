package bouncingsprites;

import java.awt.*;
import java.rmi.Remote;

/**
 * Created by Roderick on 2016-10-22.
 */
public interface SpriteSimulationInterface extends Remote {

    String printSomething() throws java.rmi.RemoteException;

    Dimension getPanelDimensions() throws java.rmi.RemoteException;

    Dimension getRectDimension() throws java.rmi.RemoteException;
}
