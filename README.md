# Pruebas con JMeter - Tecnologias y Sistemas Web

Este repositorio contiene los archivos y configuraciones utilizados para realizar pruebas de rendimiento y carga con Apache JMeter en el proyecto de Tecnologias y Sistemas Web.

---

## Descripción General

Las pruebas se diseñaron para evaluar el rendimiento y escalabilidad del backend del sistema, incluyendo la capacidad de manejo de usuarios simultáneos y la latencia de las operaciones.

### Escenarios Probados

1. **Registro de Usuarios:**
   - Simulación de múltiples usuarios registrándose simultáneamente.
2. **Gestión de Listas:**
   - Creación, edición y eliminación de listas bajo carga.
3. **Compartición de Listas:**
   - Evaluación de la sincronización en tiempo real utilizando WebSocket.

---

## Conclusiones

- El sistema es capaz de manejar hasta **500 usuarios concurrentes** sin experimentar una degradación significativa en el rendimiento.
- Por encima de esta cifra, se observaron:
  - Incremento en el tiempo de respuesta.
  - Errores de conexión ocasionales en solicitudes HTTP y WebSocket.

---

## Requisitos

### Software

- Apache JMeter (v5.5 o superior)

### Hardware

- CPU: Procesador de 4 núcleos o superior
- RAM: 8 GB o superior

---

## Ejecución de las Pruebas

1. Instala Apache JMeter.
   - Descarga desde [JMeter Official Website](https://jmeter.apache.org/).
2. Abre el archivo del proyecto JMeter (`pruebasJmeter.jmx`).
3. Configura el archivo para el entorno local (si es necesario):
   - Ajusta la URL del backend y los puertos correspondientes.
4. Ejecuta las pruebas:
   - Inicia la prueba desde la interfaz de JMeter.
   
---

## Archivos Incluidos

- **`pruebasJmeter.jmx`**: Archivo de configuración con los casos de prueba.
- **`correos.csv`**: Lista de correos para registrar.
- **`confirmarCuenta.csv`**: Lista con tokens para confirmar la cuenta(Cambia con cada ejecución).
- **`crearListas.csv`**: Lista con nombres de lista para crear.
- **`borrarLista.csv`**: Lista con las listas para borrar.
- **`comprarProductos.csv`**: Lista de productos para comprar.
- **`invitar.csv`**: Lista de correos para invitar.

---

## Mejora del Rendimiento

1. **Optimización de Backend:**
   - Implementar caché para solicitudes frecuentes.
   - Optimizar consultas a la base de datos.
2. **Escalabilidad:**
   - Configurar balanceadores de carga.
   - Añadir más instancias del backend.


---

## Autores

Estas pruebas fueron diseñadas por [Adrián Utrilla Sánchez], [Jaime Camacho Garcia] y [Victor Velasco Gutierrez]. Para consultas, contacta a [adrian.utrilla@outlook.es],[camacho.jaime5@gmail.com] y [zipi001@gmail.com].


