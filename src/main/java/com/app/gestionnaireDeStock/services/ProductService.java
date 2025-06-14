package com.app.gestionnaireDeStock.services;

import com.app.gestionnaireDeStock.models.Product;
import com.app.gestionnaireDeStock.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public List<Product> getAll() {
        return repository.findAll();
    }

    public Product getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void save(Product product) {
        repository.save(product);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<String> getAllCategories() {
        return repository.findAll().stream()
                .map(Product::getCategory)
                .filter(c -> c != null && !c.isBlank())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Product> filterProducts(String name, String category, String stockLevel,
                                        BigDecimal minPrice, BigDecimal maxPrice) {

        return repository.findAll().stream()
                .filter(p -> name == null || name.isBlank() || p.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(p -> category == null || category.isBlank() || p.getCategory().equalsIgnoreCase(category))
                .filter(p -> {
                    if ("low".equalsIgnoreCase(stockLevel)) {
                        return p.getStockQuantity() <= p.getStockThreshold();
                    } else if ("normal".equalsIgnoreCase(stockLevel)) {
                        return p.getStockQuantity() > p.getStockThreshold();
                    }
                    return true;
                })
                .filter(p -> minPrice == null || p.getUnitPrice().compareTo(minPrice) >= 0)
                .filter(p -> maxPrice == null || p.getUnitPrice().compareTo(maxPrice) <= 0)
                .collect(Collectors.toList());
    }
}
