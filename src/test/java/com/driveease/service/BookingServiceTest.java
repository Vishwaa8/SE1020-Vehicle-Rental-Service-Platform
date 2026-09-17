package com.driveease.service;

import com.driveease.model.Booking;
import com.driveease.model.Bike;
import com.driveease.model.Car;
import com.driveease.model.Van;
import com.driveease.util.BookingFileHandler;
import com.driveease.util.VehicleFileHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public class BookingServiceTest {

    public static void main(String[] args) throws IOException {

        Path testDirectory = Files.createTempDirectory(
                "driveease-booking-service-test-"
        );

        Path vehicleFile = testDirectory.resolve("vehicles.txt");
        Path bookingFile = testDirectory.resolve("bookings.txt");

        try {
            VehicleFileHandler vehicleFileHandler =
                    new VehicleFileHandler(vehicleFile);

            BookingFileHandler bookingFileHandler =
                    new BookingFileHandler(bookingFile);

            // Create test vehicles in a temporary file.
            vehicleFileHandler.saveVehicles(List.of(
                    new Car("C001", "Toyota", "Axio", 8500.00, true),
                    new Van("V001", "Toyota", "KDH", 12000.00, true),
                    new Bike("BK001", "Honda", "Dio", 2500.00, false)
            ));

            // Both services use temporary vehicle and booking files.
            VehicleService vehicleService =
                    new VehicleService(
                            vehicleFileHandler,
                            bookingFileHandler
                    );

            BookingService bookingService =
                    new BookingService(
                            bookingFileHandler,
                            vehicleService
                    );

            // Test 1: A new booking file has no bookings.
            check(
                    bookingService.getAllBookings().isEmpty(),
                    "Booking list should initially be empty."
            );

            System.out.println("PASS 1 - Empty booking list");

            // Test 2: Create a booking.
            bookingService.addBooking(
                    "B001",
                    "C001",
                    "Amal Perera",
                    "0771234567",
                    date(2026, 9, 20),
                    date(2026, 9, 23)
            );

            Booking firstBooking =
                    bookingService.getBookingById("B001");

            check(firstBooking != null,
                    "Booking B001 was not created.");

            check(firstBooking.getRentalDays() == 3,
                    "Rental duration should be three days.");

            check(firstBooking.calculateTotalCost() == 25500.00,
                    "Total rental cost should be Rs. 25,500.");

            System.out.println("PASS 2 - Create booking and calculate cost");

            // Test 3: Duplicate booking IDs must be rejected.
            expectIllegalArgument(() -> bookingService.addBooking(
                    "B001",
                    "V001",
                    "Nimali Perera",
                    "0712345678",
                    date(2026, 9, 20),
                    date(2026, 9, 22)
            ));

            System.out.println("PASS 3 - Duplicate booking ID rejected");

            // Test 4: Overlapping dates for the same vehicle are rejected.
            expectIllegalArgument(() -> bookingService.addBooking(
                    "B002",
                    "C001",
                    "Kamal Silva",
                    "0751234567",
                    date(2026, 9, 21),
                    date(2026, 9, 24)
            ));

            System.out.println("PASS 4 - Overlapping booking rejected");

            // Test 5: A booking can begin on the previous return date.
            bookingService.addBooking(
                    "B002",
                    "C001",
                    "Kamal Silva",
                    "0751234567",
                    date(2026, 9, 23),
                    date(2026, 9, 25)
            );

            check(bookingService.getAllBookings().size() == 2,
                    "Adjacent booking was not created.");

            System.out.println("PASS 5 - Adjacent booking dates allowed");

            // Test 6: A different vehicle can be booked on the same dates.
            bookingService.addBooking(
                    "B003",
                    "V001",
                    "Nimali Perera",
                    "0712345678",
                    date(2026, 9, 20),
                    date(2026, 9, 23)
            );

            check(bookingService.getAllBookings().size() == 3,
                    "Booking for another vehicle was not created.");

            System.out.println("PASS 6 - Different vehicle booking allowed");

            // Test 7: An unavailable vehicle cannot be booked.
            expectIllegalArgument(() -> bookingService.addBooking(
                    "B004",
                    "BK001",
                    "Saman Kumara",
                    "0701234567",
                    date(2026, 9, 26),
                    date(2026, 9, 28)
            ));

            System.out.println("PASS 7 - Unavailable vehicle rejected");

            // Test 8: A vehicle that does not exist cannot be booked.
            expectIllegalArgument(() -> bookingService.addBooking(
                    "B004",
                    "UNKNOWN",
                    "Saman Kumara",
                    "0701234567",
                    date(2026, 9, 26),
                    date(2026, 9, 28)
            ));

            System.out.println("PASS 8 - Missing vehicle rejected");

            // Test 9: Updating a booking to overlapping dates is rejected.
            expectIllegalArgument(() -> bookingService.updateBooking(
                    "B002",
                    "C001",
                    "Kamal Silva",
                    "0751234567",
                    date(2026, 9, 21),
                    date(2026, 9, 24)
            ));

            System.out.println("PASS 9 - Overlapping update rejected");

            // Test 10: A valid booking update works.
            bookingService.updateBooking(
                    "B002",
                    "C001",
                    "Kamal Updated",
                    "0751234567",
                    date(2026, 9, 25),
                    date(2026, 9, 28)
            );

            Booking updatedBooking =
                    bookingService.getBookingById("B002");

            check(updatedBooking != null,
                    "Updated booking was not found.");

            check(updatedBooking.getCustomerName().equals("Kamal Updated"),
                    "Customer name was not updated.");

            check(updatedBooking.getStartDate().equals(date(2026, 9, 25)),
                    "Start date was not updated.");

            check(updatedBooking.getDailyRate() == 8500.00,
                    "Original daily rate was not preserved.");

            System.out.println("PASS 10 - Update booking");

            // Test 11: Search is case-insensitive.
            List<Booking> searchResults =
                    bookingService.searchBookings("kamal");

            check(searchResults.size() == 1,
                    "Search should return one booking.");

            check(searchResults.get(0).getBookingId().equals("B002"),
                    "Search returned the wrong booking.");

            System.out.println("PASS 11 - Search bookings");

            // Test 12: Delete a booking.
            bookingService.deleteBooking("B003");

            check(bookingService.getBookingById("B003") == null,
                    "Deleted booking still exists.");

            check(bookingService.getAllBookings().size() == 2,
                    "Booking count is incorrect after deletion.");

            System.out.println("PASS 12 - Delete booking");

            // Test 13: Saved bookings can be loaded again.
            BookingService reloadedService = new BookingService(
                    new BookingFileHandler(bookingFile),
                    new VehicleService(
                            new VehicleFileHandler(vehicleFile),
                            new BookingFileHandler(bookingFile)
                    )
            );

            check(reloadedService.getAllBookings().size() == 2,
                    "Bookings were not persisted.");

            check(reloadedService.getBookingById("B002") != null,
                    "Booking B002 was not loaded from the file.");

            System.out.println("PASS 13 - Booking persistence");

            System.out.println();
            System.out.println("ALL 13 BOOKING SERVICE TESTS PASSED!");

        } finally {
            // Delete only the temporary test files.
            Files.deleteIfExists(bookingFile);
            Files.deleteIfExists(vehicleFile);
            Files.deleteIfExists(testDirectory);
        }
    }

    private static LocalDate date(int year, int month, int day) {
        return LocalDate.of(year, month, day);
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void expectIllegalArgument(
            TestAction action
    ) throws IOException {

        boolean rejected = false;

        try {
            action.run();
        } catch (IllegalArgumentException exception) {
            rejected = true;
        }

        check(rejected, "Expected an IllegalArgumentException.");
    }

    @FunctionalInterface
    private interface TestAction {
        void run() throws IOException;
    }
}