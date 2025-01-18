# Backend - Aplicación ListaCompra

Este repositorio contiene la implementación del backend del proyecto de Tecnologías y Sistemas Web. El backend proporciona servicios para la gestión de usuarios y listas de la compra mediante tecnologías como HTTP y WebSocket. Incluye pruebas funcionales automatizadas con Selenium para garantizar el correcto funcionamiento.

---

## Descripción General

El proyecto esta dividido en 3 backends distintos:

1. **fakeAccountsBE:** Maneja el registro, inicio de sesión y autenticación de usuarios.
2. **fListaComra:** Gestiona las listas de la compra, sus productos y la interacción entre usuarios.
3. **fakeAccountsBE2:** Este backend unicamente se utilizar para validar el token generado para realizar las operaciones.

### Tecnologías Utilizadas

- **Framework Backend:** Spring (Java)
- **Base de Datos:** MySQL
- **Comunicación en Tiempo Real:** WebSocket
- **Pruebas Automatizadas:** Selenium

---

## Requisitos

### Software

- Java 17
- MySQL
- Eclipse IDE
- Postman

### Hardware

- Espacio suficiente para almacenar la base de datos y logs.

---

## Ejecución

1. Inicia el servidor:
   Los 3 backends se ejecutan desde eclipse.
2. Verifica que el servidor está funcionando utilizando postman para una petición de prueba:
   ```
   http://localhost:9000/users/register1
   ```

---

## Pruebas Automatizadas con Selenium

El backend incluye pruebas funcionales automatizadas con Selenium. Estas pruebas verifican el correcto funcionamiento del sistema mediante casos de uso completos.

### Escenario de Prueba

1. Registro y autenticación de usuarios (Pepe y Ana).
2. Creación de listas y adición de productos.
3. Compartición y sincronización de listas entre usuarios mediante WebSocket.

### Ejecución de las Pruebas

1. Asegúrate de que el servidor está en ejecución.
2. Ejecuta los tests.
3. Revisa los resultados en la consola o en los reportes generados.

---

## Colaboración con Otros Servicios

- **Frontend Angular:** Proporciona la interfaz gráfica para los usuarios.
- **Pruebas con JMeter:** Evalúan el rendimiento y escalabilidad del backend.

---

## Contribuciones

El desarrollo de este proyecto fomenta el trabajo colaborativo. Por favor, sigue las pautas establecidas para commits y revisiones.

---

## Autores

Este backend fue desarrollado por [Adrián Utrilla Sánchez],[Jaime Camacho Garcia] y [Victor Velasco Gutierrez]. Para consultas, contacta a [adrian.utrilla@outlook.es],[camacho.jaime5@gmail.com] y [zipi001@gmail.com].


