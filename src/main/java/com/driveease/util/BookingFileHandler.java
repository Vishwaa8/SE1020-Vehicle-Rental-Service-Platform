package com.driveease.util;

import com.driveease.model.Booking;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

public class BookingFileHandler {

    private final Path filePath;

    public BookingFileHandler() {
        this(Path.of(
                System.getProperty("user.home"),
                "DriveEase",
                "data",
                "bookings.txt"
        ));
    }

    public BookingFileHandler(Path filePath) {
        this.filePath = Objects.requireNonNull(
                filePath, "File path cannot be null."
        ).toAbsolutePath().normalize();
    }

    public Path getFilePath() {
        return filePath;
    }

    public void saveBookings(List<Booking> bookings) throws IOException {
        Objects.requireNonNull(bookings, "Bookings cannot be null.");

        Path directory = filePath.getParent();
        Files.createDirectories(directory);

        List<String> lines = new ArrayList<>();

        for (Booking booking : bookings) {
            Objects.requireNonNull(
                    booking, "Booking list cannot contain null."
            );

            String line = String.join("\t",
                    encode(booking.getBookingId()),
                    encode(booking.getVehicleId()),
                    encode(booking.getCustomerName()),
                    encode(booking.getCustomerPhone()),
                    booking.getStartDate().toString(),
                    booking.getEndDate().toString(),
                    Double.toString(booking.getDailyRate())
            );

            lines.add(line);
        }

        Path tempFile = Files.createTempFile(
                directory, "bookings-", ".tmp"
        );

        try {
            Files.write(tempFile, lines, StandardCharsets.UTF_8);

            try {
                Files.move(
                        tempFile,
                        filePath,
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE
                );
            } catch (AtomicMoveNotSupportedException exception) {
                Files.move(
                        tempFile,
                        filePath,
                        StandardCopyOption.REPLACE_EXISTING
                );
            }
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    public List<Booking> loadBookings() throws IOException {
        List<Booking> bookings = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return bookings;
        }

        List<String> lines = Files.readAllLines(
                filePath, StandardCharsets.UTF_8
        );

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            if (line.isBlank()) {
                continue;
            }

            String[] values = line.split("\t", -1);

            if (values.length != 7) {
                throw new IOException(
                        "Invalid booking record at line " + (i + 1)
                );
            }

            try {
                Booking booking = new Booking(
                        decode(values[0]),
                        decode(values[1]),
                        decode(values[2]),
                        decode(values[3]),
                        LocalDate.parse(values[4]),
                        LocalDate.parse(values[5]),
                        Double.parseDouble(values[6])
                );

                bookings.add(booking);
            } catch (RuntimeException exception) {
                throw new IOException(
                        "Invalid booking data at line " + (i + 1),
                        exception
                );
            }
        }

        return bookings;
    }

    private String encode(String value) {
        return Base64.getUrlEncoder()
                .encodeToString(
                        value.getBytes(StandardCharsets.UTF_8)
                );
    }

    private String decode(String value) {
        return new String(
                Base64.getUrlDecoder().decode(value),
                StandardCharsets.UTF_8
        );
    }
}