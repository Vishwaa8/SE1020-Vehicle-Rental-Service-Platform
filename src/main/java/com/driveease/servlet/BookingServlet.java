package com.driveease.servlet;

import com.driveease.model.Booking;
import com.driveease.service.BookingService;
import com.driveease.service.VehicleService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@WebServlet("/bookings")
public class BookingServlet extends HttpServlet {

    private final BookingService bookingService = new BookingService();
    private final VehicleService vehicleService = new VehicleService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null || action.isBlank()) {
            action = "list";
        }

        switch (action) {
            case "list" -> showBookings(request, response);

            case "new" -> {
                request.setAttribute("formAction", "create");
                request.setAttribute(
                        "vehicles",
                        vehicleService.getAllVehicles()
                );

                request.getRequestDispatcher(
                        "/WEB-INF/views/booking-form.jsp"
                ).forward(request, response);
            }

            case "edit" -> {
                String bookingId = request.getParameter("bookingId");
                Booking booking =
                        bookingService.getBookingById(bookingId);

                if (booking == null) {
                    redirectWithStatus(response, request, "notfound");
                    return;
                }

                request.setAttribute("booking", booking);
                request.setAttribute("formAction", "update");
                request.setAttribute(
                        "vehicles",
                        vehicleService.getAllVehicles()
                );

                request.getRequestDispatcher(
                        "/WEB-INF/views/booking-form.jsp"
                ).forward(request, response);
            }

            default -> redirectWithStatus(
                    response, request, "invalid"
            );
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if (action == null) {
            action = "";
        }

        if ("delete".equals(action)) {
            try {
                bookingService.deleteBooking(
                        request.getParameter("bookingId")
                );

                redirectWithStatus(response, request, "deleted");

            } catch (IllegalArgumentException exception) {
                request.setAttribute("error", exception.getMessage());
                showBookings(request, response);
            }

            return;
        }

        if (!"create".equals(action) && !"update".equals(action)) {
            redirectWithStatus(response, request, "invalid");
            return;
        }

        try {
            String bookingId = request.getParameter("bookingId");
            String vehicleId = request.getParameter("vehicleId");
            String customerName = request.getParameter("customerName");
            String customerPhone = request.getParameter("customerPhone");

            LocalDate startDate = parseDate(
                    request.getParameter("startDate"),
                    "Pickup date"
            );

            LocalDate endDate = parseDate(
                    request.getParameter("endDate"),
                    "Return date"
            );

            if ("create".equals(action)) {
                bookingService.addBooking(
                        bookingId,
                        vehicleId,
                        customerName,
                        customerPhone,
                        startDate,
                        endDate
                );

                redirectWithStatus(response, request, "created");

            } else {
                bookingService.updateBooking(
                        bookingId,
                        vehicleId,
                        customerName,
                        customerPhone,
                        startDate,
                        endDate
                );

                redirectWithStatus(response, request, "updated");
            }

        } catch (IllegalArgumentException exception) {
            request.setAttribute("error", exception.getMessage());
            request.setAttribute("formAction", action);
            request.setAttribute(
                    "vehicles",
                    vehicleService.getAllVehicles()
            );

            if ("update".equals(action)) {
                request.setAttribute(
                        "booking",
                        bookingService.getBookingById(
                                request.getParameter("bookingId")
                        )
                );
            }

            request.getRequestDispatcher(
                    "/WEB-INF/views/booking-form.jsp"
            ).forward(request, response);
        }
    }

    private void showBookings(HttpServletRequest request,
                              HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("search");

        request.setAttribute(
                "bookings",
                bookingService.searchBookings(keyword)
        );

        request.setAttribute(
                "search",
                keyword == null ? "" : keyword
        );

        request.getRequestDispatcher(
                "/WEB-INF/views/bookings.jsp"
        ).forward(request, response);
    }

    private LocalDate parseDate(String value, String fieldName) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " is required."
            );
        }

        try {
            return LocalDate.parse(value);

        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException(
                    "Invalid " + fieldName.toLowerCase() + "."
            );
        }
    }

    private void redirectWithStatus(HttpServletResponse response,
                                    HttpServletRequest request,
                                    String status)
            throws IOException {

        response.sendRedirect(
                request.getContextPath()
                        + "/bookings?status="
                        + status
        );
    }
}