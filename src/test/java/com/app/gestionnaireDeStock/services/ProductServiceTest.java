package com.app.gestionnaireDeStock.services;

import com.app.gestionnaireDeStock.models.Product;
import com.app.gestionnaireDeStock.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;

    private Product sampleProduct;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        sampleProduct = new Product();
        sampleProduct.setId(1L);
        sampleProduct.setName("Laptop");
        sampleProduct.setCategory("Electronics");
        sampleProduct.setUnitPrice(new BigDecimal("100000"));
        sampleProduct.setStockQuantity(10);
        sampleProduct.setStockThreshold(5);
    }

    @Test
    void shouldReturnAllProducts() {
        when(repository.findAll()).thenReturn(List.of(sampleProduct));
        List<Product> products = service.getAll();
        assertEquals(1, products.size());
    }

    @Test
    void shouldSaveProduct() {
        service.save(sampleProduct);
        verify(repository, times(1)).save(sampleProduct);
    }

    @Test
    void shouldDeleteProduct() {
        service.delete(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void shouldReturnProductById() {
        when(repository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        Product result = service.getById(1L);
        assertNotNull(result);
        assertEquals("Laptop", result.getName());
    }
}
