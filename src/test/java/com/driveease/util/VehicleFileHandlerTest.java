package com.driveease.util;

import com.driveease.model.Bike;
import com.driveease.model.Car;
import com.driveease.model.Van;
import com.driveease.model.Vehicle;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class VehicleFileHandlerTest {

    public static void main(String[] args) throws Exception {

        Path testDirectory = Files.createTempDirectory("driveease-test-");
        Path testFile = testDirectory.resolve("vehicles.txt");

        try {
            VehicleFileHandler handler = new VehicleFileHandler(testFile);

            // Create sample vehicles
            List<Vehicle> vehicles = List.of(
                    new Car("C001", "Toyota", "Axio", 8500.0, true),
                    new Van("V001", "Toyota", "Hiace", 12000.0, true),
                    new Bike("B001", "Honda", "Dio", 3500.0, false)
            );

            // Save vehicles
            handler.saveVehicles(vehicles);

            if (!Files.exists(testFile)) {
                throw new AssertionError("Vehicle file was not created.");
            }

            System.out.println("PASS: Vehicle file created.");

            // Read saved vehicles
            List<Vehicle> loaded = handler.loadVehicles();

            if (loaded.size() != 3) {
                throw new AssertionError("Incorrect number of vehicles.");
            }

            System.out.println("PASS: All three vehicles loaded.");

            // Verify vehicle types
            if (!(loaded.get(0) instanceof Car)
                    || !(loaded.get(1) instanceof Van)
                    || !(loaded.get(2) instanceof Bike)) {
                throw new AssertionError("Vehicle types do not match.");
            }

            System.out.println("PASS: Vehicle types preserved.");

            // Verify saved data
            if (!loaded.get(0).getVehicleId().equals("C001")
                    || !loaded.get(0).getBrand().equals("Toyota")
                    || !loaded.get(0).getModel().equals("Axio")
                    || loaded.get(0).getDailyRate() != 8500.0) {
                throw new AssertionError("Vehicle data does not match.");
            }

            if (loaded.get(2).isAvailable()) {
                throw new AssertionError("Availability was not preserved.");
            }

            System.out.println("PASS: Vehicle details preserved.");

            // Verify rental calculation
            double total = loaded.get(0).calculateRentalCost(3);

            if (total != 25500.0) {
                throw new AssertionError("Rental calculation failed.");
            }

            System.out.println("PASS: Rental cost calculation.");

            System.out.println();
            System.out.println("ALL FILE HANDLING TESTS PASSED");

        } finally {
            Files.deleteIfExists(testFile);
            Files.deleteIfExists(testDirectory);
        }
    }
}
