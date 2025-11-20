# product-app

A product catalog application split into two components: a backend API and a frontend web UI.  
The backend implements the product services and data access,  
while the frontend provides a browser-based interface that consumes the API.

## Frontend

- Name: `product-web`
- Responsibility: Single-page web application that displays and manages products via the backend API.
- Location: `product-web/` (see its README for build/run details).
  
## Backend

- Name: `product-api`
- Responsibility: Exposes REST endpoints and handles data/storage for products.
- Location: `product-api/` (see its README for build/run details).

## Database
- Type: PostgreSQL
- Purpose: Stores product and category data for the backend API.
  
## Technologies

- Frontend: Angular 19, PrimeNG
- Backend: Java 21, Spring Boot 3, PostgreSQL
- Database: PostgreSQL 16.10
- Build tools: Maven (backend), npm (frontend)
- Containerization: Docker
- Orchestration: Kubernetes

## Quick start

1. Start the backend first: follow `product-api/README.md`.
2. Start the frontend: follow `product-web/README.md`.
3. Open the frontend in a browser and verify it connects to the backend.
