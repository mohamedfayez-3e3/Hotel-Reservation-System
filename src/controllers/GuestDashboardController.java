package controllers;

import database.HotelDatabase;
import enums.ReservationStatus;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import models.Guest;
import models.Reservation;
import utils.SceneNavigator;
import utils.SessionData;

public class GuestDashboardController {

    @FXML
    private Label usernameLabel;

    @FXML
    private Label balanceLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label preferencesLabel;

    @FXML
    private ListView<String> activeReservationsList;

    @FXML
    public void initialize() {
        loadGuestData();
    }

    private void loadGuestData() {
        Guest guest = SessionData.currentGuest;

        if (guest == null) {
            SceneNavigator.switchTo("login.fxml");
            return;
        }

        usernameLabel.setText(guest.getUsername());
        balanceLabel.setText(String.valueOf(guest.getBalance()));
        addressLabel.setText(guest.getAddress());
        preferencesLabel.setText(guest.getRoomPreferences());

        activeReservationsList.getItems().clear();

        for (Reservation reservation : HotelDatabase.reservations) {
            if (reservation.getGuest().getUsername().equals(guest.getUsername())
                    && reservation.getStatus() == ReservationStatus.CONFIRMED) {

                activeReservationsList.getItems().add(
                        "Reservation #" + reservation.getReservationId()
                                + " | Room " + reservation.getRoom().getRoomNumber()
                                + " | " + reservation.getCheckInDate()
                                + " to " + reservation.getCheckOutDate()
                );
            }
        }
    }

    @FXML
    private void openRoomBrowser() {
        SceneNavigator.switchTo("room_browser.fxml");
    }

    @FXML
    private void openReservations() {
        SceneNavigator.switchTo("reservations.fxml");
    }

    @FXML
    private void openCheckout() {
        SceneNavigator.switchTo("checkout.fxml");
    }

    @FXML
    private void logout() {
        SessionData.clearSession();
        SceneNavigator.switchTo("login.fxml");
    }
    @FXML
    private void openChat() {
        SceneNavigator.switchTo("chat.fxml");
    }
}