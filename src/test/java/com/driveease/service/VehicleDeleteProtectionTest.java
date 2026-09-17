package com.driveease.service;

import com.driveease.model.Booking;
import com.driveease.model.Car;
import com.driveease.util.BookingFileHandler;
import com.driveease.util.VehicleFileHandler;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public class VehicleDeleteProtectionTest {

    public static void main(String[] args) throws Exception {

        // This test uses separate temporary files, not application data.
        Path tempDir = Files.createTempDirectory("driveease-delete-test-");
        Path vehiclePath = tempDir.resolve("vehicles.txt");
        Path bookingPath = tempDir.resolve("bookings.txt");

        try {
            VehicleFileHandler vehicleFileHandler =
                    new VehicleFileHandler(vehiclePath);

            BookingFileHandler bookingFileHandler =
                    new BookingFileHandler(bookingPath);

            VehicleService vehicleService =
                    new VehicleService(
                            vehicleFileHandler,
                            bookingFileHandler
                    );

            // Create two vehicles in the temporary vehicle file.
            vehicleFileHandler.saveVehicles(List.of(
                    new Car("TV901", "Test", "Booked Car", 1000.0, true),
                    new Car("TV902", "Test", "Free Car", 1200.0, true)
            ));

            // Only TV901 has a booking.
            bookingFileHandler.saveBookings(List.of(
                    new Booking(
                            "TB901",
                            "TV901",
                            "Test Customer",
                            "0771234567",
                            LocalDate.of(2026, 10, 10),
                            LocalDate.of(2026, 10, 12),
                            1000.0
                    )
            ));

            // Test 1: A vehicle with a booking must not be deleted.
            boolean deletionBlocked = false;

            try {
                vehicleService.deleteVehicle("TV901");
            } catch (IllegalArgumentException e) {
                deletionBlocked = true;
            }

            if (!deletionBlocked) {
                throw new AssertionError(
                        "FAIL: Booked vehicle deletion was not blocked."
                );
            }

            System.out.println(
                    "PASS 1 - Booked vehicle deletion blocked"
            );

            // Test 2: The booked vehicle and booking must remain saved.
            if (vehicleService.getVehicleById("TV901").isEmpty()
                    || bookingFileHandler.loadBookings().size() != 1) {

                throw new AssertionError(
                        "FAIL: Booked vehicle or booking was lost."
                );
            }

            System.out.println(
                    "PASS 2 - Booked vehicle and booking preserved"
            );

            // Test 3: A vehicle without bookings can still be deleted.
            boolean deleted = vehicleService.deleteVehicle("TV902");

            if (!deleted
                    || vehicleService.getVehicleById("TV902").isPresent()) {

                throw new AssertionError(
                        "FAIL: Unbooked vehicle was not deleted."
                );
            }

            System.out.println(
                    "PASS 3 - Unbooked vehicle deletion allowed"
            );

            System.out.println(
                    "\nALL 3 VEHICLE DELETE PROTECTION TESTS PASSED!"
            );

        } finally {
            Files.deleteIfExists(vehiclePath);
            Files.deleteIfExists(bookingPath);
            Files.deleteIfExists(tempDir);
        }
    }
}