# Main functionality
- uploading files on the server
- downloading files from the server
- deleting files from the server
- available user disk space limitation (base is 1GB)
- authentication with Spring Security


## Steps to set-up the project
1. First clone the project from git repository<br>
<code>git clone https://github.com/kamilkub/java-spring-cloudstorage.git</code>
2. Go to project directory and edit <code>application.properties</code> file. <br>
3. Set MongoDB connection properties according to your settings.

```
spring.data.mongodb.host = localhost
spring.data.mongodb.port = 27017
spring.data.mongodb.database = cloudstorage
spring.data.mongodb.repositories.enabled = true
```

4. Set property <code>BASE_URL</code> to any directory you want as a main storage directory for the project.
5. Run the project typing <code>mvn spring-boot:run</code> in your terminal at project base path


# Technologies used in this project:
- AJAX 
- JQuery
- Spring Boot
- MongoDB
- Spring Security
- HTML5 CSS3
- Bootstrap 4
- Lombok
- JUnit
