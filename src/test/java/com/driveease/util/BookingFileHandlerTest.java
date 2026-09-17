package com.driveease.util;

import com.driveease.model.Booking;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public class BookingFileHandlerTest {

    public static void main(String[] args) throws IOException {
        Path tempDirectory = Files.createTempDirectory("driveease-booking-test-");
        Path testFile = tempDirectory.resolve("bookings.txt");

        try {
            BookingFileHandler handler = new BookingFileHandler(testFile);

            // Test 1: Loading a missing file returns an empty list.
            check(handler.loadBookings().isEmpty(),
                    "Missing file should return an empty list.");
            System.out.println("PASS 1 - Missing file handling");

            Booking firstBooking = new Booking(
                    "B001",
                    "C001",
                    "Amal\tPerera",
                    "0771234567",
                    LocalDate.of(2026, 9, 20),
                    LocalDate.of(2026, 9, 23),
                    8500.00
            );

            Booking secondBooking = new Booking(
                    "B002",
                    "V001",
                    "Nimali Perera",
                    "0712345678",
                    LocalDate.of(2026, 9, 24),
                    LocalDate.of(2026, 9, 26),
                    12000.00
            );

            // Test 2: Save two bookings to a file.
            handler.saveBookings(List.of(firstBooking, secondBooking));
            check(Files.exists(testFile),
                    "Booking file was not created.");
            System.out.println("PASS 2 - Save bookings");

            // Test 3: Load the saved bookings.
            List<Booking> loadedBookings = handler.loadBookings();
            check(loadedBookings.size() == 2,
                    "Expected two loaded bookings.");
            check(loadedBookings.get(0).getBookingId().equals("B001"),
                    "First booking ID does not match.");
            check(loadedBookings.get(1).getVehicleId().equals("V001"),
                    "Second booking vehicle ID does not match.");
            System.out.println("PASS 3 - Load bookings");

            // Test 4: Preserve customer text and calculate rental cost.
            check(loadedBookings.get(0).getCustomerName()
                            .equals("Amal\tPerera"),
                    "Customer name was not preserved.");
            check(loadedBookings.get(0).getRentalDays() == 3,
                    "Rental duration should be three days.");
            check(loadedBookings.get(0).calculateTotalCost() == 25500.00,
                    "Rental cost should be Rs. 25,500.");
            System.out.println("PASS 4 - Data and rental cost");

            // Test 5: Saving again replaces the previous file contents.
            handler.saveBookings(List.of(secondBooking));
            List<Booking> updatedBookings = handler.loadBookings();
            check(updatedBookings.size() == 1,
                    "Expected one booking after replacement.");
            check(updatedBookings.get(0).getBookingId().equals("B002"),
                    "Incorrect booking after replacement.");
            System.out.println("PASS 5 - Replace saved bookings");

            // Test 6: Invalid file records should be rejected.
            Files.writeString(
                    testFile,
                    "invalid-booking-record",
                    StandardCharsets.UTF_8
            );

            boolean invalidRecordRejected = false;

            try {
                handler.loadBookings();
            } catch (IOException exception) {
                invalidRecordRejected = true;
            }

            check(invalidRecordRejected,
                    "Invalid booking record was not rejected.");
            System.out.println("PASS 6 - Invalid record handling");

            System.out.println();
            System.out.println("ALL 6 BOOKING FILE HANDLING TESTS PASSED!");

        } finally {
            Files.deleteIfExists(testFile);
            Files.deleteIfExists(tempDirectory);
        }
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}