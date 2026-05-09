package controllers;

import database.HotelDatabase;
import enums.PaymentMethod;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.*;

import utils.SceneNavigator;
import utils.SessionData;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class RoomBrowserController {

    @FXML
    private TableView<Room> roomTable;

    @FXML
    private TableColumn<Room, Integer> roomNumberColumn;

    @FXML
    private TableColumn<Room, String> typeColumn;

    @FXML
    private TableColumn<Room, Double> priceColumn;

    @FXML
    private TableColumn<Room, Integer> capacityColumn;

    @FXML
    private TableColumn<Room, String> amenitiesColumn;

    @FXML
    private ComboBox<String> typeFilterBox;

    @FXML
    private TextField maxPriceField;

    @FXML
    private TextField amenityField;

    @FXML
    private DatePicker checkInPicker;

    @FXML
    private DatePicker checkOutPicker;

    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        setupTable();
        setupFilters();
        loadRooms();
    }

    private void setupTable() {
        roomNumberColumn.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().getRoomNumber()));

        typeColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getRoomType().getTypeName()));

        priceColumn.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().getRoomType().getPricePerNight()));

        capacityColumn.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().getRoomType().getCapacity()));

        amenitiesColumn.setCellValueFactory(data ->
                new SimpleStringProperty(getAmenitiesText(data.getValue())));
    }

    private void setupFilters() {
        typeFilterBox.getItems().add("All");

        for (RoomType roomType : HotelDatabase.roomTypes) {
            typeFilterBox.getItems().add(roomType.getTypeName());
        }

        typeFilterBox.setValue("All");
    }

    @FXML
    private void loadRooms() {
        String selectedType = typeFilterBox.getValue();
        String amenityText = amenityField.getText() == null ? "" : amenityField.getText().trim().toLowerCase();

        double maxPrice = -1;

        try {
            if (maxPriceField.getText() != null && !maxPriceField.getText().trim().isEmpty()) {
                maxPrice = Double.parseDouble(maxPriceField.getText().trim());
            }
        } catch (NumberFormatException e) {
            showError("Max price must be a valid number.");
            return;
        }

        roomTable.setItems(FXCollections.observableArrayList());

        for (Room room : HotelDatabase.rooms) {
            if (!room.isAvailable()) {
                continue;
            }

            if (selectedType != null && !selectedType.equals("All")
                    && !room.getRoomType().getTypeName().equals(selectedType)) {
                continue;
            }

            if (maxPrice >= 0 && room.getRoomType().getPricePerNight() > maxPrice) {
                continue;
            }

            if (!amenityText.isEmpty()
                    && !getAmenitiesText(room).toLowerCase().contains(amenityText)) {
                continue;
            }

            roomTable.getItems().add(room);
        }

        statusLabel.setText("Available rooms loaded.");
    }

    @FXML
    private void makeReservation() {
        try {
            Guest guest = SessionData.currentGuest;

            if (guest == null) {
                SceneNavigator.switchTo("login.fxml");
                return;
            }

            Room selectedRoom = roomTable.getSelectionModel().getSelectedItem();

            if (selectedRoom == null) {
                showError("Please select a room first.");
                return;
            }

            LocalDate checkIn = checkInPicker.getValue();
            LocalDate checkOut = checkOutPicker.getValue();

            Reservation reservation = guest.makeReservation(
                    generateReservationId(),
                    selectedRoom,
                    checkIn,
                    checkOut
            );

            createInvoiceForReservation(reservation);

            showInfo("Reservation created successfully.");
            loadRooms();

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void createInvoiceForReservation(Reservation reservation) {
        double totalAmount = calculateTotalAmount(reservation);

        Invoice invoice = new Invoice(
                generateInvoiceId(),
                reservation,
                totalAmount,
                PaymentMethod.CASH,
                LocalDate.now()
        );

        HotelDatabase.invoices.add(invoice);
    }

    private double calculateTotalAmount(Reservation reservation) {
        long nights = ChronoUnit.DAYS.between(
                reservation.getCheckInDate(),
                reservation.getCheckOutDate()
        );

        return nights * reservation.getRoom().getRoomType().getPricePerNight();
    }

    private int generateReservationId() {
        int max = 0;

        for (Reservation reservation : HotelDatabase.reservations) {
            if (reservation.getReservationId() > max) {
                max = reservation.getReservationId();
            }
        }

        return max + 1;
    }

    private int generateInvoiceId() {
        int max = 0;

        for (Invoice invoice : HotelDatabase.invoices) {
            if (invoice.getInvoiceId() > max) {
                max = invoice.getInvoiceId();
            }
        }

        return max + 1;
    }

    private String getAmenitiesText(Room room) {
        StringBuilder text = new StringBuilder();

        for (Amenity amenity : room.getAmenities()) {
            text.append(amenity.getName()).append(" ");
        }

        return text.toString().trim();
    }

    @FXML
    private void goBack() {
        SceneNavigator.switchTo("guest_dashboard.fxml");
    }

    private void showError(String message) {
        statusLabel.setText(message);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Room Browser Error");
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