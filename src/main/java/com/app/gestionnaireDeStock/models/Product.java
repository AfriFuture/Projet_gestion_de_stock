package com.app.gestionnaireDeStock.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "produit")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Please enter a product name.")
    @Column(name = "nom")
    private String name;

    @Column(name = "categorie")
    private String category; // plus de valeur par défaut ici

    @NotNull(message = "Please enter the unit price.")
    @DecimalMin(value = "0.01", message = "Unit price must be greater than 0.")
    @Column(name = "prix_unitaire")
    private BigDecimal unitPrice;

    @NotNull(message = "Please enter the stock quantity.")
    @Min(value = 0, message = "Stock cannot be negative.")
    @Column(name = "quantite_en_stock")
    private Integer stockQuantity;

    @NotNull(message = "Please enter a stock threshold.")
    @Min(value = 1, message = "Threshold must be at least 1.")
    @Column(name = "seuil_stock")
    private Integer stockThreshold;

    private String image; // valeur définie côté backend si null

    private String description;

}
