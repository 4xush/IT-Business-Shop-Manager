## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

## Database and JavaFX-
sqlite -jdbc 3.49.1.0 jar
openjfx-23.0.2_linux-x64


## compile root dir - 
javac -d bin -cp "lib/sqlite-jdbc.jar" src/database/DatabaseConnection.java
## run database - or init it
java -cp "bin:lib/sqlite-jdbc.jar" database.DatabaseConnection

## compile parent file first to use its package
javac -d bin -cp "lib/sqlite-jdbc.jar" $(find src -name "*.java")

## Run tests 
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
--> Backup & Restore Data – Save important shop data securely.
--> Manage Inventory – Keep track of stock availability.



## java fxml
javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml /home/ayush/Desktop/IT-Service-Shop/src/gui/Main.java