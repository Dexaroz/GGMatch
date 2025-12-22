# 🎮 GGMatch 🤝

**GGMatch** es una aplicación móvil nativa diseñada para conectar jugadores de **League of Legends (LoL)** con intereses, horarios y estilos de juego compatibles. Inspirada en el modelo de interacción de aplicaciones de emparejamiento modernas, busca reducir la fragmentación en las comunidades de videojuegos y facilitar la búsqueda de compañeros ideales tanto para partidas competitivas como casuales.

---

## 🚀 Características Principales

### 🧑‍💻 Gestión de Perfil Gamer
Configuración detallada del perfil del jugador:
- Nombre de invocador
- Roles (Top, Jungla, Mid, ADC, Support)
- Rango (Hierro a Challenger)
- Modos de juego
- Horarios de conexión

### 👉 Sistema de Swipe
Interfaz intuitiva basada en tarjetas deslizables que permite evaluar candidatos de forma rápida, visual y dinámica.

### 🧠 Algoritmo de Matchmaking
Sistema de emparejamiento que identifica coincidencias según:
- Preferencias de rol
- Nivel de habilidad
- Disponibilidad horaria

### 💬 Chat en Tiempo Real
Comunicación directa y segura entre usuarios mediante mensajería interna basada en **Firebase**.

### 🔗 Integración con Riot Games
Validación de datos reales del jugador utilizando la **API oficial de Riot Games**.

---

## 🏗️ Arquitectura y Tecnologías

El proyecto sigue los principios de **Clean Architecture** y **Domain-Driven Design (DDD)** para garantizar escalabilidad, mantenibilidad y separación de responsabilidades.

### 🧰 Stack Tecnológico

- **Lenguaje:** Kotlin  
- **Frontend:** Android Nativo con Material Design 3  
- **Backend:** Firebase  
  - Cloud Firestore (persistencia)
  - Firebase Authentication (gestión de usuarios)

### 🧩 Patrones de Diseño

- **MVP (Model-View-Presenter)**: Desacoplamiento de la interfaz de usuario  
- **CQRS / Command**: Gestión de acciones y cambios de estado  
- **Adapter**: Aislamiento de servicios externos (API de Riot, Firebase)  
- **Strategy**: Gestión dinámica del almacenamiento de imágenes  

---

## 📂 Estructura del Proyecto

La organización del código refleja una clara separación de responsabilidades:

```plaintext
/architecture
  /control      # Casos de uso, presenters y command handlers
  /io           # Implementaciones de red, API de Riot y Firebase
  /model        # Núcleo del dominio y reglas de negocio
  /sharedKernel # Utilidades y constantes comunes
  /view         # Actividades, fragmentos y componentes de UI
```

## 🛠️ Instalación y Desarrollo

### Requisitos Previos

- Android Studio Iguana (o superior)
- JDK 17
- Cuenta de Firebase configurada
- API Key de Riot Games

### Configuración

1. Clona el repositorio:

```bash
git clone https://github.com/tu-usuario/GGMatch.git
```

2. Añade el archivo `google-services.json` en la carpeta `/app`.

3. Configura tu **API Key de Riot Games** en las variables de entorno o en los archivos de configuración correspondientes.

4. Sincroniza el proyecto con **Gradle** y ejecuta la aplicación en un emulador o dispositivo físico.

---

## 🧪 Calidad y CI/CD

- **Control de Versiones:** Gitflow (features, fixes y refactors)
- **Commits:** Convención **Conventional Commits**
- **CI:** GitHub Actions
  - Build checks
  - Tests unitarios con JUnit
  - Validación de estilo con ktlint en cada Pull Request

---

## 👥 Autores

Proyecto desarrollado por:

- **Miguel Castellano Hernández**
- **Eduardo Marrero González**

---

## 📄 Licencia

Este proyecto fue realizado en el marco de la asignatura  
**Programación de Aplicaciones Móviles Nativas – ULPGC**.
