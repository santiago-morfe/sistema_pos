#!/bin/bash
# Compilar y ejecutar el proyecto Java (versión Unix)

echo "Compilando el proyecto..."

# Crear directorio bin si no existe
[ -d bin ] || mkdir bin

# Compilar todos los archivos Java recursivamente
encontrar_error=0
find src -name "*.java" > sources.txt
javac -d bin -cp src @sources.txt || encontrar_error=1
rm sources.txt

if [ $encontrar_error -ne 0 ]; then
    echo "Error al compilar"
    exit 1
fi

echo "Compilación exitosa"
echo "Ejecutando la aplicación..."

# Ejecutar la aplicación
java -cp bin Main
