package com.ntm.productservice.service;

import com.ntm.productservice.dto.ProductRequest;
import com.ntm.productservice.dto.ProductResponse;
import com.ntm.productservice.model.Product;
import com.ntm.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    private ProductRepository productRepository= Mockito.mock(ProductRepository.class);

    ProductRequest productRequest1= new ProductRequest("Bread","Proton white bread",new BigDecimal(0.95));
    ProductRequest productRequest2= new ProductRequest("Beans","Cashel baked beans",new BigDecimal(2.45));
    ProductRequest productRequest3= new ProductRequest("Peanut Butter","Mnandi Smooth Peanut Butter",new BigDecimal(1.45));

    Product product1= new Product(1L,"Bread","Proton white bread",new BigDecimal(0.95));
    Product product2= new Product(2L,"Beans","Cashel baked beans",new BigDecimal(2.45));
    Product product3= new Product(3L,"Peanut Butter","Mnandi Smooth Peanut Butter",new BigDecimal(1.45));


    @Test
    void createProduct() {
        Product product= new Product();
        product.setName(productRequest1.getName());
        product.setDescription(productRequest1.getDescription());
        product.setPrice(productRequest1.getPrice());
        Mockito.when(productRepository.save(product)).thenReturn(product);
        ProductService productService = new ProductService(productRepository);
        productService.createProduct(productRequest1);
        Assertions.assertNotNull(productRequest1);
        Assertions.assertEquals(productRequest1.getName(),product.getName());
        Assertions.assertEquals(productRequest1.getName(),"Bread");
    }

    @Test
    void getAllProducts() {
        List<Product> productList=new ArrayList<>(Arrays.asList(product1,product2,product3));
        Mockito.when(productRepository.findAll()).thenReturn(productList);
        ProductService productService = new ProductService(productRepository);
        List<ProductResponse> productResponses= productService.getAllProducts();
        Assertions.assertEquals(3,productResponses.size());
    }
}