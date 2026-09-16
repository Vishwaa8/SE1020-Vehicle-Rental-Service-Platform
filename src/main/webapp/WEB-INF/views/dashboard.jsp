<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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

        .header {
            background: #183153;
            color: white;
            padding: 24px 8%;
        }

        .header h1 {
            font-size: 28px;
        }

        .header p {
            margin-top: 8px;
            color: #d5e3f5;
        }

        .container {
            max-width: 1100px;
            margin: 60px auto;
            padding: 0 24px;
        }

        .welcome-card {
            background: white;
            padding: 45px;
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
            margin-top: 25px;
            padding: 10px 18px;
            background: #dcfce7;
            color: #166534;
            border-radius: 8px;
            font-weight: bold;
        }

        @media (max-width: 600px) {
            .container {
                margin: 30px auto;
            }

            .welcome-card {
                padding: 25px;
            }

            .welcome-card h2 {
                font-size: 24px;
            }
        }
    </style>

</head>

<body>

<header class="header">
    <h1>🚗 DriveEase</h1>
    <p>Vehicle Rental Service Platform</p>
</header>

<main class="container">

    <section class="welcome-card">

        <h2>Welcome to DriveEase!</h2>

        <p>
            Your Vehicle Rental Service Platform is ready
            for development.
        </p>

        <p>
            This dashboard will provide access to vehicle
            management, rental bookings, customer information,
            and rental statistics.
        </p>

        <div class="status">
            Dashboard Preview
        </div>

    </section>

</main>

</body>
</html>