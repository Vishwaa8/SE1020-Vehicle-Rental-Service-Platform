package com.driveease.model;

public class Bike extends Vehicle {

    // Constructor
    public Bike(String vehicleId, String brand,
                String model, double dailyRate,
                boolean available) {

        super(vehicleId, brand, model, dailyRate, available);
    }

    // Return the vehicle type
    @Override
    public String getVehicleType() {
        return "Bike";
    }
}