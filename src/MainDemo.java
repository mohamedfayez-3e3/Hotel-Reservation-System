import java.time.LocalDate;

import database.HotelDatabase;
import enums.Gender;
import enums.PaymentMethod;
import models.Admin;
import models.Amenity;
import models.Guest;
import models.Receptionist;
import models.Reservation;
import models.Room;
import models.RoomType;

public class MainDemo {
    public static void main(String[] args) {
        try {
            HotelDatabase.initializeData();

            System.out.println("========== INITIAL DATA ==========");

            Guest existingGuest = HotelDatabase.findGuestByUsername("ahmed123");
            if (existingGuest == null) {
                System.out.println("Initial guest not found.");
                return;
            }

            System.out.println("\n--- EXISTING GUEST INFO ---");
            existingGuest.printGuestInfo();

            System.out.println("\n--- AVAILABLE ROOMS AT START ---");
            existingGuest.viewAvailableRooms();

            System.out.println("\n--- EXISTING GUEST RESERVATIONS ---");
            existingGuest.viewReservations();

            System.out.println("\n========== GUEST REGISTER ==========");
            Guest newGuest = Guest.register(
                    "mario123",
                    "pass1234",
                    LocalDate.of(2003, 8, 15),
                    5000.0,
                    "Nasr City",
                    Gender.MALE,
                    "Near elevator"
            );
            System.out.println("Guest registered successfully.");
            newGuest.printGuestInfo();

            System.out.println("\n========== GUEST LOGIN ==========");
            boolean loginSuccess = newGuest.login("mario123", "pass1234");
            System.out.println("Login success: " + loginSuccess);

            System.out.println("\n========== ADMIN OPERATIONS ==========");
            Admin admin = HotelDatabase.admins.get(0);

            RoomType suite = new RoomType(3, "Suite", 2000.0, 4);
            admin.addRoomType(suite);

            Amenity jacuzzi = new Amenity(4, "Jacuzzi", "Private Jacuzzi", 300.0);
            admin.addAmenity(jacuzzi);

            Room room103 = new Room(3, 103, true, suite);
            room103.addAmenity(jacuzzi);
            admin.addRoom(room103);

            System.out.println("\nAdmin added new room type, amenity, and room successfully.");

            System.out.println("\n========== NEW GUEST MAKES RESERVATION ==========");
            Room selectedRoom = HotelDatabase.findRoomByNumber(103);

            Reservation newReservation = newGuest.makeReservation(
                    2,
                    selectedRoom,
                    LocalDate.now().plusDays(1),
                    LocalDate.now().plusDays(4)
            );

            System.out.println("Reservation created successfully.");
            newReservation.printReservationInfo();

            System.out.println("\n--- NEW GUEST RESERVATIONS ---");
            newGuest.viewReservations();

            System.out.println("\n========== NEW GUEST CANCELS RESERVATION ==========");
            newGuest.cancelReservation(2);
            newGuest.viewReservations();

            System.out.println("\n========== RECEPTIONIST OPERATIONS ==========");
            Receptionist receptionist = HotelDatabase.receptionists.get(0);
            receptionist.manageReservations();

            System.out.println("\n========== CHECKOUT EXISTING RESERVATION ==========");
            receptionist.checkOutGuest(1, PaymentMethod.CREDIT_CARD);

            System.out.println("\n--- EXISTING GUEST INFO AFTER CHECKOUT ---");
            existingGuest.printGuestInfo();

            System.out.println("\n--- AVAILABLE ROOMS AFTER CHECKOUT ---");
            existingGuest.viewAvailableRooms();

            System.out.println("\n========== DEMO FINISHED SUCCESSFULLY ==========");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}