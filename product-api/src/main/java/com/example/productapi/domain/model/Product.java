package com.example.productapi.domain.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "products")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    BigDecimal price;
    String category;
    String description;
    Integer quantity;

    public Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product(String name, BigDecimal price, String category, String description, Integer quantity) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false; // protège contre les proxys Hibernate

        Product product = (Product) o;

        // Comparaison basée sur l'id pour les entités persistées
        if (id != null && product.id != null) {
            return id.equals(product.id);
        }

        // fallback sur les champs métier pour les entités non persistées
        return Objects.equals(name, product.name)
                && Objects.equals(price, product.price)
                && Objects.equals(category, product.category)
                && Objects.equals(description, product.description)
                && Objects.equals(quantity, product.quantity);
    }

    @Override
    public int hashCode() {
        // cohérence : id prioritaire s’il existe
        return id != null
                ? id.hashCode()
                : Objects.hash(name, price, category, description, quantity);
    }
}


