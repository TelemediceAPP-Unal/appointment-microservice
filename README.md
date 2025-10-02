# Appointment Service (Java - Spring Boot)

Microservicio para gestión de citas médicas construido con Spring Boot 3.3.4 y Java 17.

## 🚀 Estado del Proyecto

✅ **Funcionando correctamente en Docker con PostgreSQL**
✅ **Configuración local sin dependencias GCP**
✅ **Auto-detección de drivers de base de datos**
✅ **Health checks implementados**

## 📋 Endpoints API

- `POST /appointments` - Crear nueva cita
- `GET /appointments` - Listar todas las citas
- `GET /appointments/{id}` - Obtener cita específica
- `PATCH /appointments/{id}/confirm` - Confirmar cita
- `DELETE /appointments/{id}` - Eliminar cita
- `GET /actuator/health` - Health check

## 🛠️ Configuración Técnica

### Perfiles de Spring Boot
- **default**: H2 en memoria para desarrollo
- **prod**: PostgreSQL para producción

### Configuración de Base de Datos
- **Auto-detección de driver**: Basado en la URL JDBC
- **PostgreSQL**: Detectado automáticamente con URLs `jdbc:postgresql://`
- **H2**: Detectado automáticamente con URLs `jdbc:h2:`

### Configuración GCP
- **Secret Manager**: Deshabilitado por defecto (`gcp.secret-manager.enabled=false`)
- **PubSub**: Deshabilitado por defecto (`pubsub.enabled=false`)

## 🐳 Despliegue con Docker

### Opción 1: Docker Compose (Recomendado)

```bash
# Ejecutar PostgreSQL + Aplicación
docker-compose -f docker-compose.local.yml up --build

# Solo PostgreSQL
docker-compose -f docker-compose.local.yml up postgres -d
```

### Opción 2: Docker Manual

```bash
# 1. Construir imagen
docker build -t appointment-service:local .

# 2. Ejecutar PostgreSQL
docker run -d --name appointment-postgres \
  -e POSTGRES_DB=appointments_local \
  -e POSTGRES_USER=app_user \
  -e POSTGRES_PASSWORD=local_password \
  -p 5432:5432 \
  postgres:15-alpine

# 3. Ejecutar aplicación
docker run -d --name appointment-service \
  --network appointment-microservice_appointment-network \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_HOST=appointment-postgres \
  -e DB_PORT=5432 \
  -e DB_NAME=appointments_local \
  -e DB_USER=app_user \
  -e DB_PASSWORD=local_password \
  -e GCP_PROJECT_ID=local-project \
  -e SECRET_MANAGER_ENABLED=false \
  -e PUBSUB_ENABLED=false \
  appointment-service:local
```

## 💻 Desarrollo Local

### Ejecutar con H2 (Sin Docker)
```bash
./gradlew clean bootJar --no-daemon -x test
./gradlew bootRun
```

### Ejecutar con PostgreSQL Local
```bash
# Configurar variables de entorno
export SPRING_PROFILES_ACTIVE=prod
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=appointments_local
export DB_USER=app_user
export DB_PASSWORD=local_password

./gradlew bootRun
```

## 🔍 Verificación del Despliegue

### Health Check
```bash
curl http://localhost:8080/actuator/health
# Respuesta esperada: {"status":"UP"}
```

### Verificar Contenedores
```bash
docker ps
# Debe mostrar appointment-service y appointment-postgres corriendo
```

### Ver Logs
```bash
# Logs de la aplicación
docker logs appointment-service

# Logs de PostgreSQL
docker logs appointment-postgres
```

## 🗃️ Configuración de Base de Datos

### PostgreSQL (Producción)
- **Host**: localhost:5432
- **Base de datos**: appointments_local
- **Usuario**: app_user
- **Contraseña**: local_password

### H2 (Desarrollo)
- **URL**: `jdbc:h2:mem:apptdb`
- **Usuario**: sa
- **Contraseña**: (vacío)

## 📦 Arquitectura del Proyecto

### Configuraciones Principales
- `LocalDataSourceConfig`: Configuración de DataSource local (sin GCP)
- `SystemHealthValidator`: Validador de salud del sistema
- Auto-detección de drivers de base de datos

### Outbox Pattern
Los eventos se escriben en la tabla `outbox`. El componente `OutboxPublisher` publica periódicamente eventos usando `LoggerEventPublisher`.

## 🔧 Solución de Problemas

### Error de Driver
Si aparece error "Driver claims to not accept jdbcUrl":
- Verificar que las variables de entorno estén correctas
- El sistema detecta automáticamente PostgreSQL vs H2

### Error de Conexión a BD
```bash
# Verificar que PostgreSQL esté ejecutándose
docker logs appointment-postgres

# Verificar conectividad de red
docker network ls
```

### Reconstruir desde Cero
```bash
# Limpiar contenedores
docker-compose -f docker-compose.local.yml down
docker system prune -f

# Reconstruir
docker-compose -f docker-compose.local.yml up --build
```
