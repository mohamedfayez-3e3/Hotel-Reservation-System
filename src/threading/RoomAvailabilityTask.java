package threading;

import database.HotelDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import models.Amenity;
import models.Room;

public class RoomAvailabilityTask extends Task<ObservableList<Room>> {

    private String selectedType;
    private double maxPrice;
    private String amenityText;

    public RoomAvailabilityTask(String selectedType, double maxPrice, String amenityText) {
        this.selectedType = selectedType;
        this.maxPrice = maxPrice;
        this.amenityText = amenityText;
    }

    @Override
    protected ObservableList<Room> call() {
        ObservableList<Room> availableRooms = FXCollections.observableArrayList();

        for (Room room : HotelDatabase.rooms) {

            if (!room.isAvailable()) {
                continue;
            }

            if (selectedType != null
                    && !selectedType.equals("All")
                    && !room.getRoomType().getTypeName().equals(selectedType)) {
                continue;
            }

            if (maxPrice >= 0 && room.getRoomType().getPricePerNight() > maxPrice) {
                continue;
            }

            if (amenityText != null
                    && !amenityText.trim().isEmpty()
                    && !getAmenitiesText(room).toLowerCase().contains(amenityText.toLowerCase())) {
                continue;
            }

            availableRooms.add(room);
        }

        return availableRooms;
    }

    private String getAmenitiesText(Room room) {
        StringBuilder text = new StringBuilder();

        for (Amenity amenity : room.getAmenities()) {
            text.append(amenity.getName()).append(" ");
        }

        return text.toString().trim();
    }
}