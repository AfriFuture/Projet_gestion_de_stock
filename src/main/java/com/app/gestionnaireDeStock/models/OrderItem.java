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
    @JoinColumn(name = "id_commande")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "id_produit")
    private Product product;

    private Integer quantite;

    @Column(name = "prix_unitaire")
    private BigDecimal unitPrice;

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public void setOrder(Order order) { this.order = order; }

}
