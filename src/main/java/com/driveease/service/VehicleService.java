package com.driveease.service;

import com.driveease.model.Booking;
import com.driveease.model.Vehicle;
import com.driveease.util.BookingFileHandler;
import com.driveease.util.VehicleFileHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public class VehicleService {

    private final VehicleFileHandler fileHandler;
    private final BookingFileHandler bookingFileHandler;

    // Use the application's default vehicle and booking files.
    public VehicleService() {
        this(new VehicleFileHandler(), new BookingFileHandler());
    }

    // Retain compatibility with existing vehicle service tests.
    public VehicleService(VehicleFileHandler fileHandler) {
        this(fileHandler, new BookingFileHandler());
    }

    // Allow both file handlers to be supplied when testing.
    public VehicleService(VehicleFileHandler fileHandler,
                          BookingFileHandler bookingFileHandler) {

        this.fileHandler = Objects.requireNonNull(
                fileHandler,
                "Vehicle file handler cannot be null."
        );

        this.bookingFileHandler = Objects.requireNonNull(
                bookingFileHandler,
                "Booking file handler cannot be null."
        );
    }

    // Add a new vehicle.
    public synchronized void addVehicle(Vehicle vehicle)
            throws IOException {

        Objects.requireNonNull(
                vehicle,
                "Vehicle cannot be null."
        );

        List<Vehicle> vehicles = fileHandler.loadVehicles();

        for (Vehicle existingVehicle : vehicles) {

            if (existingVehicle.getVehicleId()
                    .equalsIgnoreCase(vehicle.getVehicleId())) {

                throw new IllegalArgumentException(
                        "A vehicle with this ID already exists."
                );
            }
        }

        vehicles.add(vehicle);
        fileHandler.saveVehicles(vehicles);
    }

    // Get all vehicles.
    public synchronized List<Vehicle> getAllVehicles()
            throws IOException {

        return List.copyOf(fileHandler.loadVehicles());
    }

    // Find a vehicle by ID.
    public synchronized Optional<Vehicle> getVehicleById(
            String vehicleId
    ) throws IOException {

        String id = validateVehicleId(vehicleId);

        List<Vehicle> vehicles = fileHandler.loadVehicles();

        for (Vehicle vehicle : vehicles) {

            if (vehicle.getVehicleId().equalsIgnoreCase(id)) {
                return Optional.of(vehicle);
            }
        }

        return Optional.empty();
    }

    // Update an existing vehicle.
    public synchronized boolean updateVehicle(
            Vehicle updatedVehicle
    ) throws IOException {

        Objects.requireNonNull(
                updatedVehicle,
                "Vehicle cannot be null."
        );

        List<Vehicle> vehicles = fileHandler.loadVehicles();

        for (int i = 0; i < vehicles.size(); i++) {

            Vehicle existingVehicle = vehicles.get(i);

            if (existingVehicle.getVehicleId()
                    .equalsIgnoreCase(updatedVehicle.getVehicleId())) {

                vehicles.set(i, updatedVehicle);
                fileHandler.saveVehicles(vehicles);

                return true;
            }
        }

        return false;
    }

    // Delete only vehicles that have no associated bookings.
    public synchronized boolean deleteVehicle(String vehicleId)
            throws IOException {

        String id = validateVehicleId(vehicleId);

        List<Vehicle> vehicles = fileHandler.loadVehicles();

        boolean vehicleExists = vehicles.stream()
                .anyMatch(vehicle ->
                        vehicle.getVehicleId().equalsIgnoreCase(id)
                );

        if (!vehicleExists) {
            return false;
        }

        // Do not delete a vehicle referenced by a saved booking.
        List<Booking> bookings = bookingFileHandler.loadBookings();

        boolean hasBookings = bookings.stream()
                .anyMatch(booking ->
                        booking.getVehicleId().equalsIgnoreCase(id)
                );

        if (hasBookings) {
            throw new IllegalArgumentException(
                    "This vehicle has existing bookings. " +
                            "Delete its bookings before deleting the vehicle."
            );
        }

        boolean removed = vehicles.removeIf(
                vehicle ->
                        vehicle.getVehicleId().equalsIgnoreCase(id)
        );

        if (removed) {
            fileHandler.saveVehicles(vehicles);
        }

        return removed;
    }

    // Search vehicles by ID, brand, model or type.
    public synchronized List<Vehicle> searchVehicles(String keyword)
            throws IOException {

        if (keyword == null || keyword.isBlank()) {
            return getAllVehicles();
        }

        String searchText = keyword.trim()
                .toLowerCase(Locale.ROOT);

        List<Vehicle> vehicles = fileHandler.loadVehicles();
        List<Vehicle> results = new ArrayList<>();

        for (Vehicle vehicle : vehicles) {

            String id = vehicle.getVehicleId()
                    .toLowerCase(Locale.ROOT);

            String brand = vehicle.getBrand()
                    .toLowerCase(Locale.ROOT);

            String model = vehicle.getModel()
                    .toLowerCase(Locale.ROOT);

            String type = vehicle.getVehicleType()
                    .toLowerCase(Locale.ROOT);

            if (id.contains(searchText)
                    || brand.contains(searchText)
                    || model.contains(searchText)
                    || type.contains(searchText)) {

                results.add(vehicle);
            }
        }

        return List.copyOf(results);
    }

    // Validate vehicle ID.
    private String validateVehicleId(String vehicleId) {

        if (vehicleId == null || vehicleId.isBlank()) {
            throw new IllegalArgumentException(
                    "Vehicle ID cannot be empty."
            );
        }

        return vehicleId.trim();
    }
}