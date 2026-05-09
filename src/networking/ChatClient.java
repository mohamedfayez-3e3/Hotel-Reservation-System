package networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public void connect(String host, int port) throws IOException {
        socket = new Socket(host, port);

        reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
        );

        writer = new PrintWriter(socket.getOutputStream(), true);
    }

    public void sendMessage(String message) {
        if (writer != null) {
            writer.println(message);
        }
    }

    public String receiveMessage() throws IOException {
        if (reader != null) {
            return reader.readLine();
        }

        return null;
    }

    public void close() {
        try {
            if (reader != null) {
                reader.close();
            }

            if (writer != null) {
                writer.close();
            }

            if (socket != null) {
                socket.close();
            }

        } catch (IOException e) {
            System.out.println("Client close error: " + e.getMessage());
        }
    }
}