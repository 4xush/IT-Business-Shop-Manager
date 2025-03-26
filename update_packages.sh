#!/bin/bash

# Update package declarations in all Java files
find src/main/java -name "*.java" -type f -exec sed -i 's/^package main\.java\.gui\./package gui./g' {} +
find src/main/java -name "*.java" -type f -exec sed -i 's/^package main\.java\.database\./package database./g' {} +
find src/main/java -name "*.java" -type f -exec sed -i 's/^package main\.java\.models\./package models./g' {} +
find src/main/java -name "*.java" -type f -exec sed -i 's/^package main\.java\.services\./package services./g' {} +
find src/main/java -name "*.java" -type f -exec sed -i 's/^package main\.java\.utils\./package utils./g' {} +
find src/main/java -name "*.java" -type f -exec sed -i 's/^package main\.java\.tests\./package tests./g' {} +

# Update imports in all Java files
find src/main/java -name "*.java" -type f -exec sed -i 's/import main\.java\.gui\./import gui./g' {} +
find src/main/java -name "*.java" -type f -exec sed -i 's/import main\.java\.database\./import database./g' {} +
find src/main/java -name "*.java" -type f -exec sed -i 's/import main\.java\.models\./import models./g' {} +
find src/main/java -name "*.java" -type f -exec sed -i 's/import main\.java\.services\./import services./g' {} +
find src/main/java -name "*.java" -type f -exec sed -i 's/import main\.java\.utils\./import utils./g' {} +
find src/main/java -name "*.java" -type f -exec sed -i 's/import main\.java\.tests\./import tests./g' {} + 