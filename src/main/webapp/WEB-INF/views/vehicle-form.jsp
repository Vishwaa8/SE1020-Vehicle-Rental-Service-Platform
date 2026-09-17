<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.driveease.model.Vehicle" %>

<%!
    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }

        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
%>

<%
    String contextPath = request.getContextPath();

    Vehicle vehicle = (Vehicle) request.getAttribute("vehicle");

    String formAction = (String) request.getAttribute("formAction");
    boolean isEdit = "update".equals(formAction);

    String error = (String) request.getAttribute("error");

    String vehicleId = request.getParameter("vehicleId");
    String brand = request.getParameter("brand");
    String model = request.getParameter("model");
    String type = request.getParameter("type");
    String dailyRate = request.getParameter("dailyRate");
    String available = request.getParameter("available");

    if (vehicleId == null) {
        vehicleId = vehicle != null ? vehicle.getVehicleId() : "";
    }

    if (brand == null) {
        brand = vehicle != null ? vehicle.getBrand() : "";
    }

    if (model == null) {
        model = vehicle != null ? vehicle.getModel() : "";
    }

    if (type == null) {
        type = vehicle != null ? vehicle.getVehicleType() : "";
    }

    if (dailyRate == null) {
        dailyRate = vehicle != null
                ? String.valueOf(vehicle.getDailyRate())
                : "";
    }

    if (available == null) {
        available = vehicle != null
                ? String.valueOf(vehicle.isAvailable())
                : "";
    }
%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>
        <%= isEdit ? "Edit Vehicle" : "Add Vehicle" %> | DriveEase
    </title>

    <script src="<%= escapeHtml(contextPath) %>/js/theme.js"></script>

    <link rel="stylesheet"
          href="<%= escapeHtml(contextPath) %>/css/vehicles.css">

    <link rel="stylesheet"
          href="<%= escapeHtml(contextPath) %>/css/theme.css">
</head>

<body>

<div class="app-layout">

    <!-- Sidebar -->
    <aside class="sidebar">

        <div class="brand">

            <div class="brand-icon">🚗</div>

            <div>
                <h2>DriveEase</h2>
                <p>Vehicle Rental Platform</p>
            </div>

        </div>

        <div class="menu-label">MAIN MENU</div>

        <nav class="navigation" aria-label="Main navigation">

            <a href="<%= escapeHtml(contextPath) %>/dashboard"
               class="nav-link">
                <span>▦</span>
                Dashboard
            </a>

            <a href="<%= escapeHtml(contextPath) %>/vehicles"
               class="nav-link active"
               aria-current="page">
                <span>🚘</span>
                Vehicles
            </a>

            <a href="<%= escapeHtml(contextPath) %>/bookings"
               class="nav-link">
                <span>▣</span>
                Bookings
            </a>

            <span class="nav-link disabled">
                <span>♙</span>
                Customers
                <small>Soon</small>
            </span>

        </nav>

        <div class="sidebar-bottom">

            <div class="sidebar-card">

                <span>🚗</span>

                <h3>Drive with ease.</h3>

                <p>
                    Manage your rental fleet
                    in one place.
                </p>

            </div>

            <p class="sidebar-footer">
                DriveEase © 2026
            </p>

        </div>

    </aside>

    <!-- Main content -->
    <main class="main-content">

        <header class="topbar">

            <div class="page-heading">

                <p class="breadcrumb">
                    DriveEase / Vehicles /
                    <%= isEdit ? "Edit Vehicle" : "Add Vehicle" %>
                </p>

                <h1>
                    <%= isEdit ? "Edit Vehicle" : "Add New Vehicle" %>
                </h1>

                <p class="page-subtitle">
                    <%= isEdit
                            ? "Update your vehicle information."
                            : "Add a new vehicle to your rental fleet." %>
                </p>

            </div>

            <div class="admin-profile">

                <div class="avatar">AD</div>

                <div class="admin-details">
                    <strong>Administrator</strong>
                    <span>Management Panel</span>
                </div>

            </div>

        </header>

        <!-- Back link -->
        <a href="<%= escapeHtml(contextPath) %>/vehicles"
           class="back-link">
            ← Back to Vehicles
        </a>

        <!-- Vehicle form card -->
        <section class="content-card vehicle-form-card">

            <div class="form-header">

                <div class="form-header-icon">
                    🚘
                </div>

                <div>

                    <h2>
                        <%= isEdit
                                ? "Update Vehicle Details"
                                : "Vehicle Information" %>
                    </h2>

                    <p>
                        Fill in the vehicle information below.
                    </p>

                </div>

            </div>

            <% if (error != null && !error.isBlank()) { %>

                <div class="form-alert" role="alert">
                    <%= escapeHtml(error) %>
                </div>

            <% } %>

            <form action="<%= escapeHtml(contextPath) %>/vehicles"
                  method="post"
                  class="vehicle-form">

                <input type="hidden"
                       name="action"
                       value="<%= isEdit ? "update" : "create" %>">

                <!-- Vehicle ID -->
                <div class="form-group">

                    <label for="vehicleId">
                        Vehicle ID
                        <span class="required">*</span>
                    </label>

                    <input type="text"
                           id="vehicleId"
                           name="vehicleId"
                           value="<%= escapeHtml(vehicleId) %>"
                           placeholder="e.g. C001"
                           maxlength="30"
                           required
                           <%= isEdit ? "readonly" : "" %>>

                    <small>
                        <%= isEdit
                                ? "Vehicle ID cannot be changed."
                                : "Enter a unique vehicle ID." %>
                    </small>

                </div>

                <!-- Brand and model -->
                <div class="form-row">

                    <div class="form-group">

                        <label for="brand">
                            Vehicle Brand
                            <span class="required">*</span>
                        </label>

                        <input type="text"
                               id="brand"
                               name="brand"
                               value="<%= escapeHtml(brand) %>"
                               placeholder="e.g. Toyota"
                               maxlength="100"
                               required>

                    </div>

                    <div class="form-group">

                        <label for="model">
                            Vehicle Model
                            <span class="required">*</span>
                        </label>

                        <input type="text"
                               id="model"
                               name="model"
                               value="<%= escapeHtml(model) %>"
                               placeholder="e.g. Axio"
                               maxlength="100"
                               required>

                    </div>

                </div>

                <!-- Vehicle type and daily rate -->
                <div class="form-row">

                    <div class="form-group">

                        <label for="type">
                            Vehicle Type
                            <span class="required">*</span>
                        </label>

                        <select id="type"
                                name="type"
                                required>

                            <option value=""
                                    <%= type.isEmpty() ? "selected" : "" %>
                                    disabled>
                                Select vehicle type
                            </option>

                            <option value="Car"
                                    <%= "Car".equals(type) ? "selected" : "" %>>
                                Car
                            </option>

                            <option value="Van"
                                    <%= "Van".equals(type) ? "selected" : "" %>>
                                Van
                            </option>

                            <option value="Bike"
                                    <%= "Bike".equals(type) ? "selected" : "" %>>
                                Bike
                            </option>

                        </select>

                    </div>

                    <div class="form-group">

                        <label for="dailyRate">
                            Daily Rental Rate (LKR)
                            <span class="required">*</span>
                        </label>

                        <input type="number"
                               id="dailyRate"
                               name="dailyRate"
                               value="<%= escapeHtml(dailyRate) %>"
                               placeholder="e.g. 8500.00"
                               min="0.01"
                               step="0.01"
                               required>

                    </div>

                </div>

                <!-- Availability -->
                <div class="form-group">

                    <label for="available">
                        Vehicle Availability
                        <span class="required">*</span>
                    </label>

                    <select id="available"
                            name="available"
                            required>

                        <option value=""
                                <%= available.isEmpty() ? "selected" : "" %>
                                disabled>
                            Select availability
                        </option>

                        <option value="true"
                                <%= "true".equals(available) ? "selected" : "" %>>
                            Available
                        </option>

                        <option value="false"
                                <%= "false".equals(available) ? "selected" : "" %>>
                            Unavailable
                        </option>

                    </select>

                    <small>
                        Select the current availability of this vehicle.
                    </small>

                </div>

                <!-- Form actions -->
                <div class="form-actions">

                    <a href="<%= escapeHtml(contextPath) %>/vehicles"
                       class="cancel-button">
                        Cancel
                    </a>

                    <button type="submit"
                            class="save-button">
                        <%= isEdit
                                ? "Save Changes"
                                : "Add Vehicle" %>
                    </button>

                </div>

            </form>

        </section>

        <footer class="main-footer">
            © 2026 DriveEase. Vehicle Rental Service Platform.
        </footer>

    </main>

</div>

</body>
</html>