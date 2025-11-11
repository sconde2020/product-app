# Stage 1: Build the Angular app
FROM node:20-alpine AS build

# Set working directory
WORKDIR /app

# Copy dependency files
COPY package*.json ./

# Install dependencies (only production ones)
RUN npm ci --omit=dev

# Copy all source files
COPY . .

# Build the Angular app for production
RUN npm run build -- --output-path=dist/product-web --configuration production


# Stage 2: Serve with Nginx
FROM nginx:alpine

# Remove default nginx static assets
RUN rm -rf /usr/share/nginx/html/*

# ✅ Copy the built app from the "browser" folder
COPY --from=build /app/dist/product-web/browser /usr/share/nginx/html

# Copy your custom Nginx config (you already created it)
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose the web port
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]
