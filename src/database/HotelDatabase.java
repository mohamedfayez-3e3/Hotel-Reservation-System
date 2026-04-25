package database;

import java.time.LocalDate;
import java.util.ArrayList;

import enums.Gender;
import enums.PaymentMethod;
import enums.ReservationStatus;
import models.Admin;
import models.Amenity;
import models.Guest;
import models.Invoice;
import models.Receptionist;
import models.Reservation;
import models.Room;
import models.RoomType;

public class HotelDatabase {
    public static ArrayList<Guest> guests = new ArrayList<>();
    public static ArrayList<Room> rooms = new ArrayList<>();
    public static ArrayList<Reservation> reservations = new ArrayList<>();
    public static ArrayList<Invoice> invoices = new ArrayList<>();
    public static ArrayList<RoomType> roomTypes = new ArrayList<>();
    public static ArrayList<Amenity> amenities = new ArrayList<>();
    public static ArrayList<Admin> admins = new ArrayList<>();
    public static ArrayList<Receptionist> receptionists = new ArrayList<>();

    public static void initializeData() {
        clearAllData();

        RoomType single = new RoomType(1, "Single", 800.0, 1);
        RoomType doubleRoom = new RoomType(2, "Double", 1200.0, 2);

        roomTypes.add(single);
        roomTypes.add(doubleRoom);

        Amenity wifi = new Amenity(1, "WiFi", "High-speed internet", 50.0);
        Amenity tv = new Amenity(2, "TV", "Smart TV", 30.0);
        Amenity miniBar = new Amenity(3, "Mini Bar", "Snacks and drinks", 100.0);

        amenities.add(wifi);
        amenities.add(tv);
        amenities.add(miniBar);

        Room room1 = new Room(1, 101, false, single);
        room1.addAmenity(wifi);
        room1.addAmenity(tv);

        Room room2 = new Room(2, 102, true, doubleRoom);
        room2.addAmenity(wifi);
        room2.addAmenity(tv);
        room2.addAmenity(miniBar);

        rooms.add(room1);
        rooms.add(room2);

        Guest guest1 = new Guest(
                "ahmed123",
                "pass1234",
                LocalDate.of(2004, 5, 10),
                3000.0,
                "Cairo",
                Gender.MALE,
                "High floor"
        );

        guests.add(guest1);

        Admin admin1 = new Admin(
                "admin1",
                "adminpass",
                LocalDate.of(1988, 7, 20),
                8
        );

        Receptionist receptionist1 = new Receptionist(
                "rec1",
                "recpass",
                LocalDate.of(1995, 2, 10),
                6
        );

        admins.add(admin1);
        receptionists.add(receptionist1);

        Reservation reservation1 = new Reservation(
                1,
                guest1,
                room1,
                LocalDate.of(2026, 4, 25),
                LocalDate.of(2026, 4, 28),
                ReservationStatus.CONFIRMED
        );

        reservations.add(reservation1);

        Invoice invoice1 = new Invoice(
                1,
                reservation1,
                2400.0,
                PaymentMethod.CREDIT_CARD,
                LocalDate.of(2026, 4, 25)
        );

        invoices.add(invoice1);
    }

    public static void clearAllData() {
        guests.clear();
        rooms.clear();
        reservations.clear();
        invoices.clear();
        roomTypes.clear();
        amenities.clear();
        admins.clear();
        receptionists.clear();
    }

    public static Guest findGuestByUsername(String username) {
        for (Guest g : guests) {
            if (g.getUsername().equals(username)) {
                return g;
            }
        }
        return null;
    }

    public static Room findRoomByNumber(int roomNumber) {
        for (Room r : rooms) {
            if (r.getRoomNumber() == roomNumber) {
                return r;
            }
        }
        return null;
    }

    public static Reservation findReservationById(int reservationId) {
        for (Reservation r : reservations) {
            if (r.getReservationId() == reservationId) {
                return r;
            }
        }
        return null;
    }

    public static void printAllData() {
        System.out.println("===== GUESTS =====");
        for (Guest g : guests) {
            g.printGuestInfo();
            System.out.println("------------------");
        }

        System.out.println("===== ROOMS =====");
        for (Room r : rooms) {
            r.printRoomInfo();
            System.out.println("------------------");
        }

        System.out.println("===== RESERVATIONS =====");
        for (Reservation r : reservations) {
            r.printReservationInfo();
            System.out.println("------------------");
        }

        System.out.println("===== INVOICES =====");
        for (Invoice i : invoices) {
            i.printInvoiceInfo();
            System.out.println("------------------");
        }

        System.out.println("===== ADMINS =====");
        for (Admin a : admins) {
            a.printAdminInfo();
            System.out.println("------------------");
        }

        System.out.println("===== RECEPTIONISTS =====");
        for (Receptionist r : receptionists) {
            r.printReceptionistInfo();
            System.out.println("------------------");
        }
    }
}