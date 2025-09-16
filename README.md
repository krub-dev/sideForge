![sideForge Logo](assets/logo/sideForge-logo-yunque.jpg)

## Description of the project

**sideForge** is a RESTful API developed in Java with Spring Boot, focused on the management and customization of 3D assets (t-shirts, mugs, etc.) for web visualization and customization applications. The backend allows users to manage their accounts, view 3D assets, and personalize them through a flexible design system, including the application of colors, materials, logos, and texts on specific, predefined parts.

Authenticated users can create and modify 3D scenes, associate assets, and define personalized designs on different asset parts, as well as manage materials, logos, and texts applied to models.

---

## Class Diagram

![UML Diagram](assets/schema/sideForge-schema-UML-light.png)

---

## Domain Model and Structure

<details>
<summary><strong>Project Structure</strong></summary>

```plaintext
src/
├── assets/
│   ├── logo/
│   ├── schema/
│   ├── docs/
│   ├── test-report/
│   └── visual-resources/
│
├── main/
│   ├── java/
│   │   └── com/
│   │       └── sideforge/
│   │           ├── config/
│   │           │   └── OpenApiConfig.java
│   │           ├── controller/
│   │           │   ├── AdminController.java
│   │           │   ├── AssetController.java
│   │           │   ├── CustomerController.java
│   │           │   ├── DesignController.java
│   │           │   ├── SceneController.java
│   │           │   └── UserController.java
│   │           ├── dto/
│   │           │   ├── admin/
│   │           │   │   ├── AdminRequestDTO.java
│   │           │   │   ├── AdminResponseDTO.java
│   │           │   │   └── AdminUpdateDTO.java
│   │           │   ├── asset/
│   │           │   │   ├── AssetRequestDTO.java
│   │           │   │   ├── AssetResponseDTO.java
│   │           │   │   └── AssetUpdateDTO.java
│   │           │   ├── customer/
│   │           │   │   ├── CustomerRequestDTO.java
│   │           │   │   ├── CustomerResponseDTO.java
│   │           │   │   └── CustomerUpdateDTO.java
│   │           │   ├── design/
│   │           │   │   ├── DesignRequestDTO.java
│   │           │   │   ├── DesignResponseDTO.java
│   │           │   │   └── DesignUpdateDTO.java
│   │           │   ├── scene/
│   │           │   │   ├── SceneRequestDTO.java
│   │           │   │   ├── SceneResponseDTO.java
│   │           │   │   └── SceneUpdateDTO.java
│   │           │   └── user/
│   │           │       ├── UserRequestDTO.java
│   │           │       ├── UserResponseDTO.java
│   │           │       └── UserUpdateDTO.java
│   │           ├── enums/
│   │           │   ├── Department.java
│   │           │   ├── MaterialType.java
│   │           │   ├── Part.java
│   │           │   ├── PreferredLanguage.java
│   │           │   └── Role.java
│   │           ├── exception/
│   │           │   ├── ApiErrorResponse.java
│   │           │   ├── BadRequestException.java
│   │           │   ├── GlobalExceptionHandler.java
│   │           │   ├── InvalidCredentialsException.java
│   │           │   ├── ResourceNotFoundException.java
│   │           │   └── UsernameAlreadyExistsException.java
│   │           ├── model/
│   │           │   ├── Admin.java
│   │           │   ├── Asset.java
│   │           │   ├── Customer.java
│   │           │   ├── Design.java
│   │           │   ├── Scene.java
│   │           │   └── User.java
│   │           ├── repository/
│   │           │   ├── AdminRepository.java
│   │           │   ├── AssetRepository.java
│   │           │   ├── CustomerRepository.java
│   │           │   ├── DesignRepository.java
│   │           │   ├── SceneRepository.java
│   │           │   └── UserRepository.java
│   │           ├── security/
│   │           │   └── SecurityConfig.java
│   │           ├── service/
│   │           │   ├── impl/
│   │           │   │   ├── AdminServiceImpl.java
│   │           │   │   ├── AssetServiceImpl.java
│   │           │   │   ├── CustomerServiceImpl.java
│   │           │   │   ├── DesignServiceImpl.java
│   │           │   │   ├── SceneServiceImpl.java
│   │           │   │   └── UserServiceImpl.java
│   │           │   └── interfaces/
│   │           │       ├── AdminService.java
│   │           │       ├── AssetService.java
│   │           │       ├── CustomerService.java
│   │           │       ├── DesignService.java
│   │           │       ├── SceneService.java
│   │           │       └── UserService.java
│   │           ├── util/
│   │           │   └── DataLoader.java
│   │           └── SideForgeApplication.java
│   └── resources/
│       ├── application.properties
│       ├── static/
│       └── templates/
│
└── test/
    └── java/
        └── com/
            └── sideforge/
                ├── controller/
                │   ├── AdminControllerTest.java
                │   ├── AssetControllerTest.java
                │   ├── CustomerControllerTest.java
                │   ├── DesignControllerTest.java
                │   ├── SceneControllerTest.java
                │   └── UserControllerTest.java
                ├── service/
                │   └── impl/
                │       ├── AdminServiceImplTest.java
                │       ├── AssetServiceImplTest.java
                │       ├── CustomerServiceImplTest.java
                │       ├── DesignServiceImplTest.java
                │       ├── SceneServiceImplTest.java
                │       └── UserServiceImplTest.java
                ├── util/
                │   └── DataLoaderTest.java
                └── SideForgeApplicationTests.java
```

</details>

---

### Explanation of the Classes and Relationships

#### User (abstract)

-   **Base entity for all users** (JPA inheritance, `@MappedSuperclass` or `@Inheritance`).
-   **Attributes:**
    -   `id`: Long, autogenerated primary key.
    -   `username`: Unique username.
    -   `email`: Unique email.
    -   `passwordHash`: Hashed password.
    -   `role`: Enum (`Role`) — CUSTOMER or ADMIN.
-   **Inheritance:**
    -   `Customer` and `Admin` extend `User`.

#### Customer

-   **Extends User**.
-   **Attributes:**
    -   `profileImageUrl`: Profile image URL.
    -   `preferredLanguage`: Enum (`PreferredLanguage`).
    -   `isVerified`: Boolean, indicates if the user is verified.
-   **Relationships:**
    -   **Scenes:** A Customer can own multiple scenes (`@OneToMany`).

#### Admin

-   **Extends User**.
-   **Attributes:**
    -   `adminLevel`: Integer, privilege level.
    -   `department`: Enum (`Department`).
    -   `departmentImageUrl`: Profile image associated with the department.
    -   `lastLogin`: Last login date/time.

#### Asset

-   **Represents a base 3D asset** (t-shirt, mug, etc).
-   **Attributes:**
    -   `id`, `name`, `description`, `glbPath`, `thumbnailDefault`
    -   `partsConfigJson`: JSON with customizable parts definition.
-   **Relationships:**
    -   **Design:** An asset has many design (`@OneToMany` with `Design`).

#### Scene

-   **Represents a 3D scene customized by a user**.
-   **Attributes:**
    -   `id`, `name`, `lightingConfigJson`, `cameraConfigJson`, `thumbnail`, `createdAt`, `updatedAt`
-   **Relationships:**
    -   `owner`: Scene owner (`@ManyToOne` with `User`).
    -   `design`: Design shown in the scene (`@ManyToOne` or `@OneToOne` with `Design`).

#### Design

-   **Customization of an asset** (textures, materials, colors, logos, texts).
-   **Attributes:**
    -   `id`, `name`
    -   `textureMapUrl`: Base texture URL.
    -   `materialsJson`, `partsColorsJson`, `logoConfigJson`, `textConfigJson`: JSONs with materials, colors, logo, and text configuration.
-   **Relationships:**
    -   `asset`: Base asset being customized (`@ManyToOne` with `Asset`).
    -   Can be associated with one or more scenes.

---

<details>
<summary><strong>Enums</strong></summary>

**Part** (customizable parts, per asset)

```java
public enum Part {
    // T-SHIRT
    CHEST,
    LEFT_CHEST,
    RIGHT_CHEST,
    BACK,
    BACK_NECK,
    RIGHT_SLEEVE,
    LEFT_SLEEVE,
    COLLAR,
    INSIDE_LABEL, // Inner area for size or brand label

    // MUG
    HANDLE,
    BASE,
    INTERIOR,
    RIGHT_SIDE,
    LEFT_SIDE,

    // MOUSE PAD
    SURFACE,
    EDGE,
    BOTTOM
}
```

**MaterialType** (available materials)

```java
public enum MaterialType {
    // CLOTHING MATERIALS (Materiales para ropa)
    COTTON,           // Algodón
    POLYESTER,        // Poliéster
    WOOL,             // Lana
    LEATHER,          // Cuero
    NYLON,            // Nailon
    SILK,             // Seda
    DENIM,            // Vaquero/Denim

    // RUBBER & ELASTOMERS (Goma y elastómeros)
    RUBBER,           // Goma
    LATEX,            // Látex
    SILICONE,         // Silicona

    // PAPER & CARDBOARD (Papel y cartón)
    PAPER,            // Papel
    CARDBOARD,        // Cartón

    // WOOD (Madera)
    PINE,             // Pino
    OAK,              // Roble
    BEECH,            // Haya

    // METALS (Metales)
    STAINLESS_STEEL,  // Acero inoxidable
    ALUMINUM,         // Aluminio
    IRON              // Hierro
}
```

**PreferredLanguage** (supported user languages)

```java
public enum PreferredLanguage {
    ES,    // Español (Spanish)
    EN,    // Inglés (English)
    FR,    // Francés (French)
    DE,    // Alemán (German)
    IT     // Italiano (Italian)
}
```

**Department** (admin departments)

```java
public enum Department {
    IT,       // Technology (Tecnología)
    HR,       // Human Resources (Recursos Humanos)
    SUPPORT,  // Soporte (Atención al cliente)
    SALES,    // Ventas
    DESIGN    // Diseño
}
```

</details>

---

<details>
<summary><strong>Testing</strong></summary>

-   **Controllers:**  
    Use `@WebMvcTest` and `MockMvc` to simulate HTTP requests and validate responses. Controller tests are located in `src/test/java/com/sideForge/app/controller/` and cover all endpoints for users, assets, scenes, and designs.

-   **Services:**  
    Use `@ExtendWith(MockitoExtension.class)` and mock repositories to test business logic in isolation. Service tests are located in `src/test/java/com/sideForge/app/service/impl/` and cover all service implementations.

-   **DataLoader:**  
    Initial data loading logic is tested in `src/test/java/com/sideForge/app/DataLoaderTest.java`.

</details>

<details>
<summary><strong>Exceptions</strong></summary>

-   **ApiException**  
    Custom base exception for API errors. Used to return structured error responses with a message and HTTP status.

-   **GlobalExceptionHandler**  
    Handles all exceptions thrown in controllers. Converts exceptions into standardized HTTP responses (with error message, status, and timestamp).

-   **ResourceNotFoundException**  
    Thrown when a requested resource (user, asset, scene, design, etc.) does not exist. Returns a 404 Not Found response.

-   **ValidationException**  
    Thrown when input data fails validation (e.g., missing required fields, invalid formats). Returns a 400 Bad Request response.

</details>

<details>
<summary><strong>Security</strong></summary>

-   **Spring Security** is configured in `config/WebSecurityConfig.java` to secure all endpoints.
-   **Role-based access:**
    -   Endpoints are protected based on user roles (`CUSTOMER`, `ADMIN`).
    -   Only `ADMIN` users can create, update, or delete assets and access certain user data.
    -   Regular users (`CUSTOMER`) can only access and modify their own resources.
-   **Password encoding:**
    -   Passwords are securely hashed using a password encoder utility.
-   **Authentication:**
    -   Basic authentication is enabled for testing.
    -   JWT authentication is prepared but not enabled by default.
-   **Test admin user:**
    -   A default admin user is created on startup for endpoint testing:
        -   **Username:** `admin`
        -   **Password:** `admin`
        -   **Role:** `ADMIN`
    -   You can use these credentials to test all admin-only endpoints.

</details>

<details>
<summary><strong>OpenAPI & Swagger</strong></summary>

-   **Swagger/OpenAPI** is configured via `config/SwaggerConfig.java`.
-   Interactive API documentation is available at: [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)

</details>

<details>
<summary><strong>Postman Collection</strong></summary>

-   The full Postman collection for demo/testing is available at:
    [`assets/docs/sideForge-demo-structured.postman_collection.json`](assets/docs/sideForge-demo-structured.postman_collection.json)
-   This collection covers registration, authentication, CRUD flows, pagination, and role-based access examples.
-   You can import it directly into Postman for quick API testing.

</details>

---

## Setup

1. **Clone the repository:**

    ```bash
    git clone https://github.com/krub-dev/sideForge.git
    cd sideForge
    ```

2. **Configure the application:**

    - Edit `src/main/resources/application.properties` with your database and environment settings.

3. **Install dependencies and build:**

    ```bash
    mvn clean install
    ```

4. **Run the application:**

    ```bash
    mvn spring-boot:run
    ```

5. **Test endpoints:**  
   The API will be available at `http://localhost:8081`.

---

## Technologies Used

-   Java 17+
-   Spring Boot
-   Spring Data JPA
-   Spring Security (role based, simple)
-   Validation (Jakarta)
-   MySQL
-   Maven
-   Lombok
-   Swagger/OpenAPI (for API documentation)
-   Postman (testing endpoints)
-   Dbeaver (data base)
-   JUnit & Mockito (testing)
-   Mermaid (UML diagram)

---

## Controllers and Routes structure

### UserController (`/users`)

-   `POST /register` — Register a new user (Customer by default)
-   `POST /login` — User login (returns JWT)
-   `GET /profile` — Get authenticated user's profile
-   `PUT /profile` — Update authenticated user's profile
-   `GET /` — List all users (admin only)
-   `GET /{id}` — Get user by ID (admin only)
-   `PUT /{id}` — Update user by ID (admin only)
-   `DELETE /{id}` — Delete user by ID (admin/self)
-   `GET /me/scenes` — List authenticated user's scenes
-   `GET /me/designs` — List authenticated user's designs

### AssetController (`/assets`)

-   `GET /` — List all assets
-   `GET /{id}` — Get asset by ID
-   `POST /` — Create asset (admin only)
-   `PUT /{id}` — Update asset (admin only)
-   `DELETE /{id}` — Delete asset (admin only)
-   `GET /{id}/designs` — List all designs for an asset

### SceneController (`/scenes`)

-   `GET /` — List all scenes (admin) or user's scenes (customer)
-   `GET /{id}` — Get scene by ID (owner or admin)
-   `POST /` — Create new scene (authenticated)
-   `PUT /{id}` — Update scene (owner or admin)
-   `DELETE /{id}` — Delete scene (owner or admin)

### DesignController (`/designs`)

-   `GET /` — List all designs (admin) or user's designs (customer)
-   `GET /{id}` — Get design by ID (owner or admin)
-   `POST /` — Create new design (authenticated)
-   `PUT /{id}` — Update design (owner or admin)
-   `DELETE /{id}` — Delete design (owner or admin)

---

<details>
<summary><strong>DTOs by Entity</strong></summary>

-   **User**

    -   **UserRequestDTO**: username, email, password, role (optional/admin)
    -   **UserResponseDTO**: id, username, email, role
    -   **UserUpdateDTO**: username, email, password, profileImageUrl, preferredLanguage, isVerified

-   **Customer**

    -   **CustomerRequestDTO**: username, email, password, profileImageUrl, preferredLanguage, isVerified
    -   **CustomerResponseDTO**: id, username, email, profileImageUrl, preferredLanguage, isVerified
    -   **CustomerUpdateDTO**: profileImageUrl, preferredLanguage, isVerified

-   **Admin**

    -   **AdminRequestDTO**: username, email, password, adminLevel, department, departmentImageUrl, lastLogin
    -   **AdminResponseDTO**: id, username, email, adminLevel, department, departmentImageUrl, lastLogin
    -   **AdminUpdateDTO**: adminLevel, department, departmentImageUrl, lastLogin

-   **Asset**

    -   **AssetRequestDTO**: name, description, glbPath, thumbnailDefault, partsConfigJson
    -   **AssetResponseDTO**: id, name, description, glbPath, thumbnailDefault, partsConfigJson
    -   **AssetUpdateDTO**: name, description, glbPath, thumbnailDefault, partsConfigJson

-   **Scene**

    -   **SceneRequestDTO**: name, lightingConfigJson, cameraConfigJson, thumbnail, designId
    -   **SceneResponseDTO**: id, name, lightingConfigJson, cameraConfigJson, thumbnail, createdAt, updatedAt, designId, ownerId
    -   **SceneUpdateDTO**: name, lightingConfigJson, cameraConfigJson, thumbnail, designId

-   **Design**
    -   **DesignRequestDTO**: name, textureMapUrl, materialsJson, partsColorsJson, logoConfigJson, textConfigJson, assetId
    -   **DesignResponseDTO**: id, name, textureMapUrl, materialsJson, partsColorsJson, logoConfigJson, textConfigJson, assetId
    -   **DesignUpdateDTO**: name, textureMapUrl, materialsJson, partsColorsJson, logoConfigJson, textConfigJson, assetId

---

</details>

---

## Resources

-   **Kanban Board:** [Kanban's link](https://github.com/users/krub-dev/projects/2)
-   **Presentation Slides:** [Presentation's link]()
-   **API Docs (Swagger):** `/swagger-ui.html`

---

## Future Work

-   JWT authentication implemented.
-   Cloud image storage.
-   Design versioning.
-   Admin panel.
-   3D integration (Babylon.js or Three.js, TBD).
-   Framework front (Vue.js or React, TBD).
-   Docker for deployment.
-   Possible microservices refactor (users/auth and main logic separated with Eureka).

---

## Architecture Considerations

-   **Domain separation:** The system may be split into two microservices: one for users/authentication (with Eureka Service Discovery) and another for main logic (assets, scenes, designs).
-   **Decoupled databases:** Consider an independent database for each microservice.
-   **Scalability & Extensibility:** The architecture is designed to allow easy addition of new asset types, customization features, or integrations (e.g., payment, analytics, external 3D services) with minimal impact on existing code.

    -   **UserUpdateDTO**: username, email, password, profileImageUrl, preferredLanguage, isVerified

-   **Customer**

    -   **CustomerRequestDTO**: username, email, password, profileImageUrl, preferredLanguage, isVerified
    -   **CustomerResponseDTO**: id, username, email, profileImageUrl, preferredLanguage, isVerified
    -   **CustomerUpdateDTO**: profileImageUrl, preferredLanguage, isVerified

-   **Admin**

    -   **AdminRequestDTO**: username, email, password, adminLevel, department, departmentImageUrl, lastLogin
    -   **AdminResponseDTO**: id, username, email, adminLevel, department, departmentImageUrl, lastLogin
    -   **AdminUpdateDTO**: adminLevel, department, departmentImageUrl, lastLogin

-   **Asset**

    -   **AssetRequestDTO**: name, description, glbPath, thumbnailDefault, partsConfigJson
    -   **AssetResponseDTO**: id, name, description, glbPath, thumbnailDefault, partsConfigJson
    -   **AssetUpdateDTO**: name, description, glbPath, thumbnailDefault, partsConfigJson

-   **Scene**

    -   **SceneRequestDTO**: name, lightingConfigJson, cameraConfigJson, thumbnail, designId
    -   **SceneResponseDTO**: id, name, lightingConfigJson, cameraConfigJson, thumbnail, createdAt, updatedAt, designId, ownerId
    -   **SceneUpdateDTO**: name, lightingConfigJson, cameraConfigJson, thumbnail, designId

-   **Design**
    -   **DesignRequestDTO**: name, textureMapUrl, materialsJson, partsColorsJson, logoConfigJson, textConfigJson, assetId
    -   **DesignResponseDTO**: id, name, textureMapUrl, materialsJson, partsColorsJson, logoConfigJson, textConfigJson, assetId
    -   **DesignUpdateDTO**: name, textureMapUrl, materialsJson, partsColorsJson, logoConfigJson, textConfigJson, assetId

---

</details>

---

## Resources

-   **Kanban Board:** [Kanban's link](https://github.com/users/krub-dev/projects/2)
-   **Presentation Slides:** [Presentation's link]()
-   **API Docs (Swagger):** `/swagger-ui.html`

---

## Future Work

-   JWT authentication implemented.
-   Cloud image storage.
-   Design versioning.
-   Admin panel.
-   3D integration (Babylon.js or Three.js, TBD).
-   Framework front (Vue.js or React, TBD).
-   Docker for deployment.
-   Possible microservices refactor (users/auth and main logic separated with Eureka).

---

## Architecture Considerations

-   **Domain separation:** The system may be split into two microservices: one for users/authentication (with Eureka Service Discovery) and another for main logic (assets, scenes, designs).
-   **Decoupled databases:** Consider an independent database for each microservice.
-   **Scalability & Extensibility:** The architecture is designed to allow easy addition of new asset types, customization features, or integrations (e.g., payment, analytics, external 3D services) with minimal impact on existing code.
