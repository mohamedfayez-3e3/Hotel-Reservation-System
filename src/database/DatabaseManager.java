package database;

import enums.Gender;
import enums.PaymentMethod;
import enums.ReservationStatus;
import models.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:hotelsystem.db";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            createTables();

            if (isDatabaseEmpty()) {
                HotelDatabase.initializeData();
                saveAllData();
            } else {
                loadAllData();
            }

        } catch (Exception e) {
            throw new RuntimeException("Database initialization error: " + e.getMessage());
        }
    }

    private static void createTables() {
        String createGuests = """
                CREATE TABLE IF NOT EXISTS guests (
                    username TEXT PRIMARY KEY,
                    password TEXT NOT NULL,
                    date_of_birth TEXT NOT NULL,
                    balance REAL NOT NULL,
                    address TEXT NOT NULL,
                    gender TEXT NOT NULL,
                    room_preferences TEXT
                )
                """;

        String createAdmins = """
                CREATE TABLE IF NOT EXISTS admins (
                    username TEXT PRIMARY KEY,
                    password TEXT NOT NULL,
                    date_of_birth TEXT NOT NULL,
                    working_hours INTEGER NOT NULL
                )
                """;

        String createReceptionists = """
                CREATE TABLE IF NOT EXISTS receptionists (
                    username TEXT PRIMARY KEY,
                    password TEXT NOT NULL,
                    date_of_birth TEXT NOT NULL,
                    working_hours INTEGER NOT NULL
                )
                """;

        String createRoomTypes = """
                CREATE TABLE IF NOT EXISTS room_types (
                    type_id INTEGER PRIMARY KEY,
                    type_name TEXT NOT NULL,
                    price_per_night REAL NOT NULL,
                    capacity INTEGER NOT NULL
                )
                """;

        String createAmenities = """
                CREATE TABLE IF NOT EXISTS amenities (
                    amenity_id INTEGER PRIMARY KEY,
                    name TEXT NOT NULL,
                    description TEXT NOT NULL,
                    extra_cost REAL NOT NULL
                )
                """;

        String createRooms = """
                CREATE TABLE IF NOT EXISTS rooms (
                    room_id INTEGER PRIMARY KEY,
                    room_number INTEGER UNIQUE NOT NULL,
                    is_available INTEGER NOT NULL,
                    type_id INTEGER NOT NULL
                )
                """;

        String createRoomAmenities = """
                CREATE TABLE IF NOT EXISTS room_amenities (
                    room_id INTEGER NOT NULL,
                    amenity_id INTEGER NOT NULL
                )
                """;

        String createReservations = """
                CREATE TABLE IF NOT EXISTS reservations (
                    reservation_id INTEGER PRIMARY KEY,
                    guest_username TEXT NOT NULL,
                    room_id INTEGER NOT NULL,
                    check_in_date TEXT NOT NULL,
                    check_out_date TEXT NOT NULL,
                    status TEXT NOT NULL
                )
                """;

        String createInvoices = """
                CREATE TABLE IF NOT EXISTS invoices (
                    invoice_id INTEGER PRIMARY KEY,
                    reservation_id INTEGER NOT NULL,
                    total_amount REAL NOT NULL,
                    payment_method TEXT NOT NULL,
                    payment_date TEXT NOT NULL
                )
                """;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(createGuests);
            statement.execute(createAdmins);
            statement.execute(createReceptionists);
            statement.execute(createRoomTypes);
            statement.execute(createAmenities);
            statement.execute(createRooms);
            statement.execute(createRoomAmenities);
            statement.execute(createReservations);
            statement.execute(createInvoices);

        } catch (SQLException e) {
            throw new RuntimeException("Create tables error: " + e.getMessage());
        }
    }

    private static boolean isDatabaseEmpty() {
        String sql = "SELECT COUNT(*) FROM rooms";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            if (resultSet.next()) {
                return resultSet.getInt(1) == 0;
            }

            return true;

        } catch (SQLException e) {
            throw new RuntimeException("Check database error: " + e.getMessage());
        }
    }

    public static void saveAllData() {
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);

            clearTables(connection);

            saveRoomTypes(connection);
            saveAmenities(connection);
            saveGuests(connection);
            saveAdmins(connection);
            saveReceptionists(connection);
            saveRooms(connection);
            saveRoomAmenities(connection);
            saveReservations(connection);
            saveInvoices(connection);

            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException("Save database error: " + e.getMessage());
        }
    }

    private static void clearTables(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM invoices");
            statement.executeUpdate("DELETE FROM reservations");
            statement.executeUpdate("DELETE FROM room_amenities");
            statement.executeUpdate("DELETE FROM rooms");
            statement.executeUpdate("DELETE FROM amenities");
            statement.executeUpdate("DELETE FROM room_types");
            statement.executeUpdate("DELETE FROM guests");
            statement.executeUpdate("DELETE FROM admins");
            statement.executeUpdate("DELETE FROM receptionists");
        }
    }

    private static void saveGuests(Connection connection) throws SQLException {
        String sql = """
                INSERT INTO guests 
                (username, password, date_of_birth, balance, address, gender, room_preferences)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (Guest guest : HotelDatabase.guests) {
                ps.setString(1, guest.getUsername());
                ps.setString(2, guest.getPassword());
                ps.setString(3, guest.getDateOfBirth().toString());
                ps.setDouble(4, guest.getBalance());
                ps.setString(5, guest.getAddress());
                ps.setString(6, guest.getGender().toString());
                ps.setString(7, guest.getRoomPreferences());
                ps.executeUpdate();
            }
        }
    }

    private static void saveAdmins(Connection connection) throws SQLException {
        String sql = """
                INSERT INTO admins 
                (username, password, date_of_birth, working_hours)
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (Admin admin : HotelDatabase.admins) {
                ps.setString(1, admin.getUsername());
                ps.setString(2, admin.getPassword());
                ps.setString(3, admin.getDateOfBirth().toString());
                ps.setInt(4, admin.getWorkingHours());
                ps.executeUpdate();
            }
        }
    }

    private static void saveReceptionists(Connection connection) throws SQLException {
        String sql = """
                INSERT INTO receptionists 
                (username, password, date_of_birth, working_hours)
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (Receptionist receptionist : HotelDatabase.receptionists) {
                ps.setString(1, receptionist.getUsername());
                ps.setString(2, receptionist.getPassword());
                ps.setString(3, receptionist.getDateOfBirth().toString());
                ps.setInt(4, receptionist.getWorkingHours());
                ps.executeUpdate();
            }
        }
    }

    private static void saveRoomTypes(Connection connection) throws SQLException {
        String sql = """
                INSERT INTO room_types
                (type_id, type_name, price_per_night, capacity)
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (RoomType roomType : HotelDatabase.roomTypes) {
                ps.setInt(1, roomType.getTypeId());
                ps.setString(2, roomType.getTypeName());
                ps.setDouble(3, roomType.getPricePerNight());
                ps.setInt(4, roomType.getCapacity());
                ps.executeUpdate();
            }
        }
    }

    private static void saveAmenities(Connection connection) throws SQLException {
        String sql = """
                INSERT INTO amenities
                (amenity_id, name, description, extra_cost)
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (Amenity amenity : HotelDatabase.amenities) {
                ps.setInt(1, amenity.getAmenityId());
                ps.setString(2, amenity.getName());
                ps.setString(3, amenity.getDescription());
                ps.setDouble(4, amenity.getExtraCost());
                ps.executeUpdate();
            }
        }
    }

    private static void saveRooms(Connection connection) throws SQLException {
        String sql = """
                INSERT INTO rooms
                (room_id, room_number, is_available, type_id)
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (Room room : HotelDatabase.rooms) {
                ps.setInt(1, room.getRoomId());
                ps.setInt(2, room.getRoomNumber());
                ps.setInt(3, room.isAvailable() ? 1 : 0);
                ps.setInt(4, room.getRoomType().getTypeId());
                ps.executeUpdate();
            }
        }
    }

    private static void saveRoomAmenities(Connection connection) throws SQLException {
        String sql = """
                INSERT INTO room_amenities
                (room_id, amenity_id)
                VALUES (?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (Room room : HotelDatabase.rooms) {
                for (Amenity amenity : room.getAmenities()) {
                    ps.setInt(1, room.getRoomId());
                    ps.setInt(2, amenity.getAmenityId());
                    ps.executeUpdate();
                }
            }
        }
    }

    private static void saveReservations(Connection connection) throws SQLException {
        String sql = """
                INSERT INTO reservations
                (reservation_id, guest_username, room_id, check_in_date, check_out_date, status)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (Reservation reservation : HotelDatabase.reservations) {
                ps.setInt(1, reservation.getReservationId());
                ps.setString(2, reservation.getGuest().getUsername());
                ps.setInt(3, reservation.getRoom().getRoomId());
                ps.setString(4, reservation.getCheckInDate().toString());
                ps.setString(5, reservation.getCheckOutDate().toString());
                ps.setString(6, reservation.getStatus().toString());
                ps.executeUpdate();
            }
        }
    }

    private static void saveInvoices(Connection connection) throws SQLException {
        String sql = """
                INSERT INTO invoices
                (invoice_id, reservation_id, total_amount, payment_method, payment_date)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (Invoice invoice : HotelDatabase.invoices) {
                ps.setInt(1, invoice.getInvoiceId());
                ps.setInt(2, invoice.getReservation().getReservationId());
                ps.setDouble(3, invoice.getTotalAmount());
                ps.setString(4, invoice.getPaymentMethod().toString());
                ps.setString(5, invoice.getPaymentDate().toString());
                ps.executeUpdate();
            }
        }
    }

    public static void loadAllData() {
        HotelDatabase.clearAllData();

        Map<Integer, RoomType> roomTypeMap = new HashMap<>();
        Map<Integer, Amenity> amenityMap = new HashMap<>();
        Map<Integer, Room> roomMap = new HashMap<>();
        Map<String, Guest> guestMap = new HashMap<>();
        Map<Integer, Reservation> reservationMap = new HashMap<>();

        try (Connection connection = getConnection()) {
            loadRoomTypes(connection, roomTypeMap);
            loadAmenities(connection, amenityMap);
            loadGuests(connection, guestMap);
            loadAdmins(connection);
            loadReceptionists(connection);
            loadRooms(connection, roomTypeMap, roomMap);
            loadRoomAmenities(connection, roomMap, amenityMap);
            loadReservations(connection, guestMap, roomMap, reservationMap);
            loadInvoices(connection, reservationMap);

        } catch (SQLException e) {
            throw new RuntimeException("Load database error: " + e.getMessage());
        }
    }

    private static void loadRoomTypes(Connection connection, Map<Integer, RoomType> roomTypeMap) throws SQLException {
        String sql = "SELECT * FROM room_types";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                RoomType roomType = new RoomType(
                        rs.getInt("type_id"),
                        rs.getString("type_name"),
                        rs.getDouble("price_per_night"),
                        rs.getInt("capacity")
                );

                HotelDatabase.roomTypes.add(roomType);
                roomTypeMap.put(roomType.getTypeId(), roomType);
            }
        }
    }

    private static void loadAmenities(Connection connection, Map<Integer, Amenity> amenityMap) throws SQLException {
        String sql = "SELECT * FROM amenities";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                Amenity amenity = new Amenity(
                        rs.getInt("amenity_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("extra_cost")
                );

                HotelDatabase.amenities.add(amenity);
                amenityMap.put(amenity.getAmenityId(), amenity);
            }
        }
    }

    private static void loadGuests(Connection connection, Map<String, Guest> guestMap) throws SQLException {
        String sql = "SELECT * FROM guests";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                Guest guest = new Guest(
                        rs.getString("username"),
                        rs.getString("password"),
                        LocalDate.parse(rs.getString("date_of_birth")),
                        rs.getDouble("balance"),
                        rs.getString("address"),
                        Gender.valueOf(rs.getString("gender")),
                        rs.getString("room_preferences")
                );

                HotelDatabase.guests.add(guest);
                guestMap.put(guest.getUsername(), guest);
            }
        }
    }

    private static void loadAdmins(Connection connection) throws SQLException {
        String sql = "SELECT * FROM admins";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                Admin admin = new Admin(
                        rs.getString("username"),
                        rs.getString("password"),
                        LocalDate.parse(rs.getString("date_of_birth")),
                        rs.getInt("working_hours")
                );

                HotelDatabase.admins.add(admin);
            }
        }
    }

    private static void loadReceptionists(Connection connection) throws SQLException {
        String sql = "SELECT * FROM receptionists";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                Receptionist receptionist = new Receptionist(
                        rs.getString("username"),
                        rs.getString("password"),
                        LocalDate.parse(rs.getString("date_of_birth")),
                        rs.getInt("working_hours")
                );

                HotelDatabase.receptionists.add(receptionist);
            }
        }
    }

    private static void loadRooms(Connection connection,
                                  Map<Integer, RoomType> roomTypeMap,
                                  Map<Integer, Room> roomMap) throws SQLException {

        String sql = "SELECT * FROM rooms";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                int typeId = rs.getInt("type_id");
                RoomType roomType = roomTypeMap.get(typeId);

                Room room = new Room(
                        rs.getInt("room_id"),
                        rs.getInt("room_number"),
                        rs.getInt("is_available") == 1,
                        roomType
                );

                HotelDatabase.rooms.add(room);
                roomMap.put(room.getRoomId(), room);
            }
        }
    }

    private static void loadRoomAmenities(Connection connection,
                                          Map<Integer, Room> roomMap,
                                          Map<Integer, Amenity> amenityMap) throws SQLException {

        String sql = "SELECT * FROM room_amenities";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                Room room = roomMap.get(rs.getInt("room_id"));
                Amenity amenity = amenityMap.get(rs.getInt("amenity_id"));

                if (room != null && amenity != null) {
                    room.addAmenity(amenity);
                }
            }
        }
    }

    private static void loadReservations(Connection connection,
                                         Map<String, Guest> guestMap,
                                         Map<Integer, Room> roomMap,
                                         Map<Integer, Reservation> reservationMap) throws SQLException {

        String sql = "SELECT * FROM reservations";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                Guest guest = guestMap.get(rs.getString("guest_username"));
                Room room = roomMap.get(rs.getInt("room_id"));

                if (guest != null && room != null) {
                    Reservation reservation = new Reservation(
                            rs.getInt("reservation_id"),
                            guest,
                            room,
                            LocalDate.parse(rs.getString("check_in_date")),
                            LocalDate.parse(rs.getString("check_out_date")),
                            ReservationStatus.valueOf(rs.getString("status"))
                    );

                    HotelDatabase.reservations.add(reservation);
                    reservationMap.put(reservation.getReservationId(), reservation);
                }
            }
        }
    }

    private static void loadInvoices(Connection connection,
                                     Map<Integer, Reservation> reservationMap) throws SQLException {

        String sql = "SELECT * FROM invoices";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                Reservation reservation = reservationMap.get(rs.getInt("reservation_id"));

                if (reservation != null) {
                    Invoice invoice = new Invoice(
                            rs.getInt("invoice_id"),
                            reservation,
                            rs.getDouble("total_amount"),
                            PaymentMethod.valueOf(rs.getString("payment_method")),
                            LocalDate.parse(rs.getString("payment_date"))
                    );

                    HotelDatabase.invoices.add(invoice);
                }
            }
        }
    }
}