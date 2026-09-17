<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
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

    List<Vehicle> vehicles = new ArrayList<>();
    Object vehicleData = request.getAttribute("vehicles");

    if (vehicleData instanceof List<?>) {
        for (Object item : (List<?>) vehicleData) {
            if (item instanceof Vehicle) {
                vehicles.add((Vehicle) item);
            }
        }
    }

    String search = request.getParameter("search");

    if (search == null) {
        search = "";
    }

    String status = request.getParameter("status");

    int totalVehicles = vehicles.size();
    int availableVehicles = 0;
    int unavailableVehicles = 0;

    for (Vehicle vehicle : vehicles) {
        if (vehicle.isAvailable()) {
            availableVehicles++;
        } else {
            unavailableVehicles++;
        }
    }
%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Vehicle Management | DriveEase</title>

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

        <!-- Topbar -->
        <header class="topbar">

            <div class="page-heading">

                <p class="breadcrumb">
                    DriveEase / Management
                </p>

                <h1>Vehicle Management</h1>

                <p class="page-subtitle">
                    Manage your fleet and vehicle availability.
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

        <!-- Welcome banner -->
        <section class="hero-banner">

            <div class="hero-content">

                <span class="hero-tag">
                    FLEET MANAGEMENT
                </span>

                <h2>
                    Your vehicles,
                    <br>
                    all in one place.
                </h2>

                <p>
                    Add, organize, and manage your
                    rental vehicles with ease.
                </p>

                <a href="<%= escapeHtml(contextPath) %>/vehicles?action=new"
                   class="hero-button">
                    + Add New Vehicle
                </a>

            </div>

            <div class="hero-decoration" aria-hidden="true">
                🚙
            </div>

        </section>

        <!-- Statistics -->
        <section class="stats-grid">

            <div class="stat-card">

                <div class="stat-top">

                    <span class="stat-icon blue">🚘</span>

                    <span class="stat-label">
                        Total Fleet
                    </span>

                </div>

                <h3><%= totalVehicles %></h3>

                <p>Vehicles in your system</p>

            </div>

            <div class="stat-card">

                <div class="stat-top">

                    <span class="stat-icon green">✓</span>

                    <span class="stat-label">
                        Available Now
                    </span>

                </div>

                <h3><%= availableVehicles %></h3>

                <p>Available vehicle records</p>

            </div>

            <div class="stat-card">

                <div class="stat-top">

                    <span class="stat-icon orange">◷</span>

                    <span class="stat-label">
                        Unavailable
                    </span>

                </div>

                <h3><%= unavailableVehicles %></h3>

                <p>Currently marked unavailable</p>

            </div>

        </section>

        <!-- Vehicle management -->
        <section class="content-card">

            <div class="section-header">

                <div>

                    <h2>All Vehicles</h2>

                    <p>
                        View, search and manage
                        your vehicle records.
                    </p>

                </div>

                <a href="<%= escapeHtml(contextPath) %>/vehicles?action=new"
                   class="primary-button">
                    + Add Vehicle
                </a>

            </div>

            <!-- Success messages -->
            <% if ("created".equals(status)) { %>

                <div class="alert success" role="status">
                    ✓ Vehicle added successfully.
                </div>

            <% } else if ("updated".equals(status)) { %>

                <div class="alert success" role="status">
                    ✓ Vehicle updated successfully.
                </div>

            <% } else if ("deleted".equals(status)) { %>

                <div class="alert success" role="status">
                    ✓ Vehicle deleted successfully.
                </div>

            <% } %>

            <!-- Search form -->
            <form action="<%= escapeHtml(contextPath) %>/vehicles"
                  method="get"
                  class="search-form">

                <div class="search-input-wrapper">

                    <span class="search-icon" aria-hidden="true">
                        ⌕
                    </span>

                    <input type="search"
                           name="search"
                           value="<%= escapeHtml(search) %>"
                           placeholder="Search by ID, brand, model or type..."
                           aria-label="Search vehicles">

                </div>

                <button type="submit"
                        class="search-button">
                    Search
                </button>

                <% if (!search.isBlank()) { %>

                    <a href="<%= escapeHtml(contextPath) %>/vehicles"
                       class="clear-button">
                        Clear
                    </a>

                <% } %>

            </form>

            <!-- Vehicle table -->
            <div class="table-container">

                <table>

                    <thead>
                    <tr>
                        <th>VEHICLE ID</th>
                        <th>VEHICLE</th>
                        <th>TYPE</th>
                        <th>DAILY RATE</th>
                        <th>STATUS</th>
                        <th>ACTIONS</th>
                    </tr>
                    </thead>

                    <tbody>

                    <% if (vehicles.isEmpty()) { %>

                        <tr>

                            <td colspan="6"
                                class="empty-state">

                                <div class="empty-icon">
                                    🚘
                                </div>

                                <h3>No vehicles found</h3>

                                <p>
                                    <% if (!search.isBlank()) { %>
                                        Try a different search term.
                                    <% } else { %>
                                        Start by adding your first vehicle.
                                    <% } %>
                                </p>

                                <% if (search.isBlank()) { %>

                                    <a href="<%= escapeHtml(contextPath) %>/vehicles?action=new"
                                       class="primary-button">
                                        + Add First Vehicle
                                    </a>

                                <% } %>

                            </td>

                        </tr>

                    <% } else { %>

                        <% for (Vehicle vehicle : vehicles) {

                            String vehicleId = vehicle.getVehicleId();

                            String encodedId = URLEncoder.encode(
                                    vehicleId,
                                    StandardCharsets.UTF_8
                            );

                            String vehicleType = vehicle.getVehicleType();

                            String dailyRate = String.format(
                                    Locale.US,
                                    "%,.2f",
                                    vehicle.getDailyRate()
                            );

                        %>

                            <tr>

                                <td>

                                    <span class="vehicle-id">
                                        <%= escapeHtml(vehicleId) %>
                                    </span>

                                </td>

                                <td>

                                    <div class="vehicle-info">

                                        <div class="vehicle-thumbnail">
                                            🚗
                                        </div>

                                        <div class="vehicle-details">

                                            <strong>
                                                <%= escapeHtml(vehicle.getBrand()) %>
                                                <%= escapeHtml(vehicle.getModel()) %>
                                            </strong>

                                            <span>
                                                Rental Vehicle
                                            </span>

                                        </div>

                                    </div>

                                </td>

                                <td>

                                    <span class="type-badge">
                                        <%= escapeHtml(vehicleType) %>
                                    </span>

                                </td>

                                <td>

                                    <span class="price">
                                        Rs. <%= dailyRate %>
                                    </span>

                                    <span class="price-period">
                                        / day
                                    </span>

                                </td>

                                <td>

                                    <% if (vehicle.isAvailable()) { %>

                                        <span class="status-badge available">
                                            <span class="status-dot"></span>
                                            Available
                                        </span>

                                    <% } else { %>

                                        <span class="status-badge unavailable">
                                            <span class="status-dot"></span>
                                            Unavailable
                                        </span>

                                    <% } %>

                                </td>

                                <td>

                                    <div class="action-buttons">

                                        <a href="<%= escapeHtml(contextPath) %>/vehicles?action=edit&amp;id=<%= escapeHtml(encodedId) %>"
                                           class="edit-button">
                                            Edit
                                        </a>

                                        <form action="<%= escapeHtml(contextPath) %>/vehicles"
                                              method="post"
                                              onsubmit="return confirm('Are you sure you want to delete this vehicle?');">

                                            <input type="hidden"
                                                   name="action"
                                                   value="delete">

                                            <input type="hidden"
                                                   name="id"
                                                   value="<%= escapeHtml(vehicleId) %>">

                                            <button type="submit"
                                                    class="delete-button">
                                                Delete
                                            </button>

                                        </form>

                                    </div>

                                </td>

                            </tr>

                        <% } %>

                    <% } %>

                    </tbody>

                </table>

            </div>

            <div class="table-footer">

                <span>
                    Showing <%= vehicles.size() %> vehicle(s)
                </span>

                <span>
                    DriveEase Fleet Management
                </span>

            </div>

        </section>

        <footer class="main-footer">
            © 2026 DriveEase. Vehicle Rental Service Platform.
        </footer>

    </main>

</div>

</body>
</html>