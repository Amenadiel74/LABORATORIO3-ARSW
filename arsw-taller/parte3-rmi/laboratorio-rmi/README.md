# Reflexión - Parte III (RPC con Java RMI)

**1. ¿Qué cambió al pasar de HTTP a RMI?**
En RMI la interacción se siente como si el objeto fuera local (transparencia de red). No hay que serializar/deserializar manualmente JSON/strings, ni armar URLs, ni manejar códigos de estado HTTP. Simplemente se llama a un método de Java (`service.consultarEquipos()`). La serialización nativa de Java (`java.io.Serializable`) hace todo el trabajo pesado.

**2. ¿Dónde está definido el contrato de comunicación en RMI?**
El contrato está definido en la interfaz de Java que extiende `Remote` (`EquipoService.java`). Tanto el cliente como el servidor deben compilar y conocer esta misma interfaz. Esto garantiza un tipado fuerte, a diferencia de los Sockets TCP o HTTP básico donde se envía texto sin tipo estricto.

**3. ¿Qué problemas tendría este sistema si un cliente no está escrito en Java?**
RMI es altamente acoplado al ecosistema de Java. Usa la serialización nativa de Java, lo que hace casi imposible que un cliente escrito en Python o C# se comunique con este servidor de manera directa. Si se necesitan clientes en otros lenguajes, RMI no es la opción adecuada, y tecnologías como gRPC (que veremos después) o HTTP/JSON son mucho mejores para la interoperabilidad.
