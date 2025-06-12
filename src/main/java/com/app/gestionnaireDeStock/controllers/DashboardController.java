package com.app.gestionnaireDeStock.controllers;

import com.app.gestionnaireDeStock.models.Order;
import com.app.gestionnaireDeStock.models.OrderItem;
import com.app.gestionnaireDeStock.models.Product;
import com.app.gestionnaireDeStock.repository.OrderRepository;
import com.app.gestionnaireDeStock.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;

@Controller
public class DashboardController {
    @Autowired private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/")
    public String showDashboard(Model model) {
        model.addAttribute("totalProducts", productRepository.count());
        model.addAttribute("totalOrders", orderRepository.count());

        long lowStockCount = productRepository.findAll().stream()
                .filter(p -> p.getStockQuantity() <= 5)
                .count();
        model.addAttribute("lowStockCount", lowStockCount);

        return "home";
    }

    @GetMapping("/reports")
    public String showReports(Model model) {
        long totalProducts = productRepository.count();
        long totalOrders = orderRepository.count();

        // Calculer le chiffre d'affaires total
        List<Order> orders = orderRepository.findAll();
        BigDecimal totalRevenue = orders.stream()
                .flatMap(o -> o.getItems().stream())
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantite())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Top 5 produits les plus commandés
        Map<Product, Integer> productCountMap = new HashMap<>();
        for (Order order : orders) {
            for (OrderItem item : order.getItems()) {
                productCountMap.merge(item.getProduct(), item.getQuantite(), Integer::sum);
            }
        }

        Map<Product, Integer> top5 = productCountMap.entrySet().stream()
                .sorted(Map.Entry.<Product, Integer>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new
                ));

        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("topProducts", top5);
        return "reports";
    }
}
