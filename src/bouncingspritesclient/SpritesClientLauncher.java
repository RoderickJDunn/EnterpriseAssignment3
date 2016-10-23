package bouncingspritesclient;

import bouncingsprites.SpriteSimulationInterface;

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

    public static void main(String[] args) {

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
            SpriteSimulationInterface simulation = (SpriteSimulationInterface)
                    Naming.lookup("rmi://"+serverName+":"+port+"/SpriteSimulationService");

            setupUserInterface();
            do {
                System.out.print("Input> ");
                try {
                    message = br.readLine();
                    System.out.println(simulation.printSomething());

                }catch(IOException e){
                    e.printStackTrace();
                    message = null;
                }
            } while (message != null);
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
    }
}

