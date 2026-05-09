package controllers;

import database.HotelDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Admin;
import models.Guest;
import models.Receptionist;
import utils.SceneNavigator;
import utils.SessionData;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> roleBox;

    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        roleBox.getItems().addAll("Guest", "Admin", "Receptionist");
        roleBox.setValue("Guest");
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String role = roleBox.getValue();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter username and password.");
            return;
        }

        SessionData.clearSession();

        if (role.equals("Guest")) {
            loginGuest(username, password);
        } else if (role.equals("Admin")) {
            loginAdmin(username, password);
        } else if (role.equals("Receptionist")) {
            loginReceptionist(username, password);
        }
    }

    private void loginGuest(String username, String password) {
        Guest guest = HotelDatabase.findGuestByUsername(username);

        if (guest != null && guest.login(username, password)) {
            SessionData.currentGuest = guest;
            SceneNavigator.switchTo("guest_dashboard.fxml");
        } else {
            showError("Invalid guest username or password.");
        }
    }

    private void loginAdmin(String username, String password) {
        for (Admin admin : HotelDatabase.admins) {
            if (admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
                SessionData.currentAdmin = admin;
                SceneNavigator.switchTo("admin_dashboard.fxml");
                return;
            }
        }

        showError("Invalid admin username or password.");
    }

    private void loginReceptionist(String username, String password) {
        for (Receptionist receptionist : HotelDatabase.receptionists) {
            if (receptionist.getUsername().equals(username) && receptionist.getPassword().equals(password)) {
                SessionData.currentReceptionist = receptionist;
                SceneNavigator.switchTo("receptionist_dashboard.fxml");
                return;
            }
        }

        showError("Invalid receptionist username or password.");
    }

    @FXML
    private void goToRegister() {
        SceneNavigator.switchTo("register.fxml");
    }

    private void showError(String message) {
        statusLabel.setText(message);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}