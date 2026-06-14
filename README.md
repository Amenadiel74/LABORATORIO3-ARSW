# ARSW Lab 3 - Evolution of Distributed Architectures with Java
# STIVEN ESNEIDER PARDO GUTIERREZ

This repository contains the full implementation of the "Integrative Workshop: Evolution of Distributed Architectures with Java" for the ARSW course. It covers the evolution of distributed systems communication starting from raw TCP Sockets, moving through HTTP and RMI, and concluding with modern gRPC microservices and API Gateways.

## Table of Contents
1. [Part I: TCP Sockets](#part-i-tcp-sockets)
2. [Part II: HTTP](#part-ii-http)
3. [Part III: RPC with Java RMI](#part-iii-rpc-with-java-rmi)
4. [Part IV: gRPC](#part-iv-grpc)
5. [Part V: Microservices](#part-v-microservices)
6. [Part VI: API Gateway](#part-vi-api-gateway)
7. [Integrative Final Exercise: ECICIENCIA](#integrative-final-exercise-eciciencia)
8. [General Conclusions](#general-conclusions)

---

## Part I: TCP Sockets

### Description
This part demonstrates raw, low-level communication over the network using Java's `ServerSocket` and `Socket` classes. We implemented a basic Movie querying system and a Room Reservation system (`E301` to `E304`) where the server and client communicate through simple string-based protocols separated by commas (e.g., `RESERVAR_SALON,E303`).

### Execution Instructions
1. Navigate to the `salon-tcp` directory.
   ```bash
   cd arsw-taller/parte1-sockets/salon-tcp
   javac *.java
   java SalonServer
   ```
2. Open a new terminal and run the client:
   ```bash
   cd arsw-taller/parte1-sockets/salon-tcp
   java SalonClient
   ```
3. Enter commands like `CONSULTAR_SALON,E303` or `RESERVAR_SALON,E303`.

### Conclusions
TCP Sockets are very fast and lightweight but extremely tedious to maintain. The "protocol" is just an agreement on text formatting. Changes in the protocol require updating both sides and careful manual parsing. Synchronization must be manually implemented to handle concurrent clients properly, which we did using `synchronized` methods.

---

## Part II: HTTP

### Description
This section elevates communication to the HTTP layer using Java's built-in `com.sun.net.httpserver.HttpServer`. The systems expose endpoints like `/rooms/reserve?id=E303` to perform actions. 

### Execution Instructions
1. Navigate to `salon-http`.
   ```bash
   cd arsw-taller/parte2-http/salon-http
   javac *.java
   java SalonHttpServer
   ```
2. Test using `curl` or a web browser:
   ```bash
   curl http://localhost:8081/rooms
   curl -X POST http://localhost:8081/rooms/reserve?id=E303
   ```

### Conclusions
HTTP adds a standard semantic layer (Methods: GET, POST; Status Codes: 200, 404, 400). It makes the service accessible from basically any language or tool (like browsers or `curl`). However, doing this without a framework (like Spring Boot) is verbose, as developers still need to manually parse query parameters and manage the routing logic.

---

## Part III: RPC with Java RMI

### Description
Remote Method Invocation (RMI) enables a Java program to invoke methods on an object running in another JVM. We implemented an Equipment Reservation system (`PC-101`, `LAP-01`, etc.) where the client code feels like it's calling local methods.

### Execution Instructions
1. Navigate to `laboratorio-rmi`.
   ```bash
   cd arsw-taller/parte3-rmi/laboratorio-rmi
   javac *.java
   java EquipoRmiServer
   ```
2. In a new terminal:
   ```bash
   cd arsw-taller/parte3-rmi/laboratorio-rmi
   java EquipoRmiClient
   ```

### Conclusions
RMI achieves great transparency: network details and serialization are completely hidden from the developer. The explicit `java.rmi.Remote` interface acts as a strong contract. The main drawback is that RMI is exclusively tied to the Java ecosystem. If we wanted a Python or Node.js client, integrating it would be incredibly difficult.

---

## Part IV: gRPC

### Description
gRPC modernizes the RPC concept by using Protocol Buffers (`.proto` files) to define language-agnostic contracts. We created Maven projects to auto-generate the Stubs for a Student Wellness System handling medical appointments (`AppointmentService`).

### Execution Instructions
1. Navigate to `bienestar-grpc`.
   ```bash
   cd arsw-taller/parte4-grpc/bienestar-grpc
   mvn clean compile
   mvn exec:java -Dexec.mainClass="edu.eci.arsw.wellness.AppointmentServer"
   ```
2. In another terminal:
   ```bash
   cd arsw-taller/parte4-grpc/bienestar-grpc
   mvn exec:java -Dexec.mainClass="edu.eci.arsw.wellness.AppointmentClient"
   ```

### Conclusions
gRPC provides the "method invocation" feel of RMI but without being locked into Java. The `.proto` file serves as an unbreakable, strictly-typed mathematical contract between the client and server. It's incredibly fast thanks to HTTP/2 and binary serialization, making it the industry standard for internal microservice communication.

---

## Part V: Microservices

### Description
Here we broke down monoliths into decoupled microservices. The Wellness system was separated into 4 distinct, independently deployable gRPC services: `AppointmentService`, `MedicalService`, `GymService`, and `RecreationService`.

### Execution Instructions
1. In `bienestar-micro`, compile the project:
   ```bash
   cd arsw-taller/parte5-microservicios/bienestar-micro
   mvn clean compile
   ```
2. Run each service in its own terminal window from the `bienestar-micro` folder:
   ```bash
   mvn exec:java -Dexec.mainClass="edu.eci.arsw.wellness.AppointmentServer"
   # In another terminal:
   mvn exec:java -Dexec.mainClass="edu.eci.arsw.gym.GymServer"
   # And so on for MedicalServer and RecreationServer...
   ```

### Conclusions
Separating domains into independent microservices increases fault tolerance and scalability. If the `GymService` crashes under high load, `AppointmentService` remains fully operational. However, it shifts complexity from code to infrastructure, as now we must manage multiple ports, deployments, and distributed states.

---

## Part VI: API Gateway

### Description
With numerous microservices running, the client shouldn't need to know every single IP, port, and protocol. We implemented the API Gateway pattern to aggregate data. `WellnessGateway` provides a single entry point for the user, communicating internally with the 4 underlying wellness microservices via gRPC stubs.

### Execution Instructions
1. Ensure all microservices from Part V are running.
2. In a new terminal, navigate to `wellness-gateway`:
   ```bash
   cd arsw-taller/parte6-gateway/wellness-gateway
   mvn clean compile
   mvn exec:java -Dexec.mainClass="edu.eci.arsw.gateway.WellnessGateway"
   ```

### Conclusions
The API Gateway significantly simplifies the client's life and reduces network round-trips by aggregating data (e.g., fetching a student's full wellness summary in one call). The main trade-off is that the Gateway becomes a single point of failure (SPOF) and a potential bottleneck. It must be kept lightweight (routing and aggregating only) to avoid becoming a "monolithic gateway".

---

## Integrative Final Exercise: ECICIENCIA

### Description
We designed the architecture for the "ECICIENCIA" event platform, defining services like `AttendeeService`, `AgendaService`, `WorkshopService`, and the critical `CapacityService`. We generated `.proto` contract definitions and designed the system using the best practices learned in the previous sections.

### Conclusions
For a highly concurrent scenario like event registration, a monolithic architecture would likely crash when spikes occur (e.g., ticket release). By applying our learned concepts, we isolate the `CapacityService` so it can be horizontally scaled and optimized independently, demonstrating the real-world value of distributed microservice architectures.

---

## General Conclusions

1. **Evolution of Abstraction:** As we evolved from Sockets to HTTP, RMI, and finally gRPC, the framework increasingly abstracted network complexities (serialization, routing, framing). We moved from pushing bytes to invoking semantic services.
2. **Coupling vs. Cohesion:** Sockets had high coupling due to manual parsing. RMI had high coupling to the Java language. gRPC strikes the perfect balance with language-agnostic `.proto` contracts.
3. **Monolith vs. Microservices:** Microservices are not a silver bullet. They solve scalability and team autonomy issues but introduce network latency, distributed transactions, and deployment complexities. They require patterns like API Gateways to keep client interactions sane.
