@echo off
java -cp bin Main
if errorlevel 1 (
    echo Error al ejecutar la aplicacion
    pause
    exit /b 1
)
pause