#!/bin/bash

echo "Compilando script..."
mkdir -p bin
javac -d bin src/scripts/GenerarDatosPrueba.java
echo "Ejecutando script..."
java -cp bin scripts.GenerarDatosPrueba
