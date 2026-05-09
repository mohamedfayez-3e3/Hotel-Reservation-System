package threading;

import database.HotelDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import models.Guest;
import models.Reservation;

public class ReservationLoadingTask extends Task<ObservableList<Reservation>> {

    private Guest guest;

    public ReservationLoadingTask(Guest guest) {
        this.guest = guest;
    }

    @Override
    protected ObservableList<Reservation> call() {
        ObservableList<Reservation> guestReservations = FXCollections.observableArrayList();

        if (guest == null) {
            return guestReservations;
        }

        for (Reservation reservation : HotelDatabase.reservations) {
            if (reservation.getGuest().getUsername().equals(guest.getUsername())) {
                guestReservations.add(reservation);
            }
        }

        return guestReservations;
    }
}