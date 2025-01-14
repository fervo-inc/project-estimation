# Project Estimation API

This project provides an API for managing and estimating project costs. It is built using **Jakarta EE**, **Spring Data
JPA**, **Spring MVC**, and relies on **PostgreSQL** for data storage. The application is containerized using Docker and
compose.

## Main Components

1. Backend - `/backend`
2. Frontend - `/frontend`
3. PostgreSQL - `compose.yaml`

## Prerequisites

- Docker and Docker Compose installed on your system.

## Running the Application

### Running Using Docker Compose

1. Create the `.env` files with

```
cp backend/src/main/resources/.env.example backend/src/main/resources/.env
cp frontend/.env.example frontend/.env
```

2. Build and start the Docker containers by running:
   ```bash
   docker compose up --build
   ```
   This will:
    - Start a PostgreSQL container for the database.
    - Start the backend application on port **8080**.
    - Start the frontend application on port **3000**.

3. Check if the service is running:
    - Navigate to the health endpoint: [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)
    - Ensure the health status is **UP**.
    - Navigate to [http://localhost:3000](http://localhost:3000), you should see a login page if you haven't logged in
      yet.

### Run manually

1. Start the DB
   ```bash
   docker compose up db
   ```

2. Run the backend using **Gradle**:
   ```bash
   ./gradlew bootRun
   ```

3. Run the frontend using **npm/pnpm/bun**:
   ```bash
   npm install --force
   npm run dev
   ```

## API Documentation

The exposed endpoints are documented with Swagger. You can access the Swagger UI for testing purposes:

- Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- OpenAPI JSON definition: [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

## Postman Collection

To test the API using Postman, import the Postman collection from the following file:

- **Location**: `docs/TakeCost-Project-Estimation-API.postman_collection.json`

## JWT Authentication

The API uses JWT for securing its endpoints. Use the JWT token below when testing the API using **Postman** or [*
*Swagger
UI**](http://localhost:8080/swagger-ui/index.html):

| **User** | **Password** | **Role**        |
|----------|--------------|-----------------|
| admin    | password     | Admin           |
| manager  | password     | Project Manager |
| member   | password     | Team Member     |

Or you can use this Token, just add it as a request header.

**Admin JWT Token**

```plaintext
Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJUYWtlQ29zdCIsInN1YiI6ImFkbWluIiwiYXVkIjpbIlRha2VDb3N0Q2xpZW50Il0sImlhdCI6MTczNjc0ODk4NCwibmJmIjoxNzM2NzQ4OTg0LCJleHAiOjE3MzczNTM3ODQsImp0aSI6ImRkODhmNjU3LTYwYTAtNDNhZi04NjlhLTYzMzA2NTQ3ZmRmZSIsInJvbGVzIjpbIkFETUlOIl19.vtsa-DbX1yGc7M-h1lrAXKOcf09vByXV2-4qS7lFE7qsXOSnypKDdldU0kEWK_VNqmbkzX-hZp-kLqRp7zKYHA
```

### How to Use JWT

1. **Swagger UI**:
    - Click on the **Authorize** button in the top-right.
    - Paste the token (above) into the dialog box.

2. **Postman**:
    - In the Headers of your request, add:
        - **Key**: `Authorization`
        - **Value**: `Bearer <JWT-TOKEN>` (replace `<JWT-TOKEN>` with the provided token).

## Additional Information

The application utilizes the following tools and configurations:

- **Health Monitoring** via the [/actuator/health](http://localhost/actuator/health) endpoint.
- **Docker Health Check** ensures all containers are initialized properly.
- Database credentials and other sensitive data are managed in the `.env` file.

You can modify the ports and other configurations in the `compose.yaml` file as per your requirements.

## Running GitHub Actions Locally

If you'd like to run and test GitHub Actions workflows locally, refer to the *
*[Local Workflow Setup Guide](LOCAL_WORKFLOW_README.md)** for detailed instructions.

## TODO

- [x] Frontend project - Basic layout done, working on doing the REST calls.
    - [x] Login [/login](http://localhost:3000/login)
    - [x] Dashboard
    - [/] Projects
    - [/] Materials
    - [/] Materials Dashboard
    - [/] Labor
    - [/] Vendor
    - [x] Settings (Just a demo)
- [/] Unit tests - Had a setback when I enabled JWT auth, ended up removing the controller tests.
- [/] CI/CD
    - [/] Dockerfile and compose - Adding the frontend to the compose
    - [/] GitHub Actions - Backend builds, fixing a path issue with the Dockerfile