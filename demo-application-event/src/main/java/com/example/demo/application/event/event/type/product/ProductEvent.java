package com.example.demo.application.event.event.type.product;

import com.example.demo.application.event.entity.Product;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ProductEvent extends ApplicationEvent {
    private final Product product;
    private final String action;

    public ProductEvent(Object source, Product product, String action) {
        super(source);
        this.product = product;
        this.action = action;
    }
}
