@echo off
echo Compilando el proyecto...

:: Crear directorio bin si no existe
if not exist bin mkdir bin

:: Compilar todos los archivos Java
javac -d bin src/models/*.java src/controllers/*.java src/data/*.java src/view/*.java src/utils/*.java

:: Verificar si la compilación fue exitosa
if %errorlevel% neq 0 (
    echo Error en la compilación
    pause
    exit /b %errorlevel%
)

echo Compilación exitosa
echo Ejecutando la aplicación...

:: Ejecutar la aplicación
java -cp bin view.MainWindow

pause 