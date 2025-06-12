package com.app.gestionnaireDeStock.models;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import jakarta.persistence.*;

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
    private String category = "Unknown"; // valeur par défaut

    @NotNull(message = "Please enter the unit price.")
    @DecimalMin(value = "0.01", message = "Unit price must be greater than 0.")
    @Column(name = "prix_unitaire")
    private BigDecimal unitPrice;

    @NotNull(message = "Please enter the stock quantity.")
    @Min(value = 0, message = "Stock cannot be negative.")
    @Column(name = "quantite_en_stock")
    private Integer stockQuantity = 0; // valeur par défaut

    @NotNull(message = "Please enter a stock threshold.")
    @Min(value = 1, message = "Threshold must be at least 1.")
    @Column(name = "seuil_stock")
    private Integer stockThreshold = 5; // valeur par défaut

    private String image = "default.png"; // image par défaut

    private String description = "No description available.";
}
