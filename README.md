# Task Manager API

A secure REST API for managing user tasks using **Spring Boot**, **Spring Security**, **JWT Authentication**, and **MySQL**.

The project is fully containerized using **Docker Compose**, allowing you to run both the Spring Boot application and MySQL database without installing Java or MySQL locally.

---

## Features

* User registration and login with JWT authentication
* Role-based access control (USER / ADMIN)
* Task CRUD operations (Create, Read, Update, Delete)
* Input validation and global exception handling
* MySQL database with automatic table creation
* Fully containerized with Docker

---

## Technologies Used

* Java 21
* Spring Boot 3.5.14
* Spring Security 6
* JWT (JJWT)
* Spring Data JPA (Hibernate)
* MySQL 8
* Maven
* Docker
* Docker Compose

---

# Getting Started

## Prerequisites

Install the following:

* Docker
* Docker Compose

Download from: https://www.docker.com

---

## 1. Clone the Repository

```bash
git clone https://github.com/Naveen02627/task-manager-api.git
cd task-manager-api
```

> **Note:** Replace `task-manager-api` with your repository name if it is different.

---

## 2. Run the Application with Docker Compose

The project includes both a `Dockerfile` and a `docker-compose.yml` file.

Start the complete stack (Spring Boot + MySQL) using:

```bash
docker-compose up --build
```

### Services

**Spring Boot Application**

* URL: http://localhost:8080

**MySQL Database**

* Host Port: 3307
* Container Port: 3306
* Database: `taskdb`

Hibernate automatically creates and updates the required tables using:

```properties
spring.jpa.hibernate.ddl-auto=update
```

> **Note:** The first build may take several minutes because Maven needs to download dependencies and Docker must build the image.

---

## 3. Verify the Application

Open a new terminal and run:

```bash
curl -X POST http://localhost:8080/auth/login \
-H "Content-Type: application/json" \
-d '{"email":"dummy","password":"dummy"}'
```

Receiving a `401 Unauthorized` or `404 Not Found` response is expected because the credentials are invalid.

This confirms that the application is reachable.

---

# API Endpoints and Testing

You can test the API using:

* cURL
* Postman
* Insomnia
* Any REST client

### Important

Public endpoints:

* `/user/register`
* `/auth/login`

Do **not** include an Authorization header when calling these endpoints.

All other endpoints require:

```http
Authorization: Bearer <your-token>
```

---

## 1. Register a New User (Public)

```bash
curl -X POST http://localhost:8080/user/register \
-H "Content-Type: application/json" \
-d '{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "secret123",
  "role": "USER"
}'
```

### Response (201 Created)

```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "role": "USER"
}
```

![Registration Response](https://github.com/Naveen02627/taskManager/blob/main/screenshots/Screenshot%202026-06-09%20094308.png)

---

## 2. Login and Obtain a JWT Token (Public)

```bash
curl -X POST http://localhost:8080/auth/login \
-H "Content-Type: application/json" \
-d '{
  "email": "john@example.com",
  "password": "secret123"
}'
```

### Response (200 OK)

```json
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "type": "Bearer",
  "userId": 1,
  "email": "john@example.com"
}
```

Save the token value. It will be required for all authenticated requests.

![Login Response]([screenshots/login.png](https://github.com/Naveen02627/taskManager/blob/main/screenshots/Screenshot%202026-06-09%20094407.png))

---

## 3. Get User by ID (Authenticated)

```bash
curl -X GET "http://localhost:8080/user/getUser?id=1" \
-H "Authorization: Bearer <your-token>"
```

### Response (200 OK)

```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "role": "USER"
}
```

![User Profile](https://github.com/Naveen02627/taskManager/blob/main/screenshots/Screenshot%202026-06-09%20094806.png)

---

## 4. Add a Task (Authenticated)

```bash
curl -X POST http://localhost:8080/task/add \
-H "Content-Type: application/json" \
-H "Authorization: Bearer <your-token>" \
-d '{
  "task": "Buy groceries"
}'
```

### Response (201 Created)

```json
{
  "id": 1,
  "task": "Buy groceries",
  "user": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com"
  }
}
```

![Task Created](https://github.com/Naveen02627/taskManager/blob/main/screenshots/Screenshot%202026-06-09%20094922.png)

---

## 5. Get All Tasks of the Authenticated User

```bash
curl -X GET http://localhost:8080/task/getAll \
-H "Authorization: Bearer <your-token>"
```

### Response (200 OK)

```json
[
  {
    "id": 1,
    "task": "Buy groceries",
    "user": {
      ...
    }
  }
]
```

![Task List](https://github.com/Naveen02627/taskManager/blob/main/screenshots/Screenshot%202026-06-09%20095017.png)

---

## 6. Update a Task

Replace `1` with the actual task ID.

```bash
curl -X PUT http://localhost:8080/task/update/1 \
-H "Content-Type: application/json" \
-H "Authorization: Bearer <your-token>" \
-d '{
  "task": "Buy organic groceries and cook dinner"
}'
```

### Response (200 OK)

Returns the updated task object.



![Update task](https://github.com/Naveen02627/taskManager/blob/main/screenshots/Screenshot%202026-06-09%20095103.png)


---

## 7. Delete a Task

```bash
curl -X DELETE http://localhost:8080/task/delete/1 \
-H "Authorization: Bearer <your-token>"
```

### Response (200 OK)

Returns the deleted task object.


![Delete Task](https://github.com/Naveen02627/taskManager/blob/main/screenshots/Screenshot%202026-06-09%20095132.png)


---

## 8. Admin Only – Get All Users

First, create an admin account:

```bash
curl -X POST http://localhost:8080/user/register \
-H "Content-Type: application/json" \
-d '{
  "name": "Admin",
  "email": "admin@example.com",
  "password": "admin123",
  "role": "ADMIN"
}'
```

Login as the admin user and obtain a JWT token.

Then call:

![Admin login](https://github.com/Naveen02627/taskManager/blob/main/screenshots/Screenshot%202026-06-09%20095224.png)
```bash
curl -X GET http://localhost:8080/user/all \
-H "Authorization: Bearer <admin-token>"
```

### Response (200 OK)

Returns a list of all registered users (passwords excluded).

![Admin User List](https://github.com/Naveen02627/taskManager/blob/main/screenshots/Screenshot%202026-06-09%20095401.png)

---

# Manual Setup (Without Docker)

## Requirements

* Java 21 (JDK)
* MySQL 8
* Maven

### Create the Database

```sql
CREATE DATABASE taskdb;
```

### Configure Database Credentials

Update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/taskdb?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=your_password
```

### Build and Run

```bash
mvn clean package
java -jar target/Anything-0.0.1-SNAPSHOT.jar
```

Application URL:

```text
http://localhost:8080
```

---

# Project Structure

```text
com.task.Anything/
├── config/        # Security and JWT configuration
├── controller/    # REST controllers
├── dto/           # Request and response DTOs
├── entity/        # JPA entities
├── exception/     # Global exception handling
├── repository/    # Spring Data JPA repositories
├── security/      # JWT utilities and filters
└── service/       # Business logic
```

---

# Docker Configuration

## Dockerfile

A multi-stage Docker build:

1. Builds the application using Maven and JDK 21
2. Runs the application using a lightweight JRE 21 Alpine image

## docker-compose.yml

Defines two services:

### mysql

* MySQL 8
* Persistent volume
* Health checks
* Exposed on port 3307

### app

* Spring Boot application
* Built from Dockerfile
* Depends on a healthy MySQL container
* Database configuration injected via environment variables

---

# Troubleshooting

## JWT SignatureException

### Cause

Using an old JWT token generated before the application restart.

### Solution

Login again:

```bash
POST /auth/login
```

Generate a fresh token and use it in subsequent requests.

---

## MySQL Connection Refused

### Cause

MySQL is still initializing.

### Solution

Wait a few seconds after running:

```bash
docker-compose up
```

Verify that the database container is healthy.

---

## Database Not Created

If you encounter:

```text
Unknown database 'taskdb'
```

Create it manually:

```bash
docker exec -it taskdb-mysql mysql -u root -p
```

```sql
CREATE DATABASE taskdb;
```

---

# Screenshots

Add screenshots inside the `screenshots/` directory and update the image paths.

* Registration Response
* Login Response
* Create Task
* Get All Tasks
* Admin User List
* Docker Containers Running (`docker ps`)

---

# License

This project is intended for educational and learning purposes.
