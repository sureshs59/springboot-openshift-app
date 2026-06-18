package com.example.springboot.service;

import com.example.springboot.dto.ProductDTO;
import com.example.springboot.entity.Product;
import com.example.springboot.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;
    private ProductDTO testProductDTO;

    @BeforeEach
    public void setUp() {
        testProduct = new Product(1L, "Laptop", "High performance laptop", 999.99, 10,
                System.currentTimeMillis(), System.currentTimeMillis());

        testProductDTO = new ProductDTO(1L, "Laptop", "High performance laptop", 999.99, 10,
                System.currentTimeMillis(), System.currentTimeMillis());
    }

    @Test
    public void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(testProduct));

        List<ProductDTO> products = productService.getAllProducts();

        assertNotNull(products);
        assertEquals(1, products.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testGetProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        ProductDTO product = productService.getProductById(1L);

        assertNotNull(product);
        assertEquals("Laptop", product.getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetProductByIdNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.getProductById(999L));
    }

    @Test
    public void testCreateProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        ProductDTO createdProduct = productService.createProduct(testProductDTO);

        assertNotNull(createdProduct);
        assertEquals("Laptop", createdProduct.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void testCreateProductWithNullName() {
        ProductDTO dto = new ProductDTO();
        dto.setName(null);

        assertThrows(IllegalArgumentException.class, () -> productService.createProduct(dto));
    }

    @Test
    public void testUpdateProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        ProductDTO dto = new ProductDTO();
        dto.setName("Updated Laptop");

        ProductDTO updatedProduct = productService.updateProduct(1L, dto);

        assertNotNull(updatedProduct);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void testDeleteProduct() {
        when(productRepository.existsById(1L)).thenReturn(true);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testSearchByName() {
        when(productRepository.findByNameContainingIgnoreCase("Laptop"))
                .thenReturn(Arrays.asList(testProduct));

        List<ProductDTO> products = productService.searchByName("Laptop");

        assertNotNull(products);
        assertEquals(1, products.size());
        verify(productRepository, times(1)).findByNameContainingIgnoreCase("Laptop");
    }
}
