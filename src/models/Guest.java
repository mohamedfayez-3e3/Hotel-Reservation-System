package models;

import java.time.LocalDate;

import database.HotelDatabase;
import enums.Gender;
import enums.PaymentMethod;
import enums.ReservationStatus;
import exceptions.InvalidDataException;
import exceptions.InvalidPaymentException;
import exceptions.RoomNotAvailableException;
import interfaces.Payable;

public class Guest implements Payable {
    private String username;
    private String password;
    private LocalDate dateOfBirth;
    private double balance;
    private String address;
    private Gender gender;
    private String roomPreferences;

    public Guest() {
    }

    public Guest(String username, String password, LocalDate dateOfBirth,
                 double balance, String address, Gender gender, String roomPreferences) {
        setUsername(username);
        setPassword(password);
        setDateOfBirth(dateOfBirth);
        setBalance(balance);
        setAddress(address);
        setGender(gender);
        setRoomPreferences(roomPreferences);
    }

    // ================= GETTERS & SETTERS =================

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new InvalidDataException("Username cannot be empty.");
        }
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new InvalidDataException("Password cannot be empty.");
        }
        if (password.length() < 6) {
            throw new InvalidDataException("Password must be at least 6 characters.");
        }
        this.password = password;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            throw new InvalidDataException("Date of birth cannot be null.");
        }
        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw new InvalidDataException("Date of birth cannot be in the future.");
        }
        this.dateOfBirth = dateOfBirth;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        if (balance < 0) {
            throw new InvalidDataException("Balance cannot be negative.");
        }
        this.balance = balance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new InvalidDataException("Address cannot be empty.");
        }
        this.address = address;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        if (gender == null) {
            throw new InvalidDataException("Gender cannot be null.");
        }
        this.gender = gender;
    }

    public String getRoomPreferences() {
        return roomPreferences;
    }

    public void setRoomPreferences(String roomPreferences) {
        this.roomPreferences = roomPreferences;
    }

    // ================= FUNCTIONS =================

    public boolean login(String enteredUsername, String enteredPassword) {
        return this.username.equals(enteredUsername) && this.password.equals(enteredPassword);
    }

    public static Guest register(String username, String password, LocalDate dateOfBirth,
                                 double balance, String address, Gender gender, String roomPreferences) {

        if (HotelDatabase.findGuestByUsername(username) != null) {
            throw new InvalidDataException("Username already exists.");
        }

        Guest guest = new Guest(username, password, dateOfBirth, balance, address, gender, roomPreferences);
        HotelDatabase.guests.add(guest);

        return guest;
    }

    public void viewAvailableRooms() {
        System.out.println("Available Rooms:");
        for (Room room : HotelDatabase.rooms) {
            if (room.isAvailable()) {
                room.printRoomInfo();
                System.out.println("------------------");
            }
        }
    }

    public Reservation makeReservation(int reservationId, Room room,LocalDate checkInDate, LocalDate checkOutDate) 
    {

        if (room == null) {
            throw new InvalidDataException("Room cannot be null.");
        }

        if (checkInDate == null || checkOutDate == null) {
            throw new InvalidDataException("Check-in and check-out dates cannot be null.");
        }

        if (checkInDate.isBefore(LocalDate.now())) {
            throw new InvalidDataException("Check-in date cannot be in the past.");
        }

        if (!checkOutDate.isAfter(checkInDate)) {
            throw new InvalidDataException("Check-out date must be after check-in date.");
        }

        if (HotelDatabase.findReservationById(reservationId) != null) {
            throw new InvalidDataException("Reservation ID already exists.");
        }

        if (!room.isAvailable()) {
            throw new RoomNotAvailableException("Room is not available.");
        }

        Reservation reservation = new Reservation(
                reservationId,
                this,
                room,
                checkInDate,
                checkOutDate,
                ReservationStatus.CONFIRMED
        );

        room.bookRoom();
        HotelDatabase.reservations.add(reservation);

        return reservation;
    }

    public void viewReservations() {
        System.out.println("Reservations for guest: " + username);

        for (Reservation reservation : HotelDatabase.reservations) {
            if (reservation.getGuest().getUsername().equals(this.username)) {
                reservation.printReservationInfo();
                System.out.println("------------------");
            }
        }
    }

    public void cancelReservation(int reservationId) {
        for (Reservation reservation : HotelDatabase.reservations) {

            if (reservation.getReservationId() == reservationId &&
                reservation.getGuest().getUsername().equals(this.username)) {

                if (reservation.getStatus() == ReservationStatus.CANCELLED) {
                    throw new InvalidDataException("Reservation is already cancelled.");
                }

                if (reservation.getStatus() == ReservationStatus.COMPLETED) {
                    throw new InvalidDataException("Completed reservation cannot be cancelled.");
                }

                reservation.setStatus(ReservationStatus.CANCELLED);
                reservation.getRoom().releaseRoom();

                System.out.println("Reservation cancelled successfully.");
                return;
            }
        }

        throw new InvalidDataException("Reservation not found for this guest.");
    }

    // ================= PAYMENT =================

    public void payInvoice(Invoice invoice, PaymentMethod paymentMethod) {

        if (invoice == null) {
            throw new InvalidPaymentException("Invoice cannot be null.");
        }

        if (paymentMethod == null) {
            throw new InvalidPaymentException("Payment method cannot be null.");
        }

        if (invoice.getTotalAmount() > this.balance) {
            throw new InvalidPaymentException("Insufficient balance to pay invoice.");
        }

        this.balance -= invoice.getTotalAmount();
        invoice.setPaymentMethod(paymentMethod);
        invoice.setPaymentDate(LocalDate.now());

        System.out.println("Invoice paid successfully.");
        System.out.println("Remaining balance: " + this.balance);
    }

    public void checkout(int reservationId, PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            throw new InvalidPaymentException("Payment method cannot be null.");
        }

        for (Reservation reservation : HotelDatabase.reservations) {

            if (reservation.getReservationId() == reservationId &&
                reservation.getGuest().getUsername().equals(this.username)) {

                if (reservation.getStatus() == ReservationStatus.CANCELLED) {
                    throw new InvalidPaymentException("Cancelled reservation cannot be checked out.");
                }

                if (reservation.getStatus() == ReservationStatus.COMPLETED) {
                    throw new InvalidPaymentException("Reservation is already completed.");
                }

                if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
                    throw new InvalidPaymentException("Only confirmed reservations can be checked out.");
                }

                Invoice relatedInvoice = null;

                for (Invoice invoice : HotelDatabase.invoices) {
                    if (invoice.getReservation().getReservationId() == reservationId) {
                        relatedInvoice = invoice;
                        break;
                    }
                }

                if (relatedInvoice == null) {
                    throw new InvalidPaymentException("No invoice found for this reservation.");
                }

                payInvoice(relatedInvoice, paymentMethod);

                reservation.setStatus(ReservationStatus.COMPLETED);
                reservation.getRoom().releaseRoom();

                System.out.println("Checkout completed successfully.");
                return;
            }
        }

        throw new InvalidPaymentException("Reservation not found for this guest.");
    }

    // ================= PRINT =================

    public void printGuestInfo() {
        System.out.println("Guest Information:");
        System.out.println("Username: " + username);
        System.out.println("Date of Birth: " + dateOfBirth);
        System.out.println("Balance: " + balance);
        System.out.println("Address: " + address);
        System.out.println("Gender: " + gender);
        System.out.println("Room Preferences: " + roomPreferences);
    }
}