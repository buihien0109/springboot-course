package com.example.demo.application.event.event.listener;


import com.example.demo.application.event.event.type.product.ProductEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProductEventListener {

    @EventListener
    public void onProductEvent(ProductEvent event) {
        log.info("Received product event");
        log.info("Product: {}", event.getProduct());
        log.info("Action: {}", event.getAction());
        log.info("Source: {}", event.getSource());
        log.info("Source Class: {}", event.getSource().getClass().getName());
    }
}
