package controllers;

import database.HotelDatabase;
import enums.PaymentMethod;
import enums.ReservationStatus;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Guest;
import models.Invoice;
import models.Reservation;
import utils.SceneNavigator;
import utils.SessionData;

public class CheckoutController {

    @FXML
    private TableView<Reservation> reservationTable;

    @FXML
    private TableColumn<Reservation, Integer> idColumn;

    @FXML
    private TableColumn<Reservation, Integer> roomColumn;

    @FXML
    private TableColumn<Reservation, String> datesColumn;

    @FXML
    private TableColumn<Reservation, String> statusColumn;

    @FXML
    private ComboBox<PaymentMethod> paymentMethodBox;

    @FXML
    private TextArea invoiceArea;

    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        setupTable();
        paymentMethodBox.getItems().addAll(
                PaymentMethod.CASH,
                PaymentMethod.CREDIT_CARD,
                PaymentMethod.ONLINE
        );
        paymentMethodBox.setValue(PaymentMethod.CASH);

        loadConfirmedReservations();
    }

    private void setupTable() {
        idColumn.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().getReservationId()));

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

    private void loadConfirmedReservations() {
        Guest guest = SessionData.currentGuest;

        if (guest == null) {
            SceneNavigator.switchTo("login.fxml");
            return;
        }

        reservationTable.setItems(FXCollections.observableArrayList());

        for (Reservation reservation : HotelDatabase.reservations) {
            if (reservation.getGuest().getUsername().equals(guest.getUsername())
                    && reservation.getStatus() == ReservationStatus.CONFIRMED) {
                reservationTable.getItems().add(reservation);
            }
        }

        statusLabel.setText("Confirmed reservations loaded.");
    }

    @FXML
    private void showInvoice() {
        Reservation selected = reservationTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Please select a reservation first.");
            return;
        }

        Invoice invoice = findInvoiceByReservationId(selected.getReservationId());

        if (invoice == null) {
            showError("No invoice found for this reservation.");
            return;
        }

        invoiceArea.setText(
                "Invoice ID: " + invoice.getInvoiceId() + "\n"
                        + "Reservation ID: " + selected.getReservationId() + "\n"
                        + "Guest: " + selected.getGuest().getUsername() + "\n"
                        + "Room Number: " + selected.getRoom().getRoomNumber() + "\n"
                        + "Total Amount: " + invoice.getTotalAmount() + "\n"
                        + "Selected Payment Method: " + paymentMethodBox.getValue()
        );
    }

    @FXML
    private void confirmPayment() {
        try {
            Guest guest = SessionData.currentGuest;
            Reservation selected = reservationTable.getSelectionModel().getSelectedItem();

            if (selected == null) {
                showError("Please select a reservation first.");
                return;
            }

            PaymentMethod method = paymentMethodBox.getValue();

            guest.checkout(selected.getReservationId(), method);

            showInfo("Checkout completed successfully.");
            invoiceArea.clear();
            loadConfirmedReservations();

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private Invoice findInvoiceByReservationId(int reservationId) {
        for (Invoice invoice : HotelDatabase.invoices) {
            if (invoice.getReservation().getReservationId() == reservationId) {
                return invoice;
            }
        }

        return null;
    }

    @FXML
    private void goBack() {
        SceneNavigator.switchTo("guest_dashboard.fxml");
    }

    private void showError(String message) {
        statusLabel.setText(message);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Checkout Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        statusLabel.setText(message);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Checkout");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}