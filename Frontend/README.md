# Ahorro Familiar

App Android en Kotlin con Jetpack Compose y arquitectura MVVM para que familias ahorren juntas hacia metas comunes (TV, moto, vacaciones, etc).

## Arquitectura

```
ui/screens          <-- Vistas (Composables que solo observan estado)
   |
   v
viewmodel           <-- ViewModels (logica + StateFlow<UiState>)
   |
   v
data/repository     <-- MetaRepository (intermediario)
   |
   v
data/remote (Retrofit)  o  data/local (FakeApiService con datos mock)
```

## Datos de prueba (mock)

El proyecto arranca con `USE_MOCK = true` en `ViewModelFactory`. Esto hace que la app use `FakeApiService` con datos en memoria y simulacion de latencia. Util para probar la app sin tener el backend Node corriendo.

Cuando el backend este listo: poner `USE_MOCK = false`.

URL base del backend (en `RetrofitClient.kt`):
- Emulador: `http://10.0.2.2:3000/`
- Dispositivo fisico: cambiar por la IP de la PC en la LAN.

## Endpoints que debe exponer el backend

```
GET    /metas              -> List<Meta>
GET    /metas/{id}         -> Meta (con miembros embebidos)
GET    /metas/{id}/pagos   -> List<Pago>
POST   /pagos              -> Pago
```

## Pantallas

1. **Lista de metas** - cards con foto, progreso y resumen general.
2. **Detalle de meta** - hero image, resumen, miembros con sus aportes, historial de pagos.
3. **Registrar pago** - dropdown de miembros, monto, boton de registro.

## Como abrir en Android Studio

1. Open -> seleccionar la carpeta `AhorroFamiliar`.
2. Esperar sync de Gradle.
3. Run (la app trae datos mock, funciona sin backend).
