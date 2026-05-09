package controllers;

import database.DatabaseManager;
import database.HotelDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Amenity;
import models.Guest;
import models.Reservation;
import models.Room;
import models.RoomType;
import utils.SceneNavigator;
import utils.SessionData;

public class AdminDashboardController {

    @FXML
    private TextArea outputArea;

    @FXML
    private Label statusLabel;

    @FXML
    private TextField roomNumberField;

    @FXML
    private ComboBox<String> roomTypeBox;

    @FXML
    private CheckBox availableCheckBox;

    @FXML
    public void initialize() {
        if (SessionData.currentAdmin == null) {
            SceneNavigator.switchTo("login.fxml");
            return;
        }

        setupRoomTypeBox();

        statusLabel.setText("Welcome, " + SessionData.currentAdmin.getUsername());
        viewGuests();
    }

    private void setupRoomTypeBox() {
        roomTypeBox.getItems().clear();

        for (RoomType roomType : HotelDatabase.roomTypes) {
            roomTypeBox.getItems().add(roomType.getTypeName());
        }

        if (!roomTypeBox.getItems().isEmpty()) {
            roomTypeBox.setValue(roomTypeBox.getItems().get(0));
        }
    }

    @FXML
    private void addRoom() {
        try {
            String roomNumberText = roomNumberField.getText().trim();

            if (roomNumberText.isEmpty()) {
                showError("Please enter room number.");
                return;
            }

            int roomNumber = Integer.parseInt(roomNumberText);

            if (HotelDatabase.findRoomByNumber(roomNumber) != null) {
                showError("Room number already exists.");
                return;
            }

            String selectedTypeName = roomTypeBox.getValue();

            if (selectedTypeName == null) {
                showError("Please select room type.");
                return;
            }

            RoomType selectedRoomType = findRoomTypeByName(selectedTypeName);

            if (selectedRoomType == null) {
                showError("Room type not found.");
                return;
            }

            Room newRoom = new Room(
                    generateRoomId(),
                    roomNumber,
                    availableCheckBox.isSelected(),
                    selectedRoomType
            );

            SessionData.currentAdmin.addRoom(newRoom);

            DatabaseManager.saveAllData();

            roomNumberField.clear();
            availableCheckBox.setSelected(true);

            showInfo("Room added successfully and saved to database.");
            viewRooms();

        } catch (NumberFormatException e) {
            showError("Room number must be a valid number.");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private int generateRoomId() {
        int max = 0;

        for (Room room : HotelDatabase.rooms) {
            if (room.getRoomId() > max) {
                max = room.getRoomId();
            }
        }

        return max + 1;
    }

    private RoomType findRoomTypeByName(String typeName) {
        for (RoomType roomType : HotelDatabase.roomTypes) {
            if (roomType.getTypeName().equals(typeName)) {
                return roomType;
            }
        }

        return null;
    }

    @FXML
    private void viewGuests() {
        StringBuilder text = new StringBuilder();

        text.append("===== ALL GUESTS =====\n\n");

        for (Guest guest : HotelDatabase.guests) {
            text.append("Username: ").append(guest.getUsername()).append("\n");
            text.append("Date of Birth: ").append(guest.getDateOfBirth()).append("\n");
            text.append("Balance: ").append(guest.getBalance()).append("\n");
            text.append("Address: ").append(guest.getAddress()).append("\n");
            text.append("Gender: ").append(guest.getGender()).append("\n");
            text.append("Room Preferences: ").append(guest.getRoomPreferences()).append("\n");
            text.append("-----------------------------\n");
        }

        outputArea.setText(text.toString());
        statusLabel.setText("Guests loaded.");
    }

    @FXML
    private void viewRooms() {
        StringBuilder text = new StringBuilder();

        text.append("===== ALL ROOMS =====\n\n");

        for (Room room : HotelDatabase.rooms) {
            text.append("Room ID: ").append(room.getRoomId()).append("\n");
            text.append("Room Number: ").append(room.getRoomNumber()).append("\n");
            text.append("Available: ").append(room.isAvailable()).append("\n");
            text.append("Room Type: ").append(room.getRoomType().getTypeName()).append("\n");
            text.append("Price Per Night: ").append(room.getRoomType().getPricePerNight()).append("\n");
            text.append("Capacity: ").append(room.getRoomType().getCapacity()).append("\n");

            text.append("Amenities: ");
            for (Amenity amenity : room.getAmenities()) {
                text.append(amenity.getName()).append(" ");
            }

            text.append("\n-----------------------------\n");
        }

        outputArea.setText(text.toString());
        statusLabel.setText("Rooms loaded.");
    }

    @FXML
    private void viewReservations() {
        StringBuilder text = new StringBuilder();

        text.append("===== ALL RESERVATIONS =====\n\n");

        for (Reservation reservation : HotelDatabase.reservations) {
            text.append("Reservation ID: ").append(reservation.getReservationId()).append("\n");
            text.append("Guest Username: ").append(reservation.getGuest().getUsername()).append("\n");
            text.append("Room Number: ").append(reservation.getRoom().getRoomNumber()).append("\n");
            text.append("Check-in Date: ").append(reservation.getCheckInDate()).append("\n");
            text.append("Check-out Date: ").append(reservation.getCheckOutDate()).append("\n");
            text.append("Status: ").append(reservation.getStatus()).append("\n");
            text.append("-----------------------------\n");
        }

        outputArea.setText(text.toString());
        statusLabel.setText("Reservations loaded.");
    }

    @FXML
    private void viewRoomTypes() {
        StringBuilder text = new StringBuilder();

        text.append("===== ALL ROOM TYPES =====\n\n");

        for (RoomType roomType : HotelDatabase.roomTypes) {
            text.append("Type ID: ").append(roomType.getTypeId()).append("\n");
            text.append("Type Name: ").append(roomType.getTypeName()).append("\n");
            text.append("Price Per Night: ").append(roomType.getPricePerNight()).append("\n");
            text.append("Capacity: ").append(roomType.getCapacity()).append("\n");
            text.append("-----------------------------\n");
        }

        outputArea.setText(text.toString());
        statusLabel.setText("Room types loaded.");
    }

    @FXML
    private void viewAmenities() {
        StringBuilder text = new StringBuilder();

        text.append("===== ALL AMENITIES =====\n\n");

        for (Amenity amenity : HotelDatabase.amenities) {
            text.append("Amenity ID: ").append(amenity.getAmenityId()).append("\n");
            text.append("Name: ").append(amenity.getName()).append("\n");
            text.append("Description: ").append(amenity.getDescription()).append("\n");
            text.append("Extra Cost: ").append(amenity.getExtraCost()).append("\n");
            text.append("-----------------------------\n");
        }

        outputArea.setText(text.toString());
        statusLabel.setText("Amenities loaded.");
    }

    @FXML
    private void logout() {
        SessionData.clearSession();
        SceneNavigator.switchTo("login.fxml");
    }

    private void showError(String message) {
        statusLabel.setText(message);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Admin Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        statusLabel.setText(message);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Admin");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}