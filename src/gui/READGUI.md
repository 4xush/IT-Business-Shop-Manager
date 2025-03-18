## javafx-skd - in jdk 21
javac --module-path "lib/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml -d . src/gui/Main.java

## compile all
cd ~/Desktop/IT-Service-Shop
javac --module-path "lib/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml -cp "lib/sqlite-jdbc.jar" -d bin $(find src -name "*.java")

## run command
java --module-path "lib/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml -cp "bin:lib/sqlite-jdbc.jar" gui.Main