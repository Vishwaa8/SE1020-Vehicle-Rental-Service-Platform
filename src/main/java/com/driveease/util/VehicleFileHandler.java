package com.driveease.util;

import com.driveease.model.Bike;
import com.driveease.model.Car;
import com.driveease.model.Van;
import com.driveease.model.Vehicle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

public class VehicleFileHandler {

    private final Path filePath;

    // Constructor
    public VehicleFileHandler() {
        this(Path.of(
                System.getProperty("user.home"),
                "DriveEase",
                "data",
                "vehicles.txt"
        ));
    }

    public VehicleFileHandler(Path filePath) {
        this.filePath = Objects.requireNonNull(filePath);
    }

    // Get the vehicle data file location
    public Path getFilePath() {
        return filePath;
    }

    // Save vehicle records to the text file
    public void saveVehicles(List<Vehicle> vehicles) throws IOException {

        Objects.requireNonNull(vehicles);

        Path destination = filePath.toAbsolutePath();
        Path directory = destination.getParent();

        Files.createDirectories(directory);

        Path tempFile = Files.createTempFile(
                directory, "vehicles-", ".tmp"
        );

        try {

            try (BufferedWriter writer = Files.newBufferedWriter(
                    tempFile, StandardCharsets.UTF_8)) {

                for (Vehicle vehicle : vehicles) {

                    String record = String.join("\t",
                            vehicle.getVehicleType(),
                            encode(vehicle.getVehicleId()),
                            encode(vehicle.getBrand()),
                            encode(vehicle.getModel()),
                            String.valueOf(vehicle.getDailyRate()),
                            String.valueOf(vehicle.isAvailable())
                    );

                    writer.write(record);
                    writer.newLine();
                }
            }

            // Replace the existing file with the updated records
            try {
                Files.move(
                        tempFile,
                        destination,
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE
                );

            } catch (AtomicMoveNotSupportedException e) {

                Files.move(
                        tempFile,
                        destination,
                        StandardCopyOption.REPLACE_EXISTING
                );
            }

        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    // Read vehicle records from the text file
    public List<Vehicle> loadVehicles() throws IOException {

        List<Vehicle> vehicles = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return vehicles;
        }

        try (BufferedReader reader = Files.newBufferedReader(
                filePath, StandardCharsets.UTF_8)) {

            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {

                lineNumber++;

                if (line.isBlank()) {
                    continue;
                }

                String[] data = line.split("\t", -1);

                if (data.length != 6) {
                    throw new IOException(
                            "Invalid vehicle record at line " + lineNumber
                    );
                }

                try {

                    String type = data[0];
                    String vehicleId = decode(data[1]);
                    String brand = decode(data[2]);
                    String model = decode(data[3]);

                    double dailyRate = Double.parseDouble(data[4]);

                    if (!data[5].equals("true")
                            && !data[5].equals("false")) {

                        throw new IllegalArgumentException(
                                "Invalid availability value."
                        );
                    }

                    boolean available = Boolean.parseBoolean(data[5]);

                    Vehicle vehicle;

                    switch (type) {

                        case "Car":
                            vehicle = new Car(
                                    vehicleId, brand, model,
                                    dailyRate, available
                            );
                            break;

                        case "Van":
                            vehicle = new Van(
                                    vehicleId, brand, model,
                                    dailyRate, available
                            );
                            break;

                        case "Bike":
                            vehicle = new Bike(
                                    vehicleId, brand, model,
                                    dailyRate, available
                            );
                            break;

                        default:
                            throw new IllegalArgumentException(
                                    "Unknown vehicle type: " + type
                            );
                    }

                    vehicles.add(vehicle);

                } catch (IllegalArgumentException e) {

                    throw new IOException(
                            "Invalid vehicle data at line "
                                    + lineNumber, e
                    );
                }
            }
        }

        return vehicles;
    }

    // Encode text fields for safe file storage
    private String encode(String value) {
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(
                        value.getBytes(StandardCharsets.UTF_8)
                );
    }

    // Decode stored text fields
    private String decode(String value) {
        return new String(
                Base64.getUrlDecoder().decode(value),
                StandardCharsets.UTF_8
        );
    }
}