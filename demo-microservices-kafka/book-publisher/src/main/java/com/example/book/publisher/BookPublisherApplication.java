package com.example.book.publisher;

import com.example.book.publisher.config.KafkaConfigProps;
import com.example.book.publisher.domain.Author;
import com.example.book.publisher.domain.Book;
import com.example.book.publisher.repository.AuthorRepository;
import com.example.book.publisher.repository.BookRepository;
import com.example.book.publisher.service.BookPublisherService;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class BookPublisherApplication implements CommandLineRunner {
    private final KafkaConfigProps kafkaConfigProps;
    private final Faker faker;
    private final BookPublisherService bookPublisherService;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;


    public BookPublisherApplication(KafkaConfigProps kafkaConfigProps, Faker faker, BookPublisherService bookPublisherService, AuthorRepository authorRepository, BookRepository bookRepository) {
        this.kafkaConfigProps = kafkaConfigProps;
        this.faker = faker;
        this.bookPublisherService = bookPublisherService;
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(BookPublisherApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Kafka topic: {}", kafkaConfigProps.getTopic());

        for (int i = 0; i < 10; i++) {
            // create author
            Author author = Author.builder()
                    .name(faker.book().author())
                    .age(faker.number().numberBetween(20, 80))
                    .build();
            authorRepository.save(author);

            // create book
            Book book = Book.builder()
                    .isbn(faker.code().isbn10())
                    .title(faker.book().title())
                    .author(author)
                    .build();
            bookRepository.save(book);

            // publish book
            bookPublisherService.publish(book);
            log.info("Published book: {}", book);

            Thread.sleep(1000);
        }
    }
}
