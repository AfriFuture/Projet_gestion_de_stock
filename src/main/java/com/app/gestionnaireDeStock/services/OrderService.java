package com.app.gestionnaireDeStock.services;

import com.app.gestionnaireDeStock.models.Order;
import com.app.gestionnaireDeStock.models.OrderItem;
import com.app.gestionnaireDeStock.models.Product;
import com.app.gestionnaireDeStock.repository.OrderRepository;
import com.app.gestionnaireDeStock.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {

    @Autowired private OrderRepository orderRepository;
    @Autowired private ProductRepository productRepository;

    @Transactional
    public Order createOrder(Order order) {
        order.setOrderDate(LocalDate.now());

        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new IllegalArgumentException("At least one product must be selected.");
        }

        List<OrderItem> validItems = order.getItems().stream()
                .filter(i -> i.getQuantite() > 0)
                .toList();

        if (validItems.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one product with quantity > 0.");
        }

        for (OrderItem item : validItems) {
            Product product = productRepository.findById(item.getProduct().getId()).orElseThrow();
            if (product.getStockQuantity() < item.getQuantite()) {
                throw new IllegalStateException("Insufficient stock for: " + product.getName());
            }
            product.setStockQuantity(product.getStockQuantity() - item.getQuantite());
            item.setUnitPrice(product.getUnitPrice());
            item.setOrder(order);
            item.setProduct(product);
        }

        order.setItems(validItems);
        Order saved = orderRepository.save(order);
        generateInvoice(saved);
        return saved;
    }

    private void generateInvoice(Order order) {
        String filename = "factures/facture_" + order.getId() + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("INVOICE #" + order.getId() + "\n");
            writer.write("Client: " + order.getClientName() + "\n");
            writer.write("Date: " + order.getOrderDate() + "\n\n");
            writer.write(String.format("%-20s %-10s %-12s %-12s\n", "Product", "Qty", "UnitPrice", "Total"));

            BigDecimal total = BigDecimal.ZERO;
            for (OrderItem item : order.getItems()) {
                BigDecimal lineTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantite()));
                writer.write(String.format("%-20s %-10d %-12.2f %-12.2f\n",
                        item.getProduct().getName(),
                        item.getQuantite(),
                        item.getUnitPrice(),
                        lineTotal));
                total = total.add(lineTotal);
            }

            writer.write("\nTOTAL: " + total + " FCFA\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    public Order getById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }
}

