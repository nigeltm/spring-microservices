package com.ntm.orderservice.service;

import com.ntm.orderservice.dto.OrderLineItemsDto;
import com.ntm.orderservice.dto.OrderRequest;
import com.ntm.orderservice.model.Order;
import com.ntm.orderservice.model.OrderLineItem;
import com.ntm.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


class OrderServiceTest {
    private OrderRepository orderRepository= Mockito.mock(OrderRepository.class);

    @Autowired
    private WebClient.Builder webclientBuilder;

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

    ArgumentCaptor<Order> argument=ArgumentCaptor.forClass(Order.class);

    @Test
    void placeOrder() {
        Mockito.when(orderRepository.save(order)).thenReturn(order);
        OrderService orderService = new OrderService(orderRepository,webclientBuilder);
        orderService.placeOrder(orderRequest);
        Mockito.verify(orderRepository,Mockito.times(1)).save(order);
        Mockito.verify(orderRepository,Mockito.times(1)).save(argument.capture());
        Assertions.assertEquals("12",argument.getValue().getOrderNumber());
    }
}