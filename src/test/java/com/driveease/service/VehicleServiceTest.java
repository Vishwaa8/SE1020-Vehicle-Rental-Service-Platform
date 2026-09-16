package com.driveease.service;

import com.driveease.model.Car;
import com.driveease.model.Van;
import com.driveease.model.Vehicle;
import com.driveease.util.VehicleFileHandler;

import java.nio.file.Files;
import java.nio.file.Path;

public class VehicleServiceTest {

    public static void main(String[] args) throws Exception {

        Path testDirectory = Files.createTempDirectory("driveease-crud-");
        Path testFile = testDirectory.resolve("vehicles.txt");

        try {
            VehicleFileHandler fileHandler =
                    new VehicleFileHandler(testFile);

            VehicleService service = new VehicleService(fileHandler);

            // Test 1: Add vehicles
            Vehicle car = new Car(
                    "C001", "Toyota", "Axio", 8500.0, true
            );

            Vehicle van = new Van(
                    "V001", "Toyota", "Hiace", 12000.0, true
            );

            service.addVehicle(car);
            service.addVehicle(van);

            check(service.getAllVehicles().size() == 2,
                    "CREATE operation failed.");

            System.out.println("PASS: Create vehicles.");

            // Test 2: Read all vehicles
            check(service.getAllVehicles().size() == 2,
                    "READ operation failed.");

            System.out.println("PASS: Read all vehicles.");

            // Test 3: Find vehicle by ID
            Vehicle found = service.getVehicleById("C001")
                    .orElseThrow();

            check(found.getBrand().equals("Toyota"),
                    "Vehicle lookup failed.");

            System.out.println("PASS: Find vehicle by ID.");

            // Test 4: Reject duplicate vehicle IDs
            boolean duplicateRejected = false;

            try {
                service.addVehicle(
                        new Car("c001", "Honda", "Civic", 9000.0, true)
                );
            } catch (IllegalArgumentException e) {
                duplicateRejected = true;
            }

            check(duplicateRejected,
                    "Duplicate vehicle ID was accepted.");

            check(service.getAllVehicles().size() == 2,
                    "Duplicate check changed stored data.");

            System.out.println("PASS: Duplicate ID validation.");

            // Test 5: Update vehicle
            Vehicle updatedCar = new Car(
                    "C001", "Toyota", "Corolla", 9500.0, false
            );

            boolean updated = service.updateVehicle(updatedCar);

            check(updated, "UPDATE operation failed.");

            Vehicle savedCar = service.getVehicleById("C001")
                    .orElseThrow();

            check(savedCar.getModel().equals("Corolla")
                            && savedCar.getDailyRate() == 9500.0
                            && !savedCar.isAvailable(),
                    "Updated vehicle details are incorrect.");

            System.out.println("PASS: Update vehicle.");

            // Test 6: Search vehicles
            check(service.searchVehicles("corolla").size() == 1,
                    "SEARCH operation failed.");

            System.out.println("PASS: Search vehicles.");

            // Test 7: Delete vehicle
            boolean deleted = service.deleteVehicle("V001");

            check(deleted, "DELETE operation failed.");

            check(service.getAllVehicles().size() == 1,
                    "Incorrect vehicle count after deletion.");

            check(service.getVehicleById("V001").isEmpty(),
                    "Deleted vehicle still exists.");

            System.out.println("PASS: Delete vehicle.");

            // Test 8: Confirm data persistence
            VehicleService reloadedService =
                    new VehicleService(
                            new VehicleFileHandler(testFile)
                    );

            check(reloadedService.getAllVehicles().size() == 1,
                    "Data persistence failed.");

            check(reloadedService.getVehicleById("C001").isPresent(),
                    "Saved vehicle could not be loaded.");

            System.out.println("PASS: Data persistence.");

            System.out.println();
            System.out.println("ALL VEHICLE CRUD TESTS PASSED");

        } finally {
            Files.deleteIfExists(testFile);
            Files.deleteIfExists(testDirectory);
        }
    }

    private static void check(boolean condition, String message) {

        if (!condition) {
            throw new AssertionError(message);
        }
    }
}