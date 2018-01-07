# Shopping-REST-Api

A Jersey based REST API which is written in JAVA. Need a backend for a Shopping Application? Just use this :-) 

## Prerequisites

* Tomcat 8 or later. Make sure to setup `TOMCAT_HOME` environment variable. Gradle cargo plugin uses this variable to deploy the webapp.
* Gradle
* Java 8 (JDK)
* Mysql or Mariadb for DB

## Installing

1. Download the Project to local drive
2. Run `gradle assemble`
3. Run `gradle cargoRunLocal`

You are good to go :-)

## Authors

* [Prathab Murugan](https://github.com/jmprathab)


## License

This project is licensed under the Apache License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

The Project uses various awesome third party libraries including
* Java JWT library from auth0 (Yes we use JWT for authorization).
* JBCrypt Java library from Mindrot (For storing password to DB)
* libphonenumber from Google for Phone Number validation
* Apache commons-validator for email validation
* Swagger for generation awesome API docs
* Gradle Tomcat Cargo plugin for deploying the web app
* TestNG for writing Unit tests
* Okhttp3 for testing API
* MariaDB java driver
* Mongodb Java Driver