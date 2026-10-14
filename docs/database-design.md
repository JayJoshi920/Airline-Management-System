# Database Design — Airline Management System

## 1. Overview

The Airline Management System uses MySQL to organize and maintain airline-related information. The database supports flight details, airport information, passenger records, bookings, payments, and cancellations.

**Database name:** `airline_db2`

## 2. Database Tables

### Customers
Stores passenger or customer information required by the application.

### Airports
Stores airport information used to identify flight origins and destinations.

### Flights
Stores flight information, including details needed to search for available flights.

### Bookings
Stores passenger booking records and links bookings to relevant flight and customer information.

### Payments
Stores payment records associated with bookings.

### Cancellations
Stores information about cancelled bookings and their cancellation status.

## 3. Relationships

- A customer may have multiple bookings.
- A flight may be associated with multiple bookings.
- A booking may have an associated payment record.
- A booking may have a cancellation record when it is cancelled.
- Flights may reference airports as their departure and destination locations.

## 4. Database Connectivity

The Java application uses JDBC to connect to MySQL. Database operations allow the application to insert, retrieve, update, and manage records where implemented.

## 5. Data Integrity

The database should use primary keys to identify records uniquely and foreign keys to maintain relationships between related tables where supported by the existing schema.

Input validation should be performed to reduce incomplete or invalid records.

## 6. Current Status

This document describes the intended database structure. The actual table columns, primary keys, foreign keys, and constraints should be verified against the SQL schema used by the project.
