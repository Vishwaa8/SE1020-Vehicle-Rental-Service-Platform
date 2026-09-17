<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.driveease.model.Booking" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.HashSet" %>
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
%>

<%
    @SuppressWarnings("unchecked")
    List<Booking> bookings =
            (List<Booking>) request.getAttribute("bookings");

    if (bookings == null) {
        bookings = List.of();
    }

    String search = (String) request.getAttribute("search");
    if (search == null) {
        search = "";
    }

    String status = request.getParameter("status");
    String error = (String) request.getAttribute("error");
    String contextPath = request.getContextPath();

    String successMessage = null;

    if ("created".equals(status)) {
        successMessage = "Booking created successfully.";
    } else if ("updated".equals(status)) {
        successMessage = "Booking updated successfully.";
    } else if ("deleted".equals(status)) {
        successMessage = "Booking deleted successfully.";
    }

    Set<String> bookedVehicleIds = new HashSet<>();
    double totalBookingValue = 0.0;

    for (Booking booking : bookings) {
        bookedVehicleIds.add(
                booking.getVehicleId().toLowerCase(Locale.ROOT)
        );

        totalBookingValue += booking.calculateTotalCost();
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Booking Management | DriveEase</title>

    <link rel="stylesheet"
          href="<%= escapeHtml(contextPath) %>/css/bookings.css">
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

        <p class="be-sidebar-footer">DriveEase © 2026</p>
    </aside>

    <main class="be-main">

        <header class="be-topbar">
            <div>
                <p class="be-eyebrow">RENTAL OPERATIONS</p>
                <h1>Booking Management</h1>
                <p>Keep track of your vehicle rental bookings.</p>
            </div>

            <a class="be-button be-button-primary"
               href="<%= escapeHtml(contextPath) %>/bookings?action=new">
                + New Booking
            </a>
        </header>

        <section class="be-hero" aria-label="Booking overview">
            <div>
                <span class="be-hero-tag">BOOKING MANAGEMENT</span>
                <h2>Every rental,<br>all in one place.</h2>
                <p>Create, organize and manage bookings with ease.</p>

                <a class="be-button be-button-light"
                   href="<%= escapeHtml(contextPath) %>/bookings?action=new">
                    + Create Booking
                </a>
            </div>

            <div class="be-hero-icon" aria-hidden="true">🚗</div>
        </section>

        <section class="be-stats" aria-label="Booking statistics">

            <article class="be-stat-card">
                <span class="be-stat-icon be-stat-blue">📋</span>
                <p>Total Bookings</p>
                <strong><%= bookings.size() %></strong>
                <small>Bookings in the current view</small>
            </article>

            <article class="be-stat-card">
                <span class="be-stat-icon be-stat-green">🚙</span>
                <p>Booked Vehicles</p>
                <strong><%= bookedVehicleIds.size() %></strong>
                <small>Distinct vehicles in the current view</small>
            </article>

            <article class="be-stat-card">
                <span class="be-stat-icon be-stat-orange">💰</span>
                <p>Total Booking Value</p>
                <strong class="be-money">
                    Rs. <%= String.format(
                            Locale.US, "%,.2f", totalBookingValue
                    ) %>
                </strong>
                <small>Calculated value of displayed bookings</small>
            </article>

        </section>

        <section class="be-panel">

            <div class="be-panel-heading">
                <div>
                    <h2>All Bookings</h2>
                    <p>View, search and manage rental records.</p>
                </div>

                <a class="be-button be-button-primary"
                   href="<%= escapeHtml(contextPath) %>/bookings?action=new">
                    + Add Booking
                </a>
            </div>

            <% if (successMessage != null) { %>
                <div class="be-alert be-alert-success" role="status">
                    ✓ <%= escapeHtml(successMessage) %>
                </div>
            <% } %>

            <% if ("notfound".equals(status)) { %>
                <div class="be-alert be-alert-error" role="alert">
                    Booking not found.
                </div>
            <% } else if ("invalid".equals(status)) { %>
                <div class="be-alert be-alert-error" role="alert">
                    Invalid booking action.
                </div>
            <% } %>

            <% if (error != null && !error.isBlank()) { %>
                <div class="be-alert be-alert-error" role="alert">
                    <%= escapeHtml(error) %>
                </div>
            <% } %>

            <form class="be-search"
                  action="<%= escapeHtml(contextPath) %>/bookings"
                  method="get">

                <input type="search"
                       name="search"
                       value="<%= escapeHtml(search) %>"
                       placeholder="Search by booking ID, vehicle, customer or phone..."
                       aria-label="Search bookings">

                <button class="be-button be-button-dark" type="submit">
                    Search
                </button>

                <% if (!search.isBlank()) { %>
                    <a class="be-button be-button-outline"
                       href="<%= escapeHtml(contextPath) %>/bookings">
                        Clear
                    </a>
                <% } %>
            </form>

            <div class="be-table-wrap">
                <table class="be-table">
                    <thead>
                    <tr>
                        <th>BOOKING ID</th>
                        <th>CUSTOMER</th>
                        <th>VEHICLE ID</th>
                        <th>RENTAL DATES</th>
                        <th>DAYS</th>
                        <th>TOTAL COST</th>
                        <th>ACTIONS</th>
                    </tr>
                    </thead>

                    <tbody>
                    <% if (bookings.isEmpty()) { %>
                        <tr>
                            <td colspan="7">
                                <div class="be-empty">
                                    <span aria-hidden="true">🚘</span>
                                    <h3>No bookings found</h3>

                                    <% if (search.isBlank()) { %>
                                        <p>Start by creating your first booking.</p>

                                        <a class="be-button be-button-primary"
                                           href="<%= escapeHtml(contextPath) %>/bookings?action=new">
                                            + Create First Booking
                                        </a>
                                    <% } else { %>
                                        <p>No bookings match your search.</p>

                                        <a class="be-button be-button-outline"
                                           href="<%= escapeHtml(contextPath) %>/bookings">
                                            Clear Search
                                        </a>
                                    <% } %>
                                </div>
                            </td>
                        </tr>
                    <% } else {
                        for (Booking booking : bookings) {
                    %>
                        <tr>
                            <td class="be-id">
                                <%= escapeHtml(booking.getBookingId()) %>
                            </td>

                            <td>
                                <strong>
                                    <%= escapeHtml(booking.getCustomerName()) %>
                                </strong>
                                <small>
                                    <%= escapeHtml(booking.getCustomerPhone()) %>
                                </small>
                            </td>

                            <td>
                                <span class="be-vehicle-badge">
                                    <%= escapeHtml(booking.getVehicleId()) %>
                                </span>
                            </td>

                            <td>
                                <strong>
                                    <%= escapeHtml(booking.getStartDate()) %>
                                </strong>
                                <small>
                                    to <%= escapeHtml(booking.getEndDate()) %>
                                </small>
                            </td>

                            <td><%= booking.getRentalDays() %></td>

                            <td class="be-cost">
                                Rs. <%= String.format(
                                        Locale.US,
                                        "%,.2f",
                                        booking.calculateTotalCost()
                                ) %>
                            </td>

                            <td>
                                <div class="be-actions">
                                    <a class="be-button be-button-edit"
                                       href="<%= escapeHtml(contextPath) %>/bookings?action=edit&amp;bookingId=<%= java.net.URLEncoder.encode(booking.getBookingId(), java.nio.charset.StandardCharsets.UTF_8) %>">
                                        Edit
                                    </a>

                                    <form action="<%= escapeHtml(contextPath) %>/bookings"
                                          method="post"
                                          onsubmit="return confirm('Delete this booking?');">

                                        <input type="hidden"
                                               name="action"
                                               value="delete">

                                        <input type="hidden"
                                               name="bookingId"
                                               value="<%= escapeHtml(booking.getBookingId()) %>">

                                        <button class="be-button be-button-delete"
                                                type="submit">
                                            Delete
                                        </button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    <%  }
                    } %>
                    </tbody>
                </table>
            </div>

            <div class="be-table-footer">
                Showing <%= bookings.size() %> booking(s)
                <span>DriveEase Rental Management</span>
            </div>

        </section>

        <footer class="be-footer">
            © 2026 DriveEase. Vehicle Rental Service Platform.
        </footer>

    </main>
</div>

</body>
</html>