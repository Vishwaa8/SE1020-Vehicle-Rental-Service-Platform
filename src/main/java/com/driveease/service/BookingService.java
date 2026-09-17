package com.driveease.service;

import com.driveease.model.Booking;
import com.driveease.model.Vehicle;
import com.driveease.util.BookingFileHandler;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class BookingService {

    private final BookingFileHandler bookingFileHandler;
    private final VehicleService vehicleService;

    public BookingService() {
        this(new BookingFileHandler(), new VehicleService());
    }

    public BookingService(BookingFileHandler bookingFileHandler,
                          VehicleService vehicleService) {
        this.bookingFileHandler = Objects.requireNonNull(
                bookingFileHandler,
                "Booking file handler cannot be null."
        );

        this.vehicleService = Objects.requireNonNull(
                vehicleService,
                "Vehicle service cannot be null."
        );
    }

    public synchronized void addBooking(
            String bookingId,
            String vehicleId,
            String customerName,
            String customerPhone,
            LocalDate startDate,
            LocalDate endDate
    ) throws IOException {

        List<Booking> bookings = bookingFileHandler.loadBookings();

        if (findById(bookings, bookingId) != null) {
            throw new IllegalArgumentException(
                    "A booking with this ID already exists."
            );
        }

        Vehicle vehicle = requireVehicle(vehicleId);

        if (!vehicle.isAvailable()) {
            throw new IllegalArgumentException(
                    "The selected vehicle is unavailable."
            );
        }

        Booking booking = new Booking(
                bookingId,
                vehicle.getVehicleId(),
                customerName,
                customerPhone,
                startDate,
                endDate,
                vehicle.getDailyRate()
        );

        ensureNoOverlap(bookings, booking, null);

        bookings.add(booking);
        bookingFileHandler.saveBookings(bookings);
    }

    public synchronized List<Booking> getAllBookings()
            throws IOException {

        return bookingFileHandler.loadBookings();
    }

    public synchronized Booking getBookingById(String bookingId)
            throws IOException {

        return findById(
                bookingFileHandler.loadBookings(),
                bookingId
        );
    }

    public synchronized void updateBooking(
            String bookingId,
            String vehicleId,
            String customerName,
            String customerPhone,
            LocalDate startDate,
            LocalDate endDate
    ) throws IOException {

        List<Booking> bookings = bookingFileHandler.loadBookings();

        int index = findIndexById(bookings, bookingId);

        if (index == -1) {
            throw new IllegalArgumentException(
                    "Booking not found."
            );
        }

        Booking existingBooking = bookings.get(index);
        Vehicle vehicle = requireVehicle(vehicleId);

        boolean vehicleChanged = !existingBooking.getVehicleId()
                .equalsIgnoreCase(vehicle.getVehicleId());

        if (vehicleChanged && !vehicle.isAvailable()) {
            throw new IllegalArgumentException(
                    "The selected vehicle is unavailable."
            );
        }

        // Keep the original rate when the vehicle remains the same.
        double dailyRate = vehicleChanged
                ? vehicle.getDailyRate()
                : existingBooking.getDailyRate();

        Booking updatedBooking = new Booking(
                existingBooking.getBookingId(),
                vehicle.getVehicleId(),
                customerName,
                customerPhone,
                startDate,
                endDate,
                dailyRate
        );

        ensureNoOverlap(
                bookings,
                updatedBooking,
                existingBooking.getBookingId()
        );

        bookings.set(index, updatedBooking);
        bookingFileHandler.saveBookings(bookings);
    }

    public synchronized void deleteBooking(String bookingId)
            throws IOException {

        List<Booking> bookings = bookingFileHandler.loadBookings();

        int index = findIndexById(bookings, bookingId);

        if (index == -1) {
            throw new IllegalArgumentException(
                    "Booking not found."
            );
        }

        bookings.remove(index);
        bookingFileHandler.saveBookings(bookings);
    }

    public synchronized List<Booking> searchBookings(String keyword)
            throws IOException {

        List<Booking> bookings = bookingFileHandler.loadBookings();

        if (keyword == null || keyword.isBlank()) {
            return bookings;
        }

        String searchText = keyword.trim().toLowerCase(Locale.ROOT);
        List<Booking> results = new ArrayList<>();

        for (Booking booking : bookings) {
            if (booking.getBookingId().toLowerCase(Locale.ROOT)
                    .contains(searchText)
                    || booking.getVehicleId().toLowerCase(Locale.ROOT)
                    .contains(searchText)
                    || booking.getCustomerName().toLowerCase(Locale.ROOT)
                    .contains(searchText)
                    || booking.getCustomerPhone().toLowerCase(Locale.ROOT)
                    .contains(searchText)) {

                results.add(booking);
            }
        }

        return results;
    }

    private Vehicle requireVehicle(String vehicleId)
            throws IOException {

        if (vehicleId == null || vehicleId.isBlank()) {
            throw new IllegalArgumentException(
                    "Vehicle ID cannot be empty."
            );
        }

        return vehicleService.getVehicleById(vehicleId.trim())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Selected vehicle does not exist."
                ));
    }

    private void ensureNoOverlap(
            List<Booking> bookings,
            Booking requestedBooking,
            String excludedBookingId
    ) {

        for (Booking existingBooking : bookings) {

            if (excludedBookingId != null
                    && existingBooking.getBookingId()
                    .equalsIgnoreCase(excludedBookingId)) {
                continue;
            }

            boolean sameVehicle = existingBooking.getVehicleId()
                    .equalsIgnoreCase(
                            requestedBooking.getVehicleId()
                    );

            boolean datesOverlap =
                    requestedBooking.getStartDate()
                            .isBefore(existingBooking.getEndDate())
                            && existingBooking.getStartDate()
                            .isBefore(requestedBooking.getEndDate());

            if (sameVehicle && datesOverlap) {
                throw new IllegalArgumentException(
                        "This vehicle is already booked for the selected dates."
                );
            }
        }
    }

    private Booking findById(
            List<Booking> bookings,
            String bookingId
    ) {

        int index = findIndexById(bookings, bookingId);

        return index == -1 ? null : bookings.get(index);
    }

    private int findIndexById(
            List<Booking> bookings,
            String bookingId
    ) {

        if (bookingId == null || bookingId.isBlank()) {
            return -1;
        }

        String requestedId = bookingId.trim();

        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).getBookingId()
                    .equalsIgnoreCase(requestedId)) {
                return i;
            }
        }

        return -1;
    }
}