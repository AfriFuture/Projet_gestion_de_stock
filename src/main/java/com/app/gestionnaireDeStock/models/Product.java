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

    @NotBlank(message = "Veuillez saisir un nom de produit.")
    @Column(name = "nom")
    private String name;

    @Column(name = "categorie")
    private String category;

    @NotNull(message = "Veuillez saisir le prix unitaire.")
    @DecimalMin(value = "0.01", message = "Le prix unitaire doit être supérieur à 0.")
    @Column(name = "prix_unitaire")
    private BigDecimal unitPrice;

    @NotNull(message = "Veuillez saisir la quantité en stock.")
    @Min(value = 0, message = "La quantité en stock ne peut pas être négative.")
    @Column(name = "quantite_en_stock")
    private Integer stockQuantity;

    @NotNull(message = "Veuillez saisir un seuil de stock.")
    @Min(value = 1, message = "Le seuil de stock doit être au moins égal à 1.")
    @Column(name = "seuil_stock")
    private Integer stockThreshold;

    private String image;

    private String description;
}
