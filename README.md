# Memory-backend

This repository contains the backend for
the [Memory minigame](https://gamifyit-docs.readthedocs.io/en/latest/user-manuals/minigames/memory.html).

It persists the game data (configurations, game results, etc.) in a database and communicates with other backend
services.

## Table of contents

<!-- TOC -->
* [Links](#links)
* [Getting started](#getting-started)
  * [Run](#run)
    * [Docker-compose](#docker-compose)
    * [Project build](#project-build)
    * [With Docker](#with-docker)
  * [Testing Database](#testing-database)
* [REST API](#rest-api)
  * [Swagger-Ui](#swagger-ui)
<!-- TOC -->

## Links

- User documentation for the minigame can be
  found [here](https://gamifyit-docs.readthedocs.io/en/latest/user-manuals/minigames/memory.html).
- For the frontend, see the [Gamify-IT/memory repository](https://github.com/Gamify-IT/memory).
- The installation manual and setup instructions can be
  found [here](https://gamifyit-docs.readthedocs.io/en/latest/install-manuals/index.html).


## Getting started

Make sure you have the following installed:

- Java: [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or higher
- Maven: [Maven 3.6.3](https://maven.apache.org/download.cgi)
- Docker: [Docker latest or higher](https://www.docker.com/)

First you have to change the spring.datasource.username and the spring.datasource.password in the application.properties
file. If you changed the properties of the postgres db, you also have to change spring.datasource.url.

### Run

#### Run with Docker-compose

Start all dependencies with our docker-compose files.
Check the [manual for docker-compose](https://github.com/Gamify-IT/docs/blob/main/dev-manuals/languages/docker/docker-compose.md).

To run the main branch with minimal dependencies use the `docker-compose.yaml` file.\
To run the latest changes on any other branch than `main` use the `docker-compose-dev.yaml` file.


#### Project build

```sh
mvn install
```

in the folder of the project.
Go to the target folder and run

```sh
java -jar memory-service-0.0.1-SNAPSHOT.jar
```

#### With Docker

Build the Docker container with

```sh
docker build  -t memory-backend-dev .
```

And run it at port 8000 with

```
docker run -d -p 8000:80 -e POSTGRES_URL="postgresql://host.docker.internal:5432/postgres" -e POSTGRES_USER="postgres" -e POSTGRES_PASSWORD="postgres" --name memory-backend-dev memory-backend-dev
```

To monitor, stop and remove the container you can use the following commands:

```sh
docker ps -a -f name=memory-backend-dev
```

```sh
docker stop memory-backend-dev
```

```sh
docker rm memory-backend-dev
```

To run the prebuild container use

```sh
docker run -d -p 8000:80 -e POSTGRES_URL="postgresql://host.docker.internal:5432/postgres" -e POSTGRES_USER="postgres" -e POSTGRES_PASSWORD="postgres" --name memory-backend ghcr.io/gamify-it/memory-backend:latest
```

### Testing Database

to setup a database with docker for testing you can use

```sh
docker run -d -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=postgres  --rm --name memory-database postgres
```

To stop and remove it simply type

```sh
docker stop memory-database
```

## Rest API
Rest mappings are defined in 
- Config
  controller: [`src/main/java/de/unistuttgart/memorybackend/controller/ConfigController.java`](src/main/java/de/unistuttgart/memorybackend/controller/ConfigController.java)
- Game result
  controller: [`src/main/java/de/unistuttgart/memorybackend/controller/GameResultController.java`](src/main/java/de/unistuttgart/memorybackend/controller/GameResultController.java.java)

### Swagger-Ui

When the service is started (see [Getting started](#getting-started)), you can access the API documentation:

Open <http://localhost/minigames/memory/api/v1/swagger-ui/index.html#/> and
fill `http://localhost/minigames/memory/api/v1/v3/api-docs` into the input field in the navbar.
