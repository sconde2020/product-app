# PRODUCT-WEB

## Functionalities Overview

PRODUCT-WEB is an Angular-based web application that serves as the frontend for managing products. It provides a user-friendly interface to perform various operations related to product management, including:

- Viewing a list of products
- Adding new products
- Editing existing products
- Deleting products
- Searching and filtering products based on different criteria

---

## Development

This project was generated using [Angular CLI](https://github.com/angular/angular-cli) version 19.0.2.

### Development server

To start a local development server, run:

```bash
ng serve
```

Once the server is running, open your browser and navigate to `http://localhost:4200/`. The application will automatically reload whenever you modify any of the source files.

### Building

To build the project run:

```bash
ng build
```

This will compile your project and store the build artifacts in the `dist/` directory. By default, the production build optimizes your application for performance and speed.

### Running unit tests

To execute unit tests with the [Karma](https://karma-runner.github.io) test runner, use the following command:

```bash
ng test
```

This will launch the Karma test runner and execute all unit tests in your project.

---

## Deploy with Docker

### Production build

To create a production build of your application, run:

```bash
docker build -t product-web:latest .
```

### Running the production build

To run the production build in a Docker container, use the following command:

```bash
docker run -p 3000:80 product-web:latest
```

This will start the application and make it accessible at `http://localhost:3000/`

---

## Additional Resources

For more information on using the Angular CLI, including detailed command references, visit the [Angular CLI Overview and Command Reference](https://angular.dev/tools/cli) page.
