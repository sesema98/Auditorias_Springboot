# 🧩 Proyecto AOPServa

Aplicación desarrollada en **Spring Boot** que implementa:

- **Programación Orientada a Aspectos (AOP)** para registrar **auditorías y logging**  
- **CRUD** completo de la entidad `Curso`  
- **Autenticación y autorización** con **Spring Security y roles (USER / ADMIN)**  
- **Persistencia en MySQL** usando **XAMPP** como servidor local  
- **Interfaz moderna con Bootstrap 5 y Thymeleaf**  

---

## 📜 Descripción General

El proyecto forma parte del laboratorio de **Desarrollo de Aplicaciones Web Avanzado**, enfocado en aplicar:
- **AOP (Programación Orientada a Aspectos)** para auditorías automáticas,  
- **Spring Data JPA** para la capa de persistencia,  
- **Spring Security** para la gestión de usuarios y permisos,  
- Y **Thymeleaf + Bootstrap** para la interfaz visual.  

El aspecto principal (`LogginAspecto`) intercepta las operaciones del servicio de cursos para registrar auditorías automáticamente, mientras que Spring Security controla los accesos y muestra las opciones disponibles según el rol del usuario.

---

## ⚙️ Tecnologías Utilizadas

- **Java 17+**  
- **Spring Boot 3+**  
- **Spring Web**  
- **Spring Data JPA**  
- **Spring AOP**  
- **Spring Security 6 (con roles y sesiones)**  
- **Thymeleaf + thymeleaf-extras-springsecurity6**  
- **Bootstrap 5.3.3**  
- **MySQL (XAMPP puerto 3308)**  
- **Maven**

---

## 🧱 Estructura Principal

```
src/
 └── main/java/com/tecsup/aopserva/
     ├── AopServaApplication.java
     ├── aop/
     │    └── LogginAspecto.java
     ├── config/
     │    ├── MvcConfig.java
     │    └── SecurityConfig.java
     ├── controllers/
     │    ├── CursoController.java
     │    └── MainController.java
     ├── domain/
     │    ├── entities/
     │    │    ├── Curso.java
     │    │    └── Auditoria.java
     │    └── persistence/
     │         ├── CursoDao.java
     │         └── AuditoriaDao.java
     ├── services/
     │    ├── CursoService.java
     │    └── CursoServiceImpl.java
     └── resources/
          ├── templates/
          │    ├── layout/layout.html
          │    ├── listar.html
          │    ├── form.html
          │    ├── login.html
          │    ├── error_403.html
          │    └── fragments/
          │         └── header.html
          └── static/
               ├── css/
               │    ├── bootstrap.min.css
               │    └── estilo.css
               └── js/
                    ├── bootstrap.bundle.min.js
                    └── main.js
```

---

## 🔐 Seguridad con Spring Security

El sistema usa **autenticación basada en base de datos**, con las tablas:

### **Tabla: users**
| id | username | password (BCrypt) | enabled |
|----|-----------|-------------------|----------|
| 1  | sergio    | … | 1 |
| 2  | admin     | … | 1 |

### **Tabla: authorities**
| id | authority   | user_id |
|----|--------------|---------|
| 1  | ROLE_USER    | 1       |
| 2  | ROLE_ADMIN   | 2       |

🔸 **Roles y permisos:**
- **ROLE_USER** → Puede listar y crear cursos.  
- **ROLE_ADMIN** → Puede editar, eliminar y exportar cursos.  

🔸 En las vistas Thymeleaf se usa `sec:authorize` para controlar la visibilidad de botones:
```html
<a sec:authorize="hasRole('ROLE_ADMIN')" class="btn btn-danger">Eliminar</a>
<a sec:authorize="hasRole('ROLE_USER')" class="btn btn-success">Crear Curso</a>
```

🔸 El sistema también incluye una **página de error personalizada** (`error_403.html`) cuando el usuario intenta acceder a algo no permitido.

---

## 🧩 Programación Orientada a Aspectos (AOP)

El aspecto `LogginAspecto` intercepta los métodos del servicio para registrar logs y auditorías automáticas:

```java
@Around("execution(* com.tecsup.aopserva.services.*ServiceImpl.*(..))")
public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.currentTimeMillis();
    Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
    String metodo = joinPoint.getSignature().getName();

    logger.info("Ejecutando: " + metodo);
    Object result = joinPoint.proceed();
    logger.info("Tiempo: " + (System.currentTimeMillis() - start) + " ms");

    auditoriaDao.save(new Auditoria("cursos", obtenerId(joinPoint, metodo),
            new Date(), "usuario", metodo));

    return result;
}
```

---

## 🎨 Interfaz Gráfica (Bootstrap + Thymeleaf)

El proyecto cuenta con una interfaz limpia y moderna:
- **Navbar verde fija** con secciones desplegables  
- **Tablas responsivas con bordes y sombreado**  
- **Botones de acción según el rol del usuario**  
- **Mensajes flash de éxito/error usando alertas Bootstrap**  

Ejemplo de la vista principal (`listar.html`):

```html
<h1 class="text-secondary border-success border-bottom border-3 pb-2">
    Mantenimiento de Cursos
</h1>

<a sec:authorize="hasRole('ROLE_USER')" class="btn btn-success btn-sm">Crear Curso</a>

<table class="table table-striped table-bordered table-hover">
    <thead class="bg-dark text-white">
        <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Créditos</th>
            <th sec:authorize="hasRole('ROLE_ADMIN')">Acciones</th>
        </tr>
    </thead>
    <tbody>
        <tr th:each="curso : ${cursos}">
            <td th:text="${curso.id}"></td>
            <td th:text="${curso.nombre}"></td>
            <td th:text="${curso.creditos}"></td>
            <td sec:authorize="hasRole('ROLE_ADMIN')">
                <a class="btn btn-primary btn-sm" th:href="@{/form/} + ${curso.id}">Editar</a>
                <a class="btn btn-danger btn-sm" th:href="@{/eliminar/} + ${curso.id}"
                   onclick="return confirm('¿Eliminar curso?');">Eliminar</a>
            </td>
        </tr>
    </tbody>
</table>
```

---

## 📊 Resultados Obtenidos

- El sistema CRUD de cursos funciona correctamente con control de roles.  
- Los logs de AOP registran las operaciones realizadas y sus tiempos.  
- Los accesos están protegidos: los usuarios no pueden editar/eliminar.  
- Las vistas muestran u ocultan botones según el rol.  
- Interfaz moderna y coherente gracias a Bootstrap 5.  

---

## 🧾 TODO (pendiente)

- Integrar auditoría real con el usuario logueado.  
- Agregar exportación a **PDF/XLSX** con restricciones por rol.  
- Mejorar los mensajes visuales de confirmación y validación.  

---

## 🧠 Conclusiones del Laboratorio

1. Aprendí cómo Spring Security permite proteger una aplicación y definir permisos de forma sencilla y organizada.  
2. Comprobé que los roles ayudan a controlar mejor lo que cada usuario puede hacer dentro del sistema.  
3. Entendí cómo funciona el inicio y cierre de sesión con sesiones seguras en Spring Boot.  
4. Valoro la importancia del diseño visual: Bootstrap hizo la interfaz más clara y profesional.  
5. Este proyecto me permitió integrar seguridad, AOP y presentación en una aplicación completa y funcional.  

---

## 👨‍💻 Autor

**Sergio Sebastián**  
Proyecto académico – *Desarrollo de Aplicaciones Web Avanzado*  
Instituto TECSUP – 2025  
