package com.ntm.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.ntm.orderservice.dto.OrderLineItemsDto;
import com.ntm.orderservice.dto.OrderRequest;
import com.ntm.orderservice.model.Order;
import com.ntm.orderservice.model.OrderLineItem;
import com.ntm.orderservice.service.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;


    ObjectMapper om=new ObjectMapper();
    ObjectWriter writer=om.writer();


    OrderLineItemsDto orderLineItemsDto1= new OrderLineItemsDto(1L,"123",new BigDecimal(12.33),10);
    OrderLineItemsDto orderLineItemsDto2= new OrderLineItemsDto(2L,"124",new BigDecimal(13.33),100);

    List<OrderLineItemsDto> orderLineItemsDtoList= new ArrayList<>(Arrays.asList(orderLineItemsDto1,orderLineItemsDto2));
    OrderRequest orderRequest=new OrderRequest(orderLineItemsDtoList);

    List<OrderLineItem> orderLineItem = orderLineItemsDtoList.stream()
            .map(orderLineItemsDto -> mapToOrderLineItem(orderLineItemsDto)).toList();

    private OrderLineItem mapToOrderLineItem(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItem orderLineItem= new OrderLineItem();
        orderLineItem.setSkuCode(orderLineItemsDto.getSkuCode());
        orderLineItem.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItem.setPrice(orderLineItemsDto.getPrice());
        return orderLineItem;
    }

    Order order= new Order(1L,"12",orderLineItem);

    ArgumentCaptor<OrderRequest> argumentCaptor=ArgumentCaptor.forClass(OrderRequest.class);

    @Test
    void placeOrder() {
        Mockito.doNothing().when(orderService).placeOrder(orderRequest);
        orderService.placeOrder(orderRequest);
        Mockito.verify(orderService,Mockito.times(1)).placeOrder(orderRequest);
//        Mockito.verify(orderService).placeOrder(argumentCaptor.capture());
    }
}