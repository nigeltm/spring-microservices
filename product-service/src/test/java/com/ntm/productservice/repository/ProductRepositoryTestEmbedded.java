package com.ntm.productservice.repository;

import com.ntm.productservice.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@ActiveProfiles("test")
public class ProductRepositoryTestEmbedded {
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void shouldSaveProductSuccess(){
        Product product = new Product(null,"Oranges","Fresh, tasty oranges",new BigDecimal(16.99));
        Product savedProduct = productRepository.save(product);
        assertThat(savedProduct).usingRecursiveComparison().ignoringFields("id").isEqualTo(product);
    }

    @Test
    @Sql("classpath:test-data.sql")
    public void shouldSaveProductsThroughSqlFileSuccess(){
        Optional<Product> test = productRepository.findByName("Banana");
        assertThat(test).isNotEmpty();
    }

    @Test
    public void shouldGetAllProductsSuccess(){
        Product product = new Product(null,"Oranges","Fresh, tasty oranges",new BigDecimal(16.99));
        Product product2 = new Product(null,"Grapes","Fresh, tasty grapes",new BigDecimal(15.99));
        productRepository.save(product);
        productRepository.save(product2);
        List<Product> retrievedProducts =productRepository.findAll();
        assertThat(retrievedProducts).hasAtLeastOneElementOfType(Product.class);
        assertThat(retrievedProducts).hasSize(2);
    }
}
