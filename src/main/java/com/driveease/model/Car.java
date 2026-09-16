package com.driveease.model;

public class Car extends Vehicle {

    // Constructor
    public Car(String vehicleId, String brand,
               String model, double dailyRate,
               boolean available) {

        super(vehicleId, brand, model, dailyRate, available);
    }

    // Return the vehicle type
    @Override
    public String getVehicleType() {
        return "Car";
    }
}