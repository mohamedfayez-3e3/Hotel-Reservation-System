package models;

import exceptions.InvalidDataException;

public class RoomType {
    private int typeId;
    private String typeName;
    private double pricePerNight;
    private int capacity;

    public RoomType() {
    }

    public RoomType(int typeId, String typeName, double pricePerNight, int capacity) {
        setTypeId(typeId);
        setTypeName(typeName);
        setPricePerNight(pricePerNight);
        setCapacity(capacity);
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        if (typeId <= 0) {
            throw new InvalidDataException("Room type ID must be greater than 0.");
        }
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        if (typeName == null || typeName.trim().isEmpty()) {
            throw new InvalidDataException("Room type name cannot be empty.");
        }
        this.typeName = typeName;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        if (pricePerNight < 0) {
            throw new InvalidDataException("Price per night cannot be negative.");
        }
        this.pricePerNight = pricePerNight;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        if (capacity <= 0) {
            throw new InvalidDataException("Capacity must be greater than 0.");
        }
        this.capacity = capacity;
    }

    public void printRoomTypeInfo() {
        System.out.println("Room Type Information:");
        System.out.println("Type ID: " + typeId);
        System.out.println("Type Name: " + typeName);
        System.out.println("Price Per Night: " + pricePerNight);
        System.out.println("Capacity: " + capacity);
    }
}