package com.ntm.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.ntm.productservice.dto.ProductRequest;
import com.ntm.productservice.dto.ProductResponse;
import com.ntm.productservice.service.ProductService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private ObjectMapper objectMapper=new ObjectMapper();
    private ObjectWriter objectWriter=objectMapper.writer();

    ProductResponse productResponse1=new ProductResponse(1L,"Sadza","Tasty white pap",new BigDecimal(9.89));
    ProductResponse productResponse2 =new ProductResponse(2L,"Cerevita","Banana flavoured Cerevita",new BigDecimal(2.34));

    ArgumentCaptor<ProductRequest> argument = ArgumentCaptor.forClass(ProductRequest.class);


    @Test
    @DisplayName("Should create a product object when a POST request is made on endpoint - /api/v1/products")
    void createProductSuccessTest() throws Exception {
        ProductRequest productRequest = new ProductRequest("Cake","BlackForest birthday cake",new BigDecimal(18.99));
        Mockito.doNothing().when(productService).createProduct(Mockito.any(ProductRequest.class));
        MockHttpServletRequestBuilder mockRequest=MockMvcRequestBuilders.
                post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(productRequest));
        mockMvc.perform(mockRequest)
                .andExpect(MockMvcResultMatchers.status().isCreated());
        Mockito.verify(productService,Mockito.times(1)).createProduct(productRequest);
        Mockito.verify(productService,Mockito.times(1)).createProduct(argument.capture());
        assertEquals("Cake",argument.getValue().getName());
    }

    @Test
    @DisplayName("Should return a list of Products when we make a POST request to endpoint - /api/v1/products")
    void getAllProducts() throws Exception{
        List<ProductResponse> productResponseList = new ArrayList<>(Arrays.asList(productResponse1,productResponse2));
        Mockito.when(productService.getAllProducts()).thenReturn(productResponseList);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder= MockMvcRequestBuilders
                .get("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$",Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name",Matchers.is("Cerevita")));

    }
}