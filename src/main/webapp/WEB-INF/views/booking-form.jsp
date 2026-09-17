<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.driveease.model.Booking" %>
<%@ page import="com.driveease.model.Vehicle" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Locale" %>

<%!
    private String escapeHtml(Object value) {
        if (value == null) {
            return "";
        }

        return String.valueOf(value)
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private String formValue(String submittedValue,
                             String savedValue) {

        return submittedValue != null
                ? submittedValue
                : (savedValue == null ? "" : savedValue);
    }
%>

<%
    Booking booking =
            (Booking) request.getAttribute("booking");

    @SuppressWarnings("unchecked")
    List<Vehicle> vehicles =
            (List<Vehicle>) request.getAttribute("vehicles");

    if (vehicles == null) {
        vehicles = List.of();
    }

    String formAction =
            (String) request.getAttribute("formAction");

    boolean editing = "update".equals(formAction);

    if (!editing) {
        formAction = "create";
    }

    String error =
            (String) request.getAttribute("error");

    String contextPath = request.getContextPath();

    String bookingId = formValue(
            request.getParameter("bookingId"),
            booking == null ? "" : booking.getBookingId()
    );

    String vehicleId = formValue(
            request.getParameter("vehicleId"),
            booking == null ? "" : booking.getVehicleId()
    );

    String customerName = formValue(
            request.getParameter("customerName"),
            booking == null ? "" : booking.getCustomerName()
    );

    String customerPhone = formValue(
            request.getParameter("customerPhone"),
            booking == null ? "" : booking.getCustomerPhone()
    );

    String startDate = formValue(
            request.getParameter("startDate"),
            booking == null
                    ? ""
                    : booking.getStartDate().toString()
    );

    String endDate = formValue(
            request.getParameter("endDate"),
            booking == null
                    ? ""
                    : booking.getEndDate().toString()
    );

    boolean hasSelectableVehicle = false;

    for (Vehicle vehicle : vehicles) {

        boolean currentBookedVehicle =
                editing
                && booking != null
                && vehicle.getVehicleId().equalsIgnoreCase(
                        booking.getVehicleId()
                );

        if (vehicle.isAvailable() || currentBookedVehicle) {
            hasSelectableVehicle = true;
            break;
        }
    }
%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>
        <%= editing ? "Edit Booking" : "New Booking" %> | DriveEase
    </title>

    <script src="<%= escapeHtml(contextPath) %>/js/theme.js"></script>

    <link rel="stylesheet"
          href="<%= escapeHtml(contextPath) %>/css/bookings.css">

    <link rel="stylesheet"
          href="<%= escapeHtml(contextPath) %>/css/theme.css">
</head>

<body>

<div class="be-layout">

    <aside class="be-sidebar">

        <a class="be-brand"
           href="<%= escapeHtml(contextPath) %>/dashboard">
            🚘 <span>DriveEase</span>
        </a>

        <p class="be-sidebar-label">MANAGEMENT</p>

        <nav class="be-nav" aria-label="Main navigation">

            <a href="<%= escapeHtml(contextPath) %>/dashboard">
                <span>▦</span> Dashboard
            </a>

            <a href="<%= escapeHtml(contextPath) %>/vehicles">
                <span>🚙</span> Vehicles
            </a>

            <a href="<%= escapeHtml(contextPath) %>/bookings"
               class="be-active"
               aria-current="page">
                <span>▣</span> Bookings
            </a>

        </nav>

        <div class="be-sidebar-note">
            <span class="be-sidebar-note-icon">📅</span>
            <strong>Bookings made easy.</strong>
            <p>Manage your vehicle rentals in one place.</p>
        </div>

        <p class="be-sidebar-footer">
            DriveEase © 2026
        </p>

    </aside>

    <main class="be-main">

        <header class="be-topbar">

            <div>

                <p class="be-eyebrow">
                    RENTAL OPERATIONS
                </p>

                <h1>
                    <%= editing
                            ? "Edit Booking"
                            : "Create New Booking" %>
                </h1>

                <p>
                    <%= editing
                            ? "Update the details of an existing rental."
                            : "Enter the details for a new vehicle rental." %>
                </p>

            </div>

            <a class="be-button be-button-outline"
               href="<%= escapeHtml(contextPath) %>/bookings">
                ← Back to Bookings
            </a>

        </header>

        <section class="be-form-panel">

            <div class="be-form-heading">

                <span class="be-form-heading-icon">
                    📋
                </span>

                <div>

                    <h2>
                        <%= editing
                                ? "Update Rental Details"
                                : "Rental Booking Details" %>
                    </h2>

                    <p>
                        Complete the form below to
                        <%= editing
                                ? "save your changes."
                                : "create a booking." %>
                    </p>

                </div>

            </div>

            <% if (error != null && !error.isBlank()) { %>

                <div class="be-alert be-alert-error" role="alert">
                    <%= escapeHtml(error) %>
                </div>

            <% } %>

            <% if (!hasSelectableVehicle) { %>

                <div class="be-alert be-alert-error" role="alert">

                    No available vehicles found.
                    Please add an available vehicle before
                    creating a booking.

                    <a href="<%= escapeHtml(contextPath) %>/vehicles">
                        Go to Vehicle Management
                    </a>

                </div>

            <% } %>

            <form class="be-booking-form"
                  action="<%= escapeHtml(contextPath) %>/bookings"
                  method="post"
                  accept-charset="UTF-8">

                <input type="hidden"
                       name="action"
                       value="<%= escapeHtml(formAction) %>">

                <div class="be-form-grid">

                    <div class="be-form-field">

                        <label for="bookingId">
                            Booking ID *
                        </label>

                        <input type="text"
                               id="bookingId"
                               name="bookingId"
                               value="<%= escapeHtml(bookingId) %>"
                               placeholder="e.g. B001"
                               maxlength="30"
                               required
                               <%= editing ? "readonly" : "" %>>

                        <% if (editing) { %>

                            <small>
                                Booking ID cannot be changed.
                            </small>

                        <% } %>

                    </div>

                    <div class="be-form-field">

                        <label for="vehicleId">
                            Select Vehicle *
                        </label>

                        <select id="vehicleId"
                                name="vehicleId"
                                required>

                            <option value="">
                                Choose a vehicle
                            </option>

                            <% for (Vehicle vehicle : vehicles) {

                                boolean selected =
                                        vehicle.getVehicleId()
                                                .equalsIgnoreCase(
                                                        vehicleId
                                                );

                                boolean currentBookedVehicle =
                                        editing
                                        && booking != null
                                        && vehicle.getVehicleId()
                                                .equalsIgnoreCase(
                                                        booking.getVehicleId()
                                                );

                                boolean disabled =
                                        !vehicle.isAvailable()
                                        && !currentBookedVehicle;
                            %>

                                <option
                                        value="<%= escapeHtml(
                                                vehicle.getVehicleId()
                                        ) %>"
                                        <%= selected ? "selected" : "" %>
                                        <%= disabled ? "disabled" : "" %>>

                                    <%= escapeHtml(
                                            vehicle.getVehicleId()
                                    ) %>
                                    -
                                    <%= escapeHtml(
                                            vehicle.getBrand()
                                    ) %>
                                    <%= escapeHtml(
                                            vehicle.getModel()
                                    ) %>
                                    (Rs.
                                    <%= String.format(
                                            Locale.US,
                                            "%,.2f",
                                            vehicle.getDailyRate()
                                    ) %>/day)

                                    <%= disabled
                                            ? "- Unavailable"
                                            : "" %>

                                </option>

                            <% } %>

                        </select>

                        <small>
                            Only available vehicles can be
                            selected for new bookings.
                        </small>

                    </div>

                    <div class="be-form-field">

                        <label for="customerName">
                            Customer Name *
                        </label>

                        <input type="text"
                               id="customerName"
                               name="customerName"
                               value="<%= escapeHtml(
                                       customerName
                               ) %>"
                               placeholder="Enter customer's full name"
                               maxlength="100"
                               required>

                    </div>

                    <div class="be-form-field">

                        <label for="customerPhone">
                            Customer Phone *
                        </label>

                        <input type="tel"
                               id="customerPhone"
                               name="customerPhone"
                               value="<%= escapeHtml(
                                       customerPhone
                               ) %>"
                               placeholder="e.g. 0771234567"
                               maxlength="30"
                               required>

                    </div>

                    <div class="be-form-field">

                        <label for="startDate">
                            Pickup Date *
                        </label>

                        <input type="date"
                               id="startDate"
                               name="startDate"
                               value="<%= escapeHtml(startDate) %>"
                               required>

                    </div>

                    <div class="be-form-field">

                        <label for="endDate">
                            Return Date *
                        </label>

                        <input type="date"
                               id="endDate"
                               name="endDate"
                               value="<%= escapeHtml(endDate) %>"
                               required>

                    </div>

                </div>

                <div class="be-form-note">

                    <strong>
                        Rental cost calculation
                    </strong>

                    <p>
                        Total cost is calculated using the
                        vehicle's daily rate and the number
                        of rental days. The return date is
                        not counted as a rental day.
                    </p>

                </div>

                <div class="be-form-actions">

                    <a class="be-button be-button-outline"
                       href="<%= escapeHtml(contextPath) %>/bookings">
                        Cancel
                    </a>

                    <button class="be-button be-button-primary"
                            type="submit"
                            <%= hasSelectableVehicle
                                    ? ""
                                    : "disabled" %>>

                        <%= editing
                                ? "Save Changes"
                                : "Create Booking" %>

                    </button>

                </div>

            </form>

        </section>

        <footer class="be-footer">
            © 2026 DriveEase. Vehicle Rental Service Platform.
        </footer>

    </main>

</div>

</body>
</html>