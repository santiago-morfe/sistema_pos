# Sistema POS

Sistema de Punto de Venta con gestión de clientes y productos.

## Requisitos

- Java 11 o superior

## Estructura del Proyecto

```
sistema-pos/
├── src/
│   ├── models/           # Modelos de datos
│   │   ├── Cliente.java
│   │   └── Producto.java
│   ├── controllers/      # Controladores de lógica de negocio
│   │   ├── ClienteController.java
│   │   └── ProductoController.java
│   ├── data/            # Gestión de datos
│   │   └── DataManager.java
│   ├── view/            # Interfaces gráficas
│   │   ├── MainWindow.java
│   │   ├── ClienteView.java
│   │   └── ProductoView.java
│   └── utils/           # Utilidades
│       └── Validators.java
├── bin/                 # Archivos compilados
├── data/               # Archivos de datos
│   ├── clientes.txt
│   └── productos.txt
└── compile.bat         # Script de compilación y ejecución
```

## Instalación

1. Asegúrate de tener Java 11 o superior instalado
2. Clona o descarga este repositorio
3. Navega al directorio del proyecto

## Ejecución

### En Windows:
1. Ejecuta el archivo `compile.bat`
   - Esto compilará el proyecto y ejecutará la aplicación

### En Linux/Mac:
1. Compila el proyecto:
   ```bash
   javac -d bin src/models/*.java src/controllers/*.java src/data/*.java src/view/*.java src/utils/*.java
   ```
2. Ejecuta la aplicación:
   ```bash
   java -cp bin view.MainWindow
   ```

## Uso

### Gestión de Clientes

- **Crear Cliente**: Ingrese los datos del cliente y haga clic en "Crear"
- **Consultar Cliente**: Seleccione un cliente de la tabla
- **Actualizar Cliente**: Modifique los datos y haga clic en "Actualizar"
- **Eliminar Cliente**: Seleccione un cliente y haga clic en "Eliminar"

### Gestión de Productos

- **Crear Producto**: Ingrese los datos del producto y haga clic en "Crear"
- **Consultar Producto**: Seleccione un producto de la tabla
- **Actualizar Producto**: Modifique los datos y haga clic en "Actualizar"
- **Eliminar Producto**: Seleccione un producto y haga clic en "Eliminar"

## Validaciones

### Cliente
- Identificación: 8-10 dígitos
- Tipo ID: CC o CE
- Nombres: 10-30 caracteres
- Apellidos: 10-30 caracteres
- Teléfono: 10 dígitos
- Correo: Formato válido

### Producto
- Código: 5 caracteres (2 letras + 3 números)
- Nombre: Máximo 10 caracteres
- Precio: Mayor que 0

## Datos

Los datos se almacenan en archivos de texto en la carpeta `data/`:
- `clientes.txt`: Datos de clientes
- `productos.txt`: Datos de productos
