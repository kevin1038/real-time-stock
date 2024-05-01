# Real-Time Stock Portfolio System

This project is a real-time stock portfolio system that simulates the real-time value of a portfolio consisting of common stocks, European Call options, and European Put options on common stocks.

## Features

- Reads positions from a mock CSV position file.
- Fetches security definitions from an embedded database.
- Generates and publishes mock market data.
- Calculates real-time option price with the underlying price.
- Publishes real-time details of each position's market value and total portfolio's NAV.
- Listens to the above results and prints them into the console.

## Prerequisites

- Java 1.8

## Libraries Used

- Spring
- H2

## Running the Application

1. **Clone the repository**: Clone the repository to your local machine using the command `git clone <repository-url>`.

2. **Navigate to the project directory**: Use the command `cd <project-directory>` to navigate into the project directory.

3. **Build the project**: Run the command `./gradlew build` to build the project. This command compiles the Java code, runs the tests, and packages the compiled code into a JAR file.

4. **Run the application**: Run the command `./gradlew bootRun` to start the application. This command starts the Spring Boot application on an embedded Tomcat server.
