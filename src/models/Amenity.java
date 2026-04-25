package models;

import exceptions.InvalidDataException;

public class Amenity {
    private int amenityId;
    private String name;
    private String description;
    private double extraCost;

    public Amenity() {
    }

    public Amenity(int amenityId, String name, String description, double extraCost) {
        setAmenityId(amenityId);
        setName(name);
        setDescription(description);
        setExtraCost(extraCost);
    }

    public int getAmenityId() {
        return amenityId;
    }

    public void setAmenityId(int amenityId) {
        if (amenityId <= 0) {
            throw new InvalidDataException("Amenity ID must be greater than 0.");
        }
        this.amenityId = amenityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidDataException("Amenity name cannot be empty.");
        }
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new InvalidDataException("Amenity description cannot be empty.");
        }
        this.description = description;
    }

    public double getExtraCost() {
        return extraCost;
    }

    public void setExtraCost(double extraCost) {
        if (extraCost < 0) {
            throw new InvalidDataException("Amenity extra cost cannot be negative.");
        }
        this.extraCost = extraCost;
    }

    public void printAmenityInfo() {
        System.out.println("Amenity Information:");
        System.out.println("ID: " + amenityId);
        System.out.println("Name: " + name);
        System.out.println("Description: " + description);
        System.out.println("Extra Cost: " + extraCost);
    }
}