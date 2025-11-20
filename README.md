# Product Catalog Application (`product-app`)

**A full-stack product catalog application** designed for managing products and categories, supporting CRUD operations, advanced filtering and sorting, data exports, and secured via Spring Security with JWT authentication.

## This project demonstrates **end-to-end development skills**, including backend API design, frontend SPA implementation, database design, security, and containerized deployment.

## Architecture

The application is split into two main components:

### **Frontend**

- **Name:** `product-web`
- **Description:** A modern, single-page web application (SPA) that consumes the backend API.
- **Responsibilities:**
  - Display and manage products and categories
  - Support sorting, filtering, and exporting of lists
  - Responsive UI with PrimeNG components
- **Location:** `product-web/` (see its README for build/run details)

### **Backend**

- **Name:** `product-api`
- **Description:** RESTful backend service handling all business logic and data access.
- **Responsibilities:**
  - CRUD operations for products and categories
  - Sorting, filtering, and exporting data
  - Security layer with **Spring Security** and **JWT authentication**
  - Database persistence via **PostgreSQL**
- **Location:** `product-api/` (see its README for build/run details)

---

## Database

- **Type:** PostgreSQL
- **Version:** 16.10
- **Purpose:** Stores all product and category data for the backend API
- **Features:** Schema initialization and migrations, support for relational queries, sorting, and filtering

---

## Features

- **Product & Category Management:** Full CRUD operations
- **Advanced List Operations:** Sorting, filtering, and data export (CSV/Excel)
- **Security:** Role-based authentication and authorization with Spring Security and JWT
- **Responsive UI:** Intuitive SPA with Angular 19 and PrimeNG
- **Containerized Deployment:** Docker images for frontend, backend, and database
- **Orchestration:** Kubernetes-ready for scalable deployments
- **Extensible Architecture:** Designed for adding new modules, services, and microservices

---

## Technologies

- **Frontend:** Angular 19, PrimeNG
- **Backend:** Java 21, Spring Boot 3
- **Database:** PostgreSQL 16.10
- **Build Tools:** Maven (backend), npm (frontend)
- **Containerization:** Docker
- **Orchestration:** Kubernetes

---

## Quick Start

1. **Start the backend**: follow [product-api/README.md](product-api/README.md).
2. **Start the frontend**: follow [product-web/README.md](product-web/README.md).
3. **Verify:** Open the frontend in a browser and ensure it connects to the backend.

---

## Screenshots / Demo

### **Frontend Dashboard**

![Frontend Dashboard](docs/screenshots/frontend-dashboard.png)

### **Product List with Filtering and Sorting**

![Product List](docs/screenshots/product-list.png)

### **Category Management**

![Category Management](docs/screenshots/category-management.png)

### **Architecture Diagram**

![Architecture Diagram](docs/screenshots/architecture-diagram.png)

> Replace the image paths above with your actual screenshots or diagrams. Place them in a `docs/screenshots/` folder in your repo for best practice.

---

## Portfolio Highlights

This project demonstrates:

- End-to-end full-stack development
- Clean and maintainable architecture
- Security best practices (JWT, Spring Security)
- Containerization and orchestration readiness
- Advanced UI features for enterprise-grade applications
- Experience in leading design and implementation decisions
- Ability to work across the full technology stack
- Scalability and extensibility considerations
- Proficiency with modern frameworks and tools
