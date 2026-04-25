package models;

import java.time.LocalDate;

import database.HotelDatabase;
import enums.PaymentMethod;
import enums.ReservationStatus;
import enums.Role;
import exceptions.InvalidDataException;

public class Receptionist extends Staff {

    public Receptionist() {
    }

    public Receptionist(String username, String password, LocalDate dateOfBirth, int workingHours) {
        super(username, password, dateOfBirth, Role.RECEPTIONIST, workingHours);
    }

    public void checkInGuest(int reservationId) {
        Reservation reservation = HotelDatabase.findReservationById(reservationId);

        if (reservation == null) {
            throw new InvalidDataException("Reservation not found.");
        }

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new InvalidDataException("Cancelled reservation cannot be checked in.");
        }

        if (reservation.getStatus() == ReservationStatus.COMPLETED) {
            throw new InvalidDataException("Completed reservation cannot be checked in again.");
        }

        if (!reservation.getRoom().isAvailable()) {
            throw new InvalidDataException("Room is already occupied.");
        }

        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.getRoom().setAvailable(false);

        System.out.println("Guest checked in successfully.");
    }

    public void checkOutGuest(int reservationId, PaymentMethod paymentMethod) {
        Reservation reservation = HotelDatabase.findReservationById(reservationId);

        if (reservation == null) {
            throw new InvalidDataException("Reservation not found.");
        }

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new InvalidDataException("Cancelled reservation cannot be checked out.");
        }

        if (reservation.getStatus() == ReservationStatus.COMPLETED) {
            throw new InvalidDataException("Reservation is already completed.");
        }

        Guest guest = reservation.getGuest();
        guest.checkout(reservationId, paymentMethod);

        System.out.println("Guest checked out successfully.");
    }

    public void manageReservations() {
        System.out.println("All Reservations:");
        for (Reservation reservation : HotelDatabase.reservations) {
            reservation.printReservationInfo();
            System.out.println("------------------");
        }
    }

    public void printReceptionistInfo() {
        System.out.println("Receptionist Information:");
        System.out.println("Username: " + getUsername());
        System.out.println("Date of Birth: " + getDateOfBirth());
        System.out.println("Role: " + getRole());
        System.out.println("Working Hours: " + getWorkingHours());
    }
}