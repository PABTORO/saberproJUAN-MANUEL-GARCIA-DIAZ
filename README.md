# Banco de Preguntas Saber PRO – Parcial Corte II

| Servicio | Puerto | Rol |
|---|---|---|
| validation-service | 8082 | `POST /validate` |
| question-service | 8083 | `POST /questions`, `POST /questions/{id}/submit` |
| lifecycle-service | 8084 | `@RabbitListener` en `question.submit.review.queue` |

**Requisitos:** Java 17+, Docker Desktop, Postman y VS Code (con *Extension Pack for Java*).

---

## 1. Levantar RabbitMQ
1. Abrir **Docker Desktop** y esperar a que el motor esté corriendo.
2. En una terminal, dentro de la carpeta `saberpro`:
   ```
   docker compose up -d
   ```
3. Verificar en http://localhost:15672 (usuario `guest`, clave `guest`).

## 2. Correr los servicios
Abrir la carpeta `saberpro` en VS Code. Ejecutar cada servicio en este orden, abriendo su clase `...Application.java` y pulsando **Run** sobre el método `main`:

| Orden | Clase | Mensaje esperado en consola |
|---|---|---|
| 1 | `validation-service` → `ValidationServiceApplication` | `Tomcat started on port 8082` |
| 2 | `lifecycle-service` → `LifecycleServiceApplication` | `Tomcat started on port 8084` y `Created new connection` (RabbitMQ) |
| 3 | `question-service` → `QuestionServiceApplication` | `Tomcat started on port 8083` |

Alternativa por terminal (dentro de cada carpeta de servicio): `mvn spring-boot:run`

Dejar las 3 terminales abiertas mientras se hacen las pruebas.

## 3. Pruebas en Postman
1. En Postman: **Import** → seleccionar `postman/SaberPRO_Parcial.postman_collection.json`.
2. Si las URLs con variables (`{{questionUrl}}`) salen en rojo, escribir las URLs directas:

| Request | Método | URL | Resultado esperado |
|---|---|---|---|
| 1. Crear pregunta válida | POST | `http://localhost:8083/questions` | **201 Created**, estado `DRAFT` |
| 2. Crear pregunta inválida | POST | `http://localhost:8083/questions` | **400 Bad Request** con lista `errors` |
| 3. Enviar a revisión | POST | `http://localhost:8083/questions/1/submit` | **202 Accepted** con `operationId` y `PROCESSING` |
| 3b. Estado en Lifecycle | GET | `http://localhost:8084/lifecycle/questions/1` | `"state": "PENDING_REVIEW"` |
| 4. Cola en RabbitMQ | GET | `http://localhost:15672/api/queues/%2F/question.submit.review.queue` | **200 OK** (Authorization: Basic Auth `guest` / `guest`) |

3. Ejecutar el request 1 antes que el 3, para que exista la pregunta con id `1`.
4. Tomar captura de cada respuesta mostrando el código de estado y la pestaña **Test Results**.

## 4. Evidencia en la consola de RabbitMQ (gráfica de mensajes)
La gráfica **Message rates** solo muestra el último minuto, así que se hace la captura con el Lifecycle detenido:

1. Abrir http://localhost:15672 → **Queues and Streams** → `question.submit.review.queue` (dejar *Refresh every 5 seconds*).
2. **Desactivar el Lifecycle Service:** ir a su terminal en VS Code y pulsar **Ctrl+C**.
3. En Postman ejecutar el **request 3** (devuelve 202).
4. En RabbitMQ capturar la cola con **Ready = 1** y **Total = 1** (mensaje entregado y en espera).
5. **Reactivar el Lifecycle Service:** ejecutar de nuevo `LifecycleServiceApplication` (**Run**).
6. Capturar la cola con **Ready = 0** y el pico en la gráfica de **Message rates**, y la consola del Lifecycle con la línea `DRAFT -> PENDING_REVIEW`.