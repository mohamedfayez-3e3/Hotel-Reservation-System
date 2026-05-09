package controllers;

import database.HotelDatabase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Guest;
import models.Reservation;
import utils.SceneNavigator;
import utils.SessionData;

public class ReservationController {

    @FXML
    private TableView<Reservation> reservationTable;

    @FXML
    private TableColumn<Reservation, Integer> idColumn;

    @FXML
    private TableColumn<Reservation, Integer> roomColumn;

    @FXML
    private TableColumn<Reservation, String> checkInColumn;

    @FXML
    private TableColumn<Reservation, String> checkOutColumn;

    @FXML
    private TableColumn<Reservation, String> statusColumn;

    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        setupTable();
        loadReservations();
    }

    private void setupTable() {
        idColumn.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().getReservationId()));

        roomColumn.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().getRoom().getRoomNumber()));

        checkInColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getCheckInDate().toString()));

        checkOutColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getCheckOutDate().toString()));

        statusColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getStatus().toString()));
    }

    private void loadReservations() {
        Guest guest = SessionData.currentGuest;

        if (guest == null) {
            SceneNavigator.switchTo("login.fxml");
            return;
        }

        reservationTable.setItems(FXCollections.observableArrayList());

        for (Reservation reservation : HotelDatabase.reservations) {
            if (reservation.getGuest().getUsername().equals(guest.getUsername())) {
                reservationTable.getItems().add(reservation);
            }
        }

        statusLabel.setText("Reservations loaded.");
    }

    @FXML
    private void cancelReservation() {
        try {
            Guest guest = SessionData.currentGuest;
            Reservation selected = reservationTable.getSelectionModel().getSelectedItem();

            if (selected == null) {
                showError("Please select a reservation first.");
                return;
            }

            guest.cancelReservation(selected.getReservationId());

            showInfo("Reservation cancelled successfully.");
            loadReservations();

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void goBack() {
        SceneNavigator.switchTo("guest_dashboard.fxml");
    }

    private void showError(String message) {
        statusLabel.setText(message);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Reservation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        statusLabel.setText(message);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reservation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}