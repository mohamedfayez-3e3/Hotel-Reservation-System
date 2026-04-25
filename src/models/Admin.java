package models;

import java.time.LocalDate;

import database.HotelDatabase;
import enums.Role;
import exceptions.InvalidDataException;

public class Admin extends Staff {

    public Admin() {
    }

    public Admin(String username, String password, LocalDate dateOfBirth, int workingHours) {
        super(username, password, dateOfBirth, Role.ADMIN, workingHours);
    }

    // ================= ROOM CRUD =================

    public void addRoom(Room room) {
        if (room == null) {
            throw new InvalidDataException("Room cannot be null.");
        }

        if (HotelDatabase.findRoomByNumber(room.getRoomNumber()) != null) {
            throw new InvalidDataException("Room number already exists.");
        }

        HotelDatabase.rooms.add(room);
        System.out.println("Room added successfully.");
    }

    public void updateRoom(int roomNumber, RoomType newRoomType, boolean newAvailability) {
        Room room = HotelDatabase.findRoomByNumber(roomNumber);

        if (room == null) {
            throw new InvalidDataException("Room not found.");
        }

        if (newRoomType == null) {
            throw new InvalidDataException("New room type cannot be null.");
        }

        room.setRoomType(newRoomType);
        room.setAvailable(newAvailability);

        System.out.println("Room updated successfully.");
    }

    public void deleteRoom(int roomNumber) {
        Room room = HotelDatabase.findRoomByNumber(roomNumber);

        if (room == null) {
            throw new InvalidDataException("Room not found.");
        }

        HotelDatabase.rooms.remove(room);
        System.out.println("Room deleted successfully.");
    }

    // ================= ROOM TYPE CRUD =================

    public void addRoomType(RoomType roomType) {
        if (roomType == null) {
            throw new InvalidDataException("Room type cannot be null.");
        }

        HotelDatabase.roomTypes.add(roomType);
        System.out.println("Room type added successfully.");
    }

    public void updateRoomType(int typeId, String newName, double newPrice, int newCapacity) {
        for (RoomType roomType : HotelDatabase.roomTypes) {
            if (roomType.getTypeId() == typeId) {
                roomType.setTypeName(newName);
                roomType.setPricePerNight(newPrice);
                roomType.setCapacity(newCapacity);
                System.out.println("Room type updated successfully.");
                return;
            }
        }

        throw new InvalidDataException("Room type not found.");
    }

    public void deleteRoomType(int typeId) {
        for (RoomType roomType : HotelDatabase.roomTypes) {
            if (roomType.getTypeId() == typeId) {
                HotelDatabase.roomTypes.remove(roomType);
                System.out.println("Room type deleted successfully.");
                return;
            }
        }

        throw new InvalidDataException("Room type not found.");
    }

    // ================= AMENITY CRUD =================

    public void addAmenity(Amenity amenity) {
        if (amenity == null) {
            throw new InvalidDataException("Amenity cannot be null.");
        }

        HotelDatabase.amenities.add(amenity);
        System.out.println("Amenity added successfully.");
    }

    public void updateAmenity(int amenityId, String newName, String newDescription, double newExtraCost) {
        for (Amenity amenity : HotelDatabase.amenities) {
            if (amenity.getAmenityId() == amenityId) {
                amenity.setName(newName);
                amenity.setDescription(newDescription);
                amenity.setExtraCost(newExtraCost);
                System.out.println("Amenity updated successfully.");
                return;
            }
        }

        throw new InvalidDataException("Amenity not found.");
    }

    public void deleteAmenity(int amenityId) {
        for (Amenity amenity : HotelDatabase.amenities) {
            if (amenity.getAmenityId() == amenityId) {
                HotelDatabase.amenities.remove(amenity);
                System.out.println("Amenity deleted successfully.");
                return;
            }
        }

        throw new InvalidDataException("Amenity not found.");
    }

    // ================= VIEW METHODS =================

    public void viewAllReservations() {
        System.out.println("All Reservations:");

        for (Reservation reservation : HotelDatabase.reservations) {
            reservation.printReservationInfo();
            System.out.println("------------------");
        }
    }

    public void viewAllGuests() {
        System.out.println("All Guests:");

        for (Guest guest : HotelDatabase.guests) {
            guest.printGuestInfo();
            System.out.println("------------------");
        }
    }

    public void viewAllRooms() {
        System.out.println("All Rooms:");

        for (Room room : HotelDatabase.rooms) {
            room.printRoomInfo();
            System.out.println("------------------");
        }
    }

    public void viewAllRoomTypes() {
        System.out.println("All Room Types:");

        for (RoomType roomType : HotelDatabase.roomTypes) {
            roomType.printRoomTypeInfo();
            System.out.println("------------------");
        }
    }

    public void viewAllAmenities() {
        System.out.println("All Amenities:");

        for (Amenity amenity : HotelDatabase.amenities) {
            amenity.printAmenityInfo();
            System.out.println("------------------");
        }
    }

    public void printAdminInfo() {
        System.out.println("Admin Information:");
        System.out.println("Username: " + getUsername());
        System.out.println("Date of Birth: " + getDateOfBirth());
        System.out.println("Role: " + getRole());
        System.out.println("Working Hours: " + getWorkingHours());
    }
}