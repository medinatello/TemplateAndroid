# [ANDROID][MVP-01] UI básica — Análisis para Desarrolladores

Este documento explica los conceptos técnicos clave implementados en el Módulo 1, con analogías para programadores de otras plataformas como .NET, Go, Java (con Spring) o desarrollo web (React/Angular).

---

## 1. La Interfaz de Usuario: Jetpack Compose (UI Declarativa)

**¿Qué es?**
Jetpack Compose es el framework moderno de Android para construir interfaces de usuario. En lugar de definir layouts en archivos XML y manipularlos desde código (el enfoque "imperativo"), se describe la UI como una función de su estado actual. Cuando el estado cambia, la UI se "recompone" (se redibuja) automáticamente.

**Analogías:**
-   **React / Vue.js:** Si vienes del mundo web, Compose es conceptualmente idéntico a React. Los `@Composable` son como los Componentes de React, los `State<T>` son como el `useState` de React, y el flujo de datos unidireccional es un principio central en ambos.
-   **SwiftUI (iOS):** Es el equivalente directo en el ecosistema de Apple. Ambos son frameworks de UI declarativos y nativos para sus respectivas plataformas.
-   **.NET (WPF/MAUI):** Es similar al patrón MVVM con Data Binding en XAML, pero llevado un paso más allá. En lugar de vincular propiedades de un ViewModel a un control en XAML, en Compose la propia UI es una función `(State) -> UI`. Es como si todo tu XAML estuviera escrito en C# y se actualizara solo.

**Ejemplo Clave:**
```kotlin
// El estado `name` se "levanta" (hoisted) a un nivel superior.
var name by remember { mutableStateOf("") }

// El Composable `OutlinedTextField` no gestiona su propio estado.
// Recibe el valor actual y una lambda para notificar cambios.
OutlinedTextField(
    value = name,
    onValueChange = { newName -> name = newName }
)
```
Esto se conoce como **State Hoisting** (elevación de estado) y es un patrón fundamental para crear componentes reutilizables y sin estado.

---

## 2. Navegación entre Pantallas: Navigation Compose

**¿Qué es?**
Es la librería oficial de Jetpack para gestionar la navegación en una app de Compose. Permite definir un grafo de navegación, manejar la pila de pantallas (back stack) y pasar argumentos de forma segura.

**Analogías:**
-   **React Router / Angular Router:** Es el "router" de la aplicación. El `NavHost` es como el componente `<Router>` o `<RouterProvider>`, y cada `composable(route = "...")` es como una `<Route path="...">`.
-   **ASP.NET Core (MVC/Razor Pages):** Piensa en el sistema de enrutamiento que mapea URLs a Actions en un Controller o a una Razor Page. Las "rutas" en Navigation Compose son strings (`"home"`, `"details/{id}"`) que actúan como identificadores únicos para cada pantalla.
-   **Java (Spring Boot):** Es similar a cómo `@RequestMapping` o `@GetMapping` mapean una URL a un método en un `@RestController`.

**Ejemplo Clave:**
```kotlin
val navController = rememberNavController()

NavHost(navController = navController, startDestination = "home") {
    composable("home") { HomeScreen(navController) }
    composable("details") { DetailsScreen(navController) }
}

// Para navegar
navController.navigate("details")

// Para volver atrás
navController.popBackStack()
```

---

## 3. Inyección de Dependencias (DI): Preparando el Terreno

**¿Qué es?**
Aunque en este MVP no se implementó un framework de DI como Hilt, la estructura está preparada para ello. La Inyección de Dependencias es un patrón que desacopla los componentes, haciendo que no creen sus propias dependencias, sino que se les "inyecten" desde fuera.

**Analogías:**
-   **.NET Core:** Es exactamente el mismo concepto que el contenedor de servicios (`IServiceCollection` y `IServiceProvider`). En lugar de `services.AddTransient<IMyService, MyService>()`, en Android con Hilt usarías anotaciones como `@Inject` y `@Provides`. El objetivo es el mismo: invertir el control (IoC).
-   **Spring (Java):** Es el pan de cada día. Hilt (la librería recomendada para Android) se inspira mucho en los conceptos de Spring. Anotaciones como `@Inject` (estándar de Java, JSR-330), `@Singleton`, `@Component`, y `@Module` tienen equivalentes directos.
-   **Go:** En Go, la DI se suele hacer de forma más explícita, pasando interfaces como parámetros en los constructores (e.g., `NewMyService(db MyDatabaseInterface)`). Hilt automatiza este proceso, pero el principio de depender de abstracciones (interfaces) en lugar de implementaciones concretas es el mismo.

**¿Cómo se preparó el proyecto?**
Al pasar lambdas para las acciones (`onContinue: () -> Unit`) y los ViewModels (que se añadirán en el futuro) a los Composables, estamos aplicando DI manualmente. La pantalla no sabe *qué* sucederá cuando se haga clic en el botón, solo sabe que debe invocar la función `onContinue`. Esto facilita las pruebas y el reemplazo de la implementación.
