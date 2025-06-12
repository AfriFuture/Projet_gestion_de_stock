package com.app.gestionnaireDeStock.repository;

import com.app.gestionnaireDeStock.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}

