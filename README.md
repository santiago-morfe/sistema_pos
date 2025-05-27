# Sistema POS (Point of Sale)

Sistema de punto de venta desarrollado en Java con interfaz gráfica Swing.

## Requisitos

- Java JDK 8 o superior
- Sistema operativo: Windows, Linux o macOS

## Estructura del Proyecto

```plaintext
sistema_pos/
├── bin/                   # Archivos compilados
├── data/                  # Directorio de datos
│   ├── clientes.txt       # Registro de clientes
│   └── productos.txt      # Registro de productos
├── src/                   # Código fuente
│   ├── data/              # Módulo de datos
│   ├── controllers/       # Controladores
│   ├── models/            # Modelos de datos
│   ├── scripts/           # Scripts de utilidad
│   ├── utils/             # Utilidades
│   ├── view/              # Vistas
│   └── Main.java          # Punto de entrada
├── Ventas/                # Directorio de tickets de venta
└── README.md              # Este archivo
```

## Casos de Uso

### Gestión de Clientes

1. **Registrar Cliente**
   - Ingresar identificación (8-10 dígitos)
   - Seleccionar tipo de identificación (CC/CE)
   - Ingresar nombres (10-30 caracteres)
   - Ingresar apellidos (10-30 caracteres)
   - Ingresar teléfono (10 dígitos)
   - Ingresar correo electrónico (formato válido)

2. **Consultar Cliente**
   - Seleccionar cliente de la lista
   - Ver detalles en el formulario
   - Los campos son de solo lectura

3. **Actualizar Cliente**
   - Seleccionar cliente de la lista
   - Modificar campos necesarios
   - Guardar cambios

4. **Eliminar Cliente**
   - Seleccionar cliente de la lista
   - Confirmar eliminación

### Gestión de Productos

1. **Registrar Producto**
   - Ingresar código (2 letras + 3 números)
   - Ingresar nombre (máximo 20 caracteres)
   - Ingresar precio (mayor que 0)

2. **Consultar Producto**
   - Seleccionar producto de la lista
   - Ver detalles en el formulario
   - Los campos son de solo lectura

3. **Actualizar Producto**
   - Seleccionar producto de la lista
   - Modificar campos necesarios
   - Guardar cambios

4. **Eliminar Producto**
   - Seleccionar producto de la lista
   - Confirmar eliminación

### Gestión de Ventas

1. **Crear Nueva Venta**
   - Seleccionar cliente de la lista desplegable
   - Agregar productos a la venta
   - Especificar cantidad para cada producto
   - Ver subtotal, IVA y total en tiempo real

2. **Ver Detalle de Venta**
   - Seleccionar venta de la lista
   - Ver ticket completo con:
     - Información del cliente
     - Productos vendidos
     - Cantidades y precios
     - Subtotal, IVA y total
     - Fecha y hora

3. **Listar Ventas**
   - Ver todas las ventas realizadas
   - Información básica: número, fecha, cliente, total

## Compilación y Ejecución

### En Windows

1. **Compilar:**

   Ejecuta el siguiente script en la terminal:
   ```batch
   ./compile.bat
   ```

2. **Ejecutar:**

   ```batch
   ./run.bat
   ```
3. **Generar datos de prueba**

   Ejecuta el siguiente script en la terminal:
   ```batch
   ./generar_datos.bat
   ```

### En Linux/macOS

1. **Compilar:**

   ```bash
   ./compile.sh
   ```

2. **Ejecutar:**

   ```bash
   ./run.sh
   ```
3. **Generar datos de prueba**

   Ejecuta el siguiente script en la terminal:
   ```bash
   ./generar_datos.sh
   ```

## Validaciones

El sistema incluye validaciones para:

### Clientes

- Identificación: 8-10 dígitos
- Tipo de identificación: CC o CE
- Nombres: 10-30 caracteres
- Apellidos: 10-30 caracteres
- Teléfono: 10 dígitos
- Correo: formato válido

### Productos

- Código: 2 letras + 3 números
- Nombre: máximo 20 caracteres
- Precio: mayor que 0

## Almacenamiento

- Los datos se guardan en archivos de texto en el directorio `data/`
- Los tickets de venta se guardan en el directorio `Ventas/`
- Formato de archivos:
  - Clientes: `identificacion*tipoIdentificacion*nombres*apellidos*telefono*correo`
  - Productos: `codigo*nombre*precio`
