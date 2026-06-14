# Reflexión - Parte IV (gRPC)

**1. ¿Por qué el archivo `.proto` se considera un contrato formal?**
El `.proto` es un contrato formal porque es independiente del lenguaje de programación. Describe de manera estricta y matemática qué servicios existen, qué métodos exponen, y la estructura exacta de los mensajes (request/response) junto con los tipos de datos de sus campos (int32, string, enum, etc.). No hay lugar a la ambigüedad en el contrato, como ocurre en JSON sobre HTTP tradicional donde una propiedad podría omitirse o cambiar de tipo.

**2. ¿Qué tan fácil sería crear un cliente en Python o Go?**
Sería extremadamente fácil. Simplemente se toma el mismo archivo `appointment.proto` y se ejecuta el compilador `protoc` usando el plugin del lenguaje correspondiente (Python, Go, Node.js, etc.). Esto generará las clases y métodos (Stubs) necesarios para que el cliente se conecte al servidor Java existente como si llamara a una función local de Python o Go.

**3. ¿Qué diferencias concretas encuentras entre RMI y gRPC?**
- **Dependencia de plataforma:** RMI solo funciona en el ecosistema Java (requiere JVM en ambos extremos). gRPC es agnóstico del lenguaje y permite microservicios políglotas.
- **Protocolo y Serialización:** RMI utiliza serialización nativa de Java sobre un protocolo propietario (JRMP). gRPC utiliza Protobuf (serialización binaria muy eficiente) sobre HTTP/2, lo que permite cosas avanzadas como streaming bidireccional, multiplexación y es más amigable con firewalls.
- **Definición de contrato:** En RMI se usa una interfaz Java (`.java`). En gRPC se usa un archivo de definición de interfaz explícito (`.proto`).
