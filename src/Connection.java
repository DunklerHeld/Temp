import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class Connection extends Thread {

    public static HashMap<String, Connection> connections;

    BufferedReader in;
    BufferedWriter out;
    Socket clientSocket;
    String nickname;
    boolean stop = false;

    public Connection(Socket clientSocket) {

        if (Connection.connections != null) {
            Connection.connections = new HashMap<>();
        }

        try {
            this.clientSocket = clientSocket;
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void kill(){
        System.out.println("Closing connection with " + nickname);
        stop = true;
        this.interrupt();
        System.out.printf("Nicht!");
    }

    public void send(String message) {
        try {
        	System.out.println("Sending!");
            out.write(message);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {
            String data;
            this.nickname = in.readLine();
            //Connection.connections.put(nickname, this);
            do {
                data = in.readLine();
                TCPServer.server.addMessage(this.nickname, data);
            } while (!data.equals(".stop") && !stop);
            System.out.println("Closing connection with " + nickname);
            if(!stop) {
                TCPServer.server.addMessage("SERVER", "Closing server!");
                TCPServer.server.shutdown();
            }
            TCPServer.server.removeObserver(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
