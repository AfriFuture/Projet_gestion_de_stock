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
        order.setStatus("Pending"); // état initial

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

    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    public Order getById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public void markAsProcessed(Long orderId) {
        Order order = getById(orderId);
        if (order != null && !"Processed".equals(order.getStatus())) {
            order.setStatus("Processed");
            orderRepository.save(order);
        }
    }

    private void generateInvoice(Order order) {
        String filename = "factures/facture_" + order.getId() + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("=========== INVOICE #" + order.getId() + " ===========\n");
            writer.write("Client: " + order.getClientName() + "\n");
            writer.write("Date: " + order.getOrderDate() + "\n");
            writer.write("Status: " + order.getStatus() + "\n\n");

            writer.write(String.format("%-26s %10s %15s %15s\n", "Product", "Qty", "Unit Price", "Line Total"));
            writer.write("=".repeat(70) + "\n");

            BigDecimal total = BigDecimal.ZERO;
            for (OrderItem item : order.getItems()) {
                String name = item.getProduct().getName();
                if (name.length() > 25) {
                    name = name.substring(0, 22) + "...";
                }

                BigDecimal lineTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantite()));
                total = total.add(lineTotal);

                writer.write(String.format("%-26s %10d %15.2f %15.2f\n",
                        name,
                        item.getQuantite(),
                        item.getUnitPrice(),
                        lineTotal
                ));
            }

            writer.write("=".repeat(70) + "\n");
            writer.write(String.format("%-52s %15.2f FCFA\n", "TOTAL:", total));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
