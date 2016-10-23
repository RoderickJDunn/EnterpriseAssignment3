package bouncingsprites;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roderick on 2016-10-22.
 */
public class BouncingSpritesServer {

    private List<Connection> connections;
    private ServerSocket server;
//    private ObjectOutputStream output;
//    private ObjectInputStream input;
    private String message = "";
    private int messagenum;
    private int portNum;
    public static final int DEFAULT_PORT = 8081;

    private SpriteSimulation simulation = new SpriteSimulation();

    public BouncingSpritesServer() {
        this.portNum = DEFAULT_PORT;
        connections = new ArrayList<>();
    }

    public BouncingSpritesServer(int portNum) {
        this.portNum = portNum;
        connections = new ArrayList<>();
        startSimulation();
    }

    public void startSimulation() {
        new Thread(simulation).start();
    }

    public void runServer() {
        System.out.println("Server Starting...");
        try {
            System.out.println( "Creating registry" );
            LocateRegistry.createRegistry(portNum);
            System.out.println( "Exporting Objects" );
            UnicastRemoteObject.exportObject(simulation,portNum);
            Naming.rebind("//localhost:" + portNum + "/SpriteSimulationService", simulation);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
       //        while (true) {
//            try {
//                if (server == null) server = new ServerSocket(portNum);
//                Socket cnx = server.accept();
//                connections.add(new Connection(cnx));
//                new Thread(connections.get(connections.size()-1)).start();
//            } catch(IOException exception){
//                System.out.println("IO Exception");
//                exception.printStackTrace();
//            }
//        }
    }

    public class Connection implements Runnable{
        Socket connection;
        public Connection(Socket connection) {
            this.connection = connection;
        }

        @Override
        public void run() {
            try (ObjectOutputStream output = new ObjectOutputStream(connection.getOutputStream());
                 ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
            )
            {
                System.out.println("Connected to new client");
                do {
                    try {
//						System.out.println("Reading input");
                        message = (String) input.readObject();
                        System.out.println("Recieved: " + message);
                    } catch (EOFException e) {
                        System.out.println("Caught end of file exception");
                        message = null;
                    }
                    if (message != null) {
                        output.writeObject(messagenum++ + " FromServer> " + message);
                        output.flush();
                    }
                } while (message != null);
            }
            catch(SocketException exception){
                System.out.println("Connection lost");
            }
            catch(IOException exception) {
                System.out.println("IO Exception");
                exception.printStackTrace();
            }
            catch(ClassNotFoundException exception){
                System.out.println("ClassNotFound Exception");
                exception.printStackTrace();
            }
        }
    }


}
