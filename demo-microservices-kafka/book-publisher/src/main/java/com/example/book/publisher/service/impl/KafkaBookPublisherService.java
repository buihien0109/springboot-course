package com.example.book.publisher.service.impl;

import com.example.book.publisher.config.KafkaConfigProps;
import com.example.book.publisher.domain.Book;
import com.example.book.publisher.exception.BookPublishException;
import com.example.book.publisher.service.BookPublisherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaBookPublisherService implements BookPublisherService {
    private final ObjectMapper objectMapper;

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final KafkaConfigProps kafkaConfigProps;

    public KafkaBookPublisherService(
            final ObjectMapper objectMapper,
            final KafkaTemplate<String, String> kafkaTemplate,
            final KafkaConfigProps kafkaConfigProps) {
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaConfigProps = kafkaConfigProps;
    }

    @Override
    public void publish(Book book) {
        try {
            // chuyển đổi book thành chuỗi JSON
            final String payload = objectMapper.writeValueAsString(book);

            // gửi message tới topic
            kafkaTemplate.send(kafkaConfigProps.getTopic(), payload);
        } catch (final JsonProcessingException ex) {
            throw new BookPublishException("Unable to publish book", ex, book);
        }
    }
}
