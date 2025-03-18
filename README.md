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


## compile root dir - 
javac -d bin -cp "lib/sqlite-jdbc.jar" src/database/DatabaseConnection.java
## run database - or init it
java -cp "bin:lib/sqlite-jdbc.jar" database.DatabaseConnection

## compile parent file first to use its package
javac -d bin -cp "lib/sqlite-jdbc.jar" $(find src -name "*.java")



## Run tests 
javac -d bin -cp "lib/sqlite-jdbc.jar" $(find src -name "*.java")
java -cp "bin:lib/sqlite-jdbc.jar" tests.RepairServiceTest
