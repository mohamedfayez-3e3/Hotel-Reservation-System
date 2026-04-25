package models;

import java.util.ArrayList;
import exceptions.InvalidDataException;
import exceptions.RoomNotAvailableException;

public class Room {
    private int roomId;
    private int roomNumber;
    private boolean isAvailable;
    private RoomType roomType;
    private ArrayList<Amenity> amenities;

    public Room() {
        amenities = new ArrayList<>();
    }

    public Room(int roomId, int roomNumber, boolean isAvailable, RoomType roomType) {
        setRoomId(roomId);
        setRoomNumber(roomNumber);
        setAvailable(isAvailable);
        setRoomType(roomType);
        this.amenities = new ArrayList<>();
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        if (roomId <= 0) {
            throw new InvalidDataException("Room ID must be greater than 0.");
        }
        this.roomId = roomId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        if (roomNumber <= 0) {
            throw new InvalidDataException("Room number must be greater than 0.");
        }
        this.roomNumber = roomNumber;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        if (roomType == null) {
            throw new InvalidDataException("Room type cannot be null.");
        }
        this.roomType = roomType;
    }

    public ArrayList<Amenity> getAmenities() {
        return amenities;
    }

    public void addAmenity(Amenity amenity) {
        if (amenity == null) {
            throw new InvalidDataException("Amenity cannot be null.");
        }
        amenities.add(amenity);
    }

    public void bookRoom() {
        if (!isAvailable) {
            throw new RoomNotAvailableException("Room is not available for booking.");
        }
        isAvailable = false;
    }

    public void releaseRoom() {
        isAvailable = true;
    }

    public void printRoomInfo() {
        System.out.println("Room Information:");
        System.out.println("Room ID: " + roomId);
        System.out.println("Room Number: " + roomNumber);
        System.out.println("Available: " + isAvailable);
        System.out.println("Room Type: " + roomType.getTypeName());

        System.out.println("Amenities:");
        for (Amenity a : amenities) {
            System.out.println("- " + a.getName());
        }
    }
}