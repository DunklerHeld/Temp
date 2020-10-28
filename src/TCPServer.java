import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

    public static TCPServer server;

    int port = 666;

    public TCPServer() {
        if (TCPServer.server != null)
            return;

        TCPServer.server = this;
    }

    public void listen() {
        try {
            ServerSocket listenSocket = new ServerSocket(port);
            while (true) {
                Socket clientSocket = listenSocket.accept();
                Connection c = new Connection(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMessage(String nickname, String message) {
        System.out.println(nickname + ": " + message);
        Connection.connections.forEach((s, connection) -> connection.send(nickname + ": " + message));
    }



    public static void main (String[] args) {

        new TCPServer().listen();

    }

}
