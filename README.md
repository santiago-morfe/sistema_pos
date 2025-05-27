# Sistema POS (Point of Sale)

Sistema de punto de venta desarrollado en Java con interfaz gráfica Swing.

## Requisitos

- Java JDK 8 o superior
- Sistema operativo: Windows, Linux o macOS

## Estructura del Proyecto

```plaintext
sistema_pos/
├── bin/                    # Archivos compilados
├── data/                   # Directorio de datos
│   ├── clientes.txt       # Registro de clientes
│   ├── productos.txt      # Registro de productos
│   └── ventas.txt         # Registro de ventas
├── src/                    # Código fuente
│   ├── controllers/       # Controladores
│   ├── models/           # Modelos de datos
│   ├── scripts/          # Scripts de utilidad
│   ├── utils/            # Utilidades
│   └── view/             # Vistas
├── Ventas/                # Directorio de tickets de venta
├── Main.java             # Punto de entrada
└── README.md             # Este archivo
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

## Scripts de Utilidad

### Generar Datos de Prueba

El sistema incluye un script para generar datos de prueba que incluye:

- 10 clientes con datos válidos
- 20 productos de diferentes categorías

Para ejecutar el script:

1. **En Windows:**

   ```bash
   generar_datos.bat
   ```

2. **En Linux/macOS:**

   ```bash
   chmod +x generar_datos.sh
   ./generar_datos.sh
   ```

El script:

- Crea el directorio `data` si no existe
- Genera/actualiza los archivos `clientes.txt` y `productos.txt`
- Muestra mensaje de éxito o error

## Ejecución del Sistema

1. **Compilar:**

   ```bash
   javac -d bin src/Main.java
   ```

2. **Ejecutar:**

   ```bash
   java -cp bin Main
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
  - Ventas: `numeroVenta*fechaHora*identificacionCliente*subtotal*iva*total*detalles`
