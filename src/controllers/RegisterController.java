package controllers;

import enums.Gender;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Guest;
import utils.SceneNavigator;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private DatePicker dateOfBirthPicker;

    @FXML
    private TextField balanceField;

    @FXML
    private TextField addressField;

    @FXML
    private ComboBox<Gender> genderBox;

    @FXML
    private TextField preferencesField;

    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        genderBox.getItems().addAll(Gender.MALE, Gender.FEMALE);
        genderBox.setValue(Gender.MALE);
    }

    @FXML
    private void handleRegister() {
        try {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            double balance = Double.parseDouble(balanceField.getText().trim());

            Guest.register(
                    username,
                    password,
                    dateOfBirthPicker.getValue(),
                    balance,
                    addressField.getText().trim(),
                    genderBox.getValue(),
                    preferencesField.getText().trim()
            );

            showInfo("Guest registered successfully.");
            SceneNavigator.switchTo("login.fxml");

        } catch (NumberFormatException e) {
            showError("Balance must be a valid number.");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void goBackToLogin() {
        SceneNavigator.switchTo("login.fxml");
    }

    private void showError(String message) {
        statusLabel.setText(message);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Register Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Register");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}