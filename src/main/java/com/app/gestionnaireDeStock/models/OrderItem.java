package com.app.gestionnaireDeStock.models;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "ligne_commande")
@Data
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Order order;

    @ManyToOne
    private Product product;

    private int quantite;
    private BigDecimal unitPrice;

    public BigDecimal getLineTotal() {
        if (unitPrice == null || quantite <= 0) return BigDecimal.ZERO;
        return unitPrice.multiply(BigDecimal.valueOf(quantite));
    }
}
