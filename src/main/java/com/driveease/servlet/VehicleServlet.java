package com.driveease.servlet;

import com.driveease.model.Bike;
import com.driveease.model.Car;
import com.driveease.model.Van;
import com.driveease.model.Vehicle;
import com.driveease.service.BookingService;
import com.driveease.service.VehicleService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/vehicles")
public class VehicleServlet extends HttpServlet {

    private final VehicleService vehicleService = new VehicleService();
    private final BookingService bookingService = new BookingService();

    // Handle vehicle pages and search requests
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null || action.isBlank()) {
            action = "list";
        }

        try {

            switch (action) {

                case "list":
                    listVehicles(request, response);
                    break;

                case "new":
                    showNewVehicleForm(request, response);
                    break;

                case "edit":
                    showEditVehicleForm(request, response);
                    break;

                default:
                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid vehicle action."
                    );
                    break;
            }

        } catch (IOException e) {

            getServletContext().log(
                    "Error loading vehicle information.", e
            );

            if (!response.isCommitted()) {
                response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Unable to load vehicle information."
                );
            }
        }
    }

    // Handle create, update and delete requests
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if (action == null) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Vehicle action is required."
            );

            return;
        }

        try {

            switch (action) {

                case "create":
                    createVehicle(request, response);
                    break;

                case "update":
                    updateVehicle(request, response);
                    break;

                case "delete":
                    deleteVehicle(request, response);
                    break;

                default:
                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid vehicle action."
                    );
                    break;
            }

        } catch (IllegalArgumentException e) {

            if ("create".equals(action)
                    || "update".equals(action)) {

                request.setAttribute("error", e.getMessage());
                request.setAttribute("formAction", action);

                request.getRequestDispatcher(
                        "/WEB-INF/views/vehicle-form.jsp"
                ).forward(request, response);

            } else {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        e.getMessage()
                );
            }

        } catch (IOException e) {

            getServletContext().log(
                    "Error saving vehicle information.", e
            );

            if (!response.isCommitted()) {
                response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Unable to save vehicle information."
                );
            }
        }
    }

    // Display all vehicles or search results
    private void listVehicles(HttpServletRequest request,
                              HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("search");

        List<Vehicle> vehicles;

        if (keyword == null || keyword.isBlank()) {

            vehicles = vehicleService.getAllVehicles();

        } else {

            vehicles = vehicleService.searchVehicles(keyword);
        }

        request.setAttribute("vehicles", vehicles);

        request.getRequestDispatcher(
                "/WEB-INF/views/vehicles.jsp"
        ).forward(request, response);
    }

    // Display the add vehicle form
    private void showNewVehicleForm(HttpServletRequest request,
                                    HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("formAction", "create");

        request.getRequestDispatcher(
                "/WEB-INF/views/vehicle-form.jsp"
        ).forward(request, response);
    }

    // Display the edit vehicle form
    private void showEditVehicleForm(HttpServletRequest request,
                                     HttpServletResponse response)
            throws ServletException, IOException {

        String vehicleId = request.getParameter("id");

        if (vehicleId == null || vehicleId.isBlank()) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Vehicle ID is required."
            );

            return;
        }

        Vehicle vehicle = vehicleService
                .getVehicleById(vehicleId)
                .orElse(null);

        if (vehicle == null) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Vehicle not found."
            );

            return;
        }

        request.setAttribute("vehicle", vehicle);
        request.setAttribute("formAction", "update");

        request.getRequestDispatcher(
                "/WEB-INF/views/vehicle-form.jsp"
        ).forward(request, response);
    }

    // Create a new vehicle
    private void createVehicle(HttpServletRequest request,
                               HttpServletResponse response)
            throws IOException {

        Vehicle vehicle = buildVehicle(request);

        vehicleService.addVehicle(vehicle);

        redirectToVehicles(request, response, "created");
    }

    // Update an existing vehicle
    private void updateVehicle(HttpServletRequest request,
                               HttpServletResponse response)
            throws IOException {

        Vehicle vehicle = buildVehicle(request);

        boolean updated = vehicleService.updateVehicle(vehicle);

        if (!updated) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Vehicle not found."
            );

            return;
        }

        redirectToVehicles(request, response, "updated");
    }

    // Delete a vehicle only if it has no existing bookings
    private void deleteVehicle(HttpServletRequest request,
                               HttpServletResponse response)
            throws IOException {

        String vehicleId = request.getParameter("id");

        if (vehicleId == null || vehicleId.isBlank()) {

            throw new IllegalArgumentException(
                    "Vehicle ID is required."
            );
        }

        String requestedId = vehicleId.trim();

        boolean hasBookings = bookingService.getAllBookings()
                .stream()
                .anyMatch(booking ->
                        booking.getVehicleId()
                                .equalsIgnoreCase(requestedId)
                );

        if (hasBookings) {

            response.sendError(
                    HttpServletResponse.SC_CONFLICT,
                    "This vehicle has existing bookings. " +
                            "Delete its bookings before deleting the vehicle."
            );

            return;
        }

        boolean deleted = vehicleService.deleteVehicle(requestedId);

        if (!deleted) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Vehicle not found."
            );

            return;
        }

        redirectToVehicles(request, response, "deleted");
    }

    // Create a vehicle object using submitted form data
    private Vehicle buildVehicle(HttpServletRequest request) {

        String vehicleId = request.getParameter("vehicleId");
        String brand = request.getParameter("brand");
        String model = request.getParameter("model");
        String type = request.getParameter("type");

        String rateValue = request.getParameter("dailyRate");
        String availabilityValue = request.getParameter("available");

        if (rateValue == null || rateValue.isBlank()) {

            throw new IllegalArgumentException(
                    "Daily rental rate is required."
            );
        }

        double dailyRate;

        try {

            dailyRate = Double.parseDouble(rateValue.trim());

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    "Please enter a valid daily rental rate."
            );
        }

        if (availabilityValue == null
                || (!availabilityValue.equals("true")
                && !availabilityValue.equals("false"))) {

            throw new IllegalArgumentException(
                    "Please select vehicle availability."
            );
        }

        boolean available = Boolean.parseBoolean(availabilityValue);

        if (type == null) {

            throw new IllegalArgumentException(
                    "Please select a vehicle type."
            );
        }

        switch (type) {

            case "Car":
                return new Car(
                        vehicleId, brand, model,
                        dailyRate, available
                );

            case "Van":
                return new Van(
                        vehicleId, brand, model,
                        dailyRate, available
                );

            case "Bike":
                return new Bike(
                        vehicleId, brand, model,
                        dailyRate, available
                );

            default:
                throw new IllegalArgumentException(
                        "Invalid vehicle type."
                );
        }
    }

    // Redirect to vehicle list after a successful operation
    private void redirectToVehicles(HttpServletRequest request,
                                    HttpServletResponse response,
                                    String status)
            throws IOException {

        response.sendRedirect(
                request.getContextPath()
                        + "/vehicles?status=" + status
        );
    }
}