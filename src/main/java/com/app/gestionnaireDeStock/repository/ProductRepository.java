package com.app.gestionnaireDeStock.repository;

import com.app.gestionnaireDeStock.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

