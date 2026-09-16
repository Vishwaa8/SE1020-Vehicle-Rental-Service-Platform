
# 🚗 DriveEase
### Vehicle Rental Service Platform

**SE1020 – Object Oriented Programming | Individual Project**

DriveEase is a web-based Vehicle Rental Service Platform designed to simplify vehicle management and rental booking operations. The system allows users to explore available vehicles, create rental bookings, manage booking details, and calculate rental costs through a modern, user-friendly web interface.

The application is being developed using **Java, JSP, Servlets, and Object-Oriented Programming (OOP) principles**, with file-based data storage.

---

## 📌 Project Overview

Vehicle rental businesses need an organized way to manage their vehicles and customer reservations.

DriveEase aims to provide a simple and efficient solution by allowing vehicle and booking information to be managed through a centralized web application.

The project focuses on implementing Java OOP concepts, CRUD operations, file handling, and a responsive user interface.

### Project Objectives

- Develop a functional Java-based web application.
- Apply fundamental Object-Oriented Programming concepts.
- Implement Create, Read, Update, and Delete (CRUD) operations.
- Store and retrieve application data using Java file handling.
- Design a responsive and user-friendly interface.
- Maintain version control and document development progress using GitHub.

---

## ✨ Planned Features

### 🚘 Vehicle Management

- [ ] Add new vehicles.
- [ ] View available vehicles.
- [ ] Search vehicles by name or category.
- [ ] Update vehicle information.
- [ ] Delete vehicle records.
- [ ] Manage vehicle availability.

### 📅 Rental Booking Management

- [ ] Create rental bookings.
- [ ] View and search existing bookings.
- [ ] Update booking information.
- [ ] Cancel or delete bookings.
- [ ] Validate booking dates.
- [ ] Prevent conflicting vehicle reservations.

### 👤 Customer Information

- [ ] Capture customer details during booking.
- [ ] Associate customers with rental bookings.
- [ ] Display customer information in booking records.

### 💰 Rental Cost Calculation

- [ ] Calculate rental duration.
- [ ] Calculate total rental cost.
- [ ] Apply vehicle-specific rental pricing.

### 📊 Dashboard

- [ ] Display vehicle statistics.
- [ ] Show available vehicles.
- [ ] Display recent bookings.
- [ ] Provide quick access to booking operations.

---

## 🛠️ Technology Stack

### Backend

| Technology | Purpose |
|------------|---------|
| Java | Core application logic |
| JSP | Dynamic web pages |
| Java Servlets | HTTP request handling and backend operations |
| Java OOP | Object-oriented application design |
| Java File I/O | File-based data storage |

### Frontend

| Technology | Purpose |
|------------|---------|
| HTML5 | Web page structure |
| CSS3 | Styling and layout |
| JavaScript | Client-side interactions |
| Bootstrap 5 | Responsive UI components |

### Development Tools

| Tool | Purpose |
|------|---------|
| IntelliJ IDEA | Java development IDE |
| Apache Tomcat | Servlet container (planned) |
| Git | Version control |
| GitHub | Repository hosting and development history |

**Data Storage:** Text files (`.txt`) using Java file read/write operations.

---

## 🧩 Object-Oriented Programming Concepts

DriveEase is designed to demonstrate the following OOP principles.

### 1. Encapsulation

Vehicle, customer, and booking information will be stored in classes using private attributes with controlled access through methods.

### 2. Inheritance

A common `Vehicle` superclass will be extended by specialized vehicle classes.

Example:

```text
Vehicle
├── Car
├── Van
└── Bike
```

### 3. Polymorphism

Vehicle subclasses will provide different implementations of rental cost calculation methods.

### 4. Abstraction

Abstract classes and methods will be used to define common vehicle behavior while allowing specialized implementations.

### 5. Information Hiding

Internal class attributes and implementation details will be protected using appropriate Java access modifiers.

---

## 🔄 CRUD Operations

The application will implement the four primary CRUD operations.

| Operation | Vehicle Management | Booking Management |
|-----------|-------------------|--------------------|
| Create | Add a vehicle | Create a booking |
| Read | View/search vehicles | View/search bookings |
| Update | Edit vehicle details | Modify booking details |
| Delete | Remove a vehicle | Cancel/delete a booking |

These operations will be implemented using Java backend services and file-based data storage.

---

## 🗂️ Data Storage

DriveEase will use Java file handling instead of relying on a database.

Planned data files include:

```text
data/
├── vehicles.txt
├── bookings.txt
└── customers.txt
```

The application will support:

- Writing new records to files.
- Reading and searching stored records.
- Updating existing records.
- Deleting records.
- Maintaining data persistence across application restarts.

The final file structure may be refined during development.

---

## 🖥️ User Interface

The application will include the following main interfaces:

| Page | Description |
|------|-------------|
| Dashboard | Overview of vehicles and bookings |
| Vehicles | Browse and manage vehicles |
| Add/Edit Vehicle | Create and update vehicle records |
| New Booking | Create a rental reservation |
| Manage Bookings | View, update, and cancel bookings |

The interface will use a responsive layout, reusable components, clear navigation, and consistent styling.

---

## 🔁 Application Workflow

```text
Dashboard
    |
    v
View Available Vehicles
    |
    v
Select Vehicle
    |
    v
Create Rental Booking
    |
    v
Enter Customer Details
    |
    v
Select Rental Dates
    |
    v
Calculate Rental Cost
    |
    v
Save Booking to File
    |
    v
View / Update / Cancel Booking
```

---

## 📁 Proposed Project Structure

```text
DriveEase/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── driveease/
│       │           ├── model/
│       │           ├── servlet/
│       │           ├── service/
│       │           └── util/
│       │
│       └── webapp/
│           ├── WEB-INF/
│           │   └── views/
│           ├── css/
│           ├── js/
│           └── images/
│
├── data/
│
├── docs/
│
├── README.md
└── .gitignore
```

This is the proposed structure. It will be updated to reflect the actual implementation.

---

## ⚙️ Installation and Setup

### Development Environment

| Component | Version |
|-----------|---------|
| IDE | IntelliJ IDEA 2026.2.2 |
| Java JDK | Java 23.0.2 |
| Java Compilation Target | Java 21 |
| Web Server | Apache Tomcat 10.1.60 |
| Build Tool | Apache Maven |
| Backend | Java, JSP, Jakarta Servlets |
| Frontend | HTML5, CSS3, JavaScript |

### Running the Application

1. Clone the GitHub repository.
2. Open the project in IntelliJ IDEA.
3. Allow Maven to download and resolve dependencies.
4. Configure Apache Tomcat 10.1.60.
5. Add the `driveease:war exploded` deployment artifact.
6. Set the application context to `/driveease`.
7. Start the Tomcat server.

Open the following URL in your browser:

http://localhost:8080/driveease/dashboard

**Note:** The application currently displays an initial dashboard preview. Vehicle management, booking management, and other planned features are under development.

---

## 🚀 Initial Deployment Verification

The initial DriveEase web application has been successfully deployed and tested locally using Apache Tomcat.

The following components were verified:

- Maven project compilation.
- Apache Tomcat server startup.
- WAR exploded artifact deployment.
- Dashboard Servlet request handling.
- JSP page rendering in the browser.

### Initial Dashboard Screenshot

![DriveEase Initial Dashboard](docs/screenshots/dashboard-initial.png)

*Figure 1: Initial DriveEase dashboard running on Apache Tomcat.*

---

## 🧪 Testing

The following functionality will be tested during development:

- CRUD operations.
- File reading and writing.
- Form validation.
- Rental cost calculations.
- Vehicle availability checks.
- Booking date validation.
- Error handling.

---

## 📚 Project Documentation

The final project documentation will include:

- Project overview.
- System requirements.
- Class diagrams.
- Explanation of OOP concepts.
- CRUD implementation details.
- File handling implementation.
- Application screenshots.
- Testing information.
- GitHub commit history.

---

## 📈 Development Progress

| Phase | Status |
|-------|--------|
| Project planning | In Progress |
| Repository setup | Completed |
| Java project setup | Not Started |
| OOP class implementation | Not Started |
| File handling | Not Started |
| CRUD implementation | Not Started |
| Frontend development | Not Started |
| Testing | Not Started |
| Final documentation | Not Started |
| Viva preparation | Not Started |

Development progress will be updated as the project evolves.

---

## 👨‍💻 Developer

**Vishwa Udhantha Muthukudaarachchi**

BSc (Hons) in Information Technology  
Faculty of Computing  
Sri Lanka Institute of Information Technology (SLIIT)

**Module:** SE1020 – Object Oriented Programming  
**Project Type:** Individual Project

---

## 🎓 Academic Project

This project is developed as part of the SE1020 Object Oriented Programming module at SLIIT.

The primary objective is to demonstrate practical knowledge of Java programming, object-oriented design, CRUD functionality, file handling, and web application development.

---


### 🚗 DriveEase

**Your Journey, Our Wheels.**

*SE1020 – Object Oriented Programming*
