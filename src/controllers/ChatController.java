package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import networking.ChatClient;
import utils.SceneNavigator;
import utils.SessionData;

public class ChatController {

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField messageField;

    @FXML
    private Label statusLabel;

    private ChatClient chatClient;
    private String username;
    private boolean listening = true;

    @FXML
    public void initialize() {
        username = getCurrentUsername();

        if (username == null) {
            SceneNavigator.switchTo("login.fxml");
            return;
        }

        connectToServer();
    }

    private String getCurrentUsername() {
        if (SessionData.currentGuest != null) {
            return "Guest " + SessionData.currentGuest.getUsername();
        }

        if (SessionData.currentReceptionist != null) {
            return "Receptionist " + SessionData.currentReceptionist.getUsername();
        }

        if (SessionData.currentAdmin != null) {
            return "Admin " + SessionData.currentAdmin.getUsername();
        }

        return null;
    }

    private void connectToServer() {
        try {
            chatClient = new ChatClient();
            chatClient.connect("localhost", 5555);

            statusLabel.setText("Connected to chat server.");
            chatClient.sendMessage(username + " joined the chat.");

            startListeningThread();

        } catch (Exception e) {
            showError("Could not connect to chat server. Run ChatServer first.");
        }
    }

    private void startListeningThread() {
        Thread listeningThread = new Thread(() -> {
            while (listening) {
                try {
                    String message = chatClient.receiveMessage();

                    if (message == null) {
                        break;
                    }

                    Platform.runLater(() -> {
                        chatArea.appendText(message + "\n");
                    });

                } catch (Exception e) {
                    Platform.runLater(() -> {
                        statusLabel.setText("Disconnected from server.");
                    });
                    break;
                }
            }
        });

        listeningThread.setDaemon(true);
        listeningThread.start();
    }

    @FXML
    private void sendMessage() {
        String message = messageField.getText().trim();

        if (message.isEmpty()) {
            return;
        }

        chatClient.sendMessage(username + ": " + message);
        messageField.clear();
    }

    @FXML
    private void goBack() {
        listening = false;

        if (chatClient != null) {
            chatClient.sendMessage(username + " left the chat.");
            chatClient.close();
        }

        if (SessionData.currentGuest != null) {
            SceneNavigator.switchTo("guest_dashboard.fxml");
        } else if (SessionData.currentReceptionist != null) {
            SceneNavigator.switchTo("receptionist_dashboard.fxml");
        } else {
            SceneNavigator.switchTo("login.fxml");
        }
    }

    private void showError(String message) {
        statusLabel.setText(message);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Chat Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}