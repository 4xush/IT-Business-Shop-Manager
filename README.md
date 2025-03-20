## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

## Dependency Management

referencedLibraries":
 [ "lib/**/*.jar","sqlite-jdbc" ,"lib/javafx-sdk" ]

## Database and JavaFX-

sqlite -jdbc 3.49.1.0 jar -- Rename as - sqlite-jdbc.jar
openjfx-23.0.2_linux-x64  -- Rename as - javafx-sdk (folder name) it should have lib folder inside

## Compile from root dir - 
javac -d bin -cp "lib/sqlite-jdbc.jar" src/database/DatabaseConnection.java

## Run database connection to init db in sqlite3 
java -cp "bin:lib/sqlite-jdbc.jar" database.DatabaseConnection

## Compile all file with its dependency path - from root dir

javac --module-path "lib/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml -cp "lib/sqlite-jdbc.jar" -d bin $(find src -name "*.java")

## Main App entry 

java --module-path "lib/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml -cp "bin:lib/sqlite-jdbc.jar" gui.Main

## Command for tests from root dir
javac -d bin -cp "lib/sqlite-jdbc.jar" $(find src -name "*.java")
java -cp "bin:lib/sqlite-jdbc.jar" tests.RepairServiceTest
java --module-path "lib/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml -cp "bin:lib/sqlite-jdbc.jar" tests.ReportServiceTest

## Features - Shop Owner can use

1️⃣ Product Management

--> Add New Products – Add phones, accessories, chargers, headphones, etc.
--> View Products – See all available products in the shop.
--> Update Products – Modify product details like price and stock.
--> Delete Products – Remove products that are no longer available.

2️⃣ Sales Management

--> Record a Sale – When a customer buys something, record the sale.
--> Track Sales History – See all previous sales transactions.
--> Calculate Total Earnings – Get revenue reports from sales.
--> Search Sales Records – Find transactions by date, customer name, or product.

3️⃣ Repair Service Management

--> Add a Repair Request – Register customer devices for repair.
--> Track Repair Status – Check the status of ongoing repairs.
--> Update Repair Details – Modify repair costs, estimated time, and issue details.
--> Mark Repairs as Completed – Notify when repairs are done.

4️⃣ Mobile Recharge Management

--> Process Mobile Recharges – Store customer mobile numbers, operators, and amounts.
--> Track Recharge Status – Check if a recharge was completed or pending.
--> View Recharge History – See all previous recharges with timestamps.

5️⃣ Admin Features (For Shop Owner Only)

--> Search & Filter Records – Easily find products, sales, repairs, and recharges.
--> View Daily Reports – Get summaries of total sales, repairs, and recharges.
--> Manage Inventory – Keep track of stock availability.

