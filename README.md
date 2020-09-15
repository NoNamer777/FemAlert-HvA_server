# FemAlert-HvA_server

This is the Backend Server for the FemAlert Web Application. This server will receive requests from the Web Application and send back responses. 
It will also persist received data and pas it along so that it can be stored into a MySQL database server.

This Back-end server application is build using Spring Boot, Hibernate, JPA and is written in Java.

## Getting Started

- Make sure you have a JDK (Java Development Kit) of version 14 or higher:  
https://www.oracle.com/java/technologies/javase-jdk14-downloads.html

- Also check that you've installed Git:  
https://git-scm.com/downloads

Feel free to use the CLI (Command Line Interface) if you think you know what to do or use your prefered Git Client.  
*Personally I recommend Git Fork : https://git-fork.com/*

- Clone the repository using your prefered method using Git.

- Open the project in your prefered IDE and run the `FemServerApplication`. *I'd recommend InteliJ*  
Running the application will result in the project being build and after that the server is started.
The console will run through a couple lines and the last 4 lines should tell you that `LiveReload server is running on port xxxxx`,
`Tomcat started on port(s): xxx (http) with context path ''`, `Spring Data repositories initialized!`, 
and `Started FemServerApplication in x.xx seconds (JBM running for x.xx)`.
