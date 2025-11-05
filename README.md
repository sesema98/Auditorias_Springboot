# 🧩 Proyecto AOPServa

Aplicación desarrollada en **Spring Boot** que implementa:

- **Programación Orientada a Aspectos (AOP)** para registrar **auditorías y logging**
- **CRUD** de la entidad `Curso`
- **Persistencia en MySQL** usando **XAMPP** como servidor local  
- **Thymeleaf** para la capa de presentación

---

## 📜 Descripción General

El proyecto forma parte del laboratorio de **Desarrollo de Aplicaciones Web Avanzado**, enfocado en aplicar **AOP** y **Spring Data JPA** dentro de un sistema CRUD funcional.  
El aspecto principal (`LogginAspecto`) intercepta las operaciones del servicio de cursos para registrar auditorías automáticamente.

---

## ⚙️ Tecnologías Utilizadas

- **Java 17+**
- **Spring Boot 3+**
- **Spring Web**
- **Spring Data JPA**
- **Spring AOP**
- **MySQL (XAMPP puerto 3308)**
- **Thymeleaf**
- **Maven**

---

## 🛠️ Configuración del Proyecto

Archivo: `src/main/resources/application.properties`

```properties
spring.application.name=AopServa
application.title=Proyecto AOP Serva con MySQL
application.description=CRUD básico de Cursos con Spring Boot + JPA + XAMPP
application.version=1.0.0

# ============================
# CONFIGURACIÓN DE MYSQL (XAMPP)
# ============================
spring.datasource.url=jdbc:mysql://localhost:3308/escueladb?useSSL=false&serverTimezone=UTC
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=

# ============================
# CONFIGURACIÓN DE JPA / HIBERNATE
# ============================
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect

# ============================
# SERVIDOR
# ============================
server.port=8086
```

---

## 🧩 Estructura Principal

```
src/
 └── main/java/com/tecsup/aopserva/
     ├── AopServaApplication.java
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
     └── aop/
          └── LogginAspecto.java
```

---

## 🧱 Entidades Principales

### **Curso.java**
Entidad JPA que representa la tabla `cursos`.

```java
@Entity
@Table(name = "cursos")
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nombre;
    private int creditos;
}
```

### **Auditoria.java**
Guarda registros de operaciones realizadas sobre `Curso`.

```java
@Entity
@Table(name = "auditoria")
public class Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tabla;
    private int recursoId;
    private Date fecha;
    private String usuario;
    private String accion;
}
```

---

## 🧠 Programación Orientada a Aspectos (AOP)

El **aspecto `LogginAspecto`** intercepta las llamadas a métodos de los servicios (`*ServiceImpl`) para registrar automáticamente operaciones CRUD y medir tiempo de ejecución.

```java
@Aspect
@Component
public class LogginAspecto {

    private Long tx;

    @Autowired
    private AuditoriaDao auditoriaDao;

    @Around("execution(* com.tecsup.aopserva.services.*ServiceImpl.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        tx = System.currentTimeMillis();
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        String metodo = joinPoint.getSignature().getName();

        if (joinPoint.getArgs().length > 0)
            logger.info(metodo + "() INPUT: " + Arrays.toString(joinPoint.getArgs()));

        Object result = joinPoint.proceed();

        logger.info(metodo + "(): tiempo transcurrido " + (System.currentTimeMillis() - tx) + " ms.");

        // Auditoría básica
        auditoriaDao.save(new Auditoria("cursos", obtenerId(joinPoint, metodo),
                new Date(), "usuario", metodo));

        return result;
    }

    private int obtenerId(ProceedingJoinPoint joinPoint, String metodo) {
        if (metodo.startsWith("guardar")) {
            Curso curso = (Curso) joinPoint.getArgs()[0];
            return curso.getId();
        } else if (metodo.startsWith("editar") || metodo.startsWith("eliminar")) {
            return (Integer) joinPoint.getArgs()[0];
        }
        return 0;
    }
}
```

> Este aspecto aplica **@Around** a todos los métodos de servicios (`*ServiceImpl`) para registrar entrada, salida, tiempo y auditorías.

---

## 🌐 Vistas Thymeleaf

Rutas principales:
- `/` → Inicio  
- `/cursos` → Lista de cursos  
- `/cursos/nuevo` → Formulario de nuevo curso  
- `/cursos/editar/{id}` → Edición  
- `/cursos/eliminar/{id}` → Eliminación  

Archivos principales:
- `templates/inicio.html`  
- `templates/listar.html`  
- `templates/formCurso.html`  
- `templates/fragments/header.html`

---

## 📊 Resultados Esperados

- Al realizar operaciones CRUD, se registran automáticamente auditorías en la tabla `auditoria`.
- En la consola de logs se muestran:
  - Entradas (`INPUT`)
  - Tiempos de ejecución
  - Registros de operaciones

---

## 🧾 TODO (pendiente)

- Exportar reportes a **PDF** / **XLS**  
- Integrar autenticación de usuario para auditoría real

---

## 👨‍💻 Autor

**Sergio Sebastián**  
Proyecto académico para **AOP con Spring Boot**
