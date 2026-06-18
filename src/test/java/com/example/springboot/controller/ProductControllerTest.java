package com.example.springboot.controller;

import com.example.springboot.dto.ProductDTO;
import com.example.springboot.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductDTO testProductDTO;

    @BeforeEach
    public void setUp() {
        testProductDTO = new ProductDTO(1L, "Laptop", "High performance laptop", 999.99, 10,
                System.currentTimeMillis(), System.currentTimeMillis());
    }

    @Test
    public void testGetAllProducts() throws Exception {
        when(productService.getAllProducts()).thenReturn(Arrays.asList(testProductDTO));

        mockMvc.perform(get("/api/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Laptop"));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    public void testGetProductById() throws Exception {
        when(productService.getProductById(1L)).thenReturn(testProductDTO);

        mockMvc.perform(get("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(999.99));

        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    public void testCreateProduct() throws Exception {
        when(productService.createProduct(any(ProductDTO.class))).thenReturn(testProductDTO);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProductDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Laptop"));

        verify(productService, times(1)).createProduct(any(ProductDTO.class));
    }

    @Test
    public void testUpdateProduct() throws Exception {
        when(productService.updateProduct(eq(1L), any(ProductDTO.class))).thenReturn(testProductDTO);

        mockMvc.perform(put("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProductDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"));

        verify(productService, times(1)).updateProduct(eq(1L), any(ProductDTO.class));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(1L);
    }
}
