import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TCPServer extends Thread{

    public static TCPServer server;

    int port = 1666;
    
    ArrayList<Connection> observers;

    boolean stop = false;

    public TCPServer() {
        if (TCPServer.server != null)
            return;

        TCPServer.server = this;
        observers = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            ServerSocket listenSocket = new ServerSocket(port);
            while (!stop) {
                Socket clientSocket = listenSocket.accept();
                Connection c = new Connection(clientSocket);
                addObserver(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMessage(String nickname, String message) {
        System.out.println(nickname + ": " + message);
        for (Connection c : observers) {
        	c.send(nickname + ": " + message);
        }
    }
    
    public void addObserver(Connection c) {
    	observers.add(c);
    }
    
    public void removeObserver(Connection c) {
    	observers.remove(c);
    }

    public void shutdown(){
        stop = true;
        for (Connection c : observers) {
            c.kill();
        }
        System.out.println("Alle Clients beendet!" + stop);
        System.exit(0);
    }



    public static void main (String[] args) {

        new TCPServer().start();

    }

}
