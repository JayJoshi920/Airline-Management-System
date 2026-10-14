# Airline Management System

## Project Overview

The Airline Management System is a Java-based desktop application developed to manage basic airline operations through a graphical user interface. It uses Java Swing for the user interface and MySQL for database connectivity.

The system provides functionality for managing flight and passenger information, booking flights, viewing booking records, recording payments, and cancelling bookings.

## Objectives

- Manage flight and airport information.
- Store and maintain passenger details.
- Book flights and generate booking reference numbers (PNRs).
- Retrieve and display booking records.
- Record payment information.
- Support booking cancellation and status updates.
- Provide a separate login interface for administrators and passengers.

## Implemented Modules

1. **Login System:** Provides an administrator login and a passenger PNR-based entry point.
2. **Flight and Airport Management:** Allows flight details and airport information to be managed.
3. **Passenger Management:** Records passenger information, including name, email, phone number, passport number, and address.
4. **Flight Booking:** Records bookings by associating passengers with available flights.
5. **Booking Records:** Retrieves and displays booking information.
6. **Payment Management:** Records payment details and payment methods.
7. **Booking Cancellation:** Records cancellation information and updates booking status.
8. **Passenger Ticket Portal:** Retrieves ticket information using a PNR.
9. **Database Connectivity:** Connects the Java application to a MySQL database through JDBC.

## Technology Stack

- **Programming Language:** Java
- **GUI Framework:** Java Swing
- **Database:** MySQL
- **Database Connectivity:** JDBC (MySQL Connector/J)
- **Development Environment:** Eclipse IDE
- **User Interface Styling:** Java AWT and Swing

## Database

The application is configured to connect to a MySQL database named `airline_db2`.

The Java source code references database tables for customers, airports, flights, bookings, payments, and cancellations.

The database schema and required table structures must be configured in MySQL for the application to run correctly.

## Project Structure

- `src/airline/` — Java source files
- `bin/airline/` — Compiled Java class files
- `.project` — Eclipse project configuration
- `.classpath` — Eclipse classpath configuration
- `.settings/` — Eclipse project settings

## Testing and Validation

The project includes a Java test class for database operations. Individual modules should be tested for successful database connectivity, valid input handling, booking creation, payment recording, ticket retrieval, and cancellation.

## Team Members

- Member 1: Jay Joshi
- Member 2: Kartik Khandelwal
- Member 3: Naman Sharma

## Project Development

The project has been developed incrementally, with separate Java classes for the user interface and individual airline management operations. Further testing and validation are required to confirm that all modules function correctly in the configured environment.

## Current Status

Development in progress.

## Academic Information

- **Course:** B.Tech Artificial Intelligence and Data Science
- **Semester:** 5th Semester
- **Project Type:** Project-Based Learning (PBL)
