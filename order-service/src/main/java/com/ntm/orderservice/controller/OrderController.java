package com.ntm.orderservice.controller;

import com.ntm.orderservice.dto.OrderRequest;
import com.ntm.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name="inventory",fallbackMethod = "fallBackMethod")
//    @TimeLimiter(name = "inventory")
    @Retry(name="inventory")
    public String placeOrder(@RequestBody OrderRequest orderRequest){
        return orderService.placeOrder(orderRequest);
    }

    public String fallBackMethod(OrderRequest orderRequest,RuntimeException runtimeException){
        return "Oops! Something went wrong. Please order after a few minutes.";
    }
}
