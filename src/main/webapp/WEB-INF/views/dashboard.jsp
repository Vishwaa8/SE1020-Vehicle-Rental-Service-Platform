<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.Locale" %>

<%
    int totalVehicles =
            (Integer) request.getAttribute("totalVehicles");

    int availableVehicles =
            (Integer) request.getAttribute("availableVehicles");

    int unavailableVehicles =
            (Integer) request.getAttribute("unavailableVehicles");

    int totalBookings =
            (Integer) request.getAttribute("totalBookings");

    int bookedVehicles =
            (Integer) request.getAttribute("bookedVehicles");

    double totalBookingValue =
            (Double) request.getAttribute("totalBookingValue");

    String contextPath = request.getContextPath();
%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>DriveEase | Dashboard</title>

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: Arial, sans-serif;
            background: #f4f7fc;
            color: #172b4d;
            min-height: 100vh;
        }

        a {
            text-decoration: none;
        }

        .header {
            background: #183153;
            color: white;
            padding: 24px 8%;
        }

        .header-inner {
            max-width: 1150px;
            margin: 0 auto;
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 20px;
        }

        .header h1 {
            font-size: 28px;
        }

        .header p {
            margin-top: 8px;
            color: #d5e3f5;
            font-size: 14px;
        }

        .header-nav {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
        }

        .header-nav a {
            color: #ffffff;
            padding: 10px 15px;
            border-radius: 8px;
            font-size: 13px;
            font-weight: bold;
            background: rgba(255, 255, 255, 0.10);
        }

        .header-nav a:hover {
            background: rgba(255, 255, 255, 0.20);
        }

        .container {
            max-width: 1150px;
            margin: 42px auto;
            padding: 0 24px;
        }

        .welcome-card {
            background: white;
            padding: 40px;
            border-radius: 18px;
            box-shadow: 0 8px 30px rgba(0, 0, 0, 0.06);
        }

        .welcome-card h2 {
            font-size: 30px;
            margin-bottom: 16px;
        }

        .welcome-card p {
            font-size: 16px;
            color: #64748b;
            line-height: 1.8;
        }

        .status {
            display: inline-block;
            margin-top: 22px;
            padding: 10px 18px;
            background: #dcfce7;
            color: #166534;
            border-radius: 8px;
            font-size: 13px;
            font-weight: bold;
        }

        .welcome-actions {
            display: flex;
            flex-wrap: wrap;
            gap: 12px;
            margin-top: 24px;
        }

        .button {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            min-height: 43px;
            padding: 12px 18px;
            border-radius: 9px;
            font-size: 13px;
            font-weight: bold;
            transition: background 0.2s;
        }

        .button-primary {
            background: #2862e5;
            color: white;
        }

        .button-primary:hover {
            background: #194dcc;
        }

        .button-secondary {
            background: #eaf1ff;
            color: #2157c7;
        }

        .button-secondary:hover {
            background: #dbe8ff;
        }

        .section-heading {
            display: flex;
            align-items: baseline;
            justify-content: space-between;
            gap: 14px;
            margin: 35px 0 18px;
        }

        .section-heading h2 {
            font-size: 21px;
        }

        .section-heading p {
            color: #7b8ba3;
            font-size: 12px;
        }

        .stats-grid {
            display: grid;
            grid-template-columns: repeat(3, minmax(0, 1fr));
            gap: 18px;
        }

        .stat-card {
            background: white;
            padding: 25px;
            min-height: 170px;
            border: 1px solid #e5ebf6;
            border-radius: 15px;
            box-shadow: 0 5px 20px rgba(32, 62, 114, 0.05);
        }

        .stat-icon {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            width: 43px;
            height: 43px;
            border-radius: 11px;
            font-size: 21px;
            background: #eaf1ff;
        }

        .stat-icon.green {
            background: #dcfce7;
        }

        .stat-icon.orange {
            background: #fff0dc;
        }

        .stat-card h3 {
            margin-top: 17px;
            color: #52637d;
            font-size: 13px;
        }

        .stat-card strong {
            display: block;
            margin-top: 9px;
            font-size: 29px;
            letter-spacing: -0.8px;
            overflow-wrap: anywhere;
        }

        .stat-card strong.money {
            font-size: 25px;
        }

        .stat-card p {
            margin-top: 7px;
            color: #8a98ad;
            font-size: 12px;
            line-height: 1.5;
        }

        .quick-links {
            display: grid;
            grid-template-columns: repeat(2, minmax(0, 1fr));
            gap: 18px;
            margin-top: 18px;
        }

        .quick-link {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 16px;
            padding: 24px;
            background: #ffffff;
            border: 1px solid #e5ebf6;
            border-radius: 14px;
            transition: border-color 0.2s, transform 0.2s;
        }

        .quick-link:hover {
            border-color: #2862e5;
            transform: translateY(-2px);
        }

        .quick-link strong {
            display: block;
            color: #172b4d;
            font-size: 15px;
        }

        .quick-link p {
            margin-top: 7px;
            color: #7b8ba3;
            font-size: 12px;
            line-height: 1.6;
        }

        .quick-link .arrow {
            color: #2862e5;
            font-size: 25px;
            font-weight: bold;
        }

        .dashboard-note {
            margin: 24px 0 0;
            color: #7b8ba3;
            font-size: 12px;
            line-height: 1.7;
            text-align: center;
        }

        .footer {
            padding: 35px 0 10px;
            color: #8a98ad;
            font-size: 12px;
            text-align: center;
        }

        @media (max-width: 850px) {
            .header-inner {
                align-items: flex-start;
                flex-direction: column;
            }

            .stats-grid {
                grid-template-columns: repeat(2, minmax(0, 1fr));
            }
        }

        @media (max-width: 600px) {
            .container {
                margin: 25px auto;
                padding: 0 14px;
            }

            .welcome-card {
                padding: 25px;
            }

            .welcome-card h2 {
                font-size: 24px;
            }

            .stats-grid,
            .quick-links {
                grid-template-columns: 1fr;
            }

            .section-heading {
                flex-direction: column;
                align-items: flex-start;
            }

            .stat-card {
                min-height: 0;
            }

            .header {
                padding: 24px 18px;
            }
        }
    </style>
</head>

<body>

<header class="header">
    <div class="header-inner">

        <div>
            <h1>🚗 DriveEase</h1>
            <p>Vehicle Rental Service Platform</p>
        </div>

        <nav class="header-nav" aria-label="Main navigation">
            <a href="<%= contextPath %>/dashboard">Dashboard</a>
            <a href="<%= contextPath %>/vehicles">Vehicles</a>
            <a href="<%= contextPath %>/bookings">Bookings</a>
        </nav>

    </div>
</header>

<main class="container">

    <section class="welcome-card">

        <h2>Welcome to DriveEase!</h2>

        <p>
            Manage your vehicle rental service from one place.
            View your fleet, track rental bookings, and access
            your management tools.
        </p>

        <p>
            The statistics below are calculated from your saved
            vehicle and booking records.
        </p>

        <div class="status">
            ✓ Dashboard data loaded
        </div>

        <div class="welcome-actions">
            <a class="button button-primary"
               href="<%= contextPath %>/vehicles">
                Manage Vehicles
            </a>

            <a class="button button-secondary"
               href="<%= contextPath %>/bookings">
                Manage Bookings
            </a>
        </div>

    </section>

    <section aria-labelledby="fleet-heading">

        <div class="section-heading">
            <h2 id="fleet-heading">Vehicle Overview</h2>
            <p>Current vehicle records</p>
        </div>

        <div class="stats-grid">

            <article class="stat-card">
                <span class="stat-icon">🚘</span>
                <h3>Total Fleet</h3>
                <strong><%= totalVehicles %></strong>
                <p>Vehicles registered in the system</p>
            </article>

            <article class="stat-card">
                <span class="stat-icon green">✓</span>
                <h3>Marked Available</h3>
                <strong><%= availableVehicles %></strong>
                <p>Vehicles with availability set to Available</p>
            </article>

            <article class="stat-card">
                <span class="stat-icon orange">◷</span>
                <h3>Marked Unavailable</h3>
                <strong><%= unavailableVehicles %></strong>
                <p>Vehicles with availability set to Unavailable</p>
            </article>

        </div>

    </section>

    <section aria-labelledby="booking-heading">

        <div class="section-heading">
            <h2 id="booking-heading">Booking Overview</h2>
            <p>Current booking records</p>
        </div>

        <div class="stats-grid">

            <article class="stat-card">
                <span class="stat-icon">📋</span>
                <h3>Total Bookings</h3>
                <strong><%= totalBookings %></strong>
                <p>Bookings saved in the system</p>
            </article>

            <article class="stat-card">
                <span class="stat-icon green">🚙</span>
                <h3>Vehicles with Bookings</h3>
                <strong><%= bookedVehicles %></strong>
                <p>Distinct vehicle IDs across saved bookings</p>
            </article>

            <article class="stat-card">
                <span class="stat-icon orange">💰</span>
                <h3>Total Booking Value</h3>
                <strong class="money">
                    Rs. <%= String.format(
                            Locale.US,
                            "%,.2f",
                            totalBookingValue
                    ) %>
                </strong>
                <p>Calculated rental value, not collected revenue</p>
            </article>

        </div>

    </section>

    <section aria-labelledby="actions-heading">

        <div class="section-heading">
            <h2 id="actions-heading">Quick Actions</h2>
            <p>Go directly to your management tools</p>
        </div>

        <div class="quick-links">

            <a class="quick-link"
               href="<%= contextPath %>/vehicles?action=new">

                <div>
                    <strong>+ Add New Vehicle</strong>
                    <p>Register a car, van, or bike in your fleet.</p>
                </div>

                <span class="arrow" aria-hidden="true">→</span>
            </a>

            <a class="quick-link"
               href="<%= contextPath %>/bookings?action=new">

                <div>
                    <strong>+ Create New Booking</strong>
                    <p>Reserve a vehicle for selected rental dates.</p>
                </div>

                <span class="arrow" aria-hidden="true">→</span>
            </a>

        </div>

    </section>

    <p class="dashboard-note">
        Figures reflect saved records when this page loads.
        Refresh the page to see changes made in Vehicle or Booking Management.
    </p>

    <footer class="footer">
        © 2026 DriveEase. Vehicle Rental Service Platform.
    </footer>

</main>

</body>
</html>