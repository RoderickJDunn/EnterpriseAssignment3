package bouncingspritesclient;

import bouncingsprites.SpriteSimulationInterface;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by Roderick on 2016-10-22.
 */

public class SpritesClientLauncher {

    BouncingSpritesClient client;

    private JFrame frame;
    private SpritePanel panel;
//    public static int frameWidth = 400;
//    public static int frameHeight = 400;

    public SpritesClientLauncher(String[] args) {
        panel = new SpritePanel();
        client = new BouncingSpritesClient(panel);
        UIInfo uiInfo = client.initializeClient(args);

        frame = new JFrame("Bouncing Sprite");
        frame.setSize(uiInfo.panelDimensions.width, uiInfo.panelDimensions.height);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setVisible(true);
        Dimension panelDim = new Dimension(panel.getWidth(), panel.getHeight());
        Point boxXY = new Point(panel.getBoxX(), panel.getBoxY());
        client.sendUIParameters(panelDim, boxXY);

        // TODO: Wait (block?) until connected?

        new Thread(client).start();
        panel.animate();
    }

    public static void main(String[] args) {
        new SpritesClientLauncher(args);
    }

    public void startAnimation(){
        panel.animate();  // never returns due to infinite loop in animate method
    }

    // Used for testing
    public SpritePanel getPanel() {
        return panel;
    }


}
