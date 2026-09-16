package com.driveease.service;

import com.driveease.model.Vehicle;
import com.driveease.util.VehicleFileHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public class VehicleService {

    private final VehicleFileHandler fileHandler;

    // Constructors
    public VehicleService() {
        this(new VehicleFileHandler());
    }

    public VehicleService(VehicleFileHandler fileHandler) {
        this.fileHandler = Objects.requireNonNull(fileHandler);
    }

    // Add a new vehicle
    public synchronized void addVehicle(Vehicle vehicle) throws IOException {

        Objects.requireNonNull(vehicle, "Vehicle cannot be null.");

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

    // Get all vehicles
    public synchronized List<Vehicle> getAllVehicles() throws IOException {

        return List.copyOf(fileHandler.loadVehicles());
    }

    // Find a vehicle by ID
    public synchronized Optional<Vehicle> getVehicleById(String vehicleId)
            throws IOException {

        String id = validateVehicleId(vehicleId);

        List<Vehicle> vehicles = fileHandler.loadVehicles();

        for (Vehicle vehicle : vehicles) {

            if (vehicle.getVehicleId().equalsIgnoreCase(id)) {
                return Optional.of(vehicle);
            }
        }

        return Optional.empty();
    }

    // Update an existing vehicle
    public synchronized boolean updateVehicle(Vehicle updatedVehicle)
            throws IOException {

        Objects.requireNonNull(
                updatedVehicle, "Vehicle cannot be null."
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

    // Delete a vehicle by ID
    public synchronized boolean deleteVehicle(String vehicleId)
            throws IOException {

        String id = validateVehicleId(vehicleId);

        List<Vehicle> vehicles = fileHandler.loadVehicles();

        boolean removed = vehicles.removeIf(
                vehicle -> vehicle.getVehicleId().equalsIgnoreCase(id)
        );

        if (removed) {
            fileHandler.saveVehicles(vehicles);
        }

        return removed;
    }

    // Search vehicles by ID, brand, model or type
    public synchronized List<Vehicle> searchVehicles(String keyword)
            throws IOException {

        if (keyword == null || keyword.isBlank()) {
            return getAllVehicles();
        }

        String searchText = keyword.trim().toLowerCase(Locale.ROOT);

        List<Vehicle> vehicles = fileHandler.loadVehicles();
        List<Vehicle> results = new ArrayList<>();

        for (Vehicle vehicle : vehicles) {

            String id = vehicle.getVehicleId().toLowerCase(Locale.ROOT);
            String brand = vehicle.getBrand().toLowerCase(Locale.ROOT);
            String model = vehicle.getModel().toLowerCase(Locale.ROOT);
            String type = vehicle.getVehicleType().toLowerCase(Locale.ROOT);

            if (id.contains(searchText)
                    || brand.contains(searchText)
                    || model.contains(searchText)
                    || type.contains(searchText)) {

                results.add(vehicle);
            }
        }

        return List.copyOf(results);
    }

    // Validate vehicle ID
    private String validateVehicleId(String vehicleId) {

        if (vehicleId == null || vehicleId.isBlank()) {
            throw new IllegalArgumentException(
                    "Vehicle ID cannot be empty."
            );
        }

        return vehicleId.trim();
    }
}