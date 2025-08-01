# Supermarket Start Project

The Supermarket Start Project is a Java application designed to model and simulate basic supermarket operations. 
It includes functionality for handling products, customers, and supermarket data.

## Technologies Used
- Java (Modularized with `module-info.java`)
- Maven (Project build and dependency management)
- JUnit (Testing framework)

## Project Structure
- `src/main/java`: Application source code
- `src/test/java`: Unit tests
- `src/main/resources`: Input data files (JSON text)
- `pom.xml`: Maven configuration file

## How to Run

### Prerequisites
- Java 17 (or compatible version)
- Maven installed and available on your system path

### Steps
1. Navigate to the project root directory:
   ```bash
   cd Supermarket_startproject--main
   ```

2. Build the project using Maven:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn exec:java -Dexec.mainClass="main.SupermarketLauncher"
   ```

## Running Unit Tests
To execute unit tests, run:
```bash
mvn test
```
