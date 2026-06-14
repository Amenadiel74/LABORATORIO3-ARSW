# Reflexión - Parte I (Sockets TCP)

**1. ¿Qué tan fácil sería agregar una nueva operación al protocolo?**
Sería medianamente fácil. Solo se debe modificar el `switch` en el servidor (`SalonServer.java`) y añadir un nuevo método en `SalonRepository.java`. Sin embargo, al ser un protocolo basado en texto simple, a medida que crecen los comandos se vuelve más propenso a errores de formato (por ejemplo, omitir una coma).

**2. ¿Qué ocurre si dos clientes intentan reservar el mismo salón al mismo tiempo?**
En la implementación actual, los métodos de reserva y consulta en `SalonRepository` se han declarado con la palabra clave `synchronized`. De este modo, si dos hilos (creados en `SalonServer` por request concurrente) acceden a los recursos al mismo tiempo, el acceso es mutuamente excluyente. El primer hilo en entrar reservará el salón, y el segundo obtendrá el estado actualizado (ya reservado) y retornará `ERROR_OPERACION_INVALIDA`.

**3. ¿Dónde está definido realmente el contrato de comunicación?**
El contrato (los strings "CONSULTAR_SALON,E...", las respuestas "SALON_DISPONIBLE", etc.) no está formalizado en ninguna interfaz, estructura de datos tipada o archivo de definición común. Está implícito en el parseo (`request.split(",")`) del `SalonServer` y en la lógica del cliente. Esto hace que sea frágil y dependiente de que ambos lados mantengan los strings literales correctos.
