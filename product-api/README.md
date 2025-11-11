# Product API

## Overview

The Product API provides CRUD operations for products with support for pagination, sorting and filtering.

Base path: /api/products. JSON request/response bodies.

## Endpoints (brief)

### List products

- Method: GET /api/products
- What it does: Returns a paginated list of products. Supports query params: page, size, direction
- Typical response: 200 OK with paginated wrapper (content, totalElements, totalPages, etc.)

### Get product

- Method: GET /api/products/{id}
- What it does: Returns a single product by id.
- Typical responses: 200 OK (product), 404 Not Found

### Create product

- Method: POST /api/products
- What it does: Creates a new product. Body includes name, description, price, currency, category, stock.
- Typical responses: 201 Created (created product), 400 Bad Request (validation)

### Update (replace)

- Method: PUT /api/products/{id}
- What it does: Replaces a product record with the provided full payload.
- Typical responses: 200 OK (updated product), 400 Bad Request, 404 Not Found

### Delete product

- Method: DELETE /api/products/{id}
- What it does: Deletes the specified product.
- Typical responses: 204 No Content, 404 Not Found

Error responses (common): 400 Bad Request (validation/malformed), 404 Not Found, 409 Conflict (duplicates/business rules), 500 Internal Server Error.

Use the swagger/OpenAPI UI for full schema, examples and interactive testing.

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
