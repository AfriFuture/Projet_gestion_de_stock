package com.app.gestionnaireDeStock.controllers;

import com.app.gestionnaireDeStock.models.Order;
import com.app.gestionnaireDeStock.services.OrderService;
import com.app.gestionnaireDeStock.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired private OrderService orderService;
    @Autowired private ProductService productService;

    @GetMapping
    public String listOrders(Model model) {
        model.addAttribute("orders", orderService.getAll());
        return "orders";
    }

    @GetMapping("/add")
    public String showOrderForm(Model model) {
        Order order = new Order();
        order.setItems(new ArrayList<>());

        model.addAttribute("order", order);
        model.addAttribute("products", productService.getAll());
        return "order-form";
    }


    @PostMapping("/save")
    public String saveOrder(@ModelAttribute Order order, RedirectAttributes redirect) {
        try {
            orderService.createOrder(order);
            redirect.addFlashAttribute("success", "Order created successfully!");
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/orders/add";
        }
        return "redirect:/orders";
    }

}

