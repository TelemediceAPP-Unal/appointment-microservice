# Colección de Postman - Appointment Service

Esta colección contiene todas las peticiones HTTP necesarias para interactuar con el microservicio de citas médicas.

## Archivos incluidos

- `Appointment-Service.postman_collection.json` - Colección principal con todos los endpoints
- `Appointment-Service-Local.postman_environment.json` - Variables de entorno para desarrollo local

## Cómo importar en Postman

1. Abre Postman
2. Haz clic en "Import" en la parte superior izquierda
3. Arrastra y suelta ambos archivos JSON o selecciónalos usando "Choose Files"
4. Asegúrate de seleccionar el entorno "Appointment Service - Local" en la esquina superior derecha

## Endpoints disponibles

### 1. Crear Cita
- **Método**: POST
- **URL**: `{{baseUrl}}/appointments`
- **Body**:
```json
{
  "patientId": "patient-001",
  "practitionerId": "doctor-001",
  "startAt": "2025-09-29T10:00:00-05:00"
}
```

### 2. Listar Todas las Citas
- **Método**: GET
- **URL**: `{{baseUrl}}/appointments`

### 3. Listar Citas con Filtros
- **Método**: GET
- **URL**: `{{baseUrl}}/appointments?patientId=patient-001&page=0&pageSize=10`
- **Parámetros opcionales**:
  - `patientId`: ID del paciente
  - `practitionerId`: ID del médico
  - `state`: Estado de la cita (CREATED, CONFIRMED, CANCELLED)
  - `page`: Número de página (por defecto: 0)
  - `pageSize`: Tamaño de página (por defecto: 20)

### 4. Obtener Cita por ID
- **Método**: GET
- **URL**: `{{baseUrl}}/appointments/{{appointmentId}}`
- **Nota**: Establece la variable `appointmentId` con un ID válido

### 5. Confirmar Cita
- **Método**: PATCH
- **URL**: `{{baseUrl}}/appointments/{{appointmentId}}/confirm`

### 6. Cancelar Cita
- **Método**: DELETE
- **URL**: `{{baseUrl}}/appointments/{{appointmentId}}`

## Variables de entorno

- `baseUrl`: URL base del servicio (por defecto: http://127.0.0.1:8080)
- `appointmentId`: ID de una cita específica para operaciones por ID

## Flujo de prueba recomendado

1. **Crear una cita** usando el endpoint POST
2. **Copiar el ID** de la respuesta y establecerlo en la variable `appointmentId`
3. **Obtener la cita** usando el endpoint GET por ID
4. **Listar citas** con y sin filtros
5. **Confirmar la cita** usando el endpoint PATCH
6. **Verificar el cambio de estado** obteniendo la cita nuevamente
7. **Cancelar la cita** usando el endpoint DELETE

## Estados de Cita

- `CREATED`: Cita creada pero no confirmada
- `CONFIRMED`: Cita confirmada por el paciente/médico
- `CANCELLED`: Cita cancelada

## Formato de fechas

Las fechas deben enviarse en formato ISO 8601 con zona horaria:
`2025-09-29T10:00:00-05:00`

## Respuestas de ejemplo

### Crear Cita - Respuesta exitosa:
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "patientId": "patient-001",
  "practitionerId": "doctor-001",
  "startAt": "2025-09-29T10:00:00-05:00",
  "endAt": "2025-09-29T11:00:00-05:00",
  "state": "CREATED",
  "version": 1
}
```

### Listar Citas - Respuesta exitosa:
```json
{
  "content": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "patientId": "patient-001",
      "practitionerId": "doctor-001",
      "startAt": "2025-09-29T10:00:00-05:00",
      "endAt": "2025-09-29T11:00:00-05:00",
      "state": "CREATED",
      "version": 1
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20
  },
  "totalElements": 1,
  "totalPages": 1
}
```