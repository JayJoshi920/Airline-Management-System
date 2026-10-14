# Testing Documentation — Airline Management System

## 1. Objective

The purpose of testing is to verify that the Airline Management System behaves as expected and that its main functions work correctly.

## 2. Testing Environment

- **Programming Language:** Java
- **GUI Technology:** Java Swing
- **Database:** MySQL
- **Database Connectivity:** JDBC
- **IDE:** Eclipse

## 3. Test Cases

| Test Case | What to Check | Expected Result |
|---|---|---|
| Application startup | Launch the application | The application opens without errors |
| Login | Enter valid and invalid credentials | Access is granted or rejected appropriately |
| Flight management | Add or retrieve flight details | Flight information is handled correctly |
| Passenger management | Enter and retrieve passenger information | Passenger records are handled correctly |
| Booking | Create a booking using valid details | Booking details are saved correctly |
| Booking records | Retrieve an existing booking | Correct booking information is displayed |
| Payment management | Enter payment information | Payment data is processed as implemented |
| Cancellation | Cancel an eligible booking | Cancellation status is updated correctly |
| Database connection | Connect the application to MySQL | The connection succeeds when configuration is correct |
| Input validation | Submit missing or invalid information | Appropriate validation is performed |

## 4. Testing Results

Test cases should be executed using the actual application. Record each result as **Pass**, **Fail**, or **Not Tested** after checking the corresponding functionality.

## 5. Issues and Improvements

Document any errors found during testing, including database connection problems, invalid inputs, incorrect records, or unexpected application behavior.

## 6. Current Status

Testing is to be completed and verified against the current implementation. Only confirmed results should be reported as successfully tested.
