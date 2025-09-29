# Appointment Service (Java - Spring Boot)

Endpoints principales:
- POST /appointments
- GET /appointments
- GET /appointments/{id}
- PATCH /appointments/{id}/confirm
- DELETE /appointments/{id}

## Ejecutar local (H2)
```bash
./gradlew spring-boot:run
# o
gradle spring-boot:run
```

## Docker
```bash
docker build -t appointment-service:0.1.0 .
docker run -p 8080:8080 appointment-service:0.1.0
```

## Producción (Postgres)
Configura variables:
- DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD
y activa el perfil: `--spring.profiles.active=prod`

## Outbox
Los eventos se escriben en la tabla `outbox`. El componente `OutboxPublisher` publica periódicamente usando `LoggerEventPublisher` (placeholder). Reemplaza por un publisher real de Pub/Sub.
