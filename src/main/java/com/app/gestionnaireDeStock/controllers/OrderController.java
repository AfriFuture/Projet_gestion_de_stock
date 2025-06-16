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
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @GetMapping
    public String afficherCommandes(@RequestParam(name = "status", required = false) String statut, Model model) {
        List<Order> commandes = (statut == null || statut.isBlank())
                ? orderService.getAll()
                : orderService.getAll().stream()
                .filter(o -> statut.equalsIgnoreCase(o.getStatus()))
                .toList();

        model.addAttribute("orders", commandes);
        model.addAttribute("selectedStatus", statut);
        return "orders";
    }

    @GetMapping("/add")
    public String afficherFormulaireCommande(Model model) {
        Order commande = new Order();
        commande.setItems(new ArrayList<>());

        model.addAttribute("order", commande);
        model.addAttribute("products", productService.getAll());
        return "order-form";
    }

    @PostMapping("/save")
    public String enregistrerCommande(@ModelAttribute Order commande, RedirectAttributes redirect) {
        try {
            orderService.createOrder(commande);
            redirect.addFlashAttribute("success", "Commande enregistrée avec succès !");
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/orders/add";
        }
        return "redirect:/orders";
    }

    @GetMapping("/process/{id}")
    public String marquerCommandeCommeTraitee(@PathVariable Long id, RedirectAttributes redirect) {
        orderService.markAsProcessed(id);
        redirect.addFlashAttribute("success", "Commande marquée comme traitée.");
        return "redirect:/orders";
    }
}
