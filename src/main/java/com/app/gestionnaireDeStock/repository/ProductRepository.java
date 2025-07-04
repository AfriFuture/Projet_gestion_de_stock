package com.app.gestionnaireDeStock.repository;

import com.app.gestionnaireDeStock.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCaseAndCategoryContainingIgnoreCase(String name, String category);
}
