#!/bin/bash
java -cp bin Main
if [ $? -ne 0 ]; then
    echo "Error al ejecutar la aplicacion"
    exit 1
fi