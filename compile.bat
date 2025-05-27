@echo off
echo Compilando el proyecto...

:: Crear directorio bin si no existe
if not exist bin mkdir bin

:: Compilar todos los archivos Java recursivamente (compatible con Windows)
for /r %%f in (src\*.java) do (
    javac -d bin -cp src "%%f"
    if errorlevel 1 (
        echo Error al compilar
        pause
        exit /b 1
    )
)

echo Compilacion exitosa
echo Ejecutando la aplicación...

:: Ejecutar la aplicación
java -cp bin Main

pause