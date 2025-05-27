#!/bin/bash
mkdir -p bin
echo "Compilando el proyecto..."

# Buscar todos los archivos .java y compilarlos juntos
find src -name "*.java" > sources.txt
javac -d bin -cp src @sources.txt
if [ $? -ne 0 ]; then
    echo "Error al compilar"
    rm sources.txt
    exit 1
fi
rm sources.txt
echo "Compilacion exitosa"
echo "Ejecutando la aplicación..."

java -cp bin Main