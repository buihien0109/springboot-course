package com.example.demo.application.event.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    @Transient
    private final List<Object> eventCollection = new LinkedList<>();

    @DomainEvents
    Collection<Object> domainEvents() {
        return List.copyOf(eventCollection);
    }

    @AfterDomainEventPublication
    void afterEventPublication() {
        eventCollection.clear();
    }

    public void addEvent(Object event) {
        eventCollection.add(event);
    }
}
