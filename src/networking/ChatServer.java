package networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {

    private static final int PORT = 5555;
    private static ArrayList<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Chat server started on port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            while (true) {
                Socket socket = serverSocket.accept();

                ClientHandler clientHandler = new ClientHandler(socket, clients);
                clients.add(clientHandler);

                Thread thread = new Thread(clientHandler);
                thread.start();

                System.out.println("New client connected.");

            }

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
}