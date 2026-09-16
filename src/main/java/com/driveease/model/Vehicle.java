package com.driveease.model;

public abstract class Vehicle {

    // Vehicle attributes
    private final String vehicleId;
    private String brand;
    private String model;
    private double dailyRate;
    private boolean available;

    // Constructor
    public Vehicle(String vehicleId, String brand,
                   String model, double dailyRate,
                   boolean available) {

        if (vehicleId == null || vehicleId.isBlank()) {
            throw new IllegalArgumentException("Vehicle ID cannot be empty.");
        }

        this.vehicleId = vehicleId.trim();

        setBrand(brand);
        setModel(model);
        setDailyRate(dailyRate);

        this.available = available;
    }

    // Getter for vehicle ID
    public String getVehicleId() {
        return vehicleId;
    }

    // Getter and setter for brand
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        if (brand == null || brand.isBlank()) {
            throw new IllegalArgumentException("Brand cannot be empty.");
        }

        this.brand = brand.trim();
    }

    // Getter and setter for model
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException("Model cannot be empty.");
        }

        this.model = model.trim();
    }

    // Getter and setter for daily rental rate
    public double getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(double dailyRate) {
        if (!Double.isFinite(dailyRate) || dailyRate <= 0) {
            throw new IllegalArgumentException("Daily rate must be a positive number.");
        }

        this.dailyRate = dailyRate;
    }

    // Getter and setter for availability
    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    // Calculate the total rental cost
    public double calculateRentalCost(int rentalDays) {
        if (rentalDays <= 0) {
            throw new IllegalArgumentException("Rental days must be greater than zero.");
        }

        return dailyRate * rentalDays;
    }

    // Get the type of vehicle
    public abstract String getVehicleType();
}