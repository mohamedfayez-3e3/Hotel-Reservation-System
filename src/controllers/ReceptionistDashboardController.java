package controllers;

import database.DatabaseManager;
import database.HotelDatabase;
import enums.PaymentMethod;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.Receptionist;
import models.Reservation;
import utils.SceneNavigator;
import utils.SessionData;

public class ReceptionistDashboardController {

    @FXML
    private TableView<Reservation> reservationTable;

    @FXML
    private TableColumn<Reservation, Integer> idColumn;

    @FXML
    private TableColumn<Reservation, String> guestColumn;

    @FXML
    private TableColumn<Reservation, Integer> roomColumn;

    @FXML
    private TableColumn<Reservation, String> datesColumn;

    @FXML
    private TableColumn<Reservation, String> statusColumn;

    @FXML
    private ComboBox<PaymentMethod> paymentMethodBox;

    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        if (SessionData.currentReceptionist == null) {
            SceneNavigator.switchTo("login.fxml");
            return;
        }

        setupTable();

        paymentMethodBox.getItems().addAll(
                PaymentMethod.CASH,
                PaymentMethod.CREDIT_CARD,
                PaymentMethod.ONLINE
        );

        paymentMethodBox.setValue(PaymentMethod.CASH);

        loadReservations();
    }

    private void setupTable() {
        idColumn.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().getReservationId()));

        guestColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getGuest().getUsername()));

        roomColumn.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().getRoom().getRoomNumber()));

        datesColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getCheckInDate()
                                + " to "
                                + data.getValue().getCheckOutDate()
                ));

        statusColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getStatus().toString()));
    }

    @FXML
    private void loadReservations() {
        reservationTable.setItems(
                FXCollections.observableArrayList(HotelDatabase.reservations)
        );

        statusLabel.setText("Reservations loaded.");
    }

    @FXML
    private void checkIn() {
        try {
            Receptionist receptionist = SessionData.currentReceptionist;
            Reservation selected = reservationTable.getSelectionModel().getSelectedItem();

            if (selected == null) {
                showError("Please select a reservation first.");
                return;
            }

            receptionist.checkInGuest(selected.getReservationId());

            DatabaseManager.saveAllData();

            showInfo("Guest checked in successfully.");
            loadReservations();

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void checkOut() {
        try {
            Receptionist receptionist = SessionData.currentReceptionist;
            Reservation selected = reservationTable.getSelectionModel().getSelectedItem();

            if (selected == null) {
                showError("Please select a reservation first.");
                return;
            }

            PaymentMethod method = paymentMethodBox.getValue();

            receptionist.checkOutGuest(selected.getReservationId(), method);

            DatabaseManager.saveAllData();

            showInfo("Guest checked out successfully.");
            loadReservations();

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void logout() {
        SessionData.clearSession();
        SceneNavigator.switchTo("login.fxml");
    }

    private void showError(String message) {
        statusLabel.setText(message);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Receptionist Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        statusLabel.setText(message);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Receptionist");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void openChat() {
        SceneNavigator.switchTo("chat.fxml");
    }
}