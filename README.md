# ShopSync Manager

A JavaFX-based desktop application for managing a mobile phone repair and sales shop.

## Prerequisites

- Java 17 or later
- Maven 3.6 or later
- SQLite 3

## Building the Project

1. Clone the repository:
```bash
git clone https://github.com/4xush/ShopSync-Manager.git
cd shopsync-manager
```

2. Build the project using Maven:
```bash
mvn clean package
```

3. Run the application:
```bash
mvn javafx:run
```

## Development

The project uses the following technologies:
- JavaFX for the GUI
- SQLite for the database
- Maven for build and dependency management

### Project Structure

```
src/
├── main/
│   ├── java/
│   │   ├── gui/           # GUI-related classes
│   │   ├── database/      # Database-related classes
│   │   ├── models/        # Data models
│   │   ├── services/      # Business logic
│   │   └── utils/         # Utility classes
│   └── resources/
│       ├── *.fxml         # FXML files for GUI layouts
│       └── kingcom.db     # SQLite database file
```

## Features

- Dashboard with key metrics
- Product management
- Sales tracking
- Repair service management
- Customer management
- Reports generation

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## ShopSync Manager

ShopSync Manager is a JavaFX-based desktop application designed to streamline small tech shop operations. Manage product sales, repairs, mobile recharges, and inventory—all in one place. Powered by a SQLite backend, it offers real-time reporting, category-based product tracking (Phones, Headphones, etc.), and a user-friendly interface for shop owners on the go.

## Folder Structure

The workspace contains two default folders:

    src: Contains all source files (Java and FXML).
    lib: Holds dependencies (JavaFX SDK and SQLite JDBC).

## Dependency Management

Dependencies are managed manually via referenced libraries:

    Referenced Libraries:
        lib/**/*.jar (all JARs in lib/ and subdirectories)
        lib/sqlite-jdbc.jar (SQLite JDBC driver)
        lib/javafx-sdk (JavaFX SDK folder)


______________________________________________________

## Without using Maven 

## Required Dependencies-

sqlite -jdbc 3.49.1.0 jar -- Rename as - sqlite-jdbc.jar
openjfx-23.0.2_linux-x64  -- Rename as - javafx-sdk (folder name) it should have lib folder inside

## Setup and Compilation

Prerequisites : JDK 17+ (e.g., OpenJDK or Oracle JDK)
Compile the database connection class:
javac -d bin -cp "lib/sqlite-jdbc.jar" src/database/DatabaseConnection.java


## Run to create tables in kingcom.db

java -cp "bin:lib/sqlite-jdbc.jar" database.DatabaseConnection

## Compile the Project from root dir

Compile all source files with dependencies:

javac --module-path "lib/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml -cp "lib/sqlite-jdbc.jar" -d bin $(find src -name "*.java")

## Main App entry 

java --module-path "lib/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml -cp "bin:lib/sqlite-jdbc.jar" gui.Main

## Command for tests from root dir
javac -d bin -cp "lib/sqlite-jdbc.jar" $(find src -name "*.java")
java -cp "bin:lib/sqlite-jdbc.jar" tests.RepairServiceTest
java --module-path "lib/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml -cp "bin:lib/sqlite-jdbc.jar" tests.ReportServiceTest



______________________________________________________

## Uses

ShopSync Manager empowers shop owners with these practical uses:

    Product Management:
        Add new products like phones, accessories, chargers, and headphones.
        View all available products in the shop.
        Update product details such as price and stock levels.
        Delete products that are no longer available.
    Sales Management:
        Record customer purchases as they happen.
        Track sales history to review past transactions.
        Calculate total earnings with revenue reports.
        Search sales records by date, customer name, or product.
    Repair Service Management:
        Add repair requests for customer devices.
        Track the status of ongoing repairs.
        Update repair details like costs, estimated time, and issues.
        Mark repairs as completed when finished.
    Mobile Recharge Management:
        Process mobile recharges with customer numbers, operators, and amounts.
        Track recharge status (pending or completed).
        View recharge history with timestamps.
    Admin Tools:
        Search and filter records for products, sales, repairs, and recharges.
        View daily reports summarizing sales, repairs, and recharges.
        Manage inventory to monitor stock availability.
        Export or load the kingcom.db database via Settings for backups and restores.

## Contributing

Fork the repo, make your changes, and submit a pull request. Issues and feature requests are welcome—let's level up ShopSync Manager together!


## Acknowledgments

Built by Ayush Kumar for shop owners everywhere. 