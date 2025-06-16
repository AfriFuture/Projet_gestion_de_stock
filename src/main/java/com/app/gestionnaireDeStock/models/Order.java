package com.app.gestionnaireDeStock.models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "commande")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom_client")
    private String clientName;

    @Column(name = "date_commande")
    private LocalDate orderDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    @Column(name = "statut")
    private String status = "En attente";

    public BigDecimal getTotalAmount() {
        if (items == null || items.isEmpty()) return BigDecimal.ZERO;

        return items.stream()
                .filter(item -> item.getUnitPrice() != null && item.getQuantite() > 0)
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantite())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
