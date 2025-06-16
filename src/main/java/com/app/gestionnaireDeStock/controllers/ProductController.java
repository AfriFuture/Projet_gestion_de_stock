package com.app.gestionnaireDeStock.controllers;

import com.app.gestionnaireDeStock.models.Product;
import com.app.gestionnaireDeStock.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping
    public String listerProduits(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "stockLevel", required = false) String stockLevel,
            @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
            Model model) {

        List<Product> filtres = service.filterProducts(name, category, stockLevel, minPrice, maxPrice);
        model.addAttribute("products", filtres);
        model.addAttribute("categories", service.getAllCategories());
        model.addAttribute("name", name);
        model.addAttribute("category", category);
        model.addAttribute("stockLevel", stockLevel);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);

        return "products";
    }

    @GetMapping("/add")
    public String afficherFormulaireAjout(Model model) {
        model.addAttribute("product", new Product());
        return "product-form";
    }

    @PostMapping("/save")
    public String enregistrerProduit(
            @Valid @ModelAttribute("product") Product product,
            BindingResult result,
            @RequestParam("file") MultipartFile file,
            Model model,
            RedirectAttributes redirect) {

        if (result.hasErrors()) {
            model.addAttribute("product", product);
            return "product-form";
        }

        // Upload de l’image si fournie
        if (!file.isEmpty()) {
            try {
                String dossierUpload = "uploads/";
                String nomOriginal = Paths.get(file.getOriginalFilename()).getFileName().toString();
                String nomFichier = UUID.randomUUID() + "_" + nomOriginal;
                Path chemin = Paths.get(dossierUpload, nomFichier);
                Files.createDirectories(chemin.getParent());
                Files.copy(file.getInputStream(), chemin, StandardCopyOption.REPLACE_EXISTING);
                product.setImage(nomFichier);
            } catch (IOException e) {
                redirect.addFlashAttribute("error", "Échec du téléversement de l’image.");
                return "redirect:/products";
            }
        }

        // Valeurs par défaut si absentes
        if (product.getCategory() == null || product.getCategory().trim().isEmpty())
            product.setCategory("Inconnue");
        if (product.getDescription() == null || product.getDescription().trim().isEmpty())
            product.setDescription("Aucune description fournie.");
        if (product.getImage() == null || product.getImage().trim().isEmpty())
            product.setImage("default.png");
        if (product.getStockThreshold() == null || product.getStockThreshold() < 1)
            product.setStockThreshold(5);

        service.save(product);
        redirect.addFlashAttribute("success", "Produit enregistré avec succès.");
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String modifierProduit(@PathVariable Long id, Model model) {
        model.addAttribute("product", service.getById(id));
        return "product-form";
    }

    @GetMapping("/delete/{id}")
    public String supprimerProduit(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            service.delete(id);
            redirect.addFlashAttribute("success", "Produit supprimé avec succès.");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Erreur lors de la suppression du produit.");
        }
        return "redirect:/products";
    }
}
