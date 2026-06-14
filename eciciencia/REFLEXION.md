# Reflexión Arquitectónica - ECICIENCIA

A lo largo de este laboratorio, hemos evolucionado desde el uso de Sockets TCP crudos y HTTP básico, hasta el diseño robusto con gRPC, separando las lógicas en microservicios y exponiendo un API Gateway.

### ¿Por qué un monolito sería problemático para este sistema?
Un evento masivo como ECICIENCIA genera "picos" de tráfico muy agresivos en momentos puntuales (ej. apertura de inscripciones o el inicio del evento en la mañana). 
En una arquitectura monolítica (donde el control de asistencia, la agenda y el aforo están en una misma app), un pico en el registro podría saturar los recursos del servidor (CPU/Memoria), causando que TODO el sistema se caiga. Si alguien solo quiere ver la "Agenda", no podría hacerlo porque el proceso de "Reservas" saturó el sistema.

### ¿Qué servicio tendría más carga y por qué?
El servicio con mayor exigencia es el `CapacityService` y el `WorkshopService`. Cuando se abren las inscripciones a talleres famosos, miles de usuarios intentarán inscribirse en los mismos minutos. Esto genera muchísimas peticiones concurrentes a la misma base de datos o estado en memoria para verificar y reducir los cupos disponibles.

### ¿Cómo escalarías solo ese servicio?
Gracias a la arquitectura de microservicios propuesta, podríamos:
1. **Escalado Horizontal:** Desplegar 10 o 20 instancias de `CapacityService` y `WorkshopService` detrás de un balanceador de carga o un clúster de Kubernetes, mientras dejamos solo 2 instancias de `AgendaService` (ya que la agenda casi no cambia y se puede cachear).
2. **Caché:** Integrar Redis para servir lecturas rápidas de los cupos disponibles.
3. **Comunicación asíncrona:** Si la carga es excesiva, `registerForWorkshop` del Gateway no llamaría sincrónicamente al servicio. En lugar de eso, colocaría un mensaje en un broker (ej. Kafka/RabbitMQ) y retornaría un "Su petición está siendo procesada", permitiendo al `CapacityService` consumir los mensajes a su propio ritmo sin colapsar.
