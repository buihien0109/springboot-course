package com.example.demo.application.event.event.publisher;

import com.example.demo.application.event.entity.Product;
import com.example.demo.application.event.event.type.product.ProductEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductEventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    public void publishProductEvent(Product product, String action) {
        ProductEvent event = new ProductEvent(this, product, action);
        eventPublisher.publishEvent(event);
    }
}
