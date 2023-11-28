package com.example.author.persistence.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BookPublishedListener {
    @KafkaListener(topics = "${app.kafka.topic}", groupId = "author-persistence")
    public void listens(final String in) {
        log.info("Received: {}", in);
    }
}
