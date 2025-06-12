package com.app.gestionnaireDeStock.repository;

import com.app.gestionnaireDeStock.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

