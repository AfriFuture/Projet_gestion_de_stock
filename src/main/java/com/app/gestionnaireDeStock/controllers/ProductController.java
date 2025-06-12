package com.app.gestionnaireDeStock.controllers;

import com.app.gestionnaireDeStock.models.Product;
import com.app.gestionnaireDeStock.repository.ProductRepository;
import com.app.gestionnaireDeStock.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;
    @Autowired private ProductRepository productRepository;

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", service.getAll());
        return "products";
    }

    @GetMapping("/add")
    public String showForm(Model model) {
        model.addAttribute("product", new Product());
        return "product-form";
    }

    @PostMapping("/save")
    public String saveProduct(
            @Valid @ModelAttribute("product") Product product,
            BindingResult result,Model model,
            RedirectAttributes redirect) {

        if (product.getCategory() == null || product.getCategory().trim().isEmpty()) {
            product.setCategory("Unknown");
        }

        if (product.getImage() == null || product.getImage().trim().isEmpty()) {
            product.setImage("default.png");
        }

        if (product.getDescription() == null || product.getDescription().trim().isEmpty()) {
            product.setDescription("No description available.");
        }

        if (product.getStockThreshold() == null || product.getStockThreshold() < 1) {
            product.setStockThreshold(5);
        }

        if (result.hasErrors()) {
            // ajouter explicitement l'objet dans le modèle
            model.addAttribute("product", product);
            return "product-form";
        }

        productRepository.save(product);
        redirect.addFlashAttribute("success", "Product saved successfully.");
        return "redirect:/products";
    }


    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("product", service.getById(id));
        return "product-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            productRepository.deleteById(id);
            redirect.addFlashAttribute("success", "Product deleted successfully.");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error deleting product.");
        }
        return "redirect:/products";
    }

}
