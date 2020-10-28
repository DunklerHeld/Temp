import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class Connection implements Runnable {

    public static HashMap<String, Connection> connections;

    BufferedReader in;
    BufferedWriter out;
    Socket clientSocket;
    String nickname;

    public Connection(Socket clientSocket) {

        if (Connection.connections != null) {
            Connection.connections = new HashMap<>();
        }

        try {
            this.clientSocket = clientSocket;
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            new Thread(this).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void send(String message) {
        try {
            out.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {
            String data;
            this.nickname = in.readLine();
            Connection.connections.put(nickname, this);
            do {
                data = in.readLine();
                out.write(data);
                TCPServer.server.addMessage(this.nickname, data);
            } while (!data.equals(""));
            System.out.println("Closing connection with " + nickname);
            Connection.connections.remove(nickname);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
