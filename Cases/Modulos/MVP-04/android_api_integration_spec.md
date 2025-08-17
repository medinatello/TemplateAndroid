# API Integration Specification - MVP-04 Android

## Contexto
Este documento complementa la documentación del MVP-04 proporcionando detalles específicos sobre la integración de API externa usando Ktor Client en el módulo `shared` para el proyecto Kotlin Multiplatform.

## API Base: JSONPlaceholder
- **Base URL**: `https://jsonplaceholder.typicode.com`
- **Tipo**: REST API pública para testing
- **Autenticación**: No requerida
- **Rate Limits**: Ninguno

## Arquitectura Multiplataforma

### Módulo Shared (commonMain)
La lógica de red se implementará en `shared/src/commonMain` usando Ktor Client para garantizar compatibilidad multiplataforma.

### Configuración de Dependencias
```kotlin
// shared/build.gradle.kts
implementation("io.ktor:ktor-client-core:2.3.4")
implementation("io.ktor:ktor-client-content-negotiation:2.3.4")
implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.4")
implementation("io.ktor:ktor-client-logging:2.3.4")

// Platform-specific engines
androidMain.dependencies {
    implementation("io.ktor:ktor-client-okhttp:2.3.4")
}

jvmMain.dependencies {
    implementation("io.ktor:ktor-client-cio:2.3.4")
}
```

## Endpoints a Implementar

### 1. Lista de Usuarios
```
GET /users
Response: List<User>
```

**Estructura de respuesta esperada**:
```json
[
  {
    "id": 1,
    "name": "Leanne Graham",
    "username": "Bret",
    "email": "Sincere@april.biz",
    "address": {
      "street": "Kulas Light",
      "suite": "Apt. 556",
      "city": "Gwenborough",
      "zipcode": "92998-3874",
      "geo": {
        "lat": "-37.3159",
        "lng": "81.1496"
      }
    },
    "phone": "1-770-736-8031 x56442",
    "website": "hildegard.org",
    "company": {
      "name": "Romaguera-Crona",
      "catchPhrase": "Multi-layered client-server neural-net",
      "bs": "harness real-time e-markets"
    }
  }
]
```

### 2. Usuario Individual
```
GET /users/{id}
Response: User
```

### 3. Posts de Usuario
```
GET /users/{userId}/posts
Response: List<Post>
```

## Modelos de Datos

### DTOs (kotlinx.serialization)
```kotlin
// shared/src/commonMain/kotlin/data/dto/UserDto.kt
@Serializable
data class UserDto(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val address: AddressDto,
    val phone: String,
    val website: String,
    val company: CompanyDto
)

@Serializable
data class AddressDto(
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String,
    val geo: GeoDto
)

@Serializable
data class GeoDto(
    val lat: String,
    val lng: String
)

@Serializable
data class CompanyDto(
    val name: String,
    val catchPhrase: String,
    val bs: String
)

@Serializable
data class PostDto(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)
```

### Domain Models
```kotlin
// shared/src/commonMain/kotlin/domain/model/User.kt
data class User(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val address: Address,
    val phone: String,
    val website: String,
    val company: Company
)

data class Address(
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String,
    val coordinates: Coordinates
)

data class Coordinates(
    val latitude: String,
    val longitude: String
)

data class Company(
    val name: String,
    val catchPhrase: String,
    val businessStrategy: String
)

data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)
```

## Arquitectura de Componentes

### 1. ApiClient Interface (commonMain)
```kotlin
// shared/src/commonMain/kotlin/data/network/ApiClient.kt
interface ApiClient {
    suspend fun fetchUsers(): Result<List<User>>
    suspend fun fetchUser(id: Int): Result<User>
    suspend fun fetchUserPosts(userId: Int): Result<List<Post>>
}
```

### 2. Ktor Implementation (commonMain)
```kotlin
// shared/src/commonMain/kotlin/data/network/KtorApiClient.kt
class KtorApiClient(
    private val httpClient: HttpClient,
    private val baseUrl: String = "https://jsonplaceholder.typicode.com"
) : ApiClient {

    override suspend fun fetchUsers(): Result<List<User>> = 
        safeApiCall {
            val response = httpClient.get("$baseUrl/users")
            val userDtos = response.body<List<UserDto>>()
            userDtos.map { it.toDomain() }
        }

    override suspend fun fetchUser(id: Int): Result<User> = 
        safeApiCall {
            val response = httpClient.get("$baseUrl/users/$id")
            val userDto = response.body<UserDto>()
            userDto.toDomain()
        }

    override suspend fun fetchUserPosts(userId: Int): Result<List<Post>> = 
        safeApiCall {
            val response = httpClient.get("$baseUrl/users/$userId/posts")
            val postDtos = response.body<List<PostDto>>()
            postDtos.map { it.toDomain() }
        }

    private suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
        return try {
            Result.success(apiCall())
        } catch (e: Exception) {
            Result.failure(NetworkError.fromException(e))
        }
    }
}
```

### 3. HTTP Client Factory (expect/actual)
```kotlin
// shared/src/commonMain/kotlin/data/network/HttpClientFactory.kt
expect object HttpClientFactory {
    fun create(): HttpClient
}

// shared/src/androidMain/kotlin/data/network/HttpClientFactory.kt
actual object HttpClientFactory {
    actual fun create(): HttpClient = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        install(Logging) {
            level = LogLevel.INFO
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 30_000
            connectTimeoutMillis = 15_000
            socketTimeoutMillis = 15_000
        }
    }
}

// shared/src/jvmMain/kotlin/data/network/HttpClientFactory.kt
actual object HttpClientFactory {
    actual fun create(): HttpClient = HttpClient(CIO) {
        // Similar configuration
    }
}
```

### 4. Error Handling
```kotlin
// shared/src/commonMain/kotlin/data/network/NetworkError.kt
sealed class NetworkError : Exception() {
    object NoInternet : NetworkError()
    object Timeout : NetworkError()
    data class ServerError(val code: Int) : NetworkError()
    data class UnknownError(val throwable: Throwable) : NetworkError()

    companion object {
        fun fromException(exception: Exception): NetworkError {
            return when (exception) {
                is HttpRequestTimeoutException -> Timeout
                is ClientRequestException -> ServerError(exception.response.status.value)
                is ServerResponseException -> ServerError(exception.response.status.value)
                else -> UnknownError(exception)
            }
        }
    }
}
```

### 5. Mappers (DTOs to Domain)
```kotlin
// shared/src/commonMain/kotlin/data/mapper/UserMapper.kt
fun UserDto.toDomain(): User = User(
    id = id,
    name = name,
    username = username,
    email = email,
    address = address.toDomain(),
    phone = phone,
    website = website,
    company = company.toDomain()
)

fun AddressDto.toDomain(): Address = Address(
    street = street,
    suite = suite,
    city = city,
    zipcode = zipcode,
    coordinates = geo.toDomain()
)

fun GeoDto.toDomain(): Coordinates = Coordinates(
    latitude = lat,
    longitude = lng
)

fun CompanyDto.toDomain(): Company = Company(
    name = name,
    catchPhrase = catchPhrase,
    businessStrategy = bs
)

fun PostDto.toDomain(): Post = Post(
    userId = userId,
    id = id,
    title = title,
    body = body
)
```

## Repository Pattern

### Repository Interface (commonMain)
```kotlin
// shared/src/commonMain/kotlin/domain/repository/UserRepository.kt
interface UserRepository {
    suspend fun getUsers(): Result<List<User>>
    suspend fun getUser(id: Int): Result<User>
    suspend fun getUserPosts(userId: Int): Result<List<Post>>
}
```

### Repository Implementation (commonMain)
```kotlin
// shared/src/commonMain/kotlin/data/repository/UserRepositoryImpl.kt
class UserRepositoryImpl(
    private val apiClient: ApiClient
) : UserRepository {

    override suspend fun getUsers(): Result<List<User>> {
        return apiClient.fetchUsers()
    }

    override suspend fun getUser(id: Int): Result<User> {
        return apiClient.fetchUser(id)
    }

    override suspend fun getUserPosts(userId: Int): Result<List<Post>> {
        return apiClient.fetchUserPosts(userId)
    }
}
```

## Use Cases

### GetUsers Use Case
```kotlin
// shared/src/commonMain/kotlin/domain/usecase/GetUsersUseCase.kt
class GetUsersUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<List<User>> {
        return userRepository.getUsers()
    }
}
```

## Dependency Injection (Expect/Actual)

### DI Container Interface (commonMain)
```kotlin
// shared/src/commonMain/kotlin/di/AppContainer.kt
expect class AppContainer {
    val userRepository: UserRepository
    val getUsersUseCase: GetUsersUseCase
}
```

### Android Implementation
```kotlin
// shared/src/androidMain/kotlin/di/AppContainer.kt
actual class AppContainer {
    private val httpClient = HttpClientFactory.create()
    private val apiClient: ApiClient = KtorApiClient(httpClient)
    
    actual val userRepository: UserRepository = UserRepositoryImpl(apiClient)
    actual val getUsersUseCase: GetUsersUseCase = GetUsersUseCase(userRepository)
}
```

## Testing Strategy

### Common Tests (commonTest)
```kotlin
// shared/src/commonTest/kotlin/data/network/KtorApiClientTest.kt
class KtorApiClientTest {

    private val mockEngine = MockEngine { request ->
        when (request.url.encodedPath) {
            "/users" -> respond(
                content = """[{"id":1,"name":"Test User","username":"testuser","email":"test@test.com","address":{"street":"Test St","suite":"Apt 1","city":"Test City","zipcode":"12345","geo":{"lat":"0","lng":"0"}},"phone":"123-456-7890","website":"test.com","company":{"name":"Test Co","catchPhrase":"Test phrase","bs":"test bs"}}]""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
            else -> respond("", HttpStatusCode.NotFound)
        }
    }

    private val client = KtorApiClient(
        HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json()
            }
        }
    )

    @Test
    fun `fetchUsers returns success with users list`() = runTest {
        val result = client.fetchUsers()
        
        assertTrue(result.isSuccess)
        val users = result.getOrNull()
        assertNotNull(users)
        assertEquals(1, users?.size)
        assertEquals("Test User", users?.first()?.name)
    }

    @Test
    fun `fetchUser with invalid id returns failure`() = runTest {
        val result = client.fetchUser(999)
        
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is NetworkError.ServerError)
    }
}
```

### Android Integration Tests
```kotlin
// androidApp/src/androidTest/kotlin/network/ApiIntegrationTest.kt
@RunWith(AndroidJUnit4::class)
class ApiIntegrationTest {

    @Test
    fun realApiCall_fetchUsers_success() = runTest {
        val httpClient = HttpClientFactory.create()
        val apiClient = KtorApiClient(httpClient)
        
        val result = apiClient.fetchUsers()
        
        assertTrue(result.isSuccess)
        val users = result.getOrNull()
        assertNotNull(users)
        assertTrue(users!!.isNotEmpty())
    }
}
```

## UI Integration (Android)

### ViewModel Example
```kotlin
// androidApp/src/main/kotlin/presentation/users/UsersViewModel.kt
@HiltViewModel
class UsersViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UsersUiState())
    val uiState: StateFlow<UsersUiState> = _uiState.asStateFlow()

    fun loadUsers() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            getUsersUseCase().fold(
                onSuccess = { users ->
                    _uiState.value = _uiState.value.copy(
                        users = users,
                        isLoading = false,
                        error = null
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
            )
        }
    }
}

data class UsersUiState(
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
```

### Compose UI
```kotlin
// androidApp/src/main/kotlin/presentation/users/UsersScreen.kt
@Composable
fun UsersScreen(
    viewModel: UsersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUsers()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            uiState.error != null -> {
                Text(
                    text = "Error: ${uiState.error}",
                    color = MaterialTheme.colorScheme.error
                )
            }
            
            else -> {
                LazyColumn {
                    items(uiState.users) { user ->
                        UserItem(user = user)
                    }
                }
            }
        }
    }
}

@Composable
fun UserItem(user: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = user.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "${user.address.city}, ${user.address.street}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
```

## Configuración de Build

### Proguard/R8 Rules (Android)
```kotlin
# androidApp/proguard-rules.pro
-keep class kotlinx.serialization.** { *; }
-keep class com.yourapp.shared.data.dto.** { *; }
-keep class io.ktor.** { *; }

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
```

### Network Security Config (Android)
```xml
<!-- androidApp/src/main/res/xml/network_security_config.xml -->
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">jsonplaceholder.typicode.com</domain>
    </domain-config>
</network-security-config>
```

### Manifest Permissions
```xml
<!-- androidApp/src/main/AndroidManifest.xml -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

## Criterios de Validación

### Funcionalidad Core
- [ ] ApiClient implementado en shared/commonMain usando Ktor
- [ ] Configuraciones expect/actual para motores HTTP específicos por plataforma
- [ ] Mapeo correcto de DTOs a Domain models usando kotlinx.serialization
- [ ] Manejo robusto de errores con sealed classes
- [ ] Repository pattern implementado correctamente

### Testing
- [ ] Tests unitarios para ApiClient usando MockEngine
- [ ] Tests de mappers (DTO → Domain)
- [ ] Tests de integración con API real
- [ ] Tests de error scenarios (timeout, 404, network errors)
- [ ] Cobertura mínima 80% en shared module

### Arquitectura
- [ ] Separación clara entre capas (data, domain, presentation)
- [ ] Dependency injection configurado correctamente
- [ ] Use cases implementados siguiendo clean architecture
- [ ] ViewModels con manejo de estado usando StateFlow

### Multiplataforma
- [ ] Lógica de red funciona en Android y Desktop
- [ ] Configuración específica por plataforma usando expect/actual
- [ ] Tests ejecutan correctamente en commonTest

## Archivos a Crear/Modificar

### Shared Module
1. `shared/src/commonMain/kotlin/data/dto/UserDto.kt`
2. `shared/src/commonMain/kotlin/data/network/ApiClient.kt`
3. `shared/src/commonMain/kotlin/data/network/KtorApiClient.kt`
4. `shared/src/commonMain/kotlin/data/network/HttpClientFactory.kt`
5. `shared/src/commonMain/kotlin/data/mapper/UserMapper.kt`
6. `shared/src/commonMain/kotlin/domain/model/User.kt`
7. `shared/src/commonMain/kotlin/domain/repository/UserRepository.kt`
8. `shared/src/commonMain/kotlin/data/repository/UserRepositoryImpl.kt`

### Platform-Specific
9. `shared/src/androidMain/kotlin/data/network/HttpClientFactory.kt`
10. `shared/src/jvmMain/kotlin/data/network/HttpClientFactory.kt`

### Android App
11. `androidApp/src/main/kotlin/presentation/users/UsersViewModel.kt`
12. `androidApp/src/main/kotlin/presentation/users/UsersScreen.kt`

### Tests
13. `shared/src/commonTest/kotlin/data/network/KtorApiClientTest.kt`
14. `androidApp/src/androidTest/kotlin/network/ApiIntegrationTest.kt`

## Notas Importantes para la IA

1. **Usar Ktor Client**: Implementar con Ktor para compatibilidad multiplataforma
2. **kotlinx.serialization**: Usar para serialización JSON, no Gson
3. **Result pattern**: Usar Result<T> para manejo de errores, no excepciones
4. **expect/actual**: Utilizar para configuraciones específicas por plataforma
5. **Clean Architecture**: Separar responsabilidades entre capas claramente
6. **StateFlow**: Usar para reactive state management en ViewModels
7. **Dependency Injection**: Implementar usando el patrón Manual DI o Hilt
8. **Testing**: Incluir tanto unit tests como integration tests