package com.driveease.model;

public class Van extends Vehicle {

    // Constructor
    public Van(String vehicleId, String brand,
               String model, double dailyRate,
               boolean available) {

        super(vehicleId, brand, model, dailyRate, available);
    }

    // Return the vehicle type
    @Override
    public String getVehicleType() {
        return "Van";
    }
}