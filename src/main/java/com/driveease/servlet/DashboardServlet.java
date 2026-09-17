package com.driveease.servlet;

import com.driveease.model.Booking;
import com.driveease.model.Vehicle;
import com.driveease.service.BookingService;
import com.driveease.service.VehicleService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private final VehicleService vehicleService = new VehicleService();
    private final BookingService bookingService = new BookingService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        List<Booking> bookings = bookingService.getAllBookings();

        int availableVehicles = 0;
        int unavailableVehicles = 0;

        for (Vehicle vehicle : vehicles) {
            if (vehicle.isAvailable()) {
                availableVehicles++;
            } else {
                unavailableVehicles++;
            }
        }

        Set<String> bookedVehicleIds = new HashSet<>();
        double totalBookingValue = 0.0;

        for (Booking booking : bookings) {
            bookedVehicleIds.add(
                    booking.getVehicleId().toLowerCase(Locale.ROOT)
            );

            totalBookingValue += booking.calculateTotalCost();
        }

        request.setAttribute("totalVehicles", vehicles.size());
        request.setAttribute("availableVehicles", availableVehicles);
        request.setAttribute("unavailableVehicles", unavailableVehicles);

        request.setAttribute("totalBookings", bookings.size());
        request.setAttribute("bookedVehicles", bookedVehicleIds.size());
        request.setAttribute("totalBookingValue", totalBookingValue);

        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp")
                .forward(request, response);
    }
}