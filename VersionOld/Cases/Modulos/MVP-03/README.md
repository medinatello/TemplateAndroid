
# MVP-03 — Configuración y almacenamiento (Android)

Este módulo aborda la gestión de preferencias y almacenamiento seguro en Android. El objetivo es implementar DataStore para preferencias generales y EncryptedSharedPreferences/Keystore para el manejo de secretos y tokens sensibles.

**Referencias:** Sección 03 de `docs/TechMap.md`.

#### Objetivos principales
- Persistir preferencias del usuario (ej. tema oscuro) de forma reactiva y segura.
- Almacenar secretos (tokens, credenciales) usando mecanismos cifrados y recomendados por Android.
- Definir la estructura de configuración (`AppConfig`) y las migraciones necesarias.

#### Componentes clave
- DataStore (Preferences/Proto) para datos no sensibles.
- EncryptedSharedPreferences + Keystore para datos sensibles.
- Repositorios: `PreferencesRepository` y `SecureStorage`.

#### Migraciones
- Migrar datos de SharedPreferences a DataStore.
- Definir políticas de reseteo y migración de claves.
