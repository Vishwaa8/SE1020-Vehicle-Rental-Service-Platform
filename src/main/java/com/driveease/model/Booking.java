package com.driveease.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Booking {

    // Booking attributes
    private final String bookingId;
    private final String vehicleId;
    private final String customerName;
    private final String customerPhone;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final double dailyRate;

    // Constructor
    public Booking(String bookingId, String vehicleId,
                   String customerName, String customerPhone,
                   LocalDate startDate, LocalDate endDate,
                   double dailyRate) {

        if (bookingId == null || bookingId.isBlank()) {
            throw new IllegalArgumentException(
                    "Booking ID cannot be empty."
            );
        }

        if (vehicleId == null || vehicleId.isBlank()) {
            throw new IllegalArgumentException(
                    "Vehicle ID cannot be empty."
            );
        }

        if (customerName == null || customerName.isBlank()) {
            throw new IllegalArgumentException(
                    "Customer name cannot be empty."
            );
        }

        if (customerPhone == null || customerPhone.isBlank()) {
            throw new IllegalArgumentException(
                    "Customer phone cannot be empty."
            );
        }

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException(
                    "Rental dates are required."
            );
        }

        if (!endDate.isAfter(startDate)) {
            throw new IllegalArgumentException(
                    "Return date must be after the pickup date."
            );
        }

        if (!Double.isFinite(dailyRate) || dailyRate <= 0) {
            throw new IllegalArgumentException(
                    "Daily rental rate must be positive."
            );
        }

        this.bookingId = bookingId.trim();
        this.vehicleId = vehicleId.trim();
        this.customerName = customerName.trim();
        this.customerPhone = customerPhone.trim();
        this.startDate = startDate;
        this.endDate = endDate;
        this.dailyRate = dailyRate;
    }

    // Get booking ID
    public String getBookingId() {
        return bookingId;
    }

    // Get vehicle ID
    public String getVehicleId() {
        return vehicleId;
    }

    // Get customer details
    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    // Get rental dates
    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    // Get the daily rate saved for this booking
    public double getDailyRate() {
        return dailyRate;
    }

    // Calculate the rental duration
    public long getRentalDays() {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    // Calculate the total rental cost
    public double calculateTotalCost() {
        return dailyRate * getRentalDays();
    }
}