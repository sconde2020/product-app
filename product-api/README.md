# Product API

## Start API on development machine

```bash 
  SERVER_PORT=8090 mvn spring-boot:run -Dspring-boot.run.profiles=dev 
```

### Access to swagger
http://localhost:8090/swagger-ui.html

---

## Start API in a container

### Generate image

```bash 
  docker build -t product-api .
```

### Run the container

```bash 
  docker run --name product-api-container \
    --add-host=host.docker.internal:host-gateway \
    -e SPRING_PROFILES_ACTIVE=docker \
    -p 8091:8080 \
    product-api
```
### Access to swagger
Go to http://localhost:8091/swagger-ui.html