# Product API

## Overview

This service exposes two endpoint groups:

- /api/products — full CRUD for products; listing supports pagination, sorting and filtering.
- /api/categories — simple CRUD for categories.

All endpoints consume and produce JSON.

Base paths:

- Products: /api/products
- Categories: /api/categories

## Endpoints (brief)

### Products

#### List products

- Method: **GET /api/products**
- Description: Returns a paginated list of products. Supports query parameters:
  - Pagination: page, size
  - Sorting: sort (e.g. name,price), direction (asc|desc)
  - Filtering: common filters such as name, category, minPrice, maxPrice (server-defined)
- Typical response: **200 OK** with paginated wrapper (content, totalElements, totalPages, etc.)

#### Get product

- Method: **GET /api/products/{id}**
- Description: Returns a single product by id.
- Responses: **200 OK** (product), **404 Not Found**

#### Create product

- Method: **POST /api/products**
- Description: Creates a new product. Body fields typically include name, description, price, currency, category, stock.
- Responses: **201 Created** (created product), **400 Bad Request** (validation)

#### Update (replace) product

- Method: **PUT /api/products/{id}**
- Description: Replaces the product record with the provided full payload.
- Responses: **200 OK** (updated product), **400 Bad Request**, **404 Not Found**

#### Delete product

- Method: **DELETE /api/products/{id}**
- Description: Deletes the specified product.
- Responses: **204 No Content, 404 Not Found**

### Categories

#### List categories

- Method: **GET /api/categories**
- Description: Returns the list of categories (usually id, name, description). May be paginated depending on server config.
- Responses: **200 OK**

#### Create category

- Method: **POST /api/categories**
- Description: Creates a new category. Body typically includes name and optional description.
- Responses: **201 Created, 400 Bad Request**

#### Update (replace) category

- Method: **PUT /api/categories/{id}**
- Description: Replaces the category record.
- Responses: **200 OK, 400 Bad Request, 404 Not Found**

#### Delete category

- Method: **DELETE /api/categories/{id}**
- Description: Deletes the specified category.
- Responses: **204 No Content, 404 Not Found**

## Common error responses

- 400 Bad Request — validation / malformed request
- 404 Not Found — resource missing
- 500 Internal Server Error — unexpected server error

Use the swagger/OpenAPI UI for full schemas, request/response examples and interactive testing.

---

## Development commands

```bash
  SERVER_PORT=8090 mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

**Access to swagger**

http://localhost:8090/swagger-ui.html

---

## Containerization with Docker

**Generate image**

```bash
  docker build -t product-api:latest .
```

**Run the container**

```bash
  docker run --name product-api-container:latest \
    --add-host=host.docker.internal:host-gateway \
    -e SPRING_PROFILES_ACTIVE=docker \
    -p 8092:8091 \
    product-api:latest
```

**Access to swagger**

Go to http://localhost:8092/swagger-ui.html
