# FemAlert-HvA_server

This is the Backend Server for the FemAlert Web Application. This server will receive requests from the Web Application and send back responses. 
It will also persist received data and pas it along so that it can be stored into a MySQL database server.

This Back-end server application is build using Spring Boot, Hibernate, JPA and is written in Java.

## Getting Started

- Make sure you have a JDK (Java Development Kit) of version 14 or higher:  
https://www.oracle.com/java/technologies/javase-jdk14-downloads.html

- Also check that you've installed Git:  
https://git-scm.com/downloads

Feel free to use the Git CLI (Command Line Interface) if you think you know what to do or use your prefered Git Client.  

- Clone the repository using your prefered method using Git.

- Open the project in your prefered IDE, let it import and download all the dependencies that are devinded in the pom.xml and make sure the application.properties file is created as described below. After that you can run the `FemServerApplication`.

If the dependencies have not been downloaded, or they won't download automatically, you can try to import a new module from an existing source.
using that method you can import the pom.xml as a module and after that maven should recognize the configuration files and download the dependencies.

For security reasons we've decided to exclude keeping track of the application.properties file that is located at `src/main/resources/`
make sure to create that file with the following contents:


```Properties
spring.datasource.url = jdbc:mysql://<database-url>:<database-port>/<database-schema>
spring.datasource.username = <user>
spring.datasource.password = <password>
spring.datasource.initialization-mode = always #optional
spring.datasource.platform = mysql

spring.jpa.hibernate.ddl-auto = create-drop #optional

# JWT token config
jwt.issuer = <issuer>
jwt.pass-phrase = <encryption passwod>
jwt.validity-duration = <token validity time in ms>

# Email service config
spring.mail.host = <email host>
spring.mail.port = <email host smtp port>
spring.mail.username = <email username>
spring.mail.password = <email application password>
spring.mail.properties.mail.smtp.auth = true
spring.mail.properties.mail.smtp.starttls.enable = true

# Logging config, these are all optional
logging.level.org.hibernate.stat = debug
logging.level.org.hibernate.type = trace

spring.h2.console.enabled = true

spring.jpa.properties.hibernate.generate_statistics = true
spring.jpa.properties.hibernate.format_sql = true

spring.jpa.show-sql = true

spring.jackson.serialization.write-dates-as-timestamps = false
```

everything that is encased in `<>` should be replaced with your own values.

The server uses a MySQL database for data persistance, that should also be set up, prior to running the server.

The `spring.mail` propereties are necessary in order for the server to send emails via the provided email address.
how to send up those properties is explained in more detail in this article:
https://www.baeldung.com/spring-email
