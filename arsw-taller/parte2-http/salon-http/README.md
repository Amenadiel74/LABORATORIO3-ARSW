# Reflexión - Parte II (HTTP con Java)

**1. ¿Qué ventajas ofrece HTTP frente al protocolo de texto manual?**
HTTP ofrece un protocolo estándar bien documentado y ampliamente soportado. Utiliza verbos estructurados (GET, POST, etc.) y códigos de estado (200, 400, 404), además de soportar encabezados para definir el tipo de contenido. Esto facilita la integración de clientes desarrollados en otros lenguajes o usar herramientas como navegadores web o `curl`.

**2. ¿Qué limitaciones tiene construir un servidor HTTP sin framework?**
Al construirlo "desde cero" (con el paquete `com.sun.net.httpserver`), hay que gestionar manualmente el parseo de URLs, extracción de variables de la query string (`query.split`), el manejo de códigos de estado, las cabeceras HTTP y el enrutamiento. Los frameworks como Spring Boot se encargan de esta lógica rutinaria (`@GetMapping`, `@PathVariable`) de forma declarativa.

**3. ¿Cómo cambiaría esta solución si se usara JSON en lugar de HTML?**
Si se usa JSON, los controladores (`Handlers`) tendrían que serializar los objetos de Java a strings JSON (por ejemplo usando Jackson o Gson) y el `Content-Type` de las respuestas sería `application/json`. Los clientes recibirían los datos estructurados en lugar de texto plano o HTML, haciendo muy sencillo integrar el backend con interfaces dinámicas (como un frontend en React o Angular).
