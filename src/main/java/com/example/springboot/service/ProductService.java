package com.example.springboot.service;

import com.example.springboot.dto.ProductDTO;
import com.example.springboot.entity.Product;
import com.example.springboot.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Product Service
 */
@Service
@Transactional
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    /**
     * Get all products
     */
    public List<ProductDTO> getAllProducts() {
        logger.info("Fetching all products");
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get product by ID
     */
    public ProductDTO getProductById(Long id) {
        logger.info("Fetching product with ID: {}", id);
        return productRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    /**
     * Create a new product
     */
    public ProductDTO createProduct(ProductDTO productDTO) {
        logger.info("Creating new product: {}", productDTO.getName());
        
        if (productDTO.getName() == null || productDTO.getName().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (productDTO.getPrice() == null || productDTO.getPrice() < 0) {
            throw new IllegalArgumentException("Product price must be positive");
        }
        if (productDTO.getQuantity() == null || productDTO.getQuantity() < 0) {
            throw new IllegalArgumentException("Product quantity cannot be negative");
        }

        Product product = convertToEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        
        logger.info("Product created with ID: {}", savedProduct.getId());
        return convertToDTO(savedProduct);
    }

    /**
     * Update an existing product
     */
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        logger.info("Updating product with ID: {}", id);
        
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        if (productDTO.getName() != null && !productDTO.getName().isEmpty()) {
            product.setName(productDTO.getName());
        }
        if (productDTO.getDescription() != null) {
            product.setDescription(productDTO.getDescription());
        }
        if (productDTO.getPrice() != null && productDTO.getPrice() >= 0) {
            product.setPrice(productDTO.getPrice());
        }
        if (productDTO.getQuantity() != null && productDTO.getQuantity() >= 0) {
            product.setQuantity(productDTO.getQuantity());
        }

        Product updatedProduct = productRepository.save(product);
        logger.info("Product updated with ID: {}", id);
        return convertToDTO(updatedProduct);
    }

    /**
     * Delete a product
     */
    public void deleteProduct(Long id) {
        logger.info("Deleting product with ID: {}", id);
        
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        
        productRepository.deleteById(id);
        logger.info("Product deleted with ID: {}", id);
    }

    /**
     * Search products by name
     */
    public List<ProductDTO> searchByName(String name) {
        logger.info("Searching products by name: {}", name);
        return productRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convert Product entity to DTO
     */
    private ProductDTO convertToDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }

    /**
     * Convert ProductDTO to Product entity
     */
    private Product convertToEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());
        return product;
    }
}
