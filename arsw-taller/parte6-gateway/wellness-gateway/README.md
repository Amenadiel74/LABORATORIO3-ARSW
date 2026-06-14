# Reflexión - Parte VI (API Gateway)

**1. ¿Qué simplifica el Gateway para el cliente?**
Simplifica drásticamente el uso del sistema por parte del cliente. En vez de conocer las IPs, puertos y contratos de cuatro microservicios diferentes (Appointment, Medical, Gym, Recreation), el cliente hace una sola petición al Gateway. Además, el Gateway se encarga de recolectar los datos de varios servicios y combinarlos (agregación) como en la función `getStudentWellnessSummary`, ahorrando latencia y llamadas de red al cliente.

**2. ¿Qué complejidad agrega al sistema?**
El Gateway es un nuevo componente que hay que mantener, desplegar y escalar. Al ser un punto central por donde pasa todo el tráfico, puede convertirse en un cuello de botella de rendimiento (bottleneck) y en un punto único de falla (Single Point of Failure - SPOF).

**3. ¿Qué pasaría si el Gateway empieza a contener demasiada lógica de negocio?**
Si el Gateway incluye reglas de negocio pesadas (ej. validaciones complejas de salud o facturación), se convierte en un anti-patrón conocido como "Gateway Monolítico" (API Gateway Monolith). Esto acopla nuevamente los equipos de desarrollo, dificulta los despliegues independientes y diluye la filosofía de los microservicios. El Gateway solo debe ocuparse de enrutamiento, agregación de datos, autenticación y rate limiting.
