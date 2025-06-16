package com.app.gestionnaireDeStock.services;

import com.app.gestionnaireDeStock.models.*;
import com.app.gestionnaireDeStock.repository.OrderRepository;
import com.app.gestionnaireDeStock.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    private Product produit;
    private OrderItem ligneCommande;
    private Order commande;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        produit = new Product();
        produit.setId(1L);
        produit.setName("Souris");
        produit.setUnitPrice(new BigDecimal("5000"));
        produit.setStockQuantity(10);

        ligneCommande = new OrderItem();
        ligneCommande.setProduct(produit);
        ligneCommande.setQuantite(2);

        commande = new Order();
        commande.setClientName("Jean");
        commande.setItems(List.of(ligneCommande));
    }

    @Test
    void doitCréerCommandeSiStockSuffisant() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(produit));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        Order sauvegardée = orderService.createOrder(commande);

        assertEquals("Jean", sauvegardée.getClientName());
        assertEquals(1, sauvegardée.getItems().size());
        assertEquals("En attente", sauvegardée.getStatus());
        assertEquals(8, produit.getStockQuantity());
    }

    @Test
    void doitÉchouerSiStockInsuffisant() {
        produit.setStockQuantity(1);
        when(productRepository.findById(1L)).thenReturn(Optional.of(produit));

        Exception exception = assertThrows(IllegalStateException.class, () -> orderService.createOrder(commande));
        assertTrue(exception.getMessage().contains("Stock insuffisant pour"));
    }

    @Test
    void doitÉchouerSiAucunProduit() {
        commande.setItems(Collections.emptyList());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(commande));
        assertEquals("Veuillez sélectionner au moins un produit.", exception.getMessage());
    }
}
