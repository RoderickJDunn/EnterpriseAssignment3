package bouncingspritesclient;

import bouncingsprites.SpriteSimulationInterface;

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
import java.util.*;

/**
 * Created by Roderick on 2016-10-22.
 */
public class BouncingSpritesClient implements Runnable{

    private SpriteSimulationInterface simulation;
    private SpritePanel panel;


    public BouncingSpritesClient(SpritePanel panel) {
        this.panel = panel;
    }

    public UIInfo initializeClient(String[] args) {
        int port = 8081;
        String serverName = new String("localhost");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String myHostName = "localhost";

        switch (args.length) {
            case 0:
                break;
            case 1:
                serverName = args[0];
                break;
            case 2:
                serverName = args[0];
                port = Integer.parseInt(args[1]);
                break;
            default:
                System.out.println("usage: EchoClient [hostname [portnum]]");
                break;
        }
        try {
            InetAddress myHost = Inet4Address.getLocalHost();
            myHostName = myHost.getHostName();
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        }

        try {
            String message;
            System.out.println("Attempting to connect to rmi://"+serverName+":"+port+"/SpriteSimulationService");
            simulation = (SpriteSimulationInterface)
                    Naming.lookup("rmi://"+serverName+":"+port+"/SpriteSimulationService");

//            do {
//            System.out.print("Input> ");
            try {
//                message = br.readLine();
                System.out.println(simulation.printSomething());
                Dimension pDimensions = simulation.getPanelDimensions();
                Dimension rDimensions = simulation.getRectDimension();
                return new UIInfo(pDimensions, rDimensions);

            }catch(IOException e){
                e.printStackTrace();
//                message = null;
            }
//            } while (message != null);
        }
        catch (MalformedURLException murle) {
            System.out.println();
            System.out.println(
                    "MalformedURLException");
            System.out.println(murle);
        }
        catch (RemoteException re) {
            System.out.println();
            System.out.println("RemoteException");
            System.out.println(re);
        }
        catch (NotBoundException nbe) {
            System.out.println();
            System.out.println("NotBoundException");
            System.out.println(nbe);
        }
        return null;
    }


    public void run() throws RuntimeException {
        if (simulation == null) {
            System.out.println("ERROR: client not initialized yet");
            throw new RuntimeException("Not initialized");
        }

//        new Thread(new FrameLoader()).start();
//
//        try {
//            Thread.sleep(4000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        FrameLoader loader = new FrameLoader();
        loader.preloadFrames();
        loader.loadFrames();

//        // enter main loop
//        while (true) {
//            try {
//                panel.addFrame(frameQueue.remove());
//                System.out.println(frameQueue.size());
//            } catch (Exception e) { // TODO: catch specific exceptions
//
//            }
//            try {
//                Thread.sleep(40);
//            } catch (InterruptedException e)  {
//
//            }
//        }
    }

    public class FrameLoader implements Runnable {
        public FrameLoader() {}


        public void preloadFrames() {
            for (int i=0; i<200; i++) {
                try {
                    ArrayList<Point> frame = simulation.getSpriteLocations();
                    if (frame != null) panel.addFrame(frame);
                    else {
                        Thread.sleep(1000);
                    }
                } catch (Exception e) { // TODO: catch specific exceptions

                }
            }
        }

        public void loadFrames() {
//            for (Point point : frameQueue.getFirst()) {
//                System.out.println(point);
//            }
            while (true) {
                try {
                    panel.addFrame(simulation.getSpriteLocations());
                } catch (Exception e) { // TODO: catch specific exceptions

                }
                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void run() {
            preloadFrames();
//            loadFrames();
        }
    }
}
