package models;

import java.time.LocalDate;
import enums.ReservationStatus;
import exceptions.InvalidDataException;

public class Reservation {
    private int reservationId;
    private Guest guest;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private ReservationStatus status;

    public Reservation() {
    }

    public Reservation(int reservationId, Guest guest, Room room,
                       LocalDate checkInDate, LocalDate checkOutDate,
                       ReservationStatus status) {
        setReservationId(reservationId);
        setGuest(guest);
        setRoom(room);
        setCheckInDate(checkInDate);
        setCheckOutDate(checkOutDate);
        setStatus(status);
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        if (reservationId <= 0) {
            throw new InvalidDataException("Reservation ID must be greater than 0.");
        }
        this.reservationId = reservationId;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        if (guest == null) {
            throw new InvalidDataException("Guest cannot be null.");
        }
        this.guest = guest;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        if (room == null) {
            throw new InvalidDataException("Room cannot be null.");
        }
        this.room = room;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        if (checkInDate == null) {
            throw new InvalidDataException("Check-in date cannot be null.");
        }
        this.checkInDate = checkInDate;

        if (this.checkOutDate != null && !this.checkOutDate.isAfter(this.checkInDate)) {
            throw new InvalidDataException("Check-out date must be after check-in date.");
        }
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        if (checkOutDate == null) {
            throw new InvalidDataException("Check-out date cannot be null.");
        }
        this.checkOutDate = checkOutDate;

        if (this.checkInDate != null && !this.checkOutDate.isAfter(this.checkInDate)) {
            throw new InvalidDataException("Check-out date must be after check-in date.");
        }
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        if (status == null) {
            throw new InvalidDataException("Reservation status cannot be null.");
        }
        this.status = status;
    }

    public void printReservationInfo() {
        System.out.println("Reservation Information:");
        System.out.println("Reservation ID: " + reservationId);
        System.out.println("Guest Username: " + guest.getUsername());
        System.out.println("Room Number: " + room.getRoomNumber());
        System.out.println("Check-in Date: " + checkInDate);
        System.out.println("Check-out Date: " + checkOutDate);
        System.out.println("Status: " + status);
    }
}